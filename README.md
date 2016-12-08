# ist-303-project
This is a Java application developed for the IST 303 Software Development class at Claremont Graduate University

# Features
The application supports the camp registration process for Gila Breath Camp

# Prerequisites

* Java 8
* Maven 
* Sqlite3

If you have a Mac, you likely already have Sqlite3 installed, to check, open a command prompts and type:

```
sqlite3 -version
```

If you get a response like this:

```
3.8.10.2 2015-05-20 18:17:19 2ef4f3a5b1d1d0c4338f8243d40a2452cc1f7fe4
```

then you have it. If your response looks like this:

```
-bash: sqlite3: command not found
```

then you need to install it.

See https://www.tutorialspoint.com/sqlite/sqlite_installation.htm

# Instructions
Step 1. To build the tool, perform the following command:
```
mvn install
```
Step 2. To run the tool, execute the following command:
```
mvn exec:java
```

Step 3. When launching the application, it will prompt for log-in and password. Use the following:

Role          | Login | Password
------------- | ----- | --------
Clerk         | tom   | password
Director      | jerry | password 


The first time the application runs, it will create a database and schema automatically, populated with initial values.

Step 4 (optional). If you would like to load the software with a test data set of campers, then download SQLite Studio:

http://sqlitestudio.pl/

a. Click "Add a Database" icon in toolbar
b. Point to file "ist303-presentation-2.db" in the project folder
c. Click "OK"
d. Right mouse on "CAMPERS" table, select "Import into the table"
e. Click "Continue"
f. Point the file "data/CAMPERS_EXPORT.csv"
g. Click on option "Use file line represents CSV column name"
h. Click "Done"
i. Repeat steps d. through h. for the CAMPER_REGISTRATION table and "data/CAMPER_REGISTRATION_EXPORT.csv"
i. Repeat steps d. through h. for the PAYMENTS table and "data/PAYMENTS.csv"

# Developer Notes:

This project uses the DAO pattern on Sqlite. For more information:

http://www.oracle.com/technetwork/java/dataaccessobject-138824.html

Sqlite resources:

https://www.techonthenet.com/sqlite/index.php

https://www.tutorialspoint.com/sqlite/index.htm

https://www.sqlite.org/docs.html

JavaFX

To install Scene Builder for JavaFX, download and install:

http://www.oracle.com/technetwork/java/javafxscenebuilder-1x-archive-2199384.html

In IntelliJ

IntelliJ IDEA > Preferences > Language and Frameworks > JavaFX

Then set the path to the Scene Builder that was downloaded
