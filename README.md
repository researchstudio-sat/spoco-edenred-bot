# Web of Needs Bot Skeleton ReadMe

This skeleton contains an echo bot that reacts to each new atom created on a given node. For each atom, the bot sends a configurable number of contact requests (default is 3) that can be accepted by the user. Within the established chat, the bot echoes all sent messages.

> **NOTE:** Be careful with running more than one bot on a given node instance, as multiple bots may get into infinite loops.

The echo bot is a [Spring Boot Application](https://docs.spring.io/spring-boot/docs/current/reference/html/using-boot-running-your-application.html).

## Prerequisites

- [Java 8 JDK](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) or higher installed (openJDK 12 is currently not supported and won't work)
- Java IDE of choice set up
- Maven frameworks set up

## Setting up

- Download or clone this repository
- Add config files

## Running the echo bot

### On the command line

In the project directory:
```
export WON_CONFIG_DIR="$(pwd)/conf"
export WON_NODE_URI="https://hackathonnode.matchat.org/won"
mvn spring-boot:run
```

### In Intellij Idea
1. Create a run configuration for the class `won.bot.skeleton.EchoBotApp`
2. Add the environment variables

  * `WON_CONFIG_DIR` pointing to the `conf` directory
  * `WON_NODE_URI` pointing to your node uri (e.g. `https://hackathonnode.matchat.org/won` without quotes)
  
  to your run configuration.
  
3. Run your configuration

If you get a message indicating your keysize is restricted on startup (`JCE unlimited strength encryption policy is not enabled, WoN applications will not work. Please consult the setup guide.`), refer to [Enabling Unlimited Strength Jurisdiction Policy](https://github.com/open-eid/cdoc4j/wiki/Enabling-Unlimited-Strength-Jurisdiction-Policy) to increase the allowed key size.

## Implement your own bot

Once the echo bot is running, you can use it as a base for implementing your own application. Refer to the general [Bot Readme](https://github.com/researchstudio-sat/webofneeds/blob/master/webofneeds/won-bot/README.md) for more information on Web of Needs Bot applications.
