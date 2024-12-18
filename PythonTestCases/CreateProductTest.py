import requests
from concurrent.futures import ThreadPoolExecutor, as_completed
import time
import threading
import random

# API endpoint for product creation
url = "http://localhost:5454/api/admin/products/"

# Static part of the request data
static_data = {
    "color": "Blue",
    "size": [
        {"name": "S", "quantity": 20},
        {"name": "M", "quantity": 30},
        {"name": "L", "quantity": 50},
        {"name": "XXL", "quantity": 20}
    ],
    "topLevelCategory": "Men",
    "secondLevelCategory": "Clothing",
    "thirdLevelCategory": "Kurta"
}

# List of image URLs
image_urls = [
    "https://manyavar.scene7.com/is/image/manyavar/I02_IMGL6125%20copy%20copy_11-10-2021-20-13:650x900",
    "https://rukminim1.flixcart.com/image/612/612/k4d27ww0/shirt/q/w/t/l-el024-el-senor-original-imafnadnjp5pq6tg.jpeg?q=70",
    "https://rukminim1.flixcart.com/image/612/612/xif0q/shirt/f/z/l/m-white-formal-youthfirst-original-imagqjdfgwjfwgsb.jpeg?q=70",
    "https://rukminim1.flixcart.com/image/612/612/xif0q/shirt/j/x/v/-original-imaghrnsfvbhrsny.jpeg?q=70",
    "https://rukminim1.flixcart.com/image/612/612/xif0q/shirt/7/k/2/xl-kcsh-fo-1647-ma-fubar-original-imaghehzpwgjzg2y.jpeg?q=70"
]

# Authorization token
auth_token = input("ENTER THE AUTH TOKEN: ")  # Replace with the actual token

# Headers including Authorization
headers = {
    "Authorization": f"Bearer {auth_token}"
}

# Lock for synchronization
lock = threading.Lock()
last_request_time = 0  # Global tracker for the last request time

# Function to send a request
def send_request(index):
    global last_request_time
    try:
        # Dynamically change the data for each request
        dynamic_data = static_data.copy()  # Copy static data for each request
        dynamic_data["title"] = f"Dummy Product Title {index}"  # Change title dynamically
        dynamic_data["price"] = 100 + index
        dynamic_data["brand"] = f"BRAND{index}"
        dynamic_data["quantity"] = 1 + index
        dynamic_data["discountedPrice"] = 100 + index
        dynamic_data["discountPercent"] = 1 + index
        dynamic_data["description"] = f"Dummy Description For the product {index} Tis is completely for the testing purpose, the real app  will contain real products with real prcing and inventory management thank you for visiting this site"

        # Randomly select an image URL from the list
        dynamic_data["imageUrl"] = random.choice(image_urls)

        # Ensure a 1-second delay globally
        with lock:
            now = time.time()
            if now - last_request_time < 0.1:  # Check if 1 second has passed
                time.sleep(0.1 - (now - last_request_time))
            last_request_time = time.time()

        # Send the request with headers
        response = requests.post(url, json=dynamic_data, headers=headers, timeout=5)
        print(f"Request {index}: Response: {response.status_code}, {response.text}")
    except requests.exceptions.RequestException as e:
        print(f"Request {index} failed: {e}")

# Send 500 requests with a 1-second delay between them
with ThreadPoolExecutor(max_workers=10) as executor:  # Set max_workers to control concurrency
    futures = [executor.submit(send_request, i) for i in range(1, 501)]
    for future in as_completed(futures):  # Ensure completion of all tasks
        future.result()
