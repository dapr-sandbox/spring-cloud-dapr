# spring-cloud-stream-dapr
[Spring Cloud Stream](https://docs.spring.io/spring-cloud-stream/docs/current/reference/html/spring-cloud-stream.html#spring-cloud-stream-reference) establishes a connection between the application and the middleware, separates the logic of sending and receiving messages with specific middleware from the application, and is finally provided by Binder. Binder shields the details of the underlying MQ message middleware and decouples our application from the specific message middleware.

In addition, Spring Cloud Stream abstracts the MQ in the Spring Cloud system and provides a unified API for sending and receiving messages, in order to simplify the development of messages in Spring Cloud applications.

Combining Spring Cloud Stream and Dapr, on the one hand Dapr can make up the dependent on a specific middleware library for Spring Cloud Stream, on the other hand, Spring Cloud Stream can help application and Dapr client decoupling. 

![img](https://user-images.githubusercontent.com/42743274/182814078-45fb0cdc-a2da-40ad-8a4c-ed395b692fa6.png "Dapr Binder")

In this project, we implemented `Spring Cloud Stream Dapr Binder (Dapr Binder)` which is to strip non-business logic parts from the application, so that developers can pay more attention to the development of business logic.
We regard the entire Dapr Sidecar as an MQ. When sending messages we call Grpc’s Client. And when receiving messages, we create a Grpc’s Server and open two interfaces to monitor Dapr’s calls. One is to tell Dapr, which topics have I subscribed to, and the other is to receive messages from the subscribed topics. 

## Usage
To use Dapr Binder, you need to add `spring-cloud-stream-binder-dapr` as a dependency to your Spring Cloud Stream application, as shown in the following example for Maven:
```xml
<dependency>
   <groupId>io.dapr.spring</groupId>
   <artifactId>spring-cloud-stream-binder-dapr</artifactId>
   <version>0.0.1-SNAPSHOT</version>
</dependency>
```
Alternatively, you can use the `spring-cloud-starter-stream-dapr`, as follows:
```xml
<dependency>
   <groupId>io.dapr.spring</groupId>
   <artifactId>spring-cloud-starter-stream-dapr</artifactId>
   <version>0.0.1-SNAPSHOT</version>
</dependency>
```
For more guidance, please refer to sample [kafka](samples).