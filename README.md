# Rapi plugin for Jmeter

## Introduction

This plugin can run frontend load test using [Rapi Runner](https://github.com/RapiTest/rapi-runner) while using JMeter and can parse the `.csv` file result to generate a frontend load report.

## User Guide
Please check [wiki](https://github.com/bobcode99/jmeter-rapi-plugin/wiki).

## Jar Dependencies Required

- rapi-jmeter-plugins-1.0.0
- rapi-api-java-1.0.1
- rapi-jmeter-report-1.0.3
- opencsv-5.7.1
- json-simple-1.1.1

Jar that already provided in JMeter 5.5 `lib` folder
- freemarker-2.3.31 
- jackson-databind-2.14.1

## Needed Jmeter Version

Recommended use
- JMeter version 5.5
- Java 11 or above

## Build instructions

```bash
mvn clean package
```

## Credits
Thank you [jmeter-slack-chanel](https://www.blazemeter.com/resources/jmeter-slack-channel) for providing a place to solve my doubts about developing JMeter plugins.

## References

- https://github.com/undera/jmeter-plugins-webdriver.git
- https://github.com/Bugazelle/hello-jmeter-plugin
- https://github.com/xreztento/JWeight

