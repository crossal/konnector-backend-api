# Konnector Backend API

## What is this repository for?

Handling Konnector web and data requests

## How do I get set up?

* Install Maven
* Create a Maven master password `$ mvn --encrypt-master-password`
* Store the encrypted master password in ~/.m2/settings-security.xml
```
<settingsSecurity>
    <master>{password}</master>
</settingsSecurity>
```

* Encrypt the database password `$ mvn --encrypt-password`
* Store the encrypted password in ~/.m2/settings.xml under the servers element
```
<settings>
    <servers>
        <server>
            <id>konnector_database</id>
            <username>root</username>
            <password>{password}</password>
        </server>
    </servers>
</settings>
```

* Encrypt the email server password `$ mvn --encrypt-password` (this should be the 'app password' for Google SMTP setup)
* Store the encrypted password in ~/.m2/settings.xml under the servers element
```
<settings>
    <servers>
        <server>
            <id>email</id>
            <username>root</username>
            <password>{password}</password>
        </server>
    </servers>
</settings>
```

* Encrypt the session remember me cookie key `$ mvn --encrypt-password`
* Store the encrypted password in ~/.m2/settings.xml under the servers element
```
<settings>
    <servers>
        <server>
            <id>session_remember_me_cookie</id>
            <key>{key}</key>
        </server>
    </servers>
</settings>
```

* To run the integration tests and run the app locally, install MySQL and follow the konnector-database README.md

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

## Running the API

* Navigate to the project root
* Run `$ mvn spring-boot:run`
* Uncomment the `jvmArguments` element from the `spring-boot-maven-plugin` `configuration` element in the pom to enable remote debugging
