# IES - LAB01  
---
 
## 1.1 - Gestao da build com maven

Construir um projeto -> varias etapas/goals
- dependencias
- compilacao codigo fonte
- producao binarios (artifacts)
- atualizar documentos
- instalar no servidor

Medio/Grande porte -> usar build tool (JAVA - MAVEN, GRADLE)
Build cicle available in command line as well as IDE. However, command-line makes it more flexible to use w/IDE & interactive GUI.



##### b)
**project cml commands**
```zsh
$ mvn archetype:generate -DgroupId=pt.ua.u93102 -DartifactId=lab1.1 -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.4 -DinteractiveMode=false_
$ cd lab1.1/
$ mvn package
$ java -cp target/lab1.1-1.0-SNAPSHOT.jar pt.ua.u93102.App
```  
**output: 'Hello World!'**  
  
**ArcheType** -> plugin; templating toolkit; helps authors create Maven project templates for users, provides the same means to generate parameterized versions of the previous. Quick way to enable developers quickly consistently w/ their company's/project's best practices.

**GroupID** -> uniquely identifies the project across all projects; it should start with a reversed domain name within one's control (not mandatory by MAVEN, but it will be troublesome to accept a single word group ID in the Maven Central repository). 

**ArtifactID** -> jar's name w/out version

---

### Dependencies
- WeatherRadar needs to open HTTP (GET JSON, analyze the data and show the output).  
- Artifacts will generate dependencies

#### g)
> Retrofit Libs (REST request to API); turns HTTP API into JAVA interface. 
> Gson parses JSON into classes  

**Both will be declared in *POM.xml***

**MavenGoal** -> Each phase in Maven's Build LifeCycle is a sequence of goals; Each goal is responsible for a specific task.
***Main Maven Goals***: 
- *compiler:compile*
- *compiler:testCompile*
- *surefire:test*
- *install:install*
- *jar:jar*
>> Check image to see Maven Goals' order :) 
---
#
## 1.2 - Gestao do Codigo Fonte
>> No notes here, familiarized with it and using it frequently :) 

---
#
## 1.3 - Introducao ao Docker
**Docker** is a set of platforms as a service that use *OS-Level* virtualization; able to deliver software in containers and allows to build, run and share applications with software.
> **Containers** are basically a running proccess with added encapsulation (for isolation between each one); it interacts with its own private filesystem (from a Docker Image) and runs natively (sharing the kernel of the host) - therefore being a *discrete proccess*

>> **Containerization** is the use of containers. It is flexible, lightweight, portable, loosely coupled, scalable and secure

#### CHEAT SHEET
```zsh
$ docker --version (checks version)
$ docker run <image-name> (runs that image)
$ docker image ls (lists all running images)
$ docker image ps (all running containers; use '-all' to list them all)
$ docker build (builds the image) -tag (changes its name)
$ docker run (runs that container) --publish (forwards target from host to container) --detach (background) --name (to be used in subsequent commands)
$ docker rm <image> (deletes container) --force (stops it if running)
```

> **DockerFile** describes how to assemble a private filesystem for a container; it can contain metadata on how to run it (optionally)

> **Docker Image** is read-only. When launched, a read-write layer is added to the top of the stack (called Unified File System). However, when that image is deleted, the read-write layer is lost (so all the data that it had)

> **Docker Volume** stays outside the container, in the host; it allows data to persist, even after deletion; Thus, it is a convenient way to share data (between the host and the container). It can also be shared between containers.
###### Given that, using a Volume when preparing a container to serve a DB is a major advantage.

```$ docker container ls -all```  lists all containers (running and not running)
```zsh
Franciscas-Air:~ $ docker container ls -all
CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              PORTS                      NAMES
48bfb8e96dfc        eg_postgresql       "/usr/lib/postgresql…"   5 minutes ago       Up 5 minutes        0.0.0.0:32768->5432/tcp   pg_test
```