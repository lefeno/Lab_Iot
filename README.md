# IOT LAB WEEK 4 _ SERVER COMMUNICATION ON ANDROIDTHINGS

*Course ID: CO3038 - Group: L03*

*Instructor: Nguyen Tran Huu Nguyen*

## Introduction

This repository contains the source code of the assignment that required to be done after lab 4

This assignment are meant to be compiled on Android Studio and ran on Raspberry Pi 3.

## Assignment

Combine the assignment of Lab 3 that implements MFRC522 with the provided project
to:

➢ Read the UID of an RFID card, turn on/off LEDs correspondingly

➢ Send the UID to a server.

➢ The syntax to send data is as follows:
http://demo1.chipfc.com/SensorValue/update?sensorid=7&sensorvalue=[UID].

Please note that the UID is a unique value that are read from an RFID card.

➢ You can check the results in this webpage: http://demo1.chipfc.com/SensorValue/List/7

## Authors

* **Ta Huynh Thuy Linh _ 1511778**
* **Nguyen Luong Phuc Vinh _ 1514063**
* **Tui Nhat Quang _ 1512664**
* **Nguyen Van Minh _ 1512009**

## Prerequisites

In this exercise, we will use module RC522 RFID module for implementing SPI and send the uid data to a server. 

Please refer to this link: https://github.com/lefeno/lab_iot/tree/lab03_iot to understand how to set up your hardware and how to use the application to READ and WRITE to RFID cards.

## Upload to server

- Add those folders Models, MVVM, Network and files NPNConstants.java, Utils.java from lebBlink project to your project. In this poject, I added those already.

- After that, use the function updateToServer(url) in NPNHomeViewModel to update uid to your sever. 

- When using this project, please note that the UID value in http://demo1.chipfc.com/SensorValue/update?sensorid=7&sensorvalue=[UID] must contain digits only. And for the READ and WRITE to function smoothly, please remove all Callbacks, Messages and Asyn Thread when implement WRITE button's setOnClickListener.

If you don't want to add those folders and files, please refer to volley library for GET, POST request and implement the updateToServer yourself.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

