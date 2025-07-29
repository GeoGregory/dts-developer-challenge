## HMCTS Task Manager

### Project Overview

This project provides a task management system for HMCTS caseworkers. It includes:

- A **Spring Boot** backend API for creating, updating, retrieving, and deleting tasks.
- A **Flask** frontend for interacting with tasks through a web interface.
- A **SQLite** database to persist tasks locally.
- Integrated **validation**, **unit tests**, and **clean UI styling**.

---

### Technical Stack

#### Backend:

- Java 17
- Spring Boot
- Spring Data JPA (with SQLite)
- Hibernate Validator
- JUnit 5

#### Frontend:

- Python 3.8+
- Flask
- HTML/CSS
- Jinja2 templating

#### Testing:

- JUnit 5 (Backend)
- Selenium (Frontend validation)
- Hibernate Validator (Model validation)

---

### Prerequisites

#### Backend:

- Java 17+
- Gradle
- SQLite (optional for inspecting the DB)

#### Frontend:

- Python 3.8+
- pip / virtualenv

---

### Setup and Installation

#### Backend:

1. Navigate to the backend directory.
2. Build the project:
   ```bash
   ./gradlew build
   ```
3. Run the application:
   ```bash
   ./gradlew bootRun
   ```
4. The backend API will be available at `http://localhost:8080/api/tasks`.

#### Frontend:

1. Navigate to the frontend directory.
2. Set up a virtual environment:
   ```bash
   python3 -m venv venv
   source venv/bin/activate
   ```
3. Install dependencies:
   ```bash
   pip install -r requirements.txt
   ```
4. Start the Flask server:
   ```bash
   python app.py
   ```
5. Visit `http://localhost:5000` in your browser.

---

### Running the Application with Database Support

The Flask frontend uses SQLite through SQLAlchemy to store tasks locally for searching and rendering.
To initialize the database:

1. Start the db up with:

   ```bash
   python init_db.py
   ```

The database file (`tasks.db`) will be created in the root directory.

---

### Running Tests

#### Backend Tests:

From the backend project root, run:

```bash
./gradlew test
```

Includes:

- Unit tests for `TaskManagerControllerTest`, `TaskValidationTest`
- Model validation tests for `Task` fields (`@NotBlank`, `@NotNull`)

#### Frontend Tests:

Ensure the Flask app is running, navigate to the test folder, then run the Selenium tests:

```bash
python test_validation.py
```

> Requires [ChromeDriver](https://chromedriver.chromium.org/downloads) in your PATH.

---

### Endpoints:

| Method | Endpoint       | Description               |
| ------ | -------------- | ------------------------- |
| POST   | `/`            | Create a new task         |
| GET    | `/`            | Get all tasks             |
| GET    | `/{id}`        | Get a specific task by ID |
| PUT    | `/{id}/status` | Update task status        |
| DELETE | `/{id}`        | Delete a task by ID       |

