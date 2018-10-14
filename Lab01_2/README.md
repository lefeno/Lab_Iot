# Exercise 1.2

Get input from a button and then change the pace of color displaying (ex 1). Foexample, the RGB LED changes colors in 2s by default. After a button is pressed, the rate will change to 1s, then 0.5s, 0.1s and back to 2s.

## Authors

* **Nguyen Luong Phuc Vinh _ 1514063**

## Getting Started

Please follow the general instructions for all exercises mentioned above.

In this exercise, we will use RGB Led Common Cathode, with I is the common pin and a button for changing interval time between color changes.

### Prerequisites

1 RGB LED

1 resistor

7 Jumper Wires

1 Breadboard

1 Button

Interval Timer: a timer that requires a defined amount of time for RGB LED to change color.

**GPIO Connection Diagram**

![alt text](https://github.com/lefeno/lab_iot/blob/vinh/Images/%5B1.1%5D%20Diagram.png)

**GPIO Connection Table:**

|Raspberry Pi 3|Peripherals|
|:--|:--|
|BCM2|B pin| 
BCM3|R pin
BCM4|G pin
BCM5|Button 

### State chart diagram

This state chart demonstrate how interval timer works. When we press a button, the interval timer change in order: 1s, 0.5s, 0.1s and then back to 2s
![alt text](https://github.com/lefeno/lab_iot/blob/vinh/Images/%5B1.2%5D%20Interval%20change.png)

The following state chart shows the change of color whenever the interval timer count down to 0.
![alt text](https://github.com/lefeno/lab_iot/blob/vinh/Images/%5B1.2%5D%20State%20Interval.png)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

