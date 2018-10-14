
# Exercise 2

The previous assignment includes 5 exercises that detail as follows:
1. Using three pins to control an RGB LED displaying in different colors.
2. Get input from a button and then change the pace of color displaying (ex 1). For
example, the RGB LED changes colors in 2s by default. After a button is pressed, the rate
will change to 1s, then 0.5s, 0.1s and back to 2s.
3. Similar to exercise 1, control the RGB LED by using PWM to change the brightness of
the led. Please read this link for more details of PWM:
https://developer.android.com/things/sdk/pio/pwm
4. Get input from a button and change the brightness of each color of the RGB LED. For
example, the RGB LED changes the brightness of red, green, blue by default. After a
button is pressed, only the red one is changing its brightness, then green, blue and back
to three colors.
5. Blink each LED in different paces. The red LED is blinking every 0.5s, the green is 2s,
and the blue is 3s.

Your task in this assignment is to implement an app that receives commands from UART
and run corresponding exercises above. The commands are detailed as follows:
1. ‘O’: App starts ready to receive commands.
2. ‘1’: App runs exercises 1.
3. ‘2’: App runs exercises 2.
4. ‘3’: App runs exercises 3.
5. ‘4’: App runs exercises 4.
6. ‘5’: App runs exercises 5.
7. ‘F’: App stops any running.

## Authors

* **Ta Huynh Thuy Linh _ 1511778**
* **Nguyen Luong Phuc Vinh _ 1514063**
* **Tui Nhat Quang _ 1512664**
* **Nguyen Van Minh _ 1512009**

## Getting Started

Please follow the general instructions for all exercises mentioned above.

In this exercise, we will use the USB to TTL for implementing UART. 

### Prerequisites

Everything required in Lab 1 and an USB to TTL module.

The following is the UART Connection

![alt text](https://developer.android.com/things/images/raspberrypi-console.png)


The following is the GPIO Connection Diagram. This diagram will be used in those 5 exercises in this assignment.

![alt text](https://github.com/lefeno/lab_iot/blob/vinh/Images/%5B1.4%5D%20Diagram.PNG)

**GPIO Connection Table:**

|Raspberry Pi 3|Peripherals|
|:--|:--|
|BCM2|B pin| 
BCM3|R pin
BCM4|G pin
PWM0|I pin 
BCM5|Button 
UART0 (TXD) | RX (USB TTL)
UART0 (RXD) | TX (USB TTL) 

### State Chart Diagram

Please review the state chart diagram for implementing those 5 exercises in file readme.md in c1ex.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

