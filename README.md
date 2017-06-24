[![GitHub license](https://img.shields.io/crates/l/rustc-serialize.svg)](https://innersource.accenture.com/projects/MICROSERVICES/repos/spring-cloud-microservices/browse/LICENSE)

# SpringBoot -- Microservices -- Docker

This is a proof-of-concept application, which demonstrates [Microservice Architecture Pattern](http://martinfowler.com/microservices/) using Spring Boot, Spring Cloud and Docker.


## Functional services

This sample was decomposed into 2 core microservices. Both are independently deployable applications, organized around certain business capability.

<img width="880" alt="Functional services" src="https://aniruthmp.github.io/images/functionalnew.png">

#### Reservation API
This service uses [HSQLDB](http://hsqldb.org/) as the database. Contains Entity resources exposed as HATEOS. Apart from that, there are couple of extra methods that are used to search the accounts database

Method	| Path	| Description
------------- | ------------------------- | -------------
GET	| /reservations/{id}	| Get specified reservation data
GET	| /reservations/search/findByFirstName	| Search by first name
GET	| /reservations/search/findByLastName	| Search by last name
GET	| /reservations/1	| Get demo reservation data
POST	| /reservations/	| Register new reservation


#### Venue service
This service talks to [Couchbase DB](http://www.couchbase.com/nosql-databases/couchbase-server). Instead of exposing entities as HATEOS based, I have taken another approach to expose them through actual REST services. Following are the methods exposed

Method	| Path	| Description
------------- | ------------------------- | -------------
GET	| /findAll?page={page}&size={size}	| Get records in pages 	
GET	| /findByReservationId?reservationId={reservationId}	| Search by reservationId

#### Gateway service
The [Edge/Gateway](https://github.com/Netflix/zuul/wiki) service acts as the bridge between UI (or) external applications to this Micro-services domain. At the moment, I have included some Orchestration logic also in here, but this is not recommended for Production scenario. Later, I will move this to something called as [Composite service](https://en.wikipedia.org/wiki/Service-oriented_modeling)

Method	| Path	| Description
------------- | ------------------------- | -------------
GET	| /client/names	| Get all the names from reservation-service 	
GET	| /venues?page={page}&size={size}	| Get the venues from venue-service
GET	| /bookings?id={id}| For a given reservationId, get reservation from reservation-service and venues from venue-service 	
PUT	| /booking/update?id={id}	| Update the reservation through **REST** to reservation-service and through **Apache Kafka** to venue-service

#### Notes
- Each micro-service has it's own database, so there is no way to bypass API and access persistence data directly.
- Service-to-service communication is quite simplified: microservices talking using only synchronous REST API. Common practice in a real-world systems is to use combination of interaction styles. For example, perform synchronous GET request to retrieve data and use asynchronous approach via Message broker for create/update operations in order to decouple services and buffer messages. However, this brings us in [eventual consistency](http://martinfowler.com/articles/microservice-trade-offs.html#consistency) world.
- In this sample application, micro-services are orchestrated through the Edge Service through [Zuul](https://github.com/Netflix/zuul/wiki)

## Infrastructure / Auxiliary services
There's a bunch of common patterns in distributed systems, which could help us to make described core services work. [Spring cloud](http://projects.spring.io/spring-cloud/) provides powerful tools that enhance Spring Boot applications behaviour to implement those patterns. I'll cover them briefly.
<img width="880" alt="Infrastructure services" src="https://aniruthmp.github.io/images/infra.png">

### Config service
[Spring Cloud Config](http://cloud.spring.io/spring-cloud-config/spring-cloud-config.html) is horizontally scalable centralized configuration service for distributed systems. It uses a pluggable repository layer that currently supports local storage, Git, and Subversion.

In this project, I use git profile, which simply loads config files from a given git repository URL. Now, when reservation-service requests it's configuration, Config service responses with `git url`/reservation-service.yml and `git url`/application.yml (which is shared between all client applications).

##### Client side usage
Just build Spring Boot application with `spring-cloud-starter-config` dependency, autoconfiguration will do the rest.

Now you don't need any embedded properties in your application. Just provide `bootstrap.yml` with application name and Config service url:
```yml
spring:
  application:
    name: reservation-service
  cloud:
    config:
      uri: http://configserver:8888
      fail-fast: true
```

##### With Spring Cloud Config, you can change app configuration dynamically.
For example, [MessageController](https://innersource.accenture.com/projects/MICROSERVICES/repos/spring-cloud-microservices/browse/sb-ref-reservation-service/src/main/java/com/example/reservation/controller/MessageRestController.java) was annotated with `@RefreshScope`. That means, you can change message without rebuild and restart Reservation service application.

First, change required properties in Config server. Then, perform refresh request to Notification service:
`curl -H "Authorization: Bearer #token#" -XPOST http://localhost:8000/refresh`

Also, you could use Repository [webhooks to automate this process](http://cloud.spring.io/spring-cloud-config/spring-cloud-config.html#_push_notifications_and_spring_cloud_bus)

##### Notes
- There are some limitations for dynamic refresh though. `@RefreshScope` doesn't work with `@Configuration` classes and doesn't affect `@Scheduled` methods
- `fail-fast` property means that Spring Boot application will fail startup immediately, if it cannot connect to the Config Service. That's very useful when start [all applications together](https://innersource.accenture.com/projects/MICROSERVICES/repos/spring-cloud-microservices/browse/README.md#how-to-run-all-the-things)


### API Gateway
As you can see, there are 2 core services, which expose external API to client. In a real-world systems, this number can grow very quickly as well as whole system complexity. Actually, hundreds of services might be involved in rendering one complex webpage.

In theory, a client could make requests to each of the microservices directly. But obviously, there are challenges and limitations with this option, like necessity to know all endpoints addresses, perform http request for each peace of information separately, merge the result on a client side. Another problem is non web-friendly protocols, which might be used on the backend.

Usually a much better approach is to use API Gateway. It is a single entry point into the system, used to handle requests by routing them to the appropriate backend service or by invoking multiple backend services and [aggregating the results](http://techblog.netflix.com/2013/01/optimizing-netflix-api.html). Also, it can be used for authentication, insights, stress and canary testing, service migration, static response handling, active traffic management.

Netflix open-sourced [such an edge service](http://techblog.netflix.com/2013/06/announcing-zuul-edge-service-in-cloud.html), and now with Spring Cloud we can enable it with one `@EnableZuulProxy` annotation. Here's a simple prefix-based routing configuration for Venue service:

```yml
zuul:
  routes:
    venue-service:
        path: /venues/**
        serviceId: venue-service
        stripPrefix: false

```

That means all requests starting with `/venues` will be routed to Venue service. There is no hardcoded address, as you can see. Zuul uses [Service discovery](https://innersource.accenture.com/projects/MICROSERVICES/repos/spring-cloud-microservices/browse/README.md#service-discovery) mechanism to locate Venue service instances and also [Circuit Breaker and Load Balancer](https://innersource.accenture.com/projects/MICROSERVICES/repos/spring-cloud-microservices/browse/README.md#load-balancer-circuit-breaker-and-http-client), described below.

### Service discovery

Another commonly known architecture pattern is Service discovery. It allows automatic detection of network locations for service instances, which could have dynamically assigned addresses because of auto-scaling, failures and upgrades.

The key part of Service discovery is Registry. I use Netflix Eureka in this project. Eureka is a good example of the client-side discovery pattern, when client is responsible for determining locations of available service instances (using Registry server) and load balancing requests across them.

With Spring Boot, you can easily build Eureka Registry with `spring-cloud-starter-eureka-server` dependency, `@EnableEurekaServer` annotation and simple configuration properties.

Client support enabled with `@EnableDiscoveryClient` annotation an `bootstrap.yml` with application name:
``` yml
spring:
  application:
    name: reservation-service
```

Now, on application startup, it will register with Eureka Server and provide meta-data, such as host and port, health indicator URL, home page etc. Eureka receives heartbeat messages from each instance belonging to a service. If the heartbeat fails over a configurable timetable, the instance will be removed from the registry.

Also, Eureka provides a simple interface, where you can track running services and number of available instances: `http://localhost:8761`

### Authentication & Authorization service
#### Why OAuth2?
OAuth2 is a well known authorization technology. It is widely used, to give developers access to users data at Google/Facebook/GitHub directly from the foreign services in a secure way.

**Approach**: The user authenticates on a authorization service, which maps the user session to a token. Any further API call to the resource services must provide this token. The services are able to recognize the provided token and ask the authorization service, which authorities this token grants, and who is the owner of this token.

This sounds like a good solution, doesn’t it? But what’s about secure token transmission? How to distinguish between access from a user and access from another service (and this is also something we could need!)

So this leads us to: OAuth2. Accessing sensible data from Facebook/Google is pretty much the same as accessing protected data from the own backend. Since they are working for some years on this solution, we can apply this battleground approved solution for our needs.

#### Why JWT?

OAuth2 is not about, how the token is looking like and where it is stored. So one approach is, to generate random strings and save token related data to these string in a store. Over a token endpoint, other services may ask something “is this token valid, and which permissions does it grant?”. This is the “user info URL” approach, where the authorization server is turning into a resource server for the user info endpoint.

As we talking about microservices, we need a way to replicate such a token store, to make the authorization server scalable. Despite this is a tricky task, there is one more issue with user info URL. For each request against a resource microservice containing a token in the header, the service will perform another request to authorization server to check the token. So without caching tricks, we got twice more request we really need, only for security. So both, scaling token store and token request are affecting the scalability of our architecture heavily. This is where [JWT](https://jwt.io/) (pronounced as “jot”) comes into play.

In short, the answer of that user info URL request, containing info about the OAuth client, optionally the user with its authorities and the granted scope, is serialized into JSON first, encoded with base64 and finally signed using a token. The result is a so called JSON Webtoken, which we use as an access token directly. When these JWTs are signed using RSA, the authorization server first signs it with the RSA private key, assuming every resource server will have a public key. When this token is passed via HTTP header, the resource servers just have to take this JWT, verify it was really signed by the proper private key (meaning this token is coming from authorization server), and instead of asking user info, deserializing the JSON content into a OAuth2Authentication, establishing a SecurityContext based on this.

Using JWT provides a simple way of transmitting hard to fake tokens, containing the permissions and user data in the access token string. Since all the data is already inside, there neither is a need to maintain a token store, nor the resource servers must ask authorization for token checks.

So, using JWT makes OAuth2 available to microservices, without affecting the architectures scalability. **Note**: ForgeRock does support JWT. It has a working sample [here](https://github.com/ForgeRock/jwt-bearer-client)

### Load balancer, Circuit breaker and Http client

Netflix OSS provides another great set of tools.

#### Ribbon
Ribbon is a client side load balancer which gives you a lot of control over the behaviour of HTTP and TCP clients. Compared to a traditional load balancer, there is no need in additional hop for every over-the-wire invocation - you can contact desired service directly.

Out of the box, it natively integrates with Spring Cloud and Service Discovery. [Eureka Client](https://innersource.accenture.com/projects/MICROSERVICES/repos/spring-cloud-microservices/browse/README.md#service-discovery) provides a dynamic list of available servers so Ribbon could balance between them.

#### Orchestration Service
This is commonly referred as *Orchestration* or *Aggregator* service. This serves the following purposes

 1. Aggregator would be a simple web page that invokes multiple services to achieve the functionality required by the application. Since each service (`reservation-service` and `venue-service`) is exposed using a lightweight REST mechanism, the web page can retrieve the data and process/display it accordingly. If some sort of processing is required, say applying business logic to the data received from individual services, then you may likely have a CDI bean that would transform the data so that it can be displayed by the web page.
 2. Another option for Aggregator is where no display is required, and instead it is just a higher level composite microservice which can be consumed by other services. In this case, the aggregator would just collect the data from each of the individual microservice, apply business logic to it, and further publish it as a REST endpoint. This can then be consumed by other services that need it.
 3. Improved fault isolation. For example, if there is a memory leak in one service then only that service will be affected. The other services will continue to handle requests. In comparison, one misbehaving component of a monolithic architecture can bring down the entire system.

This design pattern follows the DRY principle. If there are multiple services that need to access `reservation-service` and `venue-service`, then its recommended to abstract that logic into a composite microservice and aggregate that logic into one service. An advantage of abstracting at this level is that the individual services, i.e. `reservation-service` and `venue-service` and can evolve independently and the business need is still provided by the composite microservice.

Note that each individual microservice has its own (optional) caching and database. If Aggregator is a composite microservice, then it may have its own caching and database layer as well. Aggregator can scale independently on X-axis and Z-axis as well.

#### Hystrix
Hystrix is the implementation of [Circuit Breaker pattern](http://martinfowler.com/bliki/CircuitBreaker.html), which gives a control over latency and failure from dependencies accessed over the network. The main idea is to stop cascading failures in a distributed environment with a large number of microservices. That helps to fail fast and recover as soon as possible - important aspects of fault-tolerant systems that self-heal.

Besides circuit breaker control, with Hystrix you can add a fallback method that will be called to obtain a default value in case the main command fails.

Moreover, Hystrix generates metrics on execution outcomes and latency for each command, that we can use to [monitor system behavior](https://innersource.accenture.com/projects/MICROSERVICES/repos/spring-cloud-microservices/browse/README.md#hystrix-dashboard).

#### Zipkin
[Zipkin](http://zipkin.io/) is a distributed tracing system. It helps gather timing data needed to troubleshoot latency problems in microservice architectures.

In this project, I have enabled Spring Sleuth. This implements a distributed tracing solution for Spring Cloud. For most users Sleuth should be invisible, and all your interactions with external systems should be instrumented automatically. You can capture data simply in logs, or by sending it to a remote collector service.

<img width="880" src="https://aniruthmp.github.io/images/zipkin.png">

#### Feign
Feign is a declarative Http client, which seamlessly integrates with Ribbon and Hystrix. Actually, with one `spring-cloud-starter-feign` dependency and `@EnableFeignClients` annotation you have a full set of Load balancer, Circuit breaker and Http client with sensible ready-to-go default configuration.

Here is an example from Venue Service:

``` java
@FeignClient(name = "venue-service")
public interface VenueReader {

	@RequestMapping(method = RequestMethod.GET, value = "/venues")
	Resources<Venue> read();

}
```

- Everything you need is just an interface
- You can share `@RequestMapping` part between Spring MVC controller and Feign methods
- Above example specifies just desired service id - `venue-service`, thanks to auto-discovery through Eureka (but obviously you can access any resource with a specific url)

### Hystrix dashboard

The Monitoring project is just a small Spring boot application with [Hystrix Dashboard](https://github.com/Netflix/Hystrix/tree/master/hystrix-dashboard).

See below [how to get it up and running](https://innersource.accenture.com/projects/MICROSERVICES/repos/spring-cloud-microservices/browse/README.md#how-to-run-all-the-things).

Let's see our system behavior under load: Hystrix Dashboard lists all the available services, their circuit status, API hit count, thread pools, etc.

<img width="880" src="https://aniruthmp.github.io/images/hystrix-capture.png">


## Infrastructure automation

Deploying microservices, with their interdependence, is much more complex process than deploying monolithic application. It is important to have fully automated infrastructure. We can achieve following benefits with Continuous Delivery approach:

- The ability to release software anytime
- Any build could end up being a release
- Build artifacts once - deploy as needed

In this reference application, DevOps is not covered. However, I will update this section with some more information on the same...

## How to run all the things?

Keep in mind, that you are going to start 7 Spring Boot applications. Make sure you have `4 to 8 Gb` RAM available on your machine. You can always run just vital services though: Gateway, Registry, Config, and Reservation Service.

#### Before you start
- Install [Docker](https://www.docker.com/products/overview) ver 1.12.
- Export environment variables: `GIT_USERID`, `GIT_PASSWORD` and `HOST_IP`
- Export `COUCHBASE_IP` as `couchbase01-devint.sdlc.gxicloud.local`
- The following environment variables are used for OAuth2/JWT
	- ACME_SERVICE_SECRET=acmesecret
	- EDGE_SERVICE_SECRET=edgesecret
	- RESERVATION_SERVICE_SECRET=reservationsecret
	- VENUE_SERVICE_SECRET=venuesecret
- Clone all the [Repository](https://innersource.accenture.com/scm/microservices/spring-cloud-microservices.git) and build artifacts with maven. Issue the command from the project root folder `mvn clean install`. This will generate the Docker images in your local.
- Before issuing a build, ensure to remove all the previously running containers (related to this project) and remove all the existing images of this project.
- Note that this also builds couchbase database image with the required set up for you !!
- Ensure to install [jq](https://stedolan.github.io/jq/) This will be used to make testing the URLs easy

#### Docker mode
Go to the [docker folder](https://innersource.accenture.com/projects/MICROSERVICES/repos/spring-cloud-microservices/browse/docker) and issue the [docker-compose](https://docs.docker.com/compose/) command

 `docker-compose up` This will start all the application docker containers.

#### Important endpoints
- http://DOCKER-HOST:11111 - Config Server
- http://DOCKER-HOST:8500 - Consul UI
- http://DOCKER-HOST:11104 - Gateway
- http://DOCKER-HOST:11103 - Composite Service
- http://DOCKER-HOST:11107 - Venue Service
- http://DOCKER-HOST:11106 - Reservation Service
- http://DOCKER-HOST:11105/hystrix.html - Hystrix Dashboard
- http://DOCKER-HOST:11108 - Zipkin

#### Non-docker mode
Clone all the [Repository](https://innersource.accenture.com/scm/microservices/spring-cloud-microservices.git)  and build artifacts with maven. Issue the command from the project root folder `mvn clean package`. Once the build is successful, individual jars need to be started from terminal windows (or CMD for PC :smirk:) as from the root folder.
``` cmd
$maven-example2: java -Dspring.profiles.active=dev -jar ./eureka-service/target/eureka-service-*.jar
....
....

$maven-example2: java -Dspring.profiles.active=dev -jar ./config-service/target/config-service-*.jar
....
....
```

#### Swagger UI
Swagger UI is exposed for 3 services i.e. `composite-service`, `reservation-service` and `venue-service`. They can be reached in the following URLs
http://localhost:11107/swagger-ui.html#/ - Venue Service
http://localhost:11106/swagger-ui.html#/ - Reservation Service
http://localhost:11103/swagger-ui.html#/ - Composite Service

Ensure to generate the JWT token using the `auth-service` and use that in the Swagger UI

#### Test URLs
First you need to generate JWT token. Please note that I had already created JWT Token Keystore and attached in the source code. This can very well be replaced by proper signed certificate in future.

Generate the Access Token using the URL
``` bash
$ TOKEN=`curl -s -X POST -u acme:acmesecret http://localhost:7777/uaa/oauth/token -H "Accept: application/json" -d "password=password&username=user&grant_type=password&scope=server&client_secret=acmesecret&client_id=acme" | jq -r .access_token`
```
URL to get 50 records from page 1 from the `venue-service`
``` bash
$ curl -H "Authorization: Bearer $TOKEN" "http://localhost:11104/shipwright/v1/venues?size=50&page=1"
```
URL to get all the bookings for a given reservationId (in this case it is "3"). This is a complex call because internally the `composite-service` calls both `reservation-service` and `venue-service`
``` bash
$ curl -H "Authorization: Bearer $TOKEN" "http://localhost:11104/shipwright/v1/bookings?id=3"
```
Zipkin URL `http://localhost:9411` You can search for the traces for a given criteria (or you can just search for all traces)

Hystrix URL `http://localhost:11105/hystrix.html` -- this opens the Hystrix dashboard. Once the page loads, give the value `http://gateway:11104/admin/hystrix.stream` or `http://composite:11103/admin/hystrix.stream` in the box and submit. If you happen to change the management port, ensure to put that value in the above URLs.

URL to make a POST REST call to `reservation-service` as well as asynchrounous kafka message publish to `venue-service`
``` bash
$ curl -H "Authorization: Bearer $TOKEN" -H "Accept: application/json" -H "Content-Type: application/json" -X PUT -d '{"firstName":"James", "lastName": "Bond"}' "http://localhost:11104/shipwright/v1/booking/update?id=3"
```
URL to make a DELETE REST call to `venue-service` through the `composite-service`
``` bash
$ curl -X DELETE -H "Authorization: Bearer $TOKEN" "http://localhost:11104/shipwright/v1/venues/deleteByReservationId?reservationId=13"
```

#### Notes
All Spring Boot applications require already running [Config Server](https://innersource.accenture.com/projects/MICROSERVICES/repos/spring-cloud-microservices/browse/sb-ref-config-service) for startup. But we can start all containers simultaneously because of `fail-fast` Spring Boot property and `restart: always` docker-compose option. That means all dependent containers will try to restart until Config Server will be up and running.

Also, Service Discovery mechanism needs some time after all applications startup. Any service is not available for discovery by clients until the instance, the Eureka server and the client all have the same metadata in their local cache, so it could take 3 heartbeats. Default heartbeat period is 30 seconds.

Our Spring Boot Reference Application framework uses lombok as a "helper" to remove common boilerplate code with annotations which must be added to your IDE in order to compile properly.  Instructions to do this for Eclipse and IntelliJ can be found in confluence:

[Using Lombok with Eclipse](https://projectlombok.org/setup/eclipse)
[Using Lombok with IntelliJ](https://projectlombok.org/setup/intellij)

## Feedback welcome

This is open source, and would greatly appreciate your input. Feel free to contact <mailto:a.c.parthasarathy@accenture.com> with any question.