#include <Adafruit_NeoPixel.h>
#include <DHT.h>
#include <SoftwareSerial.h>

//pinMapping
#define SW_PUMP 2  //펌프제어 인터럽트 핀
#define SW_FAN 3   //환풍기제어 인터럽트 핀
#define TEMP_HUM 4        //온습도센서
#define SOIL_MOISTURE A0  //토양수분센서
#define NEO_LED 5         //8-LED센서
#define CONTROL_PUMP 6    //펌프상태변환(릴레이1)
#define CONTROL_FAN 7     //환풍기상태변환(릴레이2)
#define BLUETOOTH_TX 8    //블루투스 송신
#define BLUETOOTH_RX 9    //블루투스 수신

#define DHT_TYPE DHT11    //온습도센서 데이터 값
#define NUM_LED_PIX 8     //링에 연결되어 있는 WS2812 LED 갯수
#define DELAY_MS 1000

void changePumpStatus();
void changeFanStatus();

DHT dht(TEMP_HUM, DHT_TYPE);  //온습도센서 객체 선언
SoftwareSerial BLUETOOTH(BLUETOOTH_TX, BLUETOOTH_RX);   //블루투스 소프트웨어시리얼 선언
Adafruit_NeoPixel myLeds = Adafruit_NeoPixel(NUM_LED_PIX, NEO_LED, NEO_GRB + NEO_KHZ800); // Parameter 1 = 링에 연결되어 있는 WS2812 LED 갯수
                                                                                          // Parameter 2 = WS2812에 연결하는데 사용하는 pin 번호
                                                                                          // Parameter 3 = pixel type flags, add together as needed:
                                                                                          // NEO_KHZ800 800 KHz bitstream (most NeoPixel products w/WS2812 LEDs)
                                                                                          // NEO_KHZ400 400 KHz (classic 'v1' (not v2) FLORA pixels, WS2811 drivers)
                                                                                          // NEO_GRB Pixels are wired for GRB bitstream (most NeoPixel products)
                                                                                          // NEO_RGB Pixels are wired for RGB bitstream (v1 FLORA pixels, not v2)

volatile boolean pumpStatus  = true;
volatile boolean fanStatus   = true;
char sendMessage[100];
char receiveMessage[100];
char receiveData;
int receiceMessagePosition = 0;
String data = "";

int soilValue         = 0;
int convertSoilValue  = 0;
int humidity          = 0;
int temperature       = 0;

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
  digitalWrite(CONTROL_PUMP, LOW);
  digitalWrite(CONTROL_FAN, LOW);
}

void loop() {
  soilValue         = analogRead(SOIL_MOISTURE);          //토양수분값 가져오기
  convertSoilValue  = map(soilValue, 240, 1023, 100, 0);  //토양수분값 비율로 전환
  humidity          = dht.readHumidity();                 //습도값 가져오기
  temperature       = dht.readTemperature();              //온도값 가져오기
  
  data = " " + (String)convertSoilValue + "," + (String)humidity + "," + (String)temperature + " ";
  data.toCharArray(sendMessage, data.length()+1);
  BLUETOOTH.write(sendMessage);

  changePumpStatus();
  changeFanStatus();

//  if (BLUETOOTH.available()) {
//    receiveData = BLUETOOTH.read();
//    Serial.println(receiveData);
//    receiveMessage[receiceMessagePosition++] = receiveData;
//    if(receiveData == '3') {
//      Serial.println(receiveMessage);
//    }
//  }
  
  delay(DELAY_MS);
}

void changePumpStatus() {
  if(digitalRead(SW_PUMP) == HIGH) {
    pumpStatus = !pumpStatus;
  }
  digitalWrite(CONTROL_PUMP, pumpStatus);
}

void changeFanStatus() {
  if(digitalRead(SW_FAN) == HIGH) {
    fanStatus = !fanStatus;
  }
  digitalWrite(CONTROL_FAN, fanStatus);
}
