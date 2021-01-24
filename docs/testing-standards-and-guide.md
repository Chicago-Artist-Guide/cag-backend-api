# Testing Standards & Guide

In order to ensure our application is always running as intended throughout the lifetime of the project development, we must write good tests to ensure the behavior of our project is correct in ALL use cases.

### Organizing Our Test Files

In our **/src/test/** we have a **java/** folder with a **com.cag.cagbackendapi/** folder inside that. Inside **com.cag.cagbackendapi/** there is are folders: **controllers/**, **services/**, **util/**, and **integration/**. Notice how this directory structure mirrors our **src/main/java/com.cag.cagbackendapi/** structure. When working on a spring project, the *unit* testing classes should mirror the location of the files you're testing in your **src/main/java/** project. For example: We have the class **src/main/java/com.cag.cagbackendapi/controllers/UserController.java** and the unit testing file is located at **src/test/java/com.cag.cagbackendapi/controllers/UserControllerTest.kt** (NOTE: I chose to implement this class in Java and test it in Kotlin, but you could do both in Java or both in Kotlin as well). You'll notice the same structure for the **services/**. This structure is how unit tests are supposed to be in Spring and if you don't follow this convention Spring will give you weird errors. The **util/** and **integration/** testing folders do not mirror our **src/main/** structure but we'll talk about that next!

### Unit Tests Vs Integration Tests

There are two different types of tests: **Integration Tests** and **Unit Tests**. The purpose of a *Unit Test* is to mock the behavior of our application using fake data and fake dependencies. The purpose of an *Integration Test* is to actually start the application and test it with real data using real dependencies. We use both together to cover all potential cases our app will encounter and to ensure that new changes made to our application will not break old features.

For example: Let's say our first story is to implement user registration in our application. A user signs up using a name (required) and a email (required) and then they're registered in our app. We write a unit test each of the cases including: (1) User registers with a blank name, (2) null name, (3) blank email, (4) null email, (5) null name and null email, (6) blank name and email, (7) valid name and email. The unit test will also ensure that all of our functions that are called aren't called more times than necessary and that all of our dependencies (like the logger) are working correctly. Then we write out integration test which actually starts the application, creates a request with a valid user and sends the request to our app. We do the same integration tests that we did for our unit tests and verify we get the correct valid responses and the correct error responses. The integration tests ensure that the actual response you get from the app are correct, while the unit test ensure that our dependencies are getting used correctly and teh correct number of times. We use a logger in our app to log when events happen. If the logger breaks, the integration test won't catch that but the unit test will. If our database is down, the unit test doesn't use the database and will still pass but the integration test that sends real data to the database will fail which will inform us that we need to fix the problem. There are many many more examples of unit tests and integration tests working together to make our development faster and easier - even though it is tedious and time-consuming to have to write them. They save us time in the long run.   

### Integration Test File Location

Integration tests do not need to have their testing files mirror the location of the actual implementation files in **src/main/** like unit tests do. We'll dump all of our integration testing files into our **integration/** directory inside our **src/test/java/com.cag.cagbackendapi/** directory.

NOTE: We should make a unique integration testing class for each controller we are trying to test to be organized.

### SpringCommandLineProfileResolver

There is a **util/** folder in our testing directory that has the **SpringCommandLineProfileResolver.java** class in it. This class is used to ensure our integration tests use the correct Spring profile to run our integration tests. This is important for you because if you're on a Mac and you run integration tests using the Windwos profile - all of your tests will fail. You can make sure the correct profile is being run in your integration test file by seeing the **@ActiveProfiles** annotation at the top of the class. Example of using the WINDOWS profile on the UserControllerIntegration testing class:

```
// NOTE: Update active profile to reflect your operating system to connect to database
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(value = ["WINDOWS"], resolver = SpringCommandLineProfileResolver::class)
class UserControllerIntegrationTests {
```

### Naming Tests

When you create a new test you'll add a new function to the testing class. The function will work regardless of the name - but we want to name our testing functions in a way that makes it easy to knwo what they do. The naming convention that is most popular is:

```
{nameOfWhatYouAreTesting}_{inputDescription}_{resultDescription}
```

For example, some of my test names include:

```
(User Controller Integration Tests)
registerUser_validInput_201Success
registerUser_emptyFirstFirstName_400BadRequest
registerUser_nullEmailAndFirstName_400BadRequest
(Validation Service Unit Tests)
validateAuthKey_emptyAuthKey_401Unauthorized
validateAuthKey_wrongAuthKey_401Unauthorized
(User Service Unit Tests)
registerUser_validUser_logsAndSucceeds
registerUser_missingFirstNameAndEmail_BadRequest
registerUser_validInputWithDatabaseDown_InternalServerError
```

See how easy it is to read the test name and know exactly what's supposed to happen? That's what we want in all of our unit tests and integration tests!
