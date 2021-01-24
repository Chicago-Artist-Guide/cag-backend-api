# Chicago Artist Guide Backend API Project

This backend API powers our frontend react application using tools including: Java, Kotlin, Spring Boot, Docker, Postgres. The Chicago Artist Guide web app is a place for Chicago theatre companies, individual artists, and communities to come together and facilitates the casting process for theatre organizations.

## To develop on and run this project you'll need to install these free apps:

- Intellij IDEA Community Edition: https://www.jetbrains.com/idea/download/#section=windows
- Docker: https://docs.docker.com/get-docker/
- Postman: https://www.postman.com/downloads/
- Git: https://git-scm.com/downloads

## Configuring Intellij & Installing Java JDK 11

To open the project in Intellij, open Intellij and click "Open or Import project". Navigate to the project **_and double click the build.gradle file_** to open the project.

To install Java JDK 11, Intellij has a convenient tool that makes it very easy to install. Go to **File -> Project Structure -> Project SDK -> Add SDK -> Download SDK**.

Once you are at this menu, make the Vendor **AdoptOpenJDK** and select version **11.0.9.1**. Download that, click "Apply", and you should be good! See the image below:

![alt tag](./docs/imgs/install-java-jdk-11.JPG)

Now that you have your JDK 11, go to: **File -> Settings -> Build, Execution, Deployment -> Build Tools -> Gradle -> Gradle JVM** and select AdoptJdk11 as your Gradle JVM. Click "Apply" and wait for the project to reload.

## Configuring Intellij Lombok Plugin

Lombok is a tool we use with Java to make creating model classes easier, but it's a bit of a pain to setup. The first step is to go to **File -> Settings -> Plugins -> Marketplace** and then search for "Lombok". It should look like the below picture, install it and apply changes:

![alt tag](./docs/imgs/lombok-plugin.JPG)

Now that you have the plugin we need to apply Annotation Processing to the project in order to use the plugin. To do this, go to: **File -> Settings -> Build, Execution, Deployment -> Compiler -> Annotation Processing**. Once you are at this menu, enable Annotation Processing by checking the checkbox like this:

![alt tag](./docs/imgs/enable-annotation-processing.JPG)

## Configuring Intellij DB Navigator Plugin

DB Navigator is a plugin we use to manage our database connections that works great on both windows and mac. The first step is to go to **File -> Settings -> Plugins -> Marketplace** and then search for "DB Navigator". It should look like the below picture, install it and apply changes:

![alt tag](./docs/imgs/db-navigator-plugin.JPG)

## Configuring Your Containerized Postgres Database Using DB Navigator & Docker

The first step to running the project is to setup your database because if your database is unavailable at the time you run the project it will crash.

To do this: If you're on mac you can do to your terminal, if you're on windows go to Docker Quickstart Terminal. Then "cd" into the project and into the "docker/" directory. From there, run the command: "docker-compose up". This should download the postgres docker container and start to run your postgres database.

![alt tag](./docs/imgs/windows-docker-run-container.JPG)

With the DB Navigator plugin installed, the **DB Browser** menu should appear in your intellij. Open the DB Browser menu, click the green plus sign, add a new PostgreSQL connection. On the menu that appears to add the connection, populate it with values like this:

Windows:

![alt tag](./docs/imgs/windows-db-navigator-config.JPG)

MacOS:

![alt tag](./docs/imgs/macos-db-navigator-config.png)

> NOTE: The only difference in Windows & MacOS is the host address

## Setting Up Your Build Configuration & Running The Project

Now that your database is running, navigate in Intellij to the: **src -> main -> java -> com.cag.cagbackendapi -> CagBackendApiApplication** file. Once in there, if your gradle is working properly you should see a green play button next to the left of the class. Click this play button and run the app. If you're on windows, it will crash because we need to specify the correct spring profile in the build configuration. Go to the build configuration as seen in the image below, and set the **VM options** to: **-Dspring.profiles.active=WINDOWS** in your build configuration. Click apply, and try to run again.
 
![alt tag](./docs/imgs/build-config.jpg)

See my windows build config for reference:

![alt tag](./docs/imgs/windows-build-config.JPG)

MacOS Build Config:

![alt tag](./docs/imgs/macos-build-config.png)

> NOTE: If you're using MacOS - you don't need to add the "VM options" for your build configuration because the default Spring profile works on MacOS since it has the host address "localhost". However, you will need to change the active spring profile in our integration test classes from "WINDOWS" to "MACOS" if you want to be able to run our tests on MacOS.

## Running Our Tests

Along with our features we develop, we'll also include testing for each specific use case our feature could possibly encounter. To run our tests, the first thing you'll need to do is ensure that your Intellij preferences build and run our tests from **Gradle** and not **Intellij**. Go to: **Intellij -> Preferences -> Build, Execution, Deployment -> Build Tools -> Gradle**. From there, select: **Gradle** for the "Build and run using:" and the "Run tests using:" drop-down menus. It should look like this when you're done:

![alt tag](./docs/imgs/build-and-run-using-gradle.png)

Now that your settings are correct, make sure your database is up and running. You'll now be able to right-click on the "test" folder and run all of our project tests. See the image below for the correct button to click and the resulting passing tests:

![alt tag](./docs/imgs/run-tests.png)

## Postman

Now that you're running your project that's connected to the database, let's hit some endpoints! Open Postman. In Postman, click the black "Import" button on the top left of the Postman App next to the "+ New" button. Now navigate to the project directory and click the latest postman collection in the **postman** folder. This will import the collection into your Postman and you'll be able to run the requests in the collection against our app running on your local machine. See below image for help importing:

![alt tag](./docs/imgs/postman-import-collection.jpg)

In our collection, you'll see a **local/** and **prod/** directory. Our local requests point to the local app running on our local machines, while the prod folder will point to our deployed application. If your app is running in Intellij and your postgres database is running in docker - you should be able to use the saveUser request in the local directory in our postman collection to save a new test user to our database. It should look like this when you run it:

![alt tag](./docs/imgs/postman-save-user-request.jpg)

## Swagger UI

To make it even easier to interact with our endpoints - I added Swagger-UI. Now when you run the application and view it in your browser you'll be taken to our Swagger-UI page where you can send requests to any of our endpoints just like you would in Postman. The URL I use to get to this page is: **http://localhost:9000/**.

![alt tag](./docs/imgs/swagger-ui.JPG)

Once on this screen, you can click any of our requests, then click the "Try It Out" button on the request. You'll then be prompted to enter in all the information for the request. In this case, it's the authKey and the user request body:

![alt tag](./docs/imgs/swagger-register-user-request.JPG)

Now that you've entered this information you can click the blue "Execute" button to send the request:

![alt tag](./docs/imgs/swagger-register-user-response.JPG)

If you scroll down, you'll see the response from the request. In this case, I got 201 and the created user details returned back to me.
