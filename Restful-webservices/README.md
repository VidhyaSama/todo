# Synchrony useCase

- UserRegistration 
- User login
- upload/view/delete imgaes associated to user
- TestCases using MockMVC
- SpringSecurity+JWT
- Comments , loggers , exception handling 

## URLS
Once application is up below SwaggerURL can be accessed
### Swagger - http://localhost:8080/user/swagger-ui.html

## USER_CONTROLLER

### Add a course: post/course

Request
```
{
  "courseCode": "101",
  "courseDescription": "Ecology",
  "price": 1000
}
```

Response 
```
status: 201
Undocumented
Response headers
 connection: keep-alive 
 content-length: 0 
 date: Sat,25 Dec 2021 15:27:46 GMT 
 keep-alive: timeout=60 
 location: http://localhost:8082/upskill/course 
```

### Add a course: post/course with validation error

Request 
```
{
  "courseCode": "string",
  "courseDescription":"",
  "price": 0
}
```
Response 
```
{
  "timeStamp": "2021-12-25T15:19:40.357+00:00",
  "message": "Validation failed",
  "details": [
    "Course Description cannot be blank",
    "courseDescription should contain atleast 3 and atmost 16 in length"
  ]
}
```


### Update course Details:put/course/1

Request 
``` 
{
  "courseCode": "110",
  "courseDescription": "cosmotology",
  "price": 900
}
```

Response 
```
{
    "courseCode": "110",
    "courseDescription": "cosmotology",
    "price": 900
}
```

Note: Based on our requirement we can update any field. 

### Get course based on id : get/course/1

Response
``` 
{
    "courseCode": "101",
    "courseDescription": "Ecology",
    "price": 1000
}
```

* Get request to a non existing resource.
* The response shows a Customized Message Structure

Response  404 NOT_FOUND

```
{
  "timeStamp": "2021-12-25T16:03:18.379+00:00",
  "message": "Record Not Found",
  "details": [
    "Course Not Found"
  ]
}
```

### Get all courses: get/course

Response 
```
[
  {
    "courseCode": "101",
    "courseDescription": "Ecology",
    "price": 1000
  },
  {
    "courseCode": "110",
    "courseDescription": "cosmotology",
    "price": 900
  },
  
]
```

### Delete course :Delete/course/1 

* Removes the course along with mapped students.

Response
```
204 - NO_CONTENT 
```

## STUDENT_CONTROLLER
---------------------

### Add student with course: post/student

Request
``` 
{
  "name": "vidhya",
  "rollNumber": "12jj19",
  "course": {
    "courseCode": "110"
  }
}
```

Response
```
201 - created
```
### Add student with course: post/student - with validations

Roll Number should be unique.

Response status is 302 

```
{
  "timeStamp": "2021-12-25T16:54:57.400+00:00",
  "message": "RecordAvailable",
  "details": [
    "12jj19 Already registered with course "
  ]
}
```

## Get all studentDetails : get/student

Response
```
[
  {
    "name": "vidhya",
    "rollNumber": "12jj12",
    "course": {
      "courseCode": "110",
      "courseDescription": "cosmotology",
      "price": 900
    }
  }
]
```

## Get student with given id : get/student/1

Response
```
{
  "name": "vidhya",
  "rollNumber": "12jj12",
  "course": {
    "courseCode": "110",
    "courseDescription": "cosmotology",
    "price": 900
  }
}
```

* Get request to a non existing resource.
* The response shows a Customized Message Structure.

Response  404 NOT_FOUND 

```
{
  "timeStamp": "2021-12-25T16:58:57.887+00:00",
  "message": "Record Not Found",
  "details": [
    "Student not Found"
  ]
}
```

### Update student details:put/student/1

Request
```
{
  "name": "sama vidhya",
  "rollNumber": "12jj14",
  "course": {
    "courseCode": "101"
  }
}
```
Response
```
{
  "name": "sama vidhya",
  "rollNumber": "12jj14",
  "course": {
    "courseCode": "101",
    "courseDescription": "ecology",
    "price": 1000
  }
}
```
### Delete student:delete/1

Response
```  
204 NO_CONTENT 
```

