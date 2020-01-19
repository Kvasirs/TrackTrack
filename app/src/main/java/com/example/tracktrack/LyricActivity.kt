package com.example.tracktrack

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView

/**
 * Lyric Activity - Started when the user selects the music note icon on the game screen.
 * This will allow the user to view all of the collected lyrics in game.
 */
class LyricActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lyric)

        val lyricList = findViewById<LinearLayout>(R.id.lyricList)
        val collectedLyricsText = findViewById<TextView>(R.id.collectedLyricsText)

        //for each lyric in the song's lyrics
        for(lyric in Game.selectedLyrics){

            //create textView for particular lyric.
            val lyricTextView = TextView(this)
            lyricTextView.textSize = 20f

            //If lyric is collected, display full text.
            if (Game.collectedLyrics.contains(lyric)){
                lyricTextView.text = lyric
            } else {
                lyricTextView.text = "???"
            }
            lyricList.addView(lyricTextView)
        }

        //Show total number of collected lyrics.
        collectedLyricsText.text = getString(R.string.lyrics_collected,
            Game.collectedLyrics.size)

    }

}
