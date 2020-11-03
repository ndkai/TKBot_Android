package com.iot.blockly

import android.app.AlertDialog
import android.content.DialogInterface
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.os.ConfigurationCompat
import com.google.android.gms.nearby.connection.Strategy
import com.google.blockly.android.AbstractBlocklyActivity
import com.google.blockly.model.DefaultBlocks
import java.util.*

/**
 * Created by Raul Portales on 17/09/18.
 */
abstract class RainbowHatBlocklyBaseActivity() : AbstractBlocklyActivity() {

    companion object {
        var startClick:Boolean = true
        var stopClick:Boolean = false
        const val SERVICE_ID = "com.plattysoft.nearby"
        val STRATEGY = Strategy.P2P_STAR as Strategy
    }

    lateinit var stopMenu: MenuItem

    lateinit var startMenu: MenuItem
    lateinit var bluetoothMenu: MenuItem


    override fun onCreateContentView(containerId: Int): View {
        return layoutInflater.inflate(R.layout.beret_unified_workspace, null)
    }

    override fun getToolboxContentsXmlPath(): String {
        val currentLocale = ConfigurationCompat.getLocales(resources.configuration)[0]
        if(currentLocale.language == "ja"){
            return "toolbox_jp.xml"
        }
        return "toolbox.xml"
    }

    override fun getBlockDefinitionsJsonPaths(): MutableList<String> {
        val assetPaths = ArrayList(DefaultBlocks.getAllBlockDefinitions())
        val currentLocale = ConfigurationCompat.getLocales(resources.configuration)[0]
        Log.d("TAG", "getBlockDefinitionsJsonPaths: "+currentLocale.language)
        if(currentLocale.language == "en"){
            assetPaths.add("rainbowHat_blocks.json")
            assetPaths.add("turtle_blocks.json")
            assetPaths.add("events_blocks.json")
        }  else{
            if(currentLocale.language == "ja"){
                assetPaths.add("rainbowHat_blocks.json")
                assetPaths.add("turtle_blocks_jp.json")
                Log.d("TAG", "getBlockDefinitionsJsonPaths: jpne")
                assetPaths.add("events_blocks.json")
            }
            else{
                assetPaths.add("rainbowHat_blocks.json")
                assetPaths.add("turtle_blocks.json")
                assetPaths.add("events_blocks.json")
            }
        }
        return assetPaths
    }

    override fun getGeneratorsJsPaths(): MutableList<String> {
        return Arrays.asList("rainbowHat_generators.js", "events_generators.js", "move_generators.js")
      //  return Arrays.asList("generators.js","rainbowHat_generators.js", "move_generators.js","showscreenled_generators.js", "events_generators.js")
    }

    override fun getActionBarMenuResId(): Int {
        return R.menu.beret_menu
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.beret_menu, menu)
        stopMenu = menu!!.findItem(R.id.action_stop)
        startMenu = menu!!.findItem(R.id.action_run)
        stopMenu.setVisible(false);
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back_icon)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_clear) {
            // Ask for confirmation
            if (controller.workspace.hasBlocks()) {
                askForClearConfirmation()
            }
            return true
        }
        else if (id == R.id.blockly_zoom_in_button) {
            controller.zoomIn()
            return true
        }
        else if (id == R.id.blockly_zoom_out_button) {
            controller.zoomOut()
            return true
        }
        else if (id == R.id.action_run){
            if (controller.workspace.hasBlocks()) {
                item.setVisible(false);
                stopMenu.setVisible(true);
                onAutosave()
                onRunCode()
            }
            else {
                Toast.makeText(this, R.string.no_blocks_error, Toast.LENGTH_LONG).show()
            }
            return true
        }
        else if (id == R.id.action_stop){
            startMenu.setVisible(true)
            stopMenu.setVisible(false);
            //code
            onClear()
            
        } else if(id == android.R.id.home){
            openMainActivity()
        }  
        return super.onOptionsItemSelected(item)
    }

    
    abstract fun openMainActivity()
    abstract fun onClear()
    private fun askForClearConfirmation() {
        // Consider doing this with a snackbar and an UNDO option instead
        AlertDialog.Builder(this)
                .setMessage(R.string.confirm_clear_workspace_message)
                .setTitle(R.string.confirm_clear_workspace_title)
                .setPositiveButton(R.string.yes) { dialogInterface: DialogInterface, i: Int ->
                    onClearWorkspace()
                    dialogInterface.dismiss()
                }
                .setNegativeButton(R.string.no) { dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.dismiss()
                }
                .create()
                .show()
    }
}
