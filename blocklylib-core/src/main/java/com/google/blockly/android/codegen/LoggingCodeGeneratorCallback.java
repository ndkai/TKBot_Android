
package com.google.blockly.android.codegen;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;
/**
 * Simple generator callback that logs the generated code to device Log and a Toast.
 */
public class LoggingCodeGeneratorCallback implements CodeGenerationRequest.CodeGeneratorCallback {

    protected final String mTag;
    protected final Context mContext;
    private String code = "";
    public LoggingCodeGeneratorCallback(Context context, String loggingTag) {
        mTag = loggingTag;
        mContext = context;
    }

    @Override
    public void onFinishCodeGeneration(String generatedCode) {
        // Sample callback.

        if (generatedCode.isEmpty()) {
            Toast.makeText(mContext,
                    "Something went wrong with code generation.", Toast.LENGTH_LONG).show();
        } else {
            Log.d(mTag, "code_duy111: " + generatedCode);
            code = generatedCode;
            SharedPreferences sharedPreferences = mContext.getSharedPreferences("DUY", Context.MODE_PRIVATE);
            sharedPreferences.edit().putString("DUY", "123").commit();
            Log.d(mTag, "code_duy112: " + code);
        }
    }

    public String getGeneratedCode(){
        return getGeneratedCode();
    }

}
