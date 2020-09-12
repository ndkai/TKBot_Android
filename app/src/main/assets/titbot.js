'use strict';

var Turtle = {};

Turtle.sensor = 0;

Turtle.touchPort = "00";


Turtle.temperature = 0;

Turtle.execute = function(code) {
try {
    eval(code);

  } catch (e) {
    if (e !== Infinity) {
      alert(e);
    }
  }


};


function sleep(milliseconds) {
  const date = Date.now();
  let currentDate = null;
  do {
    currentDate = Date.now();
  } while (currentDate - date < milliseconds);
}

Turtle.MB = function(value){
  Android.moveBackward(value);
};

Turtle.MF = function(value){
  Android.moveForward(value);
};

Turtle.TL = function(value){
  Android.turnLeft(value);
};

Turtle.TR = function(value){
  Android.turnRight(value);
};

Turtle.motor1 = function(value){
  Android.moveMotor1(value);
};

Turtle.motor2 = function(value){
  Android.moveMotor2(value);
};

//ultrasensor
 Turtle.ultraSensor = function(port){
 Turtle.sensor = Android.requestSensor(port);
 return Turtle.sensor;
 }

//servo
Turtle.servo = function(port, slot, angle){
    Android.servo(port, slot, angle);
}

//temperature
Turtle.temperature = function(port,slot){
     return Android.getTemVal(port, slot);
}

//follow1
 Turtle.lineFollow1 = function(port){
     Android.lineFollow1(port);
 }

//follow2
Turtle.lineFollow2 = function(port, side, color){
    Android.lineFollow2(port, side, color);
}

//touch
Turtle.touch = function(port){
    Turtle.touchPort = port;
    return Android.getTouchVal(port);
}

Turtle.requestTouch = function(){
    return Android.requestTouch(Turtle.touchPort);
}

//light
Turtle.lightSensor = function(port){
    Android.lightSensor(port);
}

//sound
Turtle.sound = function(port){
   return Android.getSoundVal(port);
}

//leb
Turtle.setLed = function(port, numleb, red, green, blue){
   return Android.getSoundVal(port, numleb, red, green, blue);
}

//stop
Turtle.isStop = function(){
if(Android.isStop() == 1){
    return true;
}
return false;
}
