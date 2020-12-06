# Konnector Backend API

## What is this repository for?

Handling Konnector web and data requests

## How do I get set up?

* Create a Maven master password `$ mvn --encrypt-master-password`
* Store the encrypted master password in ~/.m2/settings-security.xml
```
<settingsSecurity>
    <master>{password}</master>
</settingsSecurity>
```
* Encrypt the database schema password `$ mvn --encrypt-password`
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

## Building & Testing

* Navigate to the project root
* Run `$ mvn clean verify`

<br/>

* Unit tests can be skipped by providing `-DskipUnitTests`
* Integration tests can be skipped by providing `-DskipIntegrationTests`
* All tests can be skipped by providing `-DskipTests`
* A test database is required to be running for the integration tests

## Running the API

* Navigate to the project root
* Run `$ mvn spring-boot:run`
