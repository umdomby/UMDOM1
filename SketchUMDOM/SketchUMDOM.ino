char incomingbyteChar;
String inData;
boolean flagRelay0=false,flagRelay1=false,flagRelay2=false,flagRelay3=false,flagRelay4=false, 
flagRelay5=false,flagRelay6=false,flagRelay7=false,flagRelay8=false;
unsigned long timing1;

char incomingbyteChar1;
String myStrings[] ={"a","b","c","d","e","f","g","h","i","DHT1","DHT1","DHT2","DHT2","G1","G2","M1"};

//DHT
#include "DHT.h"
#define DHTPIN 2 
#define DHTPIN2 3 
#define DHTTYPE DHT11   // DHT 11
#define DHTTYPE2 DHT11   // DHT 11
//#define DHTTYPE DHT22   // DHT 22  (AM2302)
DHT dht(DHTPIN, DHTTYPE);
DHT dht2(DHTPIN2, DHTTYPE2);

//Gaz
int pushGaz1 = 4;
int pushGaz2 = 5;

//Motion
#define pirPin 6

unsigned long currentMillis;
String SerialData;
unsigned long previousMillis1 = 0;
int dataGaz1 = 0 ;
int dataGaz2 = 0;
int pirVal = 0;


void setup() 
 {
   Serial.begin(9600); 
   Serial1.begin(9600); 
   pinMode(22, OUTPUT);
   pinMode(23, OUTPUT);
   pinMode(24, OUTPUT);
   pinMode(25, OUTPUT);
   pinMode(26, OUTPUT);
   pinMode(27, OUTPUT);
   pinMode(28, OUTPUT);
   pinMode(29, OUTPUT);
   digitalWrite(22,HIGH);
   digitalWrite(23,HIGH);
   digitalWrite(24,HIGH);
   digitalWrite(25,HIGH);
   digitalWrite(26,HIGH);
   digitalWrite(27,HIGH);
   digitalWrite(28,HIGH);
   digitalWrite(29,HIGH);
   pinMode(pushGaz1, INPUT);
   pinMode(pushGaz2, INPUT);
   dht.begin();
   dht2.begin();   
   pinMode(pirPin, INPUT);
   
 }

void loop() 
 {
   currentMillis = millis();
   dataGaz1 = digitalRead(pushGaz1);
   dataGaz2 = digitalRead(pushGaz2);
   pirVal = digitalRead(pirPin);


    if(Serial1.available() > 0 ) // есть ли что-то в буфере
    {
        delay(2);
          incomingbyteChar1 = Serial1.read(); 
          
//Relay0 общий
        if (incomingbyteChar1 == 'a'){    
            digitalWrite(22,HIGH);digitalWrite(23,HIGH);digitalWrite(24,HIGH);digitalWrite(25,HIGH);digitalWrite(26,HIGH);digitalWrite(27,HIGH);
            digitalWrite(28,HIGH);digitalWrite(29,HIGH);
            flagRelay0 = false;
            myStrings[0]= "a";myStrings[1] = "b";myStrings[2] = "c";myStrings[3] = "d";myStrings[4] = "e";myStrings[5] = "f";myStrings[6] = "g";
            myStrings[7] = "h";myStrings[8] = "i";   
        }
        if (incomingbyteChar1 == 'A'){        
            digitalWrite(22,LOW);digitalWrite(23,LOW);digitalWrite(24,LOW);digitalWrite(25,LOW);digitalWrite(26,LOW);digitalWrite(27,LOW);
            digitalWrite(28,LOW);digitalWrite(29,LOW);
            flagRelay0 = false;
            myStrings[0]= "A";myStrings[1] = "B";myStrings[2] = "C";myStrings[3] = "D";myStrings[4] = "E";myStrings[5] = "F";myStrings[6] = "G";
            myStrings[7] = "H";myStrings[8] = "I";
           }
           
//Relay1
        if (incomingbyteChar1 == 'b'){   
           digitalWrite(22,HIGH);
           flagRelay1 = false;
           myStrings[1] = "b";}          
        if (incomingbyteChar1 == 'B'){      
           digitalWrite(22,LOW);
           flagRelay1 = true;
           myStrings[1] = "B";}
              
//Relay2
        if (incomingbyteChar1 == 'c'){   
           digitalWrite(23,HIGH);
           myStrings[2] = "c";
           flagRelay2 = false;
        }
        if (incomingbyteChar1 == 'C'){      
           digitalWrite(23,LOW);
           myStrings[2] = "C";
           flagRelay2 = true;}
           
//Relay3
        if (incomingbyteChar1 == 'd'){        
           digitalWrite(24,HIGH);
           myStrings[3] = "d";
           flagRelay3 = false; 
           }
        if (incomingbyteChar1 == 'D'){
           digitalWrite(24,LOW);
           myStrings[3] = "D";
           flagRelay3 = true;}
           
//Relay4
        if (incomingbyteChar1 == 'e'){
           digitalWrite(25,HIGH);
           myStrings[4] = "e";
           flagRelay4 = false; 
        }
        if (incomingbyteChar1 == 'E'){
           digitalWrite(25,LOW);
           myStrings[4] = "E";
           flagRelay4 = true; }
           
//Relay5
        if (incomingbyteChar1 == 'f'){   
           digitalWrite(26,HIGH);
           myStrings[5] = "f";
           flagRelay5 = false; 
        }
        if (incomingbyteChar1 == 'F'){      
           digitalWrite(26,LOW);
           myStrings[5] = "F";
           flagRelay5 = true;} 
            
//Relay6
        if (incomingbyteChar1 == 'g'){   
           digitalWrite(27,HIGH);
           myStrings[6] = "g";
           flagRelay6 = false; 
        }
        if (incomingbyteChar1 == 'G'){      
           digitalWrite(27,LOW);
           myStrings[6] = "G";
           flagRelay6 = true;}
           
//Relay7
        if (incomingbyteChar1 == 'h'){   
           digitalWrite(28,HIGH);
           myStrings[7] = "h";
           flagRelay7 = false;  
         }
        if (incomingbyteChar1 == 'H'){      
           digitalWrite(28,LOW);
           myStrings[7] = "H";
           flagRelay7 = true;} 

//Relay8
        if (incomingbyteChar1 == 'i'){   
           digitalWrite(29,HIGH);
           myStrings[8] = "i";
           flagRelay8 = false;  
         }
        if (incomingbyteChar1 == 'I'){      
           digitalWrite(29,LOW);
           myStrings[8] = "I";
           flagRelay8 = true;}  
    } 


myStrings[9] = dht.readTemperature();
myStrings[10] = dht.readHumidity();
myStrings[11] = dht2.readTemperature();
myStrings[12] = dht2.readHumidity();
myStrings[13] = dataGaz1;
myStrings[14] = dataGaz2;
myStrings[15] = pirVal;




                  if (currentMillis - previousMillis1 >= 500){
              previousMillis1 = currentMillis;             
                    for (int i = 0; i < 16; i++){        
                    SerialData += myStrings[i];
                    SerialData += ",";   
                    }
                Serial.println(SerialData);
                Serial1.println(SerialData);
                SerialData = "";
              }
   
//                   if (millis() - timingTemp > 5000 || dataGaz1 == 0 || dataGaz2 == 0 || pirVal == 1){ 
//                    timingTemp = millis(); 
//                    int t = dht.readTemperature();
//                    int h = dht.readHumidity();
//                    int a = dht2.readTemperature();
//                    int b = dht2.readHumidity();
//                    
//                    Serial1.print("X");Serial1.print("  t1: "); Serial1.print(t); Serial1.print(" h1: "); Serial1.print(h); Serial1.print(" t2: "); Serial1.print(a); Serial1.print(" h2: "); Serial1.print(b); 
//                    Serial1.print(" G1: "); Serial1.print(dataGaz1);Serial1.print(" G2: "); Serial1.print(dataGaz2); 
//                    Serial1.print(" M: "); Serial1.print(pirVal); 
//                    Serial1.println(" ");     
//                    delay(100);  
//                    }


              
                          

}




