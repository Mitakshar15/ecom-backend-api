import requests
from concurrent.futures import ThreadPoolExecutor, as_completed
import string
import time
import threading

# API endpoint
url = "http://localhost:5454/auth/signup"

# Function to generate unique email
def generate_unique_email(index):
    return f"ecomApiTestSample{index}@example.com"

# Lock for synchronization
lock = threading.Lock()
last_request_time = 0  # Global tracker for the last request time
s_t = time.time()
# Function to send a request
def send_request(index):
    global last_request_time
    unique_email = generate_unique_email(index)  # Generate unique email for each request
    data = {
        "firstName": f"User{index}",
        "lastName": f"Name{index}",
        "email": unique_email,
        "password": "password123"
    }
    try:
        # Ensure a 1-second delay globally
        with lock:
            now = time.time()
            if now - last_request_time < 0.001:  # Check if 1 second has passed
                time.sleep(0.001 - (now - last_request_time))
            last_request_time = time.time()

        # Send the request
        response = requests.post(url, json=data, timeout=5)
        print(f"Request {index}: Response: {response.status_code}, {response.text}")
    except requests.exceptions.RequestException as e:
        print(f"Request {index} failed: {e}")

# Send 500 requests concurrently
with ThreadPoolExecutor(max_workers=4) as executor:  # Set max_workers=1 for sequential execution
    futures = [executor.submit(send_request, i) for i in range(1, 10000)]
    for future in as_completed(futures):  # Ensure completion of all tasks
        future.result()
l_t = time.time()
t= l_t-s_t
print(f"TIME TAKEN : {t} SECONDS")
