# VLINGO

## Background
The project was started by Vaughn Vernon. After positive feedback, the VLINGO/PLATFORM project was created under the leadership of Vaughn Vernon with the help of the community. Developers who have worked their way into the customer's domain, for example with event storming, should be able to use VLINGO to quickly get started developing an application.

VLINGO/PLATFORM follows the three principles of "simplicity", "model fluency" and "DDD-friendly" and, according to its developers, this combination clearly sets it apart from other frameworks. The complexity of concurrent processes is to be reduced and the development around the business logic is to be put in the foreground.

From a technical perspective, VLINGO/PLATFORM is a collection of open source tools designed with a reactive architecture. Domain-driven design (DDD) is at the forefront of development with VLINGO. It also focuses on an event-driven and microservice-oriented architecture.

A responsive architecture has four key characteristics:

- Responsive - Provide fast and consistent response times.
- Resilient - the system remains responsive even in the event of a failure
- Elastic - The system can handle fluctuating loads
- Message Driven - Asynchronous messaging for communication.

For more information on responsive systems click [here](https://www.reactivemanifesto.org).


The following figure visualizes the components that make up VLINGO/PLATFORM:

![alt text](https://gblobscdn.gitbook.com/assets%2F-LLB-V2sJmANuWISDmBf%2F-M9CUGIjqvocvRm5c5Jb%2F-M9H0FwLGxPXIMkLGEwd%2FVLINGO_PLATFORM.png?alt=media&token=418a86e0-859d-4806-87f2-a651c01fffca "VLINGO/PLATFORM structure")

Source: https://docs.vlingo.io

### VLINGO/XOOM & Starter
The starter can be used to quickly generate new DDD projects with a focus on microservice architecture. The starter can be used via a GUI in the browser, or with a configuration file in the console.

### VLINGO/HTTP
VLINGO provides a reactive HTTP server that handles concurrent, non-blocking requests.

### VLINGO/ACTORS
VLINGO provides an implementation of the "Actor model" with this component. The actors are the basis for the reactive architecture and foundation of the framework. They enable asynchronous and type-safe message exchange. Another advantage is the abstraction of the complex implementation of concurrent processes. Concurrency plays an increasingly important role in modern systems, since CPUs have been relying on a higher number of cores for several years and applications with many users must be able to process requests in parallel. (Geer, 2005)

For more information on the components of VLINGO, the official documentation can be used:
https://docs.vlingo.io
