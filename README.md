# ZX Venture Challenge

This is a REST application with some features. It was written in Java using Spring Boot, Spring Data and Mongodb as database.

# Requirements

  - Java 8
  - Maven 3.x
  - Mongodb

### Java

On linux

    sudo apt-get install python-software-properties
    sudo add-apt-repository ppa:webupd8team/java
    sudo apt-get update
    sudo apt-get install oracle-java8-installer
    
### Maven

#### Installation

On linux

    sudo apt-get update
    sudo apt-get install maven

### Mongodb


#### Installation

Download mongo and install it 

```sh
$ sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv EA312927
$ echo "deb http://repo.mongodb.org/apt/ubuntu xenial/mongodb-org/3.2 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-3.2.list
$ sudo apt-get update
$ sudo apt-get install -y mongodb-org
```

When the pdv collection is created, execute these commands to create indexes

    use zxventures
    db.getCollection('pdvs').createIndex( { coverageArea : '2dsphere' } )
    db.getCollection('pdvs').ensureIndex( { coverageArea : '2dsphere' } )

### Build  

Retrieve the project

    git clone https://github.com/gustavotsuji/pdv.git

Then build with maven

    mvn clean install

### Tests
Just execute

    mvn test

### Deploy

Spring Boot has a embedded Tomcat. It will start at localhost:8080 by executing

    java -jar pdv-api-0.1.0.jar

### Todos

 - Put Docker
 - Report coverage
 - Create unit tests

