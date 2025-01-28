


import requests
from concurrent.futures import ThreadPoolExecutor, as_completed
import time
import threading

# API endpoint
url = "http://localhost:5454/v1/products?color=&size=&minPrice=0&maxPrice=10000&minDiscount=0&category=&sort=price_low&stock=null&pageNumber=0&pageSize=100"


# Lock for synchronization
lock = threading.Lock()
last_request_time = 0  # Global tracker for the last request time

# Function to send a request with a 1-second global delay
def send_request(index):
    global last_request_time
    try:
        # Ensure a 1-second delay globally
        with lock:
            now = time.time()
            if now - last_request_time < 0.01:
                time.sleep(0.01 - (now - last_request_time))
            last_request_time = time.time()
        
        # Send the request
        response = requests.get(url,timeout=5)
        print(f"Request {index}: Response: {response.status_code}, {response.text}")
    except requests.exceptions.RequestException as e:
        print(f"Request {index} failed: {e}")

# Send 500 requests concurrently with a 1-second delay between them
with ThreadPoolExecutor(max_workers=4) as executor:  # Set max_workers=1 to control global rate
    futures = [executor.submit(send_request, i) for i in range(1, 10001)]
    for future in as_completed(futures):  # Ensure completion of all tasks
        future.result()

