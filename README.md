# Overview
A simple browser chat based on Spring framework and websocket technology.

# Features
* Writing text messages to common chat
* Real-time check of active users.

# Technologies
* Spring framework, SockJS, Stomp.

# Requirements
  Java 8, maven, browser.

# How to build app?
  Enter in command line interpreter the command:
  ```
  mvn package;
  ```
  
# How to start app?
  After the end of app building enter in command line interpreter the command:
  ```
  java -Dport=<required tcp port> -jar <name of jar file>
  ```
