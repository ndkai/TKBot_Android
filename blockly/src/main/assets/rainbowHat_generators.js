'use strict';

Blockly.JavaScript['rainbow_hat_button_action'] = function(block) {
    var button = Blockly.JavaScript.variableDB_.getName(block.getFieldValue('BUTTON'), Blockly.Variables.NAME_TYPE);
    var buttonName = 'A';
    if (button == "BUTTON_A") {
        buttonName = 'A';
    }
    else if (button == "BUTTON_B") {
        buttonName = 'B';
    }
    else if (button == "BUTTON_C") {
        buttonName = 'C';
    }

    var state = Blockly.JavaScript.variableDB_.getName(block.getFieldValue('STATE'), Blockly.Variables.NAME_TYPE);
    var buttonState = 'Pressed';
    if (state == "BUTTON_PRESSED") {
        buttonState = 'Pressed';
    }
    else if (state == "BUTTON_RELEASED") {
        buttonState = 'Released';
    }
    else if (state == "BUTTON_CHANGED") {
        buttonState = 'Changed';
    }

    var nextCode = Blockly.JavaScript.statementToCode(block, 'DO');
    return 'function onButton'+buttonName+buttonState+'() {\n' +
                          nextCode+'\n' +
                          '}\n';
};

Blockly.JavaScript['rainbow_hat_read_button'] = function(block) {
    var button = Blockly.JavaScript.variableDB_.getName(block.getFieldValue('BUTTON'), Blockly.Variables.NAME_TYPE);
    var buttonName = 'A';
    if (button == "BUTTON_A") {
        buttonName = 'A';
    }
    else if (button == "BUTTON_B") {
        buttonName = 'B';
    }
    else if (button == "BUTTON_C") {
        buttonName = 'C';
    }
    return ["Android.getStateButton"+buttonName+"()", Blockly.JavaScript.ORDER_NONE];
};

Blockly.JavaScript['rainbow_hat_read_led'] = function(block) {
    var led = Blockly.JavaScript.variableDB_.getName(block.getFieldValue('LED'), Blockly.Variables.NAME_TYPE);
    var ledName = 'Red';
    if (led == "LED_RED") {
        ledName = 'Red';
    }
    else if (led == "LED_GREEN") {
        ledName = 'Green';
    }
    else {
        ledName = 'Blue';
    }
    return ["Android.getStateLed"+ledName+"()", Blockly.JavaScript.ORDER_NONE];
};


Blockly.JavaScript['rainbow_hat_set_led_value'] = function(block) {
    var led = Blockly.JavaScript.variableDB_.getName(block.getFieldValue('LED'), Blockly.Variables.NAME_TYPE);
    var pressed = Blockly.JavaScript.valueToCode(block, 'VALUE', Blockly.JavaScript.ORDER_FUNCTION_CALL) || 'true'
    if (led == "LED_RED") {
        return 'Android.setRedLed('+pressed+');';
    }
    else if (led == "LED_GREEN") {
        return 'Android.setGreenLed('+pressed+');';
    }
    else {
        return 'Android.setBlueLed('+pressed+');';
    }
};

Blockly.JavaScript['rainbow_hat_display_text'] = function(block) {
    var text = Blockly.JavaScript.valueToCode(block, 'VALUE', Blockly.JavaScript.ORDER_FUNCTION_CALL) || "''"
    return 'AlphanumericDisplay.displayText('+text+');\n';
};

Blockly.JavaScript['rainbow_hat_display_number'] = function(block) {
    var number = Blockly.JavaScript.valueToCode(block, 'VALUE', Blockly.JavaScript.ORDER_FUNCTION_CALL) || 0.0
    return 'AlphanumericDisplay.displayNumber('+number+');\n';
};

Blockly.JavaScript['rainbow_hat_read_temperature'] = function(block) {
    return ["Bmx280.readTemperature()", Blockly.JavaScript.ORDER_NONE];
};

Blockly.JavaScript['rainbow_hat_read_pressure'] = function(block) {
    return ["Bmx280.readPressure()", Blockly.JavaScript.ORDER_NONE];
};

Blockly.JavaScript['rainbow_hat_play'] = function(block) {
    var frequency = Blockly.JavaScript.valueToCode(block, 'VALUE', Blockly.JavaScript.ORDER_FUNCTION_CALL) || "0.0"
    return 'Speaker.play('+frequency+');';
};

Blockly.JavaScript['rainbow_hat_stop_playing'] = function(block) {
    return 'Speaker.stop();';
};

Blockly.JavaScript['rainbow_hat_write_led_strip'] = function(block) {
    return '{\n'+
            'let colors = [\n'+
            block.getFieldValue('COLOR1').replace('#','0x')+',\n'+
            block.getFieldValue('COLOR2').replace('#','0x')+',\n'+
            block.getFieldValue('COLOR3').replace('#','0x')+',\n'+
            block.getFieldValue('COLOR4').replace('#','0x')+',\n'+
            block.getFieldValue('COLOR5').replace('#','0x')+',\n'+
            block.getFieldValue('COLOR6').replace('#','0x')+',\n'+
            block.getFieldValue('COLOR7').replace('#','0x')+'\n'+
            '];\n'+
            'Apa102.write(colors);\n'+
            '}';
};

Blockly.JavaScript['rainbow_hat_write_led_strip_array'] = function(block) {
    var colorsArray = Blockly.JavaScript.valueToCode(block, 'COLORS', Blockly.JavaScript.ORDER_FUNCTION_CALL) || "[]";
    return  '{\n'+
            'let colors = [];\n'+
            'for (let i = 0; i < '+colorsArray+'.length; i++) {\n'+
            '   colors[i] = parseInt('+colorsArray+'[i].replace(\"#\",\"0x\"));\n'+
            '}\n'+
            'Apa102.write(colors);\n'+
            '}';
};