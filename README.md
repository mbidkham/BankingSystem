What is BankingSystem?
===========
This is a simple console-based Java application developed by Spring boot and also Spring data jpa using h2-db.


Quick start
===========
A common workflow includes:

| Task                                  | Do                                                                                                                                                                                                                                                           |
|---------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Install Requirements (no docker use): |                                                                                                                                                                                                                                                              |
| -  openjdk:17                         | ``$ sudo apt install openjdk-17-jdk``                                                                                                                                                                                                                        |
| -  maven:3.8.4-openjdk-17             | ``$ wget https://downloads.apache.org/maven/maven-3/3.8.4/binaries/apache-maven-3.8.4-bin.tar.gz`` <br>  ``$ sudo tar -xf apache-maven-3.8.4-bin.tar.gz -C /opt`` <br> ``$ export M2_HOME=/opt/apache-maven-3.8.4`` <br> ``$ export PATH=${M2_HOME}/bin:${PATH}`` |
| Build application:                    | Go to the project root first,                                                                                                                                                                                                                                |
| -  without docker                     | ``$ ./mvnw clean install -DskipTests``                                                                                                                                                                                                                       |
| Run app based on custom port:         |                                                                                                                                                                                                                                                                   |
| -  without docker                     | edit application.properties, exp: server.port:9090 <br> ``$ ./mvnw spring-boot:run ``                                                                                                                                                                                                                                                                   |