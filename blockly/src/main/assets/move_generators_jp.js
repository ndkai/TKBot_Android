'use strict';

          //led
         Blockly.JavaScript['led'] = function(block) {
           var dropdown_port = block.getFieldValue('port');
           var dropdown_led_num = block.getFieldValue('led_num');
           var dropdown_red = block.getFieldValue('red');
           var dropdown_green = block.getFieldValue('green');
           var dropdown_blue = block.getFieldValue('blue');
           // TODO: Assemble JavaScript into code variable.
         var code = "Turtle.setLed(\'"+dropdown_port+"\',\'"+dropdown_led_num+"\',\'"+dropdown_red+"\',\'"+dropdown_green+"\',\'"+dropdown_blue+"\')\n";
           return code;
         };
         //sound sensor
         Blockly.JavaScript['sound_sensor'] = function(block) {
           var dropdown_sound_port = block.getFieldValue('sound_port');
           // TODO: Assemble JavaScript into code variable.

           return [dropdown_sound_port, Blockly.JavaScript.ORDER_NONE];;
         };

        //touch sensor
        Blockly.JavaScript['touch_sensor'] = function(block) {
          var dropdown_touch_port = block.getFieldValue('touch_port');
          // TODO: Assemble JavaScript into code variable.

          // TODO: Change ORDER_NONE to the correct strength.
          return [dropdown_touch_port, Blockly.JavaScript.ORDER_NONE];
        };

        //avoid sensor
                Blockly.JavaScript['avoid_sensor'] = function(block) {
                  var dropdown_avoid_port = block.getFieldValue('avoid_port');
                  // TODO: Assemble JavaScript into code variable.

                  // TODO: Change ORDER_NONE to the correct strength.
                  return [dropdown_avoid_port, Blockly.JavaScript.ORDER_NONE];
                };

         //line forlower
         Blockly.JavaScript['line_follow'] = function(block) {
           var dropdown_line_port = block.getFieldValue('line_port');
           // TODO: Assemble JavaScript into code variable.
          return [dropdown_line_port, Blockly.JavaScript.ORDER_NONE];
         };

        Blockly.JavaScript['line_follow2'] = function(block) {
          var dropdown_line_port = block.getFieldValue('line_port');
          var dropdown_side = block.getFieldValue('side');
          var dropdown_color = block.getFieldValue('color');
          // TODO: Assemble JavaScript into code variable.
        var code = "Turtle.lineFollow2(\'"+dropdown_line_port+"\',\'"+dropdown_side+"\',\'"+dropdown_color+"\')\n";
          return [code, Blockly.JavaScript.ORDER_NONE];
        };


        //light sensor
        Blockly.JavaScript['light_sensor'] = function(block) {
          var dropdown_name = block.getFieldValue('NAME');
          // TODO: Assemble JavaScript into code variable.
           var code = "Turtle.lightSensor(\'"+ dropdown_name+"\');\n";
              return [dropdown_name, Blockly.JavaScript.ORDER_NONE];
        };
       //temperature
        Blockly.JavaScript['temperature_tit'] = function(block) {
          var dropdown_tem_port = block.getFieldValue('tem_port');
          // TODO: Assemble JavaScript into code variable.
          var code ="Turtle.temperature(\'"+dropdown_tem_port+"\')";
          return [code, Blockly.JavaScript.ORDER_NONE];
        };
        //pontel

                        Blockly.JavaScript['potenl_tit'] = function(block) {
                             var dropdown_tem_port = block.getFieldValue('potenl_port');
                             // TODO: Assemble JavaScript into code variable.
                             var code ="Turtle.potenl(\'"+dropdown_tem_port+"\')";
                             return [code, Blockly.JavaScript.ORDER_NONE];
                           };
      //servo
      Blockly.JavaScript['servo_tit'] = function(block) {
        var dropdown_servo_port = block.getFieldValue('servo_port');
        var number_servo_angle = block.getFieldValue('servo_angle');
        // TODO: Assemble JavaScript into code variable.
        var code = "Turtle.servo(\'"+dropdown_servo_port+"\',\'"+'01'+"\',"+number_servo_angle+");\n if(Turtle.isStop() == true){break;}\n";
        return code;
      };

     //wait
     Blockly.JavaScript['wait'] = function(block) {
       var number_name = block.getFieldValue('NAME');
       // TODO: Assemble JavaScript into code variable.
       return "sleep("+number_name+");";
     };

     //motor 1
    Blockly.JavaScript['move1'] = function(block) {
      var dropdown_valuemotor1 = block.getFieldValue('valuemotor1');
      // TODO: Assemble JavaScript into code variable.
      var code = "Turtle.motor1("+dropdown_valuemotor1+");\n if(Turtle.isStop() == true){break;}\n";
      return code;
    };

      //motor 2
Blockly.JavaScript['move2'] = function(block) {
  var dropdown_valuemotor1 = block.getFieldValue('valuemotor2');
  // TODO: Assemble JavaScript into code variable.
  var code = "Turtle.motor2("+dropdown_valuemotor1+");\n if(Turtle.isStop() == true){break;}\n";
  return code;
};

    //compare
  Blockly.JavaScript['compare'] = function(block) {
    var value_number1 = Blockly.JavaScript.valueToCode(block, 'number1', Blockly.JavaScript.ORDER_ATOMIC);
    var dropdown_dropdown = block.getFieldValue('dropdown');
    var value_number2 = Blockly.JavaScript.valueToCode(block, 'number2', Blockly.JavaScript.ORDER_ATOMIC);
    // TODO: Assemble JavaScript into code variable.
    var code = value_number1 + dropdown_dropdown + value_number2;
    // TODO: Change ORDER_NONE to the correct strength.
    return [code, Blockly.JavaScript.ORDER_NONE];
  };
  // untra
   Blockly.JavaScript['ultrasonic_port'] = function(block) {
  var dropdown_ul_port = block.getFieldValue('Ul_Port');
  // TODO: Assemble JavaScript into code variable.
  var code = dropdown_ul_port  ;
// TODO: Change ORDER_NONE to the correct strength.
  return [code, Blockly.JavaScript.ORDER_NONE];
    };


  Blockly.JavaScript['motor_total'] = function(block) {
    // Generate JavaScript for moving forward or backwards.
    var value = block.getFieldValue('VALUE');
   return 'Turtle.' + block.getFieldValue('DIR') +
         '(' + value + ', \'block_id_' + block.id + '\');\n'+ "if(Turtle.isStop() == true){break;}\n";
  };

    Blockly.JavaScript['motor_m1_m2_value'] = function(block) {
      // Generate JavaScript for moving forward or backwards.
      var value = block.getFieldValue('VALUE');
      return 'Turtle.' + block.getFieldValue('DIR') +
            '(' + value + ', \'block_id_' + block.id + '\');\n'+"if(Turtle.isStop() == true){break;}\n";
    };

// Extensions to Blockly's language and JavaScript generator.
Blockly.JavaScript['turtle_move_internal'] = function(block) {
  // Generate JavaScript for moving forward or backwards.
  var value = block.getFieldValue('VALUE');
 return 'Turtle.' + block.getFieldValue('DIR') +
       '(' + value + ', \'block_id_' + block.id + '\');\n if(Turtle.isStop() == true){break;}\n';
};

Blockly.JavaScript['turtle_turn_internal'] = function(block) {
  // Generate JavaScript for turning left or right.
  var value = block.getFieldValue('VALUE');
 return 'Turtle.' + block.getFieldValue('DIR') +
       '(' + value + ', \'block_id_' + block.id + '\');\n'+ "if(Turtle.isStop() == true){break;}\n";
};

Blockly.JavaScript['turtle_colour_internal'] = function(block) {
  // Generate JavaScript for setting the colour.
  var colour = block.getFieldValue('COLOUR');
  return 'Turtle.penColour(\'' + colour + '\', \'block_id_' +
      block.id + '\');\n';
};

Blockly.JavaScript['turtle_pen'] = function(block) {
  // Generate JavaScript for pen up/down.
  return 'Turtle.' + block.getFieldValue('PEN') +
      '(\'block_id_' + block.id + '\');\n';
};

Blockly.JavaScript['turtle_width'] = function(block) {
  // Generate JavaScript for setting the width.
  var width = Blockly.JavaScript.valueToCode(block, 'WIDTH',
      Blockly.JavaScript.ORDER_NONE) || '1';
  return 'Turtle.penWidth(' + width + ', \'block_id_' + block.id + '\');\n';
};

Blockly.JavaScript['turtle_visibility'] = function(block) {
  // Generate JavaScript for changing turtle visibility.
  return 'Turtle.' + block.getFieldValue('VISIBILITY') +
      '(\'block_id_' + block.id + '\');\n';
};

Blockly.JavaScript['turtle_print'] = function(block) {
  // Generate JavaScript for printing text.
  var argument0 = String(Blockly.JavaScript.valueToCode(block, 'TEXT',
      Blockly.JavaScript.ORDER_NONE) || '\'\'');
  return 'Turtle.drawPrint(' + argument0 + ', \'block_id_' +
      block.id + '\');\n';
};

Blockly.JavaScript['turtle_font'] = function(block) {
  // Generate JavaScript for setting the font.
  return 'Turtle.drawFont(\'' + block.getFieldValue('FONT') + '\',' +
      Number(block.getFieldValue('FONTSIZE')) + ',\'' +
      block.getFieldValue('FONTSTYLE') + '\', \'block_id_' +
      block.id + '\');\n';
};

Blockly.JavaScript['turtle_width'] = function(block) {
  // Generate JavaScript for setting the width.
  var width = Blockly.JavaScript.valueToCode(block, 'WIDTH',
      Blockly.JavaScript.ORDER_NONE) || '1';
  return 'Turtle.penWidth(' + width + ', \'block_id_' + block.id + '\');\n';
};

Blockly.JavaScript['turtle_colour'] = function(block) {
  // Generate JavaScript for setting the colour.
  var colour = Blockly.JavaScript.valueToCode(block, 'COLOUR',
      Blockly.JavaScript.ORDER_NONE) || '\'#000000\'';
  return 'Turtle.penColour(' + colour + ', \'block_id_' +
      block.id + '\');\n';
};

Blockly.JavaScript['turtle_repeat_internal'] = Blockly.JavaScript['controls_repeat'];


/**
 * The generated code for turtle blocks includes block ID strings.  These are useful for
 * highlighting the currently running block, but that behaviour is not supported in Android Blockly
 * as of May 2016.  This snippet generates the block code normally, then strips out the block IDs
 * for readability when displaying the code to the user.
 *
 * Post-processing the block code in this way allows us to use the same generators for the Android
 * and web versions of the turtle.
 */
Blockly.JavaScript.workspaceToCodeWithId = Blockly.JavaScript.workspaceToCode;

Blockly.JavaScript.workspaceToCode = function(workspace) {
  var code = this.workspaceToCodeWithId(workspace);
  // Strip out block IDs for readability.
  code = goog.string.trimRight(code.replace(/(,\s*)?'block_id_[^']+'\)/g, ')'))
  return code;
};
