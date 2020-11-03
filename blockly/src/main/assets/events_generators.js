'use strict';

Blockly.JavaScript['event_program_starts'] = function(block) {
    var nextCode = Blockly.JavaScript.statementToCode(block, 'DO');
    return 'while(true) {\n' +
                nextCode+'\n' +
                "break; if(Turtle.isStop() == true){break;}\n"+
            '}; Turtle.MF(0);\n';
};

Blockly.JavaScript['event_repeat_forever'] = function(block) {
    var nextCode = Blockly.JavaScript.statementToCode(block, 'DO');
    return 'while(true) {\n' +
                nextCode+'\n' +
                "if(Turtle.isStop() == true){break;}\n"+
            '}; Turtle.MF(0);\n';
};

Blockly.JavaScript['event_repeat_timer'] = function(block) {
    var period = Blockly.JavaScript.valueToCode(block, 'PERIOD', Blockly.JavaScript.ORDER_FUNCTION_CALL) || 500

    var nextCode = Blockly.JavaScript.statementToCode(block, 'DO');
    return "setInterval(() => {\n"+nextCode+"\n}, "+period+");"
};

Blockly.JavaScript['event_wait'] = function(block) {
    // Terrible implementation of wait for the moment
    var timeout = Blockly.JavaScript.valueToCode(block, 'TIMEOUT', Blockly.JavaScript.ORDER_FUNCTION_CALL) || 500
//    var nextBlock = block.nextConnection && block.nextConnection.targetBlock();
//    var nextCode = Blockly.JavaScript.blockToCode(nextBlock);
//    // And then remove the next block from code generation
//    block.nextConnection = null;
//    var functionName = Blockly.JavaScript.provideFunction_(
//          'timer',
//          ['function ' + Blockly.JavaScript.FUNCTION_NAME_PLACEHOLDER_ +
//              '(ms) {',
//           '  return new Promise(res => setTimeout(res, ms));',
//           '}']);
//    var functionName = Blockly.JavaScript.provideFunction_(
//             'sleep',
//             ['async function ' + Blockly.JavaScript.FUNCTION_NAME_PLACEHOLDER_ +
//                 '(ms) {',
//              '  await timer(ms);',
//              '}']);
//    return "sleep("+timeout+");\n"+nextCode
    return  "let d = Date.now();\n"+
            "do {}\n"+
            "while(Date.now()-d < "+timeout+");\n"
};

Blockly.JavaScript['logic_boolean_workaround'] = function(block) {
    var code = (block.getFieldValue('BOOL') == 'TRUE') ? 'true' : 'false';
    return [code, Blockly.JavaScript.ORDER_ATOMIC];
};