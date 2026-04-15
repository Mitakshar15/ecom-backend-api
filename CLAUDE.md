# CLAUDE.md - Ecom Backend API

## Project Overview
Spring Boot 3.2.11 e-commerce REST API (Java 17, Maven) with PostgreSQL, JWT authentication, and OpenAPI-generated DTOs. The application provides product catalog, shopping cart, order management, inventory management, ratings/reviews, and admin functionality.

## Build & Run

```bash
# Build (skipping tests)
mvn clean package -DskipTests

# Build with tests
mvn clean verify

# Run locally
mvn spring-boot:run

# Run JAR directly
java -jar target/ecomapi-0.0.1-SNAPSHOT.jar
```

**Required environment variables:**
- `POSTGRES_URL` - JDBC connection string (e.g., `jdbc:postgresql://localhost:5432/EcomApiV2`)
- `POSTGRES_USRNAME` - Database username (note: intentional typo in env var name)
- `POSTGRES_PASSWORD` - Database password
- `MAIL_PASSWORD` - SMTP mail password (optional, has fallback default)

**Server runs on port 5454.**

## Testing

```bash
# Run all tests (Spock/Groovy + JUnit)
mvn test

# Run a specific test class
mvn test -Dtest=ClassName
```

Tests use **Spock Framework 2.3** with Groovy 4.0.9. Test sources are in `src/test/`.

## Project Architecture

```
src/main/java/com/ainkai/
├── EcomapiApplication.java          # Entry point (@EnableScheduling, @EnableAsync)
├── api/v1/                          # REST Controllers
│   ├── AdminController.java         # Admin order/product management
│   ├── AdminInventoryController.java# Inventory management (restock, adjust, low-stock)
│   ├── UserProfileController.java   # Auth (signup/signin), profile, addresses
│   ├── UserProductController.java   # Product browsing, ratings, reviews, search
│   ├── UserCartController.java      # Cart operations
│   └── UserOrderController.java     # Order creation, history
├── model/                           # JPA Entities
│   ├── User.java, Product.java, Sku.java, Cart.java, CartItem.java
│   ├── Order.java, OrderItem.java, Category.java (3-level hierarchy)
│   ├── Rating.java, Review.java, Address.java
│   ├── InventoryLog.java            # Inventory audit trail
│   ├── PaymentInformation.java (embedded), PaymentDetails.java (embedded)
│   └── dtos/                        # OpenAPI-generated DTOs (do NOT edit manually)
├── repository/                      # Spring Data JPA repositories
│   └── InventoryLogRepo.java        # Inventory log queries
├── service/                         # Service interfaces + *Impl classes
│   ├── InventoryService.java        # Inventory management interface
│   └── InventoryServiceImpl.java    # Stock validation, decrement, restock, audit
├── config/
│   ├── AppConfig.java               # Security filter chain, CORS, BCrypt
│   ├── JwtProvider.java             # JWT generation/parsing (HMAC-SHA256)
│   ├── JwtValidator.java            # JWT validation filter
│   ├── CacheConfig.java             # Caffeine cache manager
│   └── OpenApiConfig.java           # Swagger/OpenAPI bean
├── mapper/EcomApiUserMapper.java    # MapStruct mapper
├── builder/ApiResponseBuilder.java  # API response construction
├── emailservice/                    # Async email sending
├── scheduler/                       # Cron jobs (pending order cleanup, old order deletion)
├── exceptions/                      # Custom exceptions + global handler
│   └── InventoryException.java      # Inventory-specific exception (409 CONFLICT)
├── api/utils/                       # BaseApiResponse, ApiMessage, Status, etc.
└── user/domain/                     # Enums: OrderStatus, PaymentStatus, PaymentMethod,
                                     #        InventoryAction, Constants
```

## Key Conventions

### DTOs
DTOs in `com.ainkai.model.dtos` are **auto-generated** by the OpenAPI Generator Maven plugin from YAML specs in `src/main/resources/`. Never edit them manually. To change DTOs, modify the corresponding YAML spec:
- `ecomUserApi.yaml` - User/auth endpoints
- `ecomCartApi.yaml` - Cart endpoints
- `ecomOrderApi.yaml` - Order endpoints
- `ecomAdminApi.yaml` - Admin endpoints
- `ecomInventoryApi.yaml` - Inventory management endpoints
- `ecomApiComponents.yaml` - Shared schema components

### Entity Design
- **Product-SKU**: Product has many SKUs (color/size variants). SKU holds price, quantity, discount.
- **SKU**: Uses `@Version` for optimistic locking to prevent race conditions on concurrent stock updates.
- **InventoryLog**: Audit trail for all stock changes (restock, order placement, cancellation, manual adjustment).
- **Category**: 3-level hierarchy (topLevel -> secondLevel -> thirdLevel) via self-referencing `parentCategory`.
- **Cart**: One-to-one with User, one-to-many CartItems. Totals recalculated on each `findUserCart()` call.
- **Order**: Created from Cart contents. OrderItems reference SKUs. Embedded PaymentDetails.
- Collections use `@BatchSize(size = 25)` to mitigate N+1 queries.

### Inventory Management
The inventory system is built around the `InventoryService` which centralizes all stock operations:

**Stock Validation Flow:**
1. **Add to Cart** (`CartServiceImpl.addCartitem`) -> validates stock via `InventoryService.validateStock()`
2. **Update Cart Quantity** (`CartItemServiceImpl.updateCartItemQuantity`) -> validates new total via `InventoryService.validateStock()`
3. **Create Order** (`OrderServiceImpl.createOrder`) -> validates ALL items, then decrements via `InventoryService.decrementStock()`
4. **Cancel Order** (`OrderServiceImpl.cancledOrder`) -> restores stock via `InventoryService.restoreStock()`

**Concurrency Safety:**
- `Sku.version` field with `@Version` annotation enables Hibernate optimistic locking
- If two concurrent requests try to decrement the same SKU, one will get an `OptimisticLockException` and retry/fail

**Audit Trail:**
- Every stock change is recorded in `inventory_log` table with: action type, quantity before/after, change amount, reference ID (orderId), reason, timestamp
- `InventoryAction` enum: `RESTOCK`, `ORDER_PLACED`, `ORDER_CANCELLED`, `MANUAL_ADJUSTMENT`, `STOCK_CORRECTION`

**Admin Inventory Endpoints** (`/api/v1/admin/inventory/`):
- `PUT /restock` - Add stock to a SKU
- `PUT /adjust` - Set absolute stock level for a SKU
- `GET /low-stock?threshold=10` - List SKUs with stock <= threshold
- `GET /out-of-stock` - List SKUs with zero stock
- `GET /history/{skuId}` - View full inventory change history for a SKU

### Authentication
All `/api/**` endpoints require JWT in the `Authorization` header. JWT contains user email claim, 24-hour expiry. Controllers extract user via `userService.findUserProfileByJwt(jwt)`.

### Database
- PostgreSQL with HikariCP connection pool (max 20 connections)
- `ddl-auto=update` (schema auto-managed by Hibernate)
- Hibernate JDBC batching enabled (batch_size=25)
- Key indexes defined on entities for email, category_id, user_id, product_id, order status, inventory_log sku_id

### Caching
Caffeine in-memory cache (10-minute TTL, max 500 entries) for:
- `products` - product listings
- `productById` - individual product lookups
- `categories` - category data
- `userByEmail` - user lookups

Cache is evicted on product create/update/delete operations.

### Transaction Management
- Write operations use `@Transactional`
- Read-only operations use `@Transactional(readOnly = true)` for Hibernate flush optimization
- OrderService.createOrder validates all stock first, then decrements and creates order in a single transaction
- Order cancellation restores stock atomically within the same transaction

### Async
- Email sending is `@Async` to avoid blocking request threads
- Enabled via `@EnableAsync` on main application class

### Scheduling
- `ClearPendingOrder`: Runs on last weekday of each month, bulk-deletes pending orders
- `DeleteOrderHistory`: Runs on last weekday of each month, bulk-deletes orders older than 1 year

## Code Style
- Lombok for boilerplate (`@Getter`, `@Setter`, `@AllArgsConstructor`, `@RequiredArgsConstructor`)
- MapStruct for entity-DTO mapping
- Constructor injection (via Lombok `@AllArgsConstructor` / `@RequiredArgsConstructor`)
- Service layer: interface + Impl pattern
- Response wrapping via `ApiResponseBuilder` -> `BaseApiResponse`

## Common Pitfalls
- `POSTGRES_USRNAME` is intentionally misspelled (not `USERNAME`) - match the env var exactly
- `userServiceImpl` class name starts with lowercase (historical convention in this project)
- OpenAPI generator runs during build - if DTOs are missing, run `mvn generate-sources`
- `spring.jpa.open-in-view=false` - lazy-loaded associations must be fetched within `@Transactional` boundaries
- Inventory operations should always go through `InventoryService` - never modify `Sku.quantity` directly
- SKU uses `@Version` for optimistic locking - handle `OptimisticLockException` for concurrent access
