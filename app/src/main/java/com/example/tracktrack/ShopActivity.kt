package com.example.tracktrack

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AlertDialog
import android.widget.TableLayout
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_shop.*

/**
 * Shop Activity - Started when the player selects "Shop" from the main menu.
 * WIll allow the user to view all items available, and purchase items with sufficient funds.
 */
class ShopActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop)

        //Show number of coins.
        val coinsText = findViewById<TextView>(R.id.coinsText)
        coinsText.text = getString(R.string.coins, Game.getNumberOfCoins())
        //Get list of already owned items.
        val ownedItems = Game.getOwnedItems()
        //Get table to show shop items in.
        val shopTable = findViewById<TableLayout>(R.id.shopTable)

        //For every item available in the shop
        for(item in Game.shopItems){

            //Set table row properties.
            val tableRow = TableRow(this)
            val tableRowParams = TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT
            )

            tableRowParams.gravity = Gravity.CENTER_VERTICAL
            tableRowParams.setMargins(0,20,0,20)
            tableRow.setBackgroundColor(ContextCompat.getColor(this, R.color.themeDarkerGrey))

            //Create layout for item text.
            val textHorizontal = LinearLayout(this)
            textHorizontal.orientation = LinearLayout.VERTICAL

            //Set item text properties.
            val itemText = TextView(this)
            itemText.text = item.name
            itemText.textSize = 20f

            //Set the item cost text properties.
            val itemCost = TextView(this)
            itemCost.text = getString(R.string.cost, item.price)

            textHorizontal.addView(itemText)
            textHorizontal.addView(itemCost)
            textHorizontal.setPadding(20, 20, 20, 20)

            //Set item image properties.
            val itemImage = ImageView(this)
            itemImage.setImageBitmap(BitmapFactory.decodeResource(this.resources, item.markerResource))
            itemImage.setPadding(30, 30, 30, 30)

            //Set buy button for item.
            val buyButton = Button(this)

            //Add each element to table row.
            tableRow.addView(itemImage)
            tableRow.addView(textHorizontal)
            tableRow.addView(buyButton)

            //Set table row properties.
            tableRow.layoutParams = tableRowParams
            shopTable.addView(tableRow)

            //If item is already owned, disable button.
            if(!(item in ownedItems)){
                setEventHandler(buyButton, item)
                buyButton.text = getString(R.string.buy)
            } else {
                buyButton.isClickable = false
                buyButton.text = getString(R.string.owned)
            }

        }

    }

    /**
     * Handles event when user tries to buy and item.
     */
    private fun setEventHandler(button: Button, item: MarkerItem){
        button.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setCancelable(false)
            builder.setMessage("Are you you sure you want to buy the " + item.name + "?")
            builder.setPositiveButton("Yes") { _, _ ->
                if(Game.getNumberOfCoins() >= item.price){
                    button.text = getString(R.string.owned)
                    button.isClickable = false
                    Game.addOwnedItem(item)
                    Game.setNumberOfCoins(Game.getNumberOfCoins() - item.price)
                    coinsText.text = getString(R.string.coins, Game.getNumberOfCoins())
                } else {
                    Toast.makeText(this,
                        "You do not have enough money for this item.", Toast.LENGTH_SHORT).show()
                }
            }
            builder.setNegativeButton("No") { dialog, _ ->
                dialog.cancel()
            }
            val alert = builder.create()
            alert.show()
        }
    }
}

