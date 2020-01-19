package com.example.tracktrack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import java.util.*

/**
 * Guess Activity - Started when the user selects the keyboard icon on the game screen. This will
 * allow the user to enter in the artist/song name which they believe to be correct.
 */
class GuessActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guess)

        val artistField = findViewById<EditText>(R.id.artistField)
        val songField = findViewById<TextView>(R.id.songField)
        val submitButton = findViewById<Button>(R.id.submitButton)

        submitButton.setOnClickListener{

            //Make user input lower case.
            val artistGuess = artistField.text.toString().toLowerCase()
            val songGuess = songField.text.toString().toLowerCase()

            //If user correctly guessed artist and song name
            if(songGuess == Game.songName && artistGuess in Game.songArtists){

                //Check if any potential achievements have been received in-game.
                findGuessAchievements()
                findWalkingAchievements()

                //Go to winning screen.
                val intent = Intent(applicationContext, SubmitActivity::class.java)
                startActivity(intent)

            } else {
                //Notify user that guess was incorrect.
                Toast.makeText(this, "Incorrect, please try again.", Toast.LENGTH_LONG).show()
                Game.inGameGuesses++
            }

        }
    }

    /**
     * Find potential guessing achievements after game.
     * Will add achievement to gameAchievements array if so.
     */
    private fun findGuessAchievements() {
        if(Game.inGameGuesses == 0){
            Game.setCorrectGuesses(Game.getCorrectGuesses() + 1)
            when {
                Game.getCorrectGuesses() == 2 -> Game.gameAchievements.add(Game.allAchievements[4])
                Game.getCorrectGuesses() == 5 -> Game.gameAchievements.add(Game.allAchievements[5])
                Game.getCorrectGuesses() == 10 -> Game.gameAchievements.add(Game.allAchievements[6])
            }
        }
    }

    /**
     * Find potential walking achievements after game.
     * Will add achievement to gameAchievements array if so.
     */
    private fun findWalkingAchievements(){
        val allStepsTaken = Game.getStepsTaken() + Game.inGameSteps
        Game.setStepsTaken(allStepsTaken)
        when {
            allStepsTaken >= 10 -> Game.gameAchievements.add(Game.allAchievements[0])
            allStepsTaken >= 50 -> Game.gameAchievements.add(Game.allAchievements[1])
            allStepsTaken >= 100 -> Game.gameAchievements.add(Game.allAchievements[2])
        }
    }
}
