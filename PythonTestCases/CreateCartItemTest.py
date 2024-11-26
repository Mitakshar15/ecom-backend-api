import requests
from concurrent.futures import ThreadPoolExecutor, as_completed
import time
import threading

# API endpoint for adding to the cart
url = "http://localhost:5454/api/cart/add"

# Static request data for adding to the cart
static_data = {
    "size": "L",
    "quantity": 3
}

# Authorization token
auth_token = input("ENTER THE AUTH TOKEN")  # Replace with the actual token

# Headers including Authorization
headers = {
    "Authorization": f"Bearer {auth_token}"
}

# Lock for synchronization
lock = threading.Lock()
last_request_time = 0  # Global tracker for the last request time

# Function to send a request with dynamic 'productId'
def send_request(index):
    global last_request_time
    try:
        # Dynamically update the productId for each request
        dynamic_data = static_data.copy()
        dynamic_data["productId"] = index  # Set a unique productId for each request

        # Ensure a 1-second delay globally
        with lock:
            now = time.time()
            if now - last_request_time < 0.1:  # Check if 1 second has passed
                time.sleep(0.1 - (now - last_request_time))
            last_request_time = time.time()

        # Send the request with headers
        response = requests.put(url, json=dynamic_data, headers=headers, timeout=5)
        print(f"Request {index}: Response: {response.status_code}, {response.text}")
    except requests.exceptions.RequestException as e:
        print(f"Request {index} failed: {e}")

# Send 500 requests with a 1-second delay between them
with ThreadPoolExecutor(max_workers=10) as executor:  # Set max_workers=10 for parallel execution
    futures = [executor.submit(send_request, i) for i in range(1, 11)]
    for future in as_completed(futures):  # Ensure completion of all tasks
        future.result()

