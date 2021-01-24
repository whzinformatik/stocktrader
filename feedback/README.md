# Feedback

[![feedback](https://github.com/whzinformatik/stocktrader/workflows/feedback/badge.svg)][feedback_actions]

The project is used to get feedback for the stock trader application.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Installing

In order to get an executable environment for development, some environment variables have to be set first:

| Variable         | Description                                  | Default Value |
|------------------|----------------------------------------------|---------------|
| RABBITMQ_SERVICE | default host to use for rabbitmq connections | localhost     |

Make sure that the local environment is active. Then the program can be executed using the following command:

```bash
mvn exec:exec
```

At the end, you can send a simple REST request to provide feedback for the stock trader application:

```bash
curl --location --request POST 'localhost:18080/' \
--header 'Content-Type: application/json' \
--data-raw '{
    "message": "your message",
    "accountId": "account id"
}'
```

## Running the tests

You can run all the automated tests of the project with the following command:

```bash
mvn verify
```

[feedback_actions]: https://github.com/whzinformatik/stocktrader/actions?query=workflow%3Afeedback
