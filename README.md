# QR Code Games

## Introduction
Utilizing socket connections and Google's Firebase MLKit, QR Code Games are multiplayer mobile games that allow players to run around in real life and tag each other using their phone cameras and QR codes. This is a project developed for CIS 350: Introduction to Software Engineering at GVSU.

### Capture the Flag
The first game being developed in the QR Code Games collection is Capture the Flag. Players can tag the opponent's base to take the flag, in hopes to return it to their own base to win. Players can tag each other to reset the flag.

## Usage
Install the apk on an Android phone running Android 6.0 or higher, and start the server on the default port of 54555 with the following command. Connect to the game server by inputing the host's IP address and specified port.
```
java -jar ./QRCodeGamesServer.jar
```
To specify a port for the server to run, use the following command to start the server.
```
java -jar ./QRCodeGamesServer.jar -port 12345
```
