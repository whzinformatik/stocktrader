# Comment tone

[![commentTone](https://github.com/whzinformatik/stocktrader/workflows/commentTone/badge.svg)][comment_tone_actions]

This project receives feedback for the stocktrader application and send it to the account project.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Installing

In order to get an executable environment for development, some environment variables have to be set first:

| Variable                   | Description                                      | Default Value  |
|----------------------------|--------------------------------------------------|----------------|
| RABBITMQ_SERVICE           | default host to use for rabbitmq connections     | localhost      |
| RABBITMQ_PUBLISH_EXCHANGE  | exchange name for comments publisher             | commentTone    |
| RABBITMQ_CONSUME_EXCHANGE  | exchange name for feedback subscriber            | feedback       |
| RABBITMQ_EXCHANGE_TYPE     | can be one of direct, fanout, topic and headers  | fanout         |

Make sure that the local environment is active. Then the program can be executed using the following command:

```bash
mvn exec:exec
```

## Documentation

You can get more details about the project with the documented code or the domain stories in the [`doc`][documentation] folder.

[comment_tone_actions]: https://github.com/whzinformatik/stocktrader/actions?query=workflow%3AcommentTone
[documentation]: ./doc
