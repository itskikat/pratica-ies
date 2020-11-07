# IES - LAB02
#### Author: Francisca Barros, NMEC-93102
---
 
## 2.1 - Server-Side Programming with Servlets

Docker Containers and Servlet Containers are VERY DIFFERENT!
- **Docker Containers** deploy virtualized runtime containers (any kind of services)
- **Servlet Containers** (environmentn that runs on the server-side) provide a runtime container to execute server-side, web-related Java code

##### a)  

**Apache Tomacat** is an open-source implementation of the Java Servlet, JavaServer Pages, Java Expression Language and WebSockets Tecnhology.
Pure Java HTTP Server environment in which Java code can run

Installing Tomcat -> Followed this tutorial [https://wolfpaulus.com/tomcat/]

**starting and stopping Tomcat**
``` zsh
$ /Library/Tomcat/bin/startup.sh
$ /Library/Tomcat/bin/shutdown.sh
```
_can be accessed through browser - http://localhost:8080/_

_Manager App_
- .war packaging, for web (web-archive)
- Deploy WAR file in server environment

##### b)
To access the ___Manager App___, one needs to register appropriate roles in ```/Library/Tomcat/conf/tomcat-users.xml ```
Added the following lines
```xml
<role rolename="manager-gui"/>
<role rolename="manager-script"/>
<user username="admin" password="admin" roles="manager-gui, manager-script"/>
```

##### g)
**server loggs** are saved under ```$ Library/Tomcat/logs/catalina.out```
Loggs showing the deployment was successful
```zsh
20-Oct-2020 09:50:04.346 INFO [main] org.apache.catalina.startup.VersionLoggerListener.log JVM Version:           13.0.2+8
20-Oct-2020 09:50:04.346 INFO [main] org.apache.catalina.startup.VersionLoggerListener.log JVM Vendor:            Oracle Corporation
20-Oct-2020 09:50:04.346 INFO [main] org.apache.catalina.startup.VersionLoggerListener.log CATALINA_BASE:         /usr/local/apache-tomcat-9.0.39
20-Oct-2020 09:50:04.346 INFO [main] org.apache.catalina.startup.VersionLoggerListener.log CATALINA_HOME:         /usr/local/apache-tomcat-9.0.39
(...)
20-Oct-2020 10:09:23.179 INFO [http-nio-8080-exec-7] org.apache.catalina.startup.HostConfig.deployWAR Deploying web application archive [/usr/local/apache-tomcat-9.0.39/webapps/lab2.1Tomcat-1.0-SNAPSHOT.war]
20-Oct-2020 10:09:23.232 INFO [http-nio-8080-exec-7] org.apache.catalina.startup.HostConfig.deployWAR Deployment of web application archive [/usr/local/apache-tomcat-9.0.39/webapps/lab2.1Tomcat-1.0-SNAPSHOT.war] has finished in [53] ms
```

##### i)
**@WebServlet** annotation is used to define a servlet component in a web application; specified on a class; contains metadata about the servlet. It _must_ specify _at least one **URL pattern**_ (other atributes are optional)
In order to use it, extend the **javax.servlet.http.HttpServlet** class. 

##### j)
When a user doesn't specify the username to be greeted. The else statement fixes this issue.

**Servlet Container** -> generate dynamic web pages; the essential part of the web server that interacts with the Java Servlets; communicated between client browsers and servlets; basic idea - user java to dynamically generate the web page on the server side (Thus, a servlet container is part of the web server interacting with the servlets)
via [What is a Servlet Container?](https://dzone.com/articles/what-servlet-container)
[Servlet Container](https://ecomputernotes.com/servlet/intro/servlet-container)


##### k)
Deploying on docker
```zsh
$ docker image pull tomcat:8.0
$ docker container create --publish 8088:8080 --name my-tomcat-container tomcat:8.0
% docker container start my-tomcat-container
```
Accessed in **http://localhost:8088/**

Docker Image File for .WAR to deploy
```Dockerfile
# we are extending everything from tomcat:8.0 image ...
FROM tomcat:8.0
MAINTAINER francisca_barros
# COPY path-to-your-application-war path-to-webapps-in-docker-tomcat
COPY ~/IES/pratica/lab02/lab2.1/lab2.1Tomcat/lab2.1Tomcat-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/
```
Building the Docker image file
```zsh
$ docker image build -t francisca_barros/lab2.1Tomcat-1.0-SNAPSHOT-image ./
$ docker container run -it --publish 8088:8080 francisca_barros/lab2.1Tomcat-1.0-SNAPSHOT-image
```

---
#
## 2.2 - Server-Side Programming with Embedded Servers
[What is in embedded server?](https://www.springboottutorial.com/java-programmer-essentials-what-is-an-embedded-server)
[Embedded Jetty Server](https://examples.javacodegeeks.com/enterprise-java/jetty/embedded-jetty-server-example/)
An **Embedded Server** will make our life a lot easier. It's embedded as part of the deployable application (in JAVA, this would be a JAR); there's no need to pre-install the server in the deployment environment.




---
#
## 2.3 - Introduction to Web Apps with Spring Boot
[Spring-Boot](https://spring.io/projects/spring-boot)
**Spring Boot** is an open source Java-Based framework used to create a micro Service. It makes it easy to create stand-alone, production-grade Spring based applications that one can "just run"
**Main Features**
- create stand-alone Spring applications
- embeded tomcat (in this case, since we're using it) -> no need to deploy .WAR
- opinated 'start' dependencies are provided, which simplify the build configuration
- automatically configures Spring and 3rd party libraries whenever possible
- provide production-ready features (metrics, health checks, externalized configuration...)
- no code generation and no requirement for XML configuration
(sounds like fun, and v-easy to get a grip tbh)

#### n)
[Spring Initializr](https://start.spring.io/)
**Add ___'Spring-Web'___ Dependency!!**
Application built using the following command-line command
```zsh 
$ ./mvnw spring-boot:run
```
*it took a while for the first time, some files were downloading, but then it was smooth*

#### o)
**@GetMapping** -> ensures HTTP GET requests to ***/greeting*** are mapped to ***greeting()*** 
**@RequestParam** -> binds the value of the query string parameter ***name*** into the ***name parameter of greeting()*** method. This query string parameter is; value added to a ***Model Object*** (make it accessible to the view template)
***not required. If absent, the default value of __World__ is used***

To start the application, simply run the command ```./mvnw spring-boot:run```
**Server Port** was changed to my NMEC, making the application available at (http://localhost:93102/greeting)

#### p)
**RESTful Web Service Controller** populates and returns a Greeting object (rather than relying on a view tecnhology to perform server-side rendering of the greeting data to HTML - ***traditional MVC controller***); object data written directly to the HTTP response as JSON.
**@RestController** -> marks the class as a controller where every method retuns a domain object instead of a view (= ***@Controller + @ResponseBody***)
**Server Port** was changed to 4231, making the application available at (http://localhost:4231/RESTgreeting)

```zsh
~ % curl -v http://localhost:4231/RESTgreeting
*   Trying ::1...
* TCP_NODELAY set
* Connected to localhost (::1) port 4231 (#0)
> GET /RESTgreeting HTTP/1.1
> Host: localhost:4231
> User-Agent: curl/7.64.1
> Accept: */*
>
< HTTP/1.1 200
< Content-Type: application/json
< Transfer-Encoding: chunked
< Date: Tue, 27 Oct 2020 10:18:24 GMT
<
* Connection #0 to host localhost left intact
{"id":1,"content":"Hello, World!"}* Closing connection 0
```
```zsh
~ % curl -v http://localhost:4231/RESTgreeting\?name\=Francisca
*   Trying ::1...
* TCP_NODELAY set
* Connected to localhost (::1) port 4231 (#0)
> GET /RESTgreeting?name=Francisca HTTP/1.1
> Host: localhost:4231
> User-Agent: curl/7.64.1
> Accept: */*
>
< HTTP/1.1 200
< Content-Type: application/json
< Transfer-Encoding: chunked
< Date: Tue, 27 Oct 2020 10:19:07 GMT
<
* Connection #0 to host localhost left intact
{"id":2,"content":"Hello, Francisca!"}* Closing connection 0
```


---
[Embedded vs External Web Server](https://stephencoakley.com/2017/07/06/embedded-vs-external-web-server)
**Standalone servers** are independent of domain controllers on the network. They run separately from the application. It's configured with its own configuration files (since all the logic is built into the app), it forwards all requests to the application and it may load it.
***PROS***
- Ease of deployment
- Better performance (smaller memory footprint)
- Better security (chances of breach are a lot smaller)
- Complete control over the server and code
- Application/Module errors don't affect each other
***CONS***
- Performance overhead
- Development environment is harder (tons of protocols to learn and many bugs)
- Complexity (need to mantain the server+app... yikes)

**Embedded servers** are embedded as part of the deployable application; (basically, the program ships with the server within); the application is responsible for server start-up and management
***PROS***
- Single object to be deployed 
- More control over the server's behaviour (filters, headers, cache, ...)
- Possible to test againts any server version (like any other dependency)
- More self-contained apps (better to develop :D)
***CONS***
- Application designs around the API/Server used (boo-hoo, but it makes it harder to change servers later on...)
- Need to include all dependencies!
- One needs a proxy in order to group multiple applications behind one server
- The entire application server is very sensible to errors (one single exception might take it down)
- Deploying hotfixes to security exploits is harder :(

~
[Spring Docs - Convention over Configuration](https://docs.spring.io/spring-framework/docs/3.0.0.M3/reference/html/ch16s10.html)
> *For a lot of projects, sticking to established conventions and having reasonable defaults is just what they (the projects) need... this theme of convention-over-configuration now has explicit support in Spring Web MVC. What this means is that if you establish a set of naming conventions and suchlike, you can substantially cut down on the amount of configuration that is required to set up handler mappings, view resolvers, ModelAndView instances, etc. This is a great boon with regards to rapid prototyping, and can also lend a degree of (always good-to-have) consistency across a codebase should you choose to move forward with it into production. This convention over configuration support address the three core areas of MVC - namely, the models, views, and controllers.*

Examples of some annotations
- @Controller ***allows implementation classes to be autodetected through the classpath scanning***
- @RequestMapping
- @RequestParam
- @SpringBootApplication ***autoconfiguration + component searching of Spring Boot***
- @Configuration ***indicates that a class declares one or more @Bean methods and may be processed by the Spring container to generate bean definitions and service requests for those beans at runtime.***

~
**@SpringBootApplication** combines three Spring annotations (providing the functionality of them all)
- @Configuration
- @ComponentScan
- @EnableAutoConfiguration