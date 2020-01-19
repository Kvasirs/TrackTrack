package com.example.tracktrack

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.content.ContextCompat

/**
 * MainActivity - Handles events made on the main menu. Will allow
 * the player to navigate to the different screens and start games.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) //test change

        //Gets UI elements.
        val currentBtn = findViewById<Button>(R.id.currentButton)
        val classicBtn = findViewById<Button>(R.id.classicButton)
        val shopBtn = findViewById<Button>(R.id.shopButton)
        val achievementBtn = findViewById<Button>(R.id.achievementButton)
        val campusBtn = findViewById<Button>(R.id.campusModeButton)

        //Event handler for current mode button.
        currentBtn.setOnClickListener{
            val intent = Intent(applicationContext, MapsActivity::class.java)
            startActivity(intent)
            Game.songType = "current"
        }
        //Event handler for classic mode button.
        classicBtn.setOnClickListener{
            val intent = Intent(applicationContext, MapsActivity::class.java)
            startActivity(intent)
            Game.songType = "classic"
        }
        //Event handler for classic mode button.
        achievementBtn.setOnClickListener{
            val intent = Intent(applicationContext, AchievementsActivity::class.java)
            startActivity(intent)
        }
        //Event handler for shop button.
        shopBtn.setOnClickListener{
            val intent = Intent(applicationContext, ShopActivity::class.java)
            startActivity(intent)
        }
        //Event handler for Bay Campus mode button.
        campusBtn.setOnClickListener{
            if(Game.campusMode){
                Game.campusMode = false
                campusBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.themeDarkerGrey))
                campusBtn.text = getString(R.string.campusOn)
            } else {
                Game.campusMode = true
                campusBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.themeOrange))
                campusBtn.text = getString(R.string.campusOff)
            }
        }

        //Get shared preferences for game. Ensures persistant storage.
        Game.setSharedPreferences(getSharedPreferences("sharedPref", Context.MODE_PRIVATE))

        //Create all game items and achievements on first load.
        if(Game.shopItems.isEmpty() && Game.allAchievements.isEmpty()){

            //Create all game items.
            Game.shopItems.add(MarkerItem("Blue User Marker",
                R.drawable.usermarker, 0, "user"))
            Game.shopItems.add(MarkerItem("Black Lyric Marker",
                R.drawable.arrow_down, 0, "lyric"))
            Game.shopItems.add(MarkerItem("Red User Marker",
                R.drawable.usermarkerred, 100, "user"))
            Game.shopItems.add(MarkerItem("Green User Marker",
                R.drawable.usermarkergreen, 100, "user"))
            Game.shopItems.add(MarkerItem("Green Lyric Marker",
                R.drawable.arrow_down_green, 100, "lyric"))
            Game.shopItems.add(MarkerItem("Blue Lyric Marker",
                R.drawable.arrow_down_blue, 100, "lyric"))

            //Create all step achievements.
            Game.allAchievements.add(StepsAchievement("Walking on Sunshine",
                "Walk 10 steps.", 10))
            Game.allAchievements.add(StepsAchievement("Walk 500 Miles",
                "Walk 50 steps.", 50))
            Game.allAchievements.add(StepsAchievement("We are the Champions",
                "Walk 100 steps.", 100))

            //Create all guessing achievements.
            Game.allAchievements.add(GuessAchievement("One Hit Wonder",
                "Correctly guess two songs in a row.", 2))
            Game.allAchievements.add(GuessAchievement("Simply The Best",
                "Correctly guess five songs in a row.", 5))
            Game.allAchievements.add(GuessAchievement("On top of the World",
                "Correctly guess ten songs in a row.", 10))

            //Set default items.
            Game.addOwnedItem(Game.shopItems.get(0))
            Game.addOwnedItem(Game.shopItems.get(1))

        }

    }
}
