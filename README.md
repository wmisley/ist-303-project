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
To build the tool, perform the following command:
```
mvn install
```
To run the tool, execute the following command:
```
ist-303-project
```

Notes:

This project uses the DAO pattern on Sqlite. For more information:

http://www.oracle.com/technetwork/java/dataaccessobject-138824.html

Sqlite resources:

https://www.techonthenet.com/sqlite/index.php

https://www.tutorialspoint.com/sqlite/index.htm

https://www.sqlite.org/docs.html
