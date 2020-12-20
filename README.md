# stocktrader

[![GitHub license](https://img.shields.io/github/license/whzinformatik/stocktrader)][license]
[![GitHub release](https://img.shields.io/github/v/release/whzinformatik/stocktrader)][release]

The project contains various projects to implement a stock trader. A stock trader should be able to buy and sell shares. Various small projects have been developed for this purpose. General information on the entire project is described on the following page. Further information can be found in the respective sub-project.

## Service Architecture

The following graphic roughly describes the architecture of the whole project:

[![ContextMap][contextMap]][contextMap]

The services can be accessed via the following default ports:

- feedback: port 18080
- comment tone: port 18081
- portfolio: port 18082
- stock quote: port 18083
- account: port 18084

## Prerequisites

The project is created with the build tool [Maven][maven], [Docker][docker] and the [Java Version 11][openjdk].

## Getting Started

If you want to run services locally, please follow the instructions in the respective projects. You can set up all other important services with Docker:

```bash
docker-compose up -d TODO
```

See deployment for notes on how to deploy the project on a live system.

## Running the tests

In order to use a uniform code style and to meet the legal requirement of the licence, two tools [spotless][spotless] and [license-maven-plugin][license-plugin] were added. The following commands can be used to check whether the current code complies with the requirements:

```bash
mvn spotless:check
mvn license:check
```

If these are unsuccessful, the following commands can be run to reformat the code and add the licence header:

```bash
mvn spotless:apply
mvn license:format
```

How further tests can be carried out is described in the corresponding sub-projects.

## Theoretical Documentation

A small documentation on the tools used can be found in the [`doc`][documentation] folder. This is only a small excerpt. Detailed documentation can be found on the corresponding developer pages.

## Deployment

If you want to deploy the complete project, you can use Docker to set up the entire environment. All you need to do is run the following command:

```bash
docker-compose up -d TODO
```

## Contributing

Please read [CONTRIBUTING.md][contributing] for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning

We use [SemVer][versioning] for versioning. For the versions available, see the [tags on this repository][tags].

## License

This project is licensed under the Mozilla Public License - see the [LICENSE.md][license] file for details

[contextMap]: ./doc/ContextMap.png
[documentation]: ./doc
[contributing]: CONTRIBUTING.md
[license]: LICENSE.md
[versioning]: http://semver.org/
[tags]: https://github.com/whzinformatik/stocktrader/tags
[release]: https://github.com/whzinformatik/stocktrader/releases
[spotless]: https://github.com/diffplug/spotless
[license-plugin]: https://github.com/mojohaus/license-maven-plugin
[maven]: https://maven.apache.org/
[openjdk]: https://openjdk.java.net/
[docker]: https://www.docker.com/
