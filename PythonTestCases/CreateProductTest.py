import requests
from concurrent.futures import ThreadPoolExecutor, as_completed
import time
import threading
import random

# API endpoint for product creation
url = "http://localhost:5454/v1/admin/product/create"

# Static part of the request data
static_data = {
    "size": [
        {"name": "S", "quantity": 20},
        {"name": "M", "quantity": 30},
        {"name": "L", "quantity": 50},
        {"name": "XXL", "quantity": 20}
    ]
}


topLevelCategories=[
"Men",
"Women"
]
secondLevelCategories=[
 "Clothing"
]

thirdLevelCategories=[
"Kurta",
"Jeans",
"Sale",
"New Arrivals"
]

# List of image URLs
image_urls = [
    "https://manyavar.scene7.com/is/image/manyavar/I02_IMGL6125%20copy%20copy_11-10-2021-20-13:650x900",
    "https://rukminim1.flixcart.com/image/612/612/k4d27ww0/shirt/q/w/t/l-el024-el-senor-original-imafnadnjp5pq6tg.jpeg?q=70",
    "https://images.unsplash.com/photo-1638953173691-671b6c2692fa?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MjE4fHxmYXNoaW9uJTIwcHJvZHVjdHxlbnwwfHwwfHx8MA%3D%3D",
    "https://rukminim1.flixcart.com/image/612/612/xif0q/shirt/j/x/v/-original-imaghrnsfvbhrsny.jpeg?q=70",
    "https://rukminim1.flixcart.com/image/612/612/xif0q/shirt/7/k/2/xl-kcsh-fo-1647-ma-fubar-original-imaghehzpwgjzg2y.jpeg?q=70",
    "https://images.unsplash.com/photo-1560343090-f0409e92791a?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTl8fGZhc2hpb24lMjBwcm9kdWN0fGVufDB8fDB8fHww",
    "https://images.unsplash.com/photo-1587467512961-120760940315?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTh8fGZhc2hpb24lMjBwcm9kdWN0fGVufDB8fDB8fHww",
    "https://images.unsplash.com/photo-1602810319250-a663f0af2f75?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTV8fGZhc2hpb24lMjBwcm9kdWN0fGVufDB8fDB8fHww",
    "https://images.unsplash.com/photo-1543163521-1bf539c55dd2?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTB8fGZhc2hpb24lMjBwcm9kdWN0fGVufDB8fDB8fHww",
    "https://images.unsplash.com/photo-1541643600914-78b084683601?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NHx8ZmFzaGlvbiUyMHByb2R1Y3R8ZW58MHx8MHx8fDA%3D",
    "https://plus.unsplash.com/premium_photo-1664392147011-2a720f214e01?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NXx8ZmFzaGlvbiUyMHByb2R1Y3R8ZW58MHx8MHx8fDA%3D",
    "https://images.unsplash.com/photo-1547887537-6158d64c35b3?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTF8fGZhc2hpb24lMjBwcm9kdWN0fGVufDB8fDB8fHww",
    "https://images.unsplash.com/photo-1617213226302-99a82b62626f?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTZ8fGZhc2hpb24lMjBwcm9kdWN0fGVufDB8fDB8fHww",
    "https://images.unsplash.com/photo-1584735174914-6b1272458e3e?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NTR8fGZhc2hpb24lMjBwcm9kdWN0fGVufDB8fDB8fHww",
    "https://images.unsplash.com/photo-1570653466075-69540bc5ebb3?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8OTZ8fGZhc2hpb24lMjBwcm9kdWN0fGVufDB8fDB8fHww"
]

colors = [
"Red",
"Blue",
"White",
"Green",
"Black"
]
# colors = [
# "VIOLET"
# ]

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
        dynamic_data["title"] = f"Product Title {index}"  # Change title dynamically
        dynamic_data["price"] = 1000 + index
        dynamic_data["brand"] = f"BRAND{index}"
        dynamic_data["quantity"] = 10 + index
        dynamic_data["discountedPrice"] = 100 + index
        dynamic_data["discountPercent"] = ((dynamic_data["price"]-dynamic_data["discountedPrice"])/dynamic_data["price"])*100
        dynamic_data["description"] = f"Description For the product {index} This is completely for the testing purpose,thank you for visiting this site, Please Give a Star fo this repository on https://github.com/Mitakshar15/ecom-backend-api"
        dynamic_data["topLevelCategory"] = random.choice(topLevelCategories)
        dynamic_data["secondLevelCategory"] = random.choice(secondLevelCategories)
        dynamic_data["thirdLevelCategory"] = random.choice(thirdLevelCategories)
        dynamic_data["color"]= random.choice(colors)

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
with ThreadPoolExecutor(max_workers=1) as executor:  # Set max_workers to control concurrency
    futures = [executor.submit(send_request, i) for i in range(2023, 3000)]
    for future in as_completed(futures):  # Ensure completion of all tasks
        future.result()
