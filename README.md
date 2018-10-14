
# Exercise 1.4

Get input from a button and change the brightness of each color of the RGB LED. For example, the RGB LED changes the brightness of red, green, blue by default. After a button is pressed, only the red one is changing its brightness, then green, blue and back to three colors.

## Authors

* **Ta Huynh Thuy Linh _ 1511778**

## Getting Started

Please follow the general instructions for all exercises mentioned above.

In this exercise, we will use RGB Led Common Cathode, with I is the common pin and a button for changing the brightness of each color of the RGB LED.

### Prerequisites

1 RGB LED

1 resistor

8 Jumper Wires

1 Breadboard

1 Button

**GPIO Connection Diagram**

![alt text](https://github.com/lefeno/lab_iot/blob/vinh/Images/%5B1.4%5D%20Diagram.PNG)

**GPIO Connection Table:**

|Raspberry Pi 3|Peripherals|
|:--|:--|
|BCM2|B pin| 
BCM3|R pin
BCM4|G pin
PWM0|I pin 
BCM5|Button 

### State chart diagram

This state chart below is used when we press a button, it will determine which led will change the brightness, which will not, for example, if state is STATE_RED, only led RED will change its brightness. The STATE_LED indicates 3 leds will change their brightness.

![alt text](https://github.com/lefeno/lab_iot/blob/linh/FC_1.PNG)

This state chart below is used to change led from red to green, and then blue based on a specific time after we pressed the button. Here for simplicity, we use a count variable that will count up to 60 for timer_flag. Based on the current state (STATE_RED, STATE_GREEN,...) the duty cycle will be 100 or gradually change for each LED_RED, LED_GREEN or LED_BLUE.

![alt text](https://github.com/lefeno/lab_iot/blob/linh/FC_2.PNG)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

