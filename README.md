# Konnector Backend API

## What is this repository for?

Handling Konnector web and data requests

## How do I get set up?

* Install Maven
* Install Docker
* To run the integration tests and run the app locally, also follow the konnector-database README.md to setup the database

## Setting up domain emails through Google smtp (for running in Production)

* Create a base account in Gmail e.g. root.konnector@gmail.com
* Set up forwarding between the desired account and the base Gmail account with the domain provider e.g. support@konnector.io -> root.konnector@gmail.com
* Set up a Gmail app password by going to https://myaccount.google.com/ > 'Security' and turning on 2-step verification, then select 'App passwords' and add a password
* In Gmail, go to 'settings' > 'Accounts and Import' > 'Send mail as:' > 'Add another email address' and use the following details:
  * 'Name' > 'Konnector Support'
  * 'Email address' > 'support@konnector.io'
  * 'SMTP Server' > 'smtp.gmail.com'
  * 'Username' > 'root.konnector'
  * 'Password' > '<app_password>'

## Building & Testing

* Navigate to the project root
* Run `$ mvn clean verify`
* Unit tests can be skipped by providing `-DskipUnitTests`
* Integration tests can be skipped by providing `-DskipIntegrationTests`
* All tests can be skipped by providing `-DskipTests`
* A test database is required to be running for the integration tests

## Dependency/bug checks

### dependency-check-report

* Navigate to the project root
* Run `$ mvn verify` or `$ mvn site`
* See report in target folder

### spotbugs-maven-plugin

* Navigate to the project root
* Run `$ mvn spotbugs:spotbugs`
* Run `$ mvn spotbugs:gui`

### Jacoco test coverage

* See target/site/jacoco/index.html

## Running the API locally as a JAR using `spring-boot:run`

* Ensure application.properties `spring.datasource.url` is set to localhost
* Uncomment the `jvmArguments` element from the `spring-boot-maven-plugin` `configuration` element in the pom to enable remote debugging
* Navigate to the project root
* Run `$ mvn clean compile; mvn spring-boot:run`

## Running the API as a JAR manually

* Follow the `How do I get set up?` section
* Ensure application-{env}.properties are set correctly
* Navigate to the project root
* Run `$ mvn clean install -DskipTests` to build the JAR file
* Run the WAR file by running the command `$ java -jar target/konnector-backend-api.jar`

## Running the API as a WAR

* Follow the `How do I get set up?` section
* Ensure application-{env}.properties are set correctly
* Uncomment `<packaging>war</packaging>` from the pom.xml
* Navigate to the project root
* Run `$ mvn clean package spring-boot:repackage -DskipTests` to build the WAR file
* Run the WAR file by running the command `$ java -jar target/konnector-backend-api.war`

## Running the API via Docker

* Install Docker
* Ensure application-{env}.properties are set correctly
* Navigate to the project root
* Run `$ mvn clean install -DskipTests` to build the JAR file
* Build the API image by running `$ docker build -f docker/Dockerfile -t konnector-backend-api .`
* Run the image by running `$ docker run -d --publish 8080:8080 --name konnector-backend-api konnector-backend-api prod tls_cert_keystore_password_replace_me`

* For production, Certbot packages on the system come with a cron job or systemd timer that will renew the TLS certificates automatically before they expire. Certbot will not need to run again, but the Spring application may need to be bounced manually or via a cron job also
* To remove any stopped containers and all unused images `$ docker system prune -a`
