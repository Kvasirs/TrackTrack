package com.example.tracktrack

import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat

/**
 * Inventory Activity - Started when the user selects the tag icon on the game screen.
 * This will allow the user to view all of their owned items, and equip any items if they so wish.
 */
class InventoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory)

        //Get table to show shop items in.
        val shopTable = findViewById<TableLayout>(R.id.inventoryTable)

        for(item in Game.getOwnedItems()){

            //Set table row properties.
            val tableRow = TableRow(this)
            val tableRowParams = TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT
            )

            tableRowParams.gravity = Gravity.CENTER_VERTICAL
            tableRowParams.setMargins(0,20,0,20)
            tableRow.setBackgroundColor(ContextCompat.getColor(this, R.color.themeDarkerGrey))

            //Set item text properties.
            val itemText = TextView(this)
            itemText.text = item.name
            itemText.textSize = 20f

            //Set item image properties.
            val itemImage = ImageView(this)
            itemImage.setImageBitmap(BitmapFactory.decodeResource(this.resources, item.markerResource))
            itemImage.setPadding(30, 30, 30, 30)

            //get item image and already set user and lyric images.
            val itemImg = item.markerResource
            val userImg = Game.getUserMarkerIcon()
            val lyricImg = Game.getLyricMarkerIcon()

            val equipButton = Button(this)

            //Add items to horizontal linear layout.
            tableRow.addView(itemImage)
            tableRow.addView(itemText)
            tableRow.addView(equipButton)

            //Set table row properties.
            tableRow.layoutParams = tableRowParams
            shopTable.addView(tableRow)

            //If item is already equipped, set button icon as equipped and
            //do not set the event handler.
            if(itemImg != userImg && itemImg != lyricImg){
                equipButton.setText(getString(R.string.equip))
                setEventHandler(equipButton, item)

            } else {
                equipButton.setText(getString(R.string.equpped))
                equipButton.isClickable = false
            }

        }
    }

    /**
     * Refreshes activity after items are equipped.
     */
    private fun refreshActivity() {
        finish()
        overridePendingTransition(0, 0)
        startActivity(intent)
        overridePendingTransition(0, 0)
    }

    /**
     * Handles event when user tries to equip an item.
     * Will only allow for item to be equipped if not already equipped.
     */
    private fun setEventHandler(button: Button, item: MarkerItem) {
        button.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setCancelable(false)
            builder.setMessage("Are you you sure you want to equip " + item.name + "?")
            builder.setPositiveButton("Yes") { _, _ ->
                if(item.type == "user"){
                    Game.setUserMarkerIcon(item.markerResource)
                } else {
                    Game.setLyricMarkerIcon(item.markerResource)
                }
                refreshActivity()
            }
            builder.setNegativeButton("No") { dialog, which ->
                dialog.cancel()
            }
            val alert = builder.create()
            alert.show()
        }
    }


}
