# Flyway Guide

Flyway allows you to keep the structure of your DB and your application in sync. The traditional way of applying database changes was manually applying it before your app is deployed and then deploy the app to your servers. With microservices slowly becoming a de-facto standard you would use for developing enterprise software, it makes sense to automate DB migrations as well. By DB migrations I mean, applying changes to your DB structure in line with your app. 

Flyway automates that. It makes it really easy to apply any sort of database migration.

All you need to do is create a .sql file with your DDL (or DML), basically a file with multiple sql statements separated by a ‘;’, just like you would create any sql script.

To create our initial flyway migration need to first add our schema .sql file to the **/src/main/resources/db.migration** directory. Our first migration file should be prefaced with "V1__" to indicate that it is the first database migration. All subsequent migrations should be names "V2__", "V3__", and so on. To initialize our first database migration we can run this command while our database is running:

```
gradle flywayMigrate -i
```

This will create the **flyway_schema_history** table under our **cag_backend_api** schema in our database. This table will contain all the records of our database as we add more tables and build out our database!
