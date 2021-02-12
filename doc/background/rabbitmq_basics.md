### RabbitMQ

RabbitMQ is a middleware for message exchange (message broker) in heterogeneous software systems. 
Important tasks of a message broker are for example:

- Connection of heterogeneous software systems
- Receiving and sending messages 
- Scalability of message switching
- Translation between message formats of senders and receivers
- Creation of a fail-safe and sufficiently fast communication for the application

RabbitMQ is released under the "Mozilla Public License Version 2.0", written in Erlang and built on the "Advanced Message Queueing Protocol (AMQP)". The functionality is defined by three basic building blocks: Producer, Message Queue and Consumer. Message transmission is customizable using various distribution methods, so-called "Exchanges", depending on the application.
The following exchanges are included in AMQP:

- Direct - message is written to a specific MQ using a specified key.
  
- Fanout - message is written to any available MQ
- Topic - message is written to at least one MQ by specified rules or pattern of keys.
  at least one MQ
- Headers - message is written to at least one MQ depending on the content.

Due to its underlying lightweight structure, RabbitMQ is suitable for use as a message broker during the development phase of heterogeneous software systems. At the same time, however, RabbitMQ is also suitable for productive use in distributed systems, since it allows message distribution to consumers working in parallel, is horizontally scalable through redundant instances, and is also suitable for complex message transmissions through the use of different routing methods.

Furthermore, programming interfaces exist for a variety of different programming languages (e.g. Java, Python, JavaScript, Go, etc.), enabling the (further) development of heterogeneous software systems by different development teams and the use of different technologies.
