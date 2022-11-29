# spring-cloud-stream-dapr
[Spring Cloud Stream](https://docs.spring.io/spring-cloud-stream/docs/current/reference/html/spring-cloud-stream.html#spring-cloud-stream-reference) establishes a connection between the application and the middleware, separates the logic of sending and receiving messages with specific middleware from the application, and is finally provided by Binder. Binder shields the details of the underlying MQ message middleware and decouples our application from the specific message middleware.

In addition, Spring Cloud Stream abstracts the MQ in the Spring Cloud system and provides a unified API for sending and receiving messages, in order to simplify the development of messages in Spring Cloud applications.

Combining Spring Cloud Stream and Dapr, on the one hand Dapr can make up the dependent on a specific middleware library for Spring Cloud Stream, on the other hand, Spring Cloud Stream can help application and Dapr client decoupling. 

## How it works

<div  align="center">
<img src="https://user-images.githubusercontent.com/42743274/182814078-45fb0cdc-a2da-40ad-8a4c-ed395b692fa6.png" height="60%" width="60%" />
</div>

In this project, `Spring Cloud Stream Dapr Binder (Dapr Binder)` was implemented to strip non-business logic parts from the application, so that developers can pay more attention to the development of business logic.
The entire Dapr Sidecar was regarded as an MQ. When sending messages a Grpc’s Client is activated. And when receiving messages, a Grpc’s Server is responsible for monitor Dapr’s calls. Two interfaces are opened, one is to tell Dapr which topics have I subscribed to, and the other is to receive messages from the subscribed topics. 

## Usage

### Step 1: Pre-requisites

Before running this demo, please ensure that you have the following software installed:

- [jdk 8](https://www.oracle.com/java/technologies/javase/javase8-archive-downloads.html) 
- [maven 3.8.*](https://maven.apache.org/download.cgi)
- [docker](https://docs.docker.com/get-docker/) / [docker-compose](https://github.com/docker/compose)
- [dapr cli](https://docs.dapr.io/getting-started/install-dapr-cli/)


### Step 2: Add dependency
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
### Step 3: Start dapr sidecar
The `dapr run` command looks for the default components directory which for Linux/MacOS is `$HOME/.dapr/components` and for Windows is `%USERPROFILE%\.dapr\components` which holds yaml definition files for components Dapr will be using at runtime. For specific purposes, you can customize your own yaml file. The following is an example yaml file for implementing kafka pubsub.
```yml
apiVersion: dapr.io/v1alpha1
kind: Component
metadata:
  name: kafka-pubsub
spec:
  type: pubsub.kafka
  initTimeout: 1m
  version: v1
  metadata:
  # Kafka broker connection setting
  - name: brokers
    value: localhost:9092
  # consumer configuration: consumer group
  - name: consumerGroup
    value: group1
  - name: authType # Required.
    value: "none"
```
Then use `dapr run` to start sidecar and your application.
```bash
dapr run --app-port 8080 --app-id test --components-path {PATH_OF_COMPONENTS_FOLDER} -- java -jar {JAR_PACKAGE_PATH_OF_YOUR_APPLICATION} 
```

For more guidance, please refer to sample [kafka](samples).