package com.example.tracktrack

import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import androidx.core.content.ContextCompat

class AchievementsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_achievements)

        //Get table to show shop items in.
        val shopTable = findViewById<TableLayout>(R.id.achievementTable)

        //For every item available in the shop
        for (achievement in Game.allAchievements) {

            //Set table row properties.
            val tableRow = TableRow(this)
            val tableRowParams = TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT
            )

            tableRowParams.gravity = Gravity.CENTER_VERTICAL
            tableRowParams.setMargins(0, 20, 0, 20)

            tableRow.setBackgroundColor(ContextCompat.getColor(this, R.color.themeDarkerGrey))

            //Check if step achievement or guess achievement
            if(achievement is StepsAchievement){
                val stepAchievement: StepsAchievement = achievement
                if(stepAchievement.stepsNeeded <= Game.getStepsTaken()){
                    achievement.setAchieved(true)
                }
            } else if (achievement is GuessAchievement){
                val guessAchievement: GuessAchievement = achievement
                if(guessAchievement.correctGuesses <= Game.getCorrectGuesses()){
                    achievement.setAchieved(true)
                }
            }

            //Create layout for achievement text.
            var textHorizontal = LinearLayout(this)
            textHorizontal.orientation = LinearLayout.VERTICAL

            //Set achievement text properties.
            val itemText = TextView(this)
            itemText.text = achievement.name
            itemText.textSize = 20f

            //Set the achievement cost text properties.
            val itemCost = TextView(this)
            itemCost.text = achievement.description

            textHorizontal.addView(itemText)
            textHorizontal.addView(itemCost)
            textHorizontal.setPadding(20, 20, 20, 20)

            //Set achievement image properties.
            val itemImage = ImageView(this)
            itemImage.setImageBitmap(BitmapFactory.decodeResource(this.resources, achievement.resource))
            itemImage.setPadding(30, 30, 30, 30)

            //Add each element to table row.
            tableRow.addView(itemImage)
            tableRow.addView(textHorizontal)

            //Set table row properties.
            tableRow.layoutParams = tableRowParams
            shopTable.addView(tableRow)

        }
    }
}
