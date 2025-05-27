# Univeristy departments manager

A simple **Spring Boot** console application to manage university **Departments** and **Lectors**, wiht support of MySQL database and Docker.  

## Description

- **Domain**  
  - **Department**: has a unique name, a single head (a Lector), and many Lectors.  
  - **Lector**: has a name, one of three degree values (`ASSISTANT`, `ASSOCIATE_PROFESSOR`, `PROFESSOR`), a salary, and can belong to many Departments.

- **For persistence**, uses a **Spring Data JPA** + **Flyway** migrations
  
- **Console interface** via a `CommandLineRunner` that supports the instructions stated in tge Google Doc for the task.

## Prerequisites

- Docker & Docker Compose
- (Optional) Java 17+ & Gradle, if you want to run locally without Docker

## How to run

1. Clone the GitHub repo
2. Start the DB:
   ```bash
   docker-compose up -d db
   ```
3. Build & run the app
   ```bash
   docker-compose run app
   ```
## Usage

After successfull start, Flyway will apply migrations with the data I predefined. App will show the possible commands. You can either type `Ctrl+C` or `exit` in the terminal to quit.
