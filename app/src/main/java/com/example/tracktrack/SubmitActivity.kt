package com.example.tracktrack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

/**
 * Submit Activity - Shown when user wins their current game.
 * Will display any coins or achievements that the player has earned in-game.
 */
class SubmitActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit)

        val coinText = findViewById<TextView>(R.id.coinsText)
        val achievementText = findViewById<TextView>(R.id.achievementsText)

        //Calculate coins earned.
        var coinsEarned = 100
        coinsEarned -= 10 * Game.inGameGuesses //Deduct 10 coins for every incorrect guess.
        coinsEarned -= 5 * Game.collectedLyrics.size //Deduct 5 coins for every lyric needed.

        //If the number of coins earned is less than 0, set to 0.
        if(coinsEarned > 0){
            Game.setNumberOfCoins(Game.getNumberOfCoins() + coinsEarned)
            coinText.text = getString(R.string.coins_earned, coinsEarned)
        } else {
            coinText.text = getString(R.string.coins_earned, 0)
        }

        //Display the number of achievements earned in-game.
        achievementText.text = getString(R.string.achievements_found, Game.gameAchievements.size)

        //Set event handler for return button.
        val returnButton = findViewById<Button>(R.id.returnButton)
        returnButton.setOnClickListener{
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) //End all previous activities.
            startActivity(intent)
            Game.resetValues()
        }

    }

}
