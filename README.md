# AccSaber Backend

## Requirements

- JDK 11
- MariaDB (optional through Docker)
- maven

## Starting a MariaDB Docker container

If Docker is installed simply run the `createDockerDb.sh`. It'll create a config with the settings defined in the application.yml.

## Creating a local configuration

Copy the contents of the `application.yml` into a file called `application-local.yml`. The gitignore will ensure that the file is not committed into the repository. The file will already contain the basic needed keys for the configuration of the application. For the database simply uncomment the first 15 values.  
It is also necessary to configure a path to where the application will store avatars and song covers with the key `image-save-location` as well uncommenting the `allowed-origins` key for the CORS configuration.

## Starting with the profile 

To start the application through the command line first run:  
`mvn clean install`   
in the root folder, then navigate into the accsaber-application module and run  
`mvn spring-boot:run -Dspring-boot.run.profiles=local`

## Generating Liquibase chnangelogs

Navigate into the accsaber-database module and run
`mvn liquibase:diff`

It will create a liquibase-temp-changeLog.xml in the changelog folder. Move this file to changelogs and rename it appropriately