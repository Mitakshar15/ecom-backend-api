import requests
from concurrent.futures import ThreadPoolExecutor, as_completed
import time
import threading

# API endpoint for create order
url = "http://localhost:5454/api/orders/"

start_time = time.time()

# Static request data for order creation
static_data = {
    "firstName": "Mitakshara",
    "lastName": "Hegde",
    "streetAddress": "12sss3 Main St",
    "city": "Sirsi",
    "state": "Karnatasska",
    "zipCode": "581402",
    "mobile": "+91980461228"
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

# Function to send a request
def send_request(index):
    global last_request_time
    try:
        # Ensure a 1-second delay globally
        with lock:
            now = time.time()
            if now - last_request_time < 0.1:  # Check if 1 second has passed
                time.sleep(0.1 - (now - last_request_time))
            last_request_time = time.time()

        # Send the request with headers
        response = requests.post(url, json=static_data, headers=headers, timeout=5)
        print(f"Request {index}: Response: {response.status_code}, {response.text}")
    except requests.exceptions.RequestException as e:
        print(f"Request {index} failed: {e}")

# Send 500 requests with a 1-second delay between them
with ThreadPoolExecutor(max_workers=10) as executor:  # Set max_workers=1 for sequential execution
    futures = [executor.submit(send_request, i) for i in range(1, 501)]
    for future in as_completed(futures):  # Ensure completion of all tasks
        future.result()
        
end_time = time.time()
execution_time = end_time - start_time
print(f"TIME TAKEN FOR EXECUION : {execution_time}  SECONDS ")

