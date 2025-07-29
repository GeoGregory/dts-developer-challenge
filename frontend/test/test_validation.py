import unittest
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.chrome.options import Options
from datetime import datetime, timedelta
import time

class TaskFormValidationTest(unittest.TestCase):

    @classmethod
    def setUpClass(cls):
        chrome_options = Options()
        chrome_options.add_argument("--headless")
        service = Service()
        cls.driver = webdriver.Chrome(service=service, options=chrome_options)
        cls.driver.get("http://localhost:5000/create")

    @classmethod
    def tearDownClass(cls):
        cls.driver.quit()

    def clear_and_submit(self, title, description, due_date):
        self.driver.get("http://localhost:5000/create")
        self.driver.find_element(By.ID, "title").clear()
        self.driver.find_element(By.ID, "description").clear()

        self.driver.find_element(By.ID, "title").send_keys(title)
        self.driver.find_element(By.ID, "description").send_keys(description)
        self.driver.execute_script(
            f"document.getElementById('dueDate').value = '{due_date}';"
        )
        self.driver.find_element(By.CSS_SELECTOR, "button[type='submit']").click()
        time.sleep(1)

    def test_empty_title_validation(self):
        future_date = (datetime.now() + timedelta(days=1)).strftime("%Y-%m-%dT%H:%M")
        self.clear_and_submit("   ", "Valid description", future_date)
        error = self.driver.find_element(By.ID, "titleError").text
        self.assertIn("Title cannot be empty", error)

    def test_special_characters_in_title(self):
        future_date = (datetime.now() + timedelta(days=1)).strftime("%Y-%m-%dT%H:%M")
        self.clear_and_submit("@@Invalid!!", "Valid description", future_date)
        error = self.driver.find_element(By.ID, "titleError").text
        self.assertIn("Title cannot contain special characters", error)

    def test_past_due_date_validation(self):
        past_date = (datetime.now() - timedelta(days=1)).strftime("%Y-%m-%dT%H:%M")
        self.clear_and_submit("Valid Title", "Valid description", past_date)
        error = self.driver.find_element(By.ID, "dueDateError").text
        self.assertIn("Due date must be in the future", error)

    def test_special_characters_in_description(self):
        future_date = (datetime.now() + timedelta(days=1)).strftime("%Y-%m-%dT%H:%M")
        self.clear_and_submit("Valid Title", "###invalid###", future_date)
        error = self.driver.find_element(By.ID, "descriptionError").text
        self.assertIn("Description cannot contain special characters", error)

    def test_valid_submission(self):
        future_date = (datetime.now() + timedelta(days=1)).strftime("%Y-%m-%dT%H:%M")
        self.clear_and_submit("Valid Title", "Simple description", future_date)
        current_url = self.driver.current_url
        self.assertIn("http://localhost:5000/", current_url)

if __name__ == '__main__':
    unittest.main()
