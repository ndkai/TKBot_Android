package com.example.iot_app.ui.main.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.iot_app.R
import com.example.iot_app.ui.base.BaseFragment
import com.example.iot_app.ui.blockly_activity.BlocklyActivity
import com.example.iot_app.ui.handed_controller.HandedControllerActivity
import com.example.iot_app.ui.main.MainActivity
import com.example.iot_app.ui.qr_activity.QrActivity
import com.example.iot_app.ui.speech_activity.SpeechActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.horizotal_view.*

@SuppressLint("UseRequireInsteadOfGet")
class MainFragment : BaseFragment() {

    private val itemAdapter by lazy {
        ItemAdapter { position: Int, item: Item ->
            when (position) {
                0 -> {            
                    val intent = Intent(activity,BlocklyActivity::class.java)
                    startActivity(intent)}
                1 -> {
                    val intent = Intent(activity,HandedControllerActivity::class.java)
                    startActivity(intent)
                    activity!!.finish()}
                2 -> {
                    val intent = Intent(activity,SpeechActivity::class.java)
                    startActivity(intent)
                    activity!!.finish()}
                3 -> {
                    val intent = Intent(activity,QrActivity::class.java)
                    startActivity(intent)
                    activity!!.finish()}
                else -> {
                      Toast.makeText(activity, getResources().getString(R.string.comingsoon), Toast.LENGTH_SHORT).show()
                }
                }
            item_list.smoothScrollToPosition(position)
            }
        } 
    private val possibleItems = listOf(
        Item("Coding", R.drawable.block_play),
        Item("Controller", R.drawable.handed_controller),
        Item("Speech", R.drawable.speech_main_icon),
        Item("QR Scan", R.drawable.qr_image),
        Item("Coming soon", R.drawable.commingsoon)
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.horizotal_view, container,false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        item_list.initialize(itemAdapter)
        item_list.setViewsToChangeColor(listOf(R.id.list_item_background, R.id.list_item_text))
        itemAdapter.setItems(getLargeListOfItems())
    }
    private fun getLargeListOfItems(): List<Item> {
        return possibleItems
    }
}
