#define LED_PIN 13

void setup() { //подготовка при запуске
  Serial.begin(9600);
  pinMode(LED_PIN,OUTPUT);
}

void error(){ //отображение ошибки миганием 13 светодиода
  for (int i=0;i<3;i++){
    digitalWrite(LED_PIN,5);
    delay(100);
    digitalWrite(LED_PIN,0);
    delay(100);
  }
}

void log(){ //функция, необходимая для отладки приложения
  digitalWrite(16,5);
  delay(300);
  digitalWrite(16,0);
  delay(300);
}

void bindPin(char pinType, int pinNom, int mode){ //установка режима пина
  switch (mode){
    case 1: pinMode(pinNom, INPUT); break;
    case 2: pinMode(pinNom, OUTPUT); break;
    case 3: pinMode(pinNom, INPUT_PULLUP); break;
  }
}

int getFromPin(char pinType, int pinNom){ //чтение входного значения с пина
  if (pinType=='D')
    return digitalRead(pinNom);
  else
    return analogRead(pinNom);
}

void setV(char pinType, int pinNom, int v){ //установка выходного значения на пин
  if (pinType=='D')
    digitalWrite(pinNom,v);
  else
    analogWrite(pinNom,v);
}

void loop() {
  if (Serial.available() != 0) {
    char pinType;
    int pinNom;
    int comm = Serial.read()-'0'; //код пришедшей команды
    
    switch (comm){
      //pinmode
      case 1:{
              while (Serial.available() == 0);
              pinType = Serial.read();
              while (Serial.available() == 0);
              pinNom = Serial.read()-'0';
              while (Serial.available() == 0);
              int mode = Serial.read()-'0';
              while (Serial.available() == 0);
              
              if (Serial.read()=='|')
                bindPin(pinType,pinNom,mode);
              else
                error();
              break;  
      }     
      //чтение с пина
      case 2:{
              while (Serial.available() == 0);
              pinType = Serial.read();
              while (Serial.available() == 0);
              pinNom = Serial.read()-'0';
              while (Serial.available() == 0);
              
              if (Serial.read()=='|'){
                for (int i=0;i<10;i++){
                Serial.print(getFromPin(pinType,pinNom));
                delay(100);
                }
              }else
                error();
              break;  
      }
      //установка значения на пин
      case 3:{ 
              while (Serial.available() == 0);
              pinType = Serial.read();
              while (Serial.available() == 0);
              pinNom = (Serial.read()-'0');
              while (Serial.available() == 0);
              int v = Serial.read()-'0';
              while (Serial.available() == 0);
              
              if (Serial.read()=='|')
                setV(pinType,pinNom,v);
              else
                error();
              break;  
      }
    }
  }
}
