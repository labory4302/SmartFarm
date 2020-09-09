#include <Adafruit_NeoPixel.h>
#include <DHT.h>
#include <SoftwareSerial.h>

//pinMapping
#define SW_PUMP 2       //펌프제어 SW 핀
#define SW_FAN 3        //환풍기제어 SW 핀
#define SW_LED 4        //8-LED제어 SW 핀
#define TEMP_HUM 5      //온습도센서
#define SOIL_MOISTURE A0//토양수분센서
#define NEO_LED 6       //8-LED센서
#define CONTROL_PUMP 7  //펌프상태변환(릴레이1)
#define CONTROL_FAN 8   //환풍기상태변환(릴레이2)
#define BLUETOOTH_TX 9  //블루투스 송신
#define BLUETOOTH_RX 10 //블루투스 수신

#define DHT_TYPE DHT11  //온습도센서 데이터 값
#define NUM_LED_PIX 8   //링에 연결되어 있는 WS2812 LED 갯수
#define DELAY_MS 1000   //루프 지연시간

void changePumpStatus();
void changeFanStatus();
void changeLEDStatus();
void controlLED(int input);
void controlPump(int input);
void controlFan(int input);
void checkSoilMoisture(int input);
void checkHumidity(int input);
void setAutoDoSprinkler(int input);
void setAutoDoFan(int input);
void changeAutoMode();

DHT dht(TEMP_HUM, DHT_TYPE);  //온습도센서 객체 선언
SoftwareSerial BLUETOOTH(BLUETOOTH_TX, BLUETOOTH_RX);   //블루투스 소프트웨어시리얼 선언
Adafruit_NeoPixel myLeds = Adafruit_NeoPixel(NUM_LED_PIX, NEO_LED, NEO_GRB + NEO_KHZ800); // Parameter 1 = 링에 연결되어 있는 WS2812 LED 갯수
                                                                                          // Parameter 2 = WS2812에 연결하는데 사용하는 pin 번호
                                                                                          // Parameter 3 = pixel type flags, add together as needed:
                                                                                          // NEO_KHZ800 800 KHz bitstream (most NeoPixel products w/WS2812 LEDs)
                                                                                          // NEO_KHZ400 400 KHz (classic 'v1' (not v2) FLORA pixels, WS2811 drivers)
                                                                                          // NEO_GRB Pixels are wired for GRB bitstream (most NeoPixel products)
                                                                                          // NEO_RGB Pixels are wired for RGB bitstream (v1 FLORA pixels, not v2)

boolean pumpStatus  = false;
boolean fanStatus   = false;
boolean LEDStatus   = false;
char sendMessage[100];
String receiveMessage="";
char receiveData;
String data = "";
int inputControlCode      = 0;
int inputControlBehavior  = 0;

int soilValue         = 0;
int convertSoilValue  = 0;
int humidity          = 0;
int temperature       = 0;

int autoDoSprinkler   = 0;
int autoDoFan         = 90;
int autoMode          = 0;

void setup() {
  Serial.begin(9600);
  BLUETOOTH.begin(9600);
  myLeds.begin();
  for(int i=0;i<NUM_LED_PIX;i++) {  //LED점등
    myLeds.setPixelColor(i, 255, 255, 255);
    myLeds.show();
  }
  pinMode(CONTROL_PUMP, OUTPUT);
  pinMode(CONTROL_FAN, OUTPUT);
  pinMode(SW_PUMP, INPUT);
  pinMode(SW_FAN, INPUT);
  pinMode(SW_LED, INPUT);
  digitalWrite(CONTROL_PUMP, !pumpStatus);
  digitalWrite(CONTROL_FAN, !fanStatus);
}

void loop() {
  soilValue         = analogRead(SOIL_MOISTURE);          //토양수분값 가져오기
  convertSoilValue  = map(soilValue, 340, 1023, 100, 0);  //토양수분값 비율로 전환
  humidity          = dht.readHumidity();                 //습도값 가져오기
  temperature       = dht.readTemperature();              //온도값 가져오기
  
  data = " " + (String)convertSoilValue + "," + (String)humidity + "," + (String)temperature + " ";
  data.toCharArray(sendMessage, data.length()+1);
  BLUETOOTH.write(sendMessage);

  changePumpStatus();
  changeFanStatus();
  changeLEDStatus();

  checkSoilMoisture(convertSoilValue);
  checkHumidity(humidity);

  while(BLUETOOTH.available()) {
    receiveData = BLUETOOTH.read();
    if((receiveData >= '0' && receiveData <= '9') || receiveData == '\n') {
      receiveMessage.concat(receiveData);
    }
    if(receiveData == '\n') {
      Serial.println(receiveMessage);
      inputControlCode = receiveMessage.toInt()/1000;
      inputControlBehavior = receiveMessage.toInt()%1000;
      switch(inputControlCode) {
        case 1:
          controlLED(inputControlBehavior);
          break;
        case 2:
          controlPump(inputControlBehavior);
          break;
        case 3:
          controlFan(inputControlBehavior);
          break;
        case 4:
          setAutoDoSprinkler(inputControlBehavior);
          break;
        case 5:
          setAutoDoFan(inputControlBehavior);
          break;
        case 9:
          changeAutoMode();
        default:
          break;
      }
      receiveMessage = "";
    }
  }
  
  delay(DELAY_MS);
}

void controlLED(int input) {
  if(input == 1) {
    for(int i=0;i<NUM_LED_PIX;i++) {
      myLeds.setPixelColor(i, 255, 255, 255);
      myLeds.show();
    }
  } else {
    for(int i=0;i<NUM_LED_PIX;i++) {
      myLeds.setPixelColor(i, 0, 0, 0);
      myLeds.show();
    }
  }
  Serial.println("complete");
}

void controlPump(int input) {
  if(input == 1) {
    pumpStatus = true;
  } else {
    pumpStatus = false;
  }
  digitalWrite(CONTROL_PUMP, !pumpStatus);
}

void controlFan(int input) {
  if(input == 1){
    fanStatus = true;
  } else {
    fanStatus = false;
  }
  digitalWrite(CONTROL_FAN, !fanStatus);
}

void setAutoDoSprinkler(int input) {
  autoDoSprinkler = input;
}

void setAutoDoFan(int input) {
  autoDoFan = input;
}

void changePumpStatus() {
  if(digitalRead(SW_PUMP) == HIGH) {
    pumpStatus = !pumpStatus;
  }
  digitalWrite(CONTROL_PUMP, !pumpStatus);
}

void changeFanStatus() {
  if(digitalRead(SW_FAN) == HIGH) {
    fanStatus = !fanStatus;
  }
  digitalWrite(CONTROL_FAN, !fanStatus);
}

void changeLEDStatus() {
  if(digitalRead(SW_LED) == HIGH) {
    LEDStatus = !LEDStatus;
    if(LEDStatus == true) {
       for(int i=0;i<NUM_LED_PIX;i++) {
        myLeds.setPixelColor(i, 0, 0, 0);
        myLeds.show();
       }
    } else {
      for(int i=0;i<NUM_LED_PIX;i++) {
        myLeds.setPixelColor(i, 255, 255, 255);
        myLeds.show();
      }
    }
  }
}

void checkSoilMoisture(int input) {
  if(autoMode == 1) {
    if(input < autoDoSprinkler) {
      digitalWrite(CONTROL_PUMP, LOW);
    } else {
      digitalWrite(CONTROL_PUMP, HIGH);
    } 
  }
}

void checkHumidity(int input) {
  if(autoMode == 1) {
    if(input > autoDoFan) {
      digitalWrite(CONTROL_FAN, LOW);
    } else {
      digitalWrite(CONTROL_FAN, HIGH);
    }
  }
}

void changeAutoMode() {
  if(autoMode == 0) {
    autoMode = 1;
  } else {
    autoMode = 0;
  }

  Serial.println(autoMode);
}
