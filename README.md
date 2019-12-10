# Web of Needs Bot Skeleton

<<<<<<< HEAD
This skeleton contains an echo bot that reacts to each new atom created on a given node. For each atom, the bot sends a configurable number of contact requests (default is 3) that can be accepted by the user. Within the established chat, the bot echoes all sent messages.

> **NOTE:** Be careful with running more than one bot on a given node instance, as multiple bots may get into infinite loops.

The echo bot is a [Spring Boot Application](https://docs.spring.io/spring-boot/docs/current/reference/html/using-boot-running-your-application.html).
=======
This skeleton contains a bot that creates a Service Atom that one can connect to. 
For each atom that has been created on the configured node(s), the bot sends a message with the atomUri of the created Atom to everyone that is connected to the Service Atom.

The Bot Skeleton is a [Spring Boot Application](https://docs.spring.io/spring-boot/docs/current/reference/html/using-boot-running-your-application.html).
>>>>>>> bot-skeleton/master

## Running the bot

### Prerequisites

<<<<<<< HEAD
- [Java 8 JDK](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) or higher installed (openJDK 12 is currently not supported and won't work)
=======
- [Openjdk 8](https://adoptopenjdk.net/index.html) - the method described here does **not work** with the Oracle 8 JDK!
>>>>>>> bot-skeleton/master
- Maven framework set up

### On the command line

```
cd bot-skeleton
export WON_NODE_URI="https://hackathonnode.matchat.org/won"
mvn clean package
java -jar target/bot.jar
```
<<<<<<< HEAD
Now [create an atom](https://hackathon.matchat.org/owner/#!/create) to see the bot in action.
=======
Now go to [What's new](https://hackathon.matchat.org/owner/#!/overview) to find your bot, connect and [create an atom](https://hackathon.matchat.org/owner/#!/create) to see the bot in action.
>>>>>>> bot-skeleton/master

### In Intellij Idea
1. Create a run configuration for the class `won.bot.skeleton.SkeletonBotApp`
2. Add the environment variables

  * `WON_NODE_URI` pointing to your node uri (e.g. `https://hackathonnode.matchat.org/won` without quotes)
  
  to your run configuration.
  
3. Run your configuration

If you get a message indicating your keysize is restricted on startup (`JCE unlimited strength encryption policy is not enabled, WoN applications will not work. Please consult the setup guide.`), refer to [Enabling Unlimited Strength Jurisdiction Policy](https://github.com/open-eid/cdoc4j/wiki/Enabling-Unlimited-Strength-Jurisdiction-Policy) to increase the allowed key size.

##### Optional Parameters for both Run Configurations:
- `WON_KEYSTORE_DIR` path to folder where `bot-keys.jks` and `owner-trusted-certs.jks` are stored (needs write access and folder must exist) 

<<<<<<< HEAD
Now [create an atom](https://hackathon.matchat.org/owner/#!/create) to see the bot in action.

=======
>>>>>>> bot-skeleton/master
## Start coding

Once the skeleton bot is running, you can use it as a base for implementing your own application. 

<<<<<<< HEAD
### Prerequisites

- [Java 8 JDK](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) or higher installed (openJDK 12 is currently not supported and won't work)
- Java IDE of choice set up
- Maven framework set up

=======
>>>>>>> bot-skeleton/master
## Setting up
- Download or clone this repository
- Add config files

Please refer to the general [Bot Readme](https://github.com/researchstudio-sat/webofneeds/blob/master/webofneeds/won-bot/README.md) for more information on Web of Needs Bot applications.

