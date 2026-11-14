# Emergency Contact Management System

## Project Overview

The Emergency Contact Management System is a Java-based application designed to manage and organize emergency contact information.

The project uses Java for application development, MySQL for database management, and JDBC for connectivity between Java and MySQL.

## Technologies Used

- **Programming Language:** Java
- **Database:** MySQL
- **Database Connectivity:** JDBC
- **IDE:** Visual Studio Code
- **Database Driver:** MySQL Connector/J

## Project Objectives

- To develop an organized system for managing emergency contact information.
- To provide a Java-based interface for interacting with contact records.
- To store contact information using a MySQL database.
- To demonstrate database connectivity using JDBC.

## Project Files

| File | Description |
|---|---|
| `EmergencyContactManagement.java` | Main Java application |
| `DBConnection.java` | Handles MySQL database connectivity |
| `TestConnection.java` | Tests the database connection |
| `lib/` | Contains the MySQL JDBC connector |
| `run.bat` | Windows batch file for running the project |

## Database Configuration

The application connects to a MySQL database using JDBC.

For security, database credentials are read from Windows environment variables rather than being stored directly in the source code.

Required environment variables:

- `DB_USER` – MySQL username
- `DB_PASSWORD` – MySQL password
- `DB_URL` – Optional JDBC database URL

## How to Run the Project

1. Install Java JDK and MySQL Server.
2. Download or clone this repository.
3. Open the project folder in Visual Studio Code.
4. Ensure the required MySQL database and tables have been created.
5. Configure the `DB_USER` and `DB_PASSWORD` environment variables.
6. Make sure the MySQL JDBC connector is available in the `lib` folder.
7. Run the project using the provided `run.bat` file or your configured Java environment.

## Project Purpose

This project was developed as an academic project to demonstrate Java programming, database connectivity, and emergency contact information management.
