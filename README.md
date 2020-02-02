# Overview
A simple browser chat based on Spring framework and websocket technology.

# Features
* Writing text messages to common chat
* Real-time check of active users.
* Possibility to connect chat bot.

# Technologies
* Spring framework, SockJS, Stomp.

# Requirements
  Java 8, maven, Chrome browser.

# Notes
  Firstly you should start the main application and then you should start the bot.
  
# How to build app?  
  Enter in command line interpreter the command:
  ```
  mvn package;
  ```
  Jar file would be placed in the target directory of ezchat module.
  
# How to start app?
  After the end of app building enter in command line interpreter the command:
  ```
  java -Dport=<required tcp port> -jar <name of jar file>
  ```

# How to build bot?
  ```
  mvn package;
  ```
  Jar file would be placed in the target directory of chatbot module.
# How to start bot?
  After the end of bot building:
  1. Set up postgresql database <db_name> without adding tables.
  2. Add properties for connection to database in file hibernate.cfg.xml.
  3. Enter in command line interpreter the command:
  ```
  java -Dport=<same port as in chat> -Dlogin=<some name of bot> -Dip=localhost -Dendpoint=chat-messaging -jar <name of jar file>
  ```
  After start bot would connect to chat.
