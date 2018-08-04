#include <SoftwareSerial.h>
#include <dht.h>
int smoke = A5;
#define led  13
int motionflag=0;
int val = 0;   
int bluetoothTx = 2;  // TX-O pin of bluetooth mate, Arduino D2
int bluetoothRx = 3;  // RX-I pin of bluetooth mate, Arduino D3
#define RELAY1  8 
#define pwm 9
#define RELAY3  10 
#define RELAY2  11 
dht DHT;
int sensor = 12;
#define DHT11_PIN 7
SoftwareSerial bluetooth(bluetoothTx, bluetoothRx);
int start=1;


void setup()
{
  pinMode(led,OUTPUT);
  pinMode(sensor, INPUT); 
  digitalWrite(RELAY2,1);
pinMode(smoke, INPUT);
  digitalWrite(led, HIGH);
Serial.begin(9600);  // Begin the serial monitor at 9600bps
pinMode(RELAY1, OUTPUT);
// Begin the serial monitor at 9600bps
pinMode(RELAY2, OUTPUT);

pinMode(RELAY3, OUTPUT) ;
  digitalWrite(RELAY1,0);
  digitalWrite(RELAY3,1);
  
  bluetooth.begin(9600);  // Start bluetooth serial at 9600
}
//auto start

void Auto(){
  
  
   if(DHT.temperature<15){
    
    
  analogWrite(pwm, 0);
  bluetooth.println("c");
    
    } 
   else if(DHT.temperature>15 && DHT.temperature<20){
    
  bluetooth.println("d");   
  analogWrite(pwm, 63);   
    
    } 
    
   else if(DHT.temperature>20 && DHT.temperature<25){
    
     bluetooth.println("e");
  analogWrite(pwm,127);   
    
    } 
    
   else if(DHT.temperature>25 && DHT.temperature<30){
    
     bluetooth.println("f");
  analogWrite(pwm, 190);   
    
    } 
    
   else {
    
     bluetooth.println("g");
  analogWrite(pwm, 255);   

    
    }
    return; 
 }
//auto end 














void loop()
{
int chk = DHT.read11(DHT11_PIN);

 if(start==1){
  
 start--;
  Auto();
 }
  
  
 
 
 int smokeflag=0;
 int analogSensor;
 analogSensor= analogRead(smoke);
 
     while(analogSensor>600)
 {
 analogSensor= analogRead(smoke);
  Serial.println(analogSensor);
if(smokeflag==0){
  
  bluetooth.println("x");
  smokeflag++;
  
  }
 
  
      digitalWrite(RELAY1,1);
   
      digitalWrite(RELAY2,0);
      digitalWrite(RELAY3,0);
      
      delay(1000);
      digitalWrite(RELAY2,1);
      digitalWrite(RELAY3,1);
      
      delay(1000);
      

  
  
  }

  if(smokeflag==1){
    bluetooth.println("y");
    smokeflag=0;
    
    }

  
 
      digitalWrite(RELAY1,0);
      
   
 
  
  
 
 String display="";
  if(bluetooth.available())  // If the bluetooth sent any characters
 {

display = (char)bluetooth.read();
}
  Serial.print(display);


if(display=="i"){
  
  Auto();
  }
else if(display=="j"){
  
String temp=(String)DHT.temperature;
String humid=(String)DHT.humidity;
 String msg="Temperature = "+temp+" Humidity ="+humid;
 
 bluetooth.println(msg);
 
 
  } 
else if(display=="b"){
  
  digitalWrite(led, HIGH);
 
 
  }
else if(display=="a"){
  digitalWrite(led, LOW);
 
  
   }  
     

      
else if(display=="c"){
  
  analogWrite(pwm, 0);
  
  }

      
else if(display=="d"){
  
  analogWrite(pwm, 63);
  
  }
        
else if(display=="e"){
  
  analogWrite(pwm, 127);
  
  }
  
else if(display=="f"){
  
  analogWrite(pwm, 190);
  
  }

else if(display=="g"){
  
  analogWrite(pwm, 255);
  
  }
else if(display=="k")  
  {
   motionflag=1;
    }
 else if(display=="l")
 {
  
  motionflag=0;
  }   


if(motionflag==1)
{
  
    val = digitalRead(sensor);   // read sensor value
  while (val == HIGH) {


        
                    // delay 100 milliseconds 
  digitalWrite(RELAY3,0);
    bluetooth.println("s");
  // check if the sensor is HIGH
Serial.println("detected");
  
  while(bluetooth.available()){
    
  display = (char)bluetooth.read();
  if(display=="l"){
  bluetooth.println("u");
  
    
    digitalWrite(RELAY3,1);  
  motionflag=0;
    return;
    }
  
    }
  
       // update variable state to HIGH
  
  }
  
  
  }





} 
