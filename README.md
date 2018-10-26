# IOT LAB WEEK 3 _ SPI ON ANDROIDTHINGS 

*Course ID: CO3038 - Group: L03*

*Instructor: Nguyen Tran Huu Nguyen*

## Introduction

This repository contains the source code of the assignment that required to be done after lab 3

This assignment are meant to be compiled on Android Studio and ran on Raspberry Pi 3.

## Assignment

Your group task in this week is to create an Android application running on Raspberry Pi 3 to:
- Write blocks of data to an RFID card including

  ➢ Name, DOB, Student ID

  ➢ Authenticate if possible

- Continuously read an UID and the written data from an RFID card and display this information
on a screen.
- Turn the green LED of an RGB LED on when the card of one of your group members is presented.
- Flashing the red LED of an RGB LED 5 times in 2 seconds when the other cards are presented.
- By default, the blue LED of an RGB LED is turned on, and only one LED is turned on at a time.

## Authors

* **Ta Huynh Thuy Linh _ 1511778**
* **Nguyen Luong Phuc Vinh _ 1514063**
* **Tui Nhat Quang _ 1512664**
* **Nguyen Van Minh _ 1512009**

## Getting Started

In this exercise, we will use module RC522 RFID module for implementing SPI. 

### Prerequisites

1 RGB LED

1 RC522 module

1 Raspberry Pi 3 

The following is the Raspberry Pi 3 and RC522 Connection

![alt text](https://github.com/lefeno/lab_iot/blob/lab03_iot/3_Diagram.PNG)

**GPIO Connection Table:**

|Raspberry Pi 3|RC522 module|
|:--|:--|
|BCM8, SS0|SDA| 
BCM11, SCKL|SCK
BCM10, MOSI|MOSI
BCM9, MISO|MISO
GND|GND
BCM25|RST
3.3V|3.3V
**Raspberry Pi 3**|**RGB LED**|
VCC|Anode pin 
BCM16|Green pin 
BCM20|Red pin
BCM21|Blue pin

## UI of the assignment and User guide

Here is the UI of our assignment when we read a RFID card:

![alt text](https://github.com/lefeno/lab_iot/blob/lab03_iot/3_UI.PNG)

- The input in Edit Text component will accept those fields: name, DOB and studentID. In the example above, we defined name "Linh", DOB "0711", studentID "1511778".

- Note that each field can contain up to 16 bytes.

- Raspberry will continuous READ the card that is hover over RC522 module.

- Only when press button WRITE, the input in Edit Text component will be written in RFID card, and then, raspberry pi will READ and display this currently written data of this card on screen. After that, it will continue to READ card as usual.

- The behaviour of RGB LED will follow those that is described in the assignment.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

