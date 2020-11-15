# Stock-Quote

### Usage

- build with Maven
- set environment variables for RabbitMQ in docker-compose.yml: 
  - RABBITMQ_SERVICE: name of the RabbitMQ service
  - RABBITMQ_EXCHANGE: name of the exchange which stocks get sent to
  - RABBITMQ_EXCHANGE_TYPE: type of the exchange which stocks get sent to
- run with docker-compose --build
- implement a subscriber:
  - declare a message queue
  - bind the queue to the specified exchange to consume messages