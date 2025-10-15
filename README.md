# BookNova

BookNova is a comprehensive library management system developed in Java. It features a layered architecture and utilizes JDBC for database connectivity, following the DAO pattern for data access. The graphical user interface is built with `JOptionPane` for simplicity and ease of use.

## Features

- **Book Management**: Full CRUD operations for books, including ISBN validation, stock control, and status tracking.
- **User Management**: Role-based access control with ADMIN and ASSISTANT roles, including secure authentication.
- **Member Management**: Member registration, editing, and status validation.
- **Loan Management**: Handling of book loans and returns, with automated fine calculation for overdue items.
- **CSV Exports**: Generation of CSV files for the entire book catalog and for overdue loans.
- **Custom Exceptions**: Robust error handling with custom exceptions like `ValidationException` and `BusinessException`.
- **Logging**: Application events are logged to `app.log`.
- **Unit Tests**: Business logic and calculations are thoroughly tested with JUnit 5.

## Tech Stack

- **Java**: Core programming language.
- **JDBC**: For database connectivity.
- **MySQL**: Relational database management system.
- **JUnit 5**: For unit testing.
- **JOptionPane**: For the graphical user interface.

## Project Structure

```
com.libronova
├── config
│   └── config.properties
├── dao
│   └── impl
├── model
│   ├── Book.java
│   ├── User.java
│   ├── Member.java
│   └── Loan.java
├── service
├── ui
├── exceptions
│   ├── ValidationException.java
│   └── BusinessException.java
└── helpers
```

## Installation

1.  **Prerequisites**:
    *   Java Development Kit (JDK) 8 or higher installed.
    *   MySQL Server installed and running.
    *   Git installed.

2.  **Clone the repository**:
    ```bash
    git clone https://github.com/your-username/BookNova.git
    cd BookNova
    ```

3.  **Compile the source code**:
    If you are not using an IDE, you can compile the project from the root directory of the project.
    ```bash
    javac -d bin src/com/libronova/*/*.java src/com/libronova/*/*/*.java
    ```
    *Note: If you are using an IDE like Eclipse or IntelliJ IDEA, you can import the project and it will handle the compilation automatically.*

## Database Setup

1.  **Create the Database**:
    Open your MySQL client and run the following command to create the database:
    ```sql
    CREATE DATABASE booknova_db;
    ```

2.  **Configure Connection Properties**:
    Navigate to `src/com/libronova/config/config.properties` and update the following properties with your MySQL credentials:
    ```properties
    db.url=jdbc:mysql://localhost:3306/booknova_db
    db.user=your_username
    db.password=your_password
    ```

3.  **Run SQL Scripts**:
    You will need to create the database schema and insert some sample data. The SQL scripts for this (`schema.sql` and `data.sql`) should be located in the `database` directory of this project. Execute them using a MySQL client.
    ```bash
    mysql -u your_username -p booknova_db < database/schema.sql
    mysql -u your_username -p booknova_db < database/data.sql
    ```
    *Note: If the `database` directory or the SQL files are not available, you will need to create them. Below is an example of how you could structure `schema.sql` based on the project's entity classes.*

    **Example `schema.sql`:**
    ```sql
    CREATE TABLE users (
        id INT AUTO_INCREMENT PRIMARY KEY,
        username VARCHAR(50) NOT NULL UNIQUE,
        password VARCHAR(255) NOT NULL,
        role VARCHAR(20) NOT NULL
    );

    CREATE TABLE members (
        id INT AUTO_INCREMENT PRIMARY KEY,
        name VARCHAR(100) NOT NULL,
        email VARCHAR(100) NOT NULL UNIQUE,
        status VARCHAR(20) NOT NULL
    );

    CREATE TABLE books (
        id INT AUTO_INCREMENT PRIMARY KEY,
        isbn VARCHAR(20) NOT NULL UNIQUE,
        title VARCHAR(255) NOT NULL,
        author VARCHAR(255) NOT NULL,
        stock INT NOT NULL,
        status VARCHAR(20) NOT NULL
    );

    CREATE TABLE loans (
        id INT AUTO_INCREMENT PRIMARY KEY,
        book_id INT NOT NULL,
        member_id INT NOT NULL,
        loan_date DATE NOT NULL,
        return_date DATE,
        fine DECIMAL(10, 2),
        FOREIGN KEY (book_id) REFERENCES books(id),
        FOREIGN KEY (member_id) REFERENCES members(id)
    );
    ```

## How to Run

1.  **Download MySQL Connector/J**:
    Make sure you have the MySQL JDBC driver JAR file. You can download it from the official [MySQL website](https://dev.mysql.com/downloads/connector/j/).

2.  **Run from the command line**:
    From the root directory of the project, run the following command. Make sure to replace `path/to/mysql-connector-java.jar` with the actual path to your MySQL connector JAR file.
    ```bash
    java -cp "bin:path/to/mysql-connector-java.jar" com.libronova.ui.Main
    ```
    *Note: Replace `com.libronova.ui.Main` with the actual main class of the application if it is different.*

3.  **Run from an IDE**:
    If you are using an IDE, you can run the main class directly from the IDE. Make sure to add the MySQL Connector/J JAR to the project's build path.

## Testing

The project uses JUnit 5 for unit testing.

1.  **Run from an IDE**:
    Most modern IDEs like Eclipse and IntelliJ IDEA have built-in support for JUnit. You can run the tests by right-clicking on the test class or the `tests` directory and selecting "Run as JUnit Test".

2.  **Run from the command line**:
    To run the JUnit 5 tests from the command line, you'll need the JUnit Platform Console Standalone JAR.
    -   **Download**: You can download it from the [Maven Central Repository](https://search.maven.org/search?q=g:org.junit.platform%20AND%20a:junit-platform-console-standalone).
    -   **Run the tests**:
        ```bash
        java -jar path/to/junit-platform-console-standalone.jar --class-path "bin:path/to/mysql-connector-java.jar" --scan-class-path
        ```
        Make sure to replace the paths to the JUnit JAR and the MySQL connector JAR with the actual paths on your system. This command will scan the classpath for tests and execute them.

## Usage

Once the application is running, you will be prompted with a series of menus to perform various actions. Below are some examples of the workflows.

-   **Logging In**: You will first be prompted to log in with a username and password. Based on your role (ADMIN or ASSISTANT), you will have access to different sets of features.

-   **Book Management**:
    -   *Add a new book*: Provide the ISBN, title, author, and initial stock. The system will validate the inputs and confirm that the book has been added.
    -   *Search for a book*: You can search for books by title or author to view their details, including stock and status.

-   **Member Management**:
    -   *Register a new member*: Enter the member's name and email. The system will create a new member with an "ACTIVE" status.
    -   *Edit member details*: You can update a member's name or email.

-   **Loan Management**:
    -   *Loan a book*: Select a member and a book to create a new loan. The system will check if the book is in stock and if the member is eligible for a new loan.
    -   *Return a book*: When a book is returned, the system calculates any applicable fines for overdue returns and updates the book's stock.

## Logging and Exceptions

-   **Logging**: The application logs important events and errors to `app.log`. This file is located in the root directory of the application.
-   **Custom Exceptions**: The application uses custom exceptions to handle specific errors:
    -   `ValidationException`: Thrown for input validation errors.
    -   `BusinessException`: Thrown for business rule violations (e.g., trying to loan a book that is out of stock).

## Contributing

Contributions are welcome! If you find a bug or have a feature request, please open an issue on GitHub. If you would like to contribute code, please fork the repository and submit a pull request.

## License

This project is licensed under the MIT License. See the `LICENSE` file for details.

## Acknowledgments

-   A big thank you to everyone who has contributed to this project.
