# Spring Cloud Dapr
## Introduction

### What is Dapr?
Dapr is a portable, event-driven runtime that makes it easy for any developer to build resilient, stateless and stateful applications that run on the cloud and edge and embraces the diversity of languages and developer frameworks.

Leveraging the benefits of a sidecar architecture, Dapr helps you tackle the challenges that come with building microservices and keeps your code platform agnostic.
![Dapr](https://docs.dapr.io/images/overview.png)

For more information about Dapr, please go https://dapr.io/.
### What is Spring Cloud?
Spring Cloud provides tools for developers to quickly build some of the common patterns in distributed systems (e.g. configuration management, service discovery, circuit breakers, intelligent routing, micro-proxy, control bus, one-time tokens, global locks, leadership election, distributed sessions, cluster state). 

Coordination of distributed systems leads to boiler plate patterns, and using Spring Cloud developers can quickly stand up services and applications that implement those patterns. They will work well in any distributed environment, including the developer’s own laptop, bare metal data centres, and managed platforms such as Cloud Foundry.

For more information, please go https://spring.io/projects/spring-cloud.

### What is Spring Cloud Dapr?
Spring Cloud Dapr is a extension to integrate Spring Cloud with Dapr.

Spring Cloud Dapr enables developers to create Spring Cloud services and applications that can easily run in Dapr sidecar model.

With the help of Dapr, these ultra lightweight Java native applications can easily interact with external application and resources. Dapr provides many useful building blocks to build modern distributed application: service invocation, state management, input/output bindings, publish & subscribe, secret management,etc.

Because of the advantages of sidecar model, the native applications can benefit from Dapr's distributed capabilities while remain lightweight without introducing too many dependencies. This is not only helping to keep the size of java native applications, but also making the native applications easy to build as native images.


> ### Why Spring Cloud?
> - The Java community has a mature ecology and a huge user base. With Dapr's sidecar, it is easier for Java 
> applications to achieve AOT(Ahead Of Time) to improve performance.
> - Spring Cloud is one of the best microservice frameworks in the java field. Its application can be well 
> adapted to distributed and cloud-native environments.


## Currently Available
| Spring Cloud Dapr | Details |
|------|-------|
|[spring-cloud-stream-dapr](/spring-cloud-stream-dapr/)| A binder to strip non-business logic parts from the Pub/Sub application, so that developers can pay more attention to the development of business logic.
|spring-cloud-config-dapr|Spring Cloud Config Dapr support for externalized configuration in a distributed system using Dapr’s configuration API. [Coming soon...]|
| | |


## Code of Conduct

Please refer to our [Dapr Community Code of Conduct](https://github.com/dapr/community/blob/master/CODE-OF-CONDUCT.md).