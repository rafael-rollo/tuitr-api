# Tuitr API

## Description
This repository contains the code for a REST API of some app similar to Twitter, referred to as "Tuitr API". The project implements the core functionalities of a social media platform, including user interactions, posting, etc. The API is built using Java, Spring Boot, and Maven, with a transient in-memory data layer powered by the H2 database.

## Technologies Used
- **Java 8**
- **Maven project**
- **Spring Framework with Spring Boot 2.4**

## How to have the API up and running locally

### Building from sources

> [!NOTE]
> Before building and running the project, ensure you have the following installed on your machine:
>
>**JDK 8+**: The API requires at least Java 8 to run. Make sure the Java Development Kit is installed. You can verify the installed Java version and confirm the JDK is in use with the following commands:
> ```bash
> java -version
> javac -version
> ```

Whatever your operating system, you can run the commands in your preferred command prompt. The project uses Make (for Unix-based systems) and batch scripts (for Windows) to encapsulate basic functions you’ll need to perform.

- For testing the project:
  ```bash
  make test
  ```

- For running the project:
  ```bash
  make run
  ```

By default, the API is available locally on http://localhost:8080. However, you can update the `src/main/resources/application.properties` file to serve on a custom port.

### Running the Java executable

> [!NOTE]
> Although you don't need to have a JDK installed, it is important to note that at least the JRE version 8 or higher is required for this to work.

Alongside the releases listed on this repo (that you can access [here](https://github.com/rafael-rollo/tuitr-api/releases)), you will find a `.jar` asset for each one so you can download and execute directly, disregarding building from sources (JDK required). You can check what each version includes via the release changelog.

After downloading the JAR file, you can run the following command from your command prompt:

```bash
java -jar tuitr-api-1.0.0.jar 
```

By default, the API is available locally on http://localhost:8080. If you want to serve on another port, you can run passing a VM arg like:

```bash
java -Dserver.port=8000 -jar tuitr-api-1.0.0.jar
```

## Interacting with the API

You can see the interactive documentation over the http://localhost:8080/swagger-ui/index.html URL.

<img width="1792" alt="Screenshot 2024-09-25 at 11 53 10" src="https://github.com/user-attachments/assets/f60a7f2b-f8ee-4ec2-b223-a3b1886081d8">

You can access the documented resources to simulate the app's usage. While the server is running, the data you create is there. Once the process is taken down, the data is destroyed, and a default set is available again next time by running a database migration with a dump file on startup. This dynamics allows you to code a set of features in a client app that goes beyond the classic read-only operations you can have by using the most famous public APIs out there. Enjoy!
