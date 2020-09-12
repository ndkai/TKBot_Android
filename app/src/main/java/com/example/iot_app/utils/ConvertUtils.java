package com.example.iot_app.utils;

import android.content.Context;

import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

public  class ConvertUtils {

    public  static  String mainString = "var process=function(){ var s = \"\" ; ";
    public  static  String functionName = "process";

    public static String runScript(Context androidContextObject, String generatedCode) {
        // Get the JavaScript in previous section
        String tempString = "";
        tempString = mainString + generatedCode+" return s;}";
        try {
            Object[] functionParams = new Object[]{};
            org.mozilla.javascript.Context rhino = org.mozilla.javascript.Context.enter();
            rhino.setOptimizationLevel(-1);

            Scriptable scope = rhino.initStandardObjects();

            rhino.evaluateString(scope, tempString, "JavaScript", 1, null);

            Object obj = scope.get(functionName, scope);

            if (obj instanceof Function) {
                Function function = (Function) obj;

                Object result = function.call(rhino, scope, scope, functionParams);

                String response = org.mozilla.javascript.Context.toString(result);
                return response;
            } else {
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            org.mozilla.javascript.Context.exit();
        }

        return null;
    }


}
