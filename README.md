# Chicago Artist Guide Backend API Project

This backend API powers our frontend react application using tools including: Java, Kotlin, Spring Boot, Docker, Postgres. The Chicago Artist Guide web app is a place for Chicago theatre companies, individual artists, and communities to come together and facilitates the casting process for theatre organizations.

## To develop on and run this project you'll need to install these free apps:

- Intellij IDEA Community Edition: https://www.jetbrains.com/idea/download/#section=linux
- Git: https://git-scm.com/downloads

### Optional Apps That Also Help:

- Docker (If you're on MacOS, it's very hard to setup on Windows - but if you are using windows.... what are you doing): https://docs.docker.com/get-docker/
- Postman: https://www.postman.com/downloads/

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

## Connecting To The QA Database

Once you have DB Navigator installed, you can click the **DB Browser** tab that will appear in your Intellij once you apply the plugin and restart Intellij. From the **DB Browser** menu, you can click the green plus sign on the top left and add a new PostgreSQL connection. You can find the database credentials for the connection in the **src/main/resources/application.yml** file in the **LOCAL** Spring Profile. Grab the host, port number, database name, user name, and user password from that file and insert the data into your new connection. Then test your connection. It should look like this when you're done:

![alt tag](./docs/imgs/connect-to-qa-db.png) 

## Running The Project

Navigate in Intellij to the: **src -> main -> java -> com.cag.cagbackendapi -> CagBackendApiApplication** file. Once in there, if your gradle is working properly you should see a green play button next to the left of the class. Click this play button and run the app.
 
Once the app has booted up completely, you can go to your browser and go to the url: **http://localhost:9000/**. Here you'll see all of our endoints and you can use any of the endpoints by providing the correct authKey value from the application.yml file for the Spring Profile you're running (Probably the LOCAL profile).

## Running Our Tests

Along with our features we develop, we'll also include testing for each specific use case our feature could possibly encounter. To run our tests, the first thing you'll need to do is ensure that your Intellij preferences build and run our tests from **Gradle** and not **Intellij**. Go to: **Intellij -> Preferences -> Build, Execution, Deployment -> Build Tools -> Gradle**. From there, select: **Gradle** for the "Build and run using:" and the "Run tests using:" drop-down menus. It should look like this when you're done:

![alt tag](./docs/imgs/build-and-run-using-gradle.png)

You'll now be able to right-click on the "test" folder and run all of our project tests. See the image below for the correct button to click and the resulting passing tests:

![alt tag](./docs/imgs/run-tests.png)
