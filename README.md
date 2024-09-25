# Tuitr API

## Description
This repository contains the code for a REST API similar to Twitter, referred to as "Tuitr API". The project implements the core functionalities of a social media platform, including user interactions, posting, and fetching data. The API is built using Java, Spring Boot, and Maven, with an ephemeral in-memory data layer powered by the H2 database.

## Technologies Used
- **Java 8**
- **Maven project**
- **Spring Framework with Spring Boot 2.4**

## Prerequisites
Before running the project, ensure you have the following installed on your machine:

- **JDK 8+**: The API requires at least Java 8 to run. Make sure the Java Development Kit is installed. You can verify the installed Java version and confirm the JDK is in use with the following commands:
  ```bash
  java -version
  javac -version
  ```

## How to have the API up and running locally

### Building from sources

Whatever your operating system, you can run the commands in your preferred command prompt. The project uses Make (for Unix-based systems) and batch scripts (for Windows) to encapsulate basic functions you’ll need to perform.

- Test the project:
  ```bash
  make test
  ```

- Run the project:
  ```bash
  make run
  ```

### Running the Java executable 🏗️

This will still be done

The API will be available locally on http://localhost:8080. You can see the interactive documentation over the http://localhost:8080/swagger-ui/index.html URL.

<img width="1792" alt="Screenshot 2024-09-25 at 11 53 10" src="https://github.com/user-attachments/assets/f60a7f2b-f8ee-4ec2-b223-a3b1886081d8">
