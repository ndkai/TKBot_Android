package com.example.iot_app.ui.main.adapter

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
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.horizotal_view.*

class MainFragment : BaseFragment() {

    private val itemAdapter by lazy {
        ItemAdapter { position: Int, item: Item ->
            when (position) {
                0 -> {
                    val intent = Intent(activity,BlocklyActivity::class.java)
                    startActivity(intent)}
                1 -> {
                    val intent = Intent(activity,HandedControllerActivity::class.java)
                    startActivity(intent)}
                else -> {
                      Toast.makeText(activity, "Chức năng hiện tại chưa được cập nhật", Toast.LENGTH_SHORT).show()
                }
                }
            item_list.smoothScrollToPosition(position)
            }
        } 
    private val possibleItems = listOf(
        Item("Blockly", R.drawable.block_play),
        Item("Controller", R.drawable.handed_controller),
        Item("Coming soon", R.drawable.commingsoon),
        Item("Coming soon", R.drawable.commingsoon),
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
