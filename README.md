# project-management

1. Clone the Repository
bash
Copy code
git clone https://github.com/melckzedeck063/project-management.git

cd project-management
3. Configure Environment Variables
The project uses environment variables to manage sensitive credentials. You can set them in your operating system or use a .env file if supported by your IDE.

Required Environment Variables
Variable	Description
DB_HOST	Database host (e.g., localhost)
DB_PORT	Database port (default: 5432)
DB_NAME	Name of the PostgreSQL database
DB_USERNAME	PostgreSQL username
DB_PASSWORD	PostgreSQL password


Set Environment Variables
On Linux/macOS:
bash

export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=project_db
export DB_USERNAME=your_username
export DB_PASSWORD=your_password
export LOGGED_USER_API_KEY=your_api_key

On Windows (Command Prompt):
cmd

set DB_HOST=localhost
set DB_PORT=5432
set DB_NAME=project_db
set DB_USERNAME=your_username
set DB_PASSWORD=your_password

Using .env file (Optional):

Create a .env file in the project root:
env

DB_HOST=localhost
DB_PORT=5432
DB_NAME=project_db
DB_USERNAME=your_username
DB_PASSWORD=your_password

Ensure your IDE or Spring Boot reads environment variables from .env.


4. Start the Application
To start the application, use the following command:

mvn spring-boot:run

The application will be accessible at http://localhost:8080 by default.

Running Tests
To run unit and integration tests, execute:

mvn test
