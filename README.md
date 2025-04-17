# 2525-04 Java Developer Coding Assignment

## Prerequisites
- **Java Version**: Java 1.8
- **Maven Version**: 3.8.1
- **Spring Boot**: 2.7.13

## API Interface Specification

### Base URL
```
http://localhost:8080/api
```

### `/book` API Endpoints

#### 1. Create a Book
- **Endpoint**: `POST /book`
- **Description**: Creates a new book.
- **Request Body**:
  ```json
  {
    "title": "string",
    "author": "string",
    "published": "boolean"
  }
  ```
- **Response**:
  - **201 Created**: Returns the created book object.
  ```json
  {
    "success": "boolean",
    "data":{
        "id": "integer",
        "title": "string",
        "author": "string",
        "published": "boolean"
    },
    "message": "string"
  }
  ```

#### 2. Get a Book by author and/or published status
- **Endpoint**: `GET /book`
- **Description**: Retrieves a book by author and or published status.
- **Query Parameter**:
  - `author` (string): The author of the book.
  - `published` (boolean) The published status of the book.
- **Response**:
  - **200 OK**: Returns the book object array.
  ```json
  {
    "success": "boolean",
    "data":[
        {
            "id": "integer",
            "title": "string",
            "author": "string",
            "published": "boolean"
        }
    ],
    "message": "string"
  }
  ```

#### 4. Delete a Book
- **Endpoint**: `DELETE /book/{id}`
- **Description**: Deletes a book by its ID.
- **Path Parameter**:
  - `id` (integer): The ID of the book.
- **Response**:
  - **200 OK**: Returns the success message if the book was successfully deleted.
  ```json
  {
    "success": "boolean",
    "data": null,
    "message": "string"
  }
  ```

## How to Run the Project

1. Clone the repository:
   ```bash
   git clone https://github.com/dereklpm/java-assignmet-202504.git
   ```
2. Navigate to the project directory:
   ```bash
   cd java-assignment-202504
   ```
3. Build the project:
   ```bash
   mvn clean install
   ```
4. Run the application:
   ```bash
   mvn spring-boot:run
   ```
5. Access the API at `http://localhost:8080/api`.

## How to Test the Project
1. Test the project using Maven
   ```bash
   mvn test
   ```
   
## Testing with Postman
1. Create a Book
![image](https://github.com/user-attachments/assets/b11e4723-e0cd-4854-90b8-82655ea975cd)

2. Get Book
![image](https://github.com/user-attachments/assets/a40eaa60-6835-4639-b022-5d21adb1212b)

3. Delete a Book
![image](https://github.com/user-attachments/assets/7ffbb133-0a80-4cdc-bdf8-24fbb281b22f)

