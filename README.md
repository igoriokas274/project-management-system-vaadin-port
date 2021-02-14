# Project management system Vaadin port

## Software requirements
1. Java SDK 1.8
2. Git
3. MySQL or MariaDB
4. MySQL Workbench tool
5. Any IDE (IntelliJ, Eclipse or Netbeans)

## Getting started
#### Updated 2021-01-20
1. Clone the source code of this project to your system via terminal or via your IDE
2. Startup earlier preinstalled MySQL Workbench tool:
    1. Create new connection with new credentials (if it is necessary)
    2. Open just created (or existed) connection and create a new schema. Name it "managerpro" or otherwise as you like
3. In your IDE tool:
    1. Open the cloned project:
    2. Open _src/main/resources/application.properties_ file to edit
    3. Edit _spring.datasource.url=_ line if created database with a different then "managerpro" name
    4. In lines _spring.datasource.username=_ and _spring.datasource.password=_ enter in Workbench tool created credentials to connect to database
    5. Open pom.xml file and edit <mysqldump.db>, <mysqldump.user> and <mysqldump.password> properties. Enter the same values as in steps before
    6. Open _src/main/java/dev/sda/team2/pma/ProjectManagementApplication.java_ file and Run the program
    7. If all the above steps are followed correctly, the IDE will create the entire database structure and load initial data. Just refresh database in your Workbench tool, and you will convinced to this ;)
    8. Now you can Stop the program in IDE
    9. App initialize SQL Data after every restart. If you want to prevent this, open _src/main/resources/application.properties_ file again and change _spring.jpa.hibernate.ddl-auto = create_ to _update_ and _spring.datasource.initialization-mode=always_ to _never_
    10. Run the app again.
4. Go to your browser:
    1. Enter _https://localhost:8443_ in address bar. You will see login page
    2. Enter default initialized credentials - Username: _admin_ / password: _admin_ or Username: _user_ / password: _user_
5. That's it! You are at "Home"! :))) Now you can create new Users directly from the Admin Panel inside your launched application.

### HTTPS using Self-Signed Certificate enabled
- PKCS12 keystore certificate generated and added to _src/main/resources/keystore_ folder;
- The SSL related properties enabled in _application.properties_;
- Default port for secure connection: _8443_.

### JavaMelody Framework enabled
1. Default port: _8443_;
2. Default path: _/actuator/monitoring_.

**Note:**
- JavaMelody interface works for users with ADMIN role only;
- Uses secure connection via HTTPS;
- User Guide: https://github.com/javamelody/javamelody/wiki/UserGuide
