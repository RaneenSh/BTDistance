/*
 * This is the Arduino code for  HC-SR04 Ultrasonic Distance Sensor with SSD1306 Display
 * to measure the distance using arduino for robotoic car and other applications
 * Watch the video https://youtu.be/Pgx5fNF4Q6M
 *  * 
 * Written by Ahmad Shamshiri for Robojax Video
 * Date: Dec 21, 2017, in Ajax, Ontario, Canada
 * Permission granted to share this code given that this
 * note is kept with the code.
 * Disclaimer: this code is "AS IS" and for educational purpose only.
 * 
 */

/* Original Code 
   from https://github.com/adafruit/Adafruit_SSD1306
// https://playground.arduino.cc/Code/NewPing
 * Modified for Robojax video on Dec 21, 2017
// ---------------------------------------------------------------------------
// Example NewPing library sketch that does a ping about 20 times per second.
// ---------------------------------------------------------------------------
*/

#include <U8g2lib.h>

#ifdef U8X8_HAVE_HW_SPI
#include <SPI.h>
#endif
#ifdef U8X8_HAVE_HW_I2C
#include <Wire.h>
#endif#include <U8g2lib.h>

#ifdef U8X8_HAVE_HW_SPI
#include <SPI.h>
#endif
#ifdef U8X8_HAVE_HW_I2C
#include <Wire.h>
#endif







#define OLED_RESET 4

#define NUMFLAKES 10
#define XPOS 0
#define YPOS 1
#define DELTAY 2

//// end of SSD1306 display 

// ---------------------------------------------------------------------------
// Example NewPing library sketch that does a ping about 20 times per second.
// ---------------------------------------------------------------------------

#include <NewPing.h>

#define TRIGGER_PIN  8  // Arduino pin tied to trigger pin on the ultrasonic sensor.
#define ECHO_PIN     9  // Arduino pin tied to echo pin on the ultrasonic sensor.
#define MAX_DISTANCE 500 // Maximum distance we want to ping for (in centimeters). Maximum sensor distance is rated at 400-500cm.

NewPing sonar(TRIGGER_PIN, ECHO_PIN, MAX_DISTANCE); // NewPing setup of pins and maximum distance.

U8G2_SSD1306_128X32_UNIVISION_F_HW_I2C u8g2(U8G2_R0);


void setup() {
   Serial.begin(9600);// set serial monitor with 9600 baud
   u8g2.begin();
   
  
}// setup end

float oldDistance = -1;
void loop() {

  float cm = sonar.ping_cm();
  float newDistance = ((int)((cm / 70.0f + 0.5f) * 2.0f)) / 2.0f;
  if (newDistance != oldDistance && cm != 0.0f)
  {
    Serial.println(newDistance); // Send ping, get distance in cm and print result (0 = outside set distance range)
    oldDistance = newDistance;
  }

  u8g2.clearBuffer();          // clear the internal memory
  u8g2.setFont(u8g2_font_ncenB08_tr); // choose a suitable font
  u8g2.drawStr(0,10,("Distance " + String(oldDistance)).c_str());  // write something to the internal memory
//  u8g2.drawStr(1,10, .c_str());  // write something to the internal memory

  u8g2.sendBuffer();          // transfer internal memory to the display

  delay(1000);                     // Wait 50ms between pings (about 20 pings/sec). 29ms should be the shortest delay between pings.


}// loop end


