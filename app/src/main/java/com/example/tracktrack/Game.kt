package com.example.tracktrack

import android.content.SharedPreferences

/**
 * Static class allowing for data to be stored and passed around between different
 * activities. Also allows for data to be persistently stored using a Shared Preferences object.
 */
class Game {

    companion object {

        lateinit var sharedPref: SharedPreferences
        lateinit var songName: String
        lateinit var songType: String

        var songArtists: ArrayList<String> = ArrayList()
        var selectedLyrics: ArrayList<String> = ArrayList()
        var lyricMarkers: ArrayList<LyricMarker> = ArrayList()
        var collectedLyrics = ArrayList<String>()
        var gameAchievements = ArrayList<Achievement>()
        var allAchievements = ArrayList<Achievement>()
        var shopItems = ArrayList<MarkerItem>()

        var inGameSteps = 0
        var inGameGuesses = 0
        var campusMode = true

        /**
         * Resets game values on game quit/finish.
         */
        fun resetValues(){
            gameAchievements.clear()
            collectedLyrics.clear()
            selectedLyrics.clear()
            lyricMarkers.clear()
            songArtists.clear()
            songName = ""
            songType = ""
            inGameGuesses = 0
            inGameSteps = 0
        }

        /**
         * Sets the number of correct guesses within shared resources.
         */
        fun setCorrectGuesses(steps: Int){
            val editor = getSharedPreferences().edit()
            editor.putInt("correctGuesses", steps)
            editor.apply()
        }

        /**
         * Gets the number of correct guesses from shared resources.
         */
        fun getCorrectGuesses(): Int{
            return getSharedPreferences().getInt("correctGuesses", 0)
        }

        /**
         * Sets the number of steps taken within shared resources.
         */
        fun setStepsTaken(steps: Int){
            val editor = getSharedPreferences().edit()
            editor.putInt("stepsTaken", steps)
            editor.apply()
        }

        /**
         * Gets the number of steps taken from shared resources.
         */
        fun getStepsTaken(): Int{
            return getSharedPreferences().getInt("stepsTaken", 0)
        }

        /**
         *  Sets new lyric marker resource Id integer within shared resources.
         */
        fun setLyricMarkerIcon(resource: Int){
            val editor = getSharedPreferences().edit()
            editor.putInt("lyricMarker", resource)
            editor.apply()
        }

        /**
         * Gets current lyric icon resource Id from shared resources.
         */
        fun getLyricMarkerIcon(): Int{
            return getSharedPreferences().getInt("lyricMarker", R.drawable.arrow_down)
        }

        /**
         * Sets new user marker resource Id integer within shared resources.
         */
        fun setUserMarkerIcon(resource: Int){
            val editor = getSharedPreferences().edit()
            editor.putInt("userMarker", resource)
            editor.apply()
        }

        /**
         * Gets current user marker resource Id from shared resources.
         */
        fun getUserMarkerIcon(): Int{
            return getSharedPreferences().getInt("userMarker", R.drawable.usermarker)
        }

        /**
         * Adds newly owned item to shared resources.
         */
        fun addOwnedItem(newItem: MarkerItem){
            val editor = getSharedPreferences().edit()
            editor.putString("ownedItems", sharedPref.getString("ownedItems", "") + "," + newItem.name)
            editor.apply()
        }

        /**
         * Gets all owned items from shared resources.
         */
        fun getOwnedItems(): ArrayList<MarkerItem>{
            val itemString = getSharedPreferences().getString("ownedItems", "")
            val ownedItems = ArrayList<MarkerItem>()
            for(item in shopItems){
                if(item.name in itemString!!.split(",")){
                    ownedItems.add(item)
                }
            }
            return ownedItems
        }

        /**
         * Sets shared resources object for persistent storage.
         */
        fun setSharedPreferences(newSharedPref: SharedPreferences){
            sharedPref = newSharedPref
        }

        /**
         * Gets shared resources object for persistent storage.
         */
        fun getSharedPreferences(): SharedPreferences{
            return sharedPref
        }

        /**
         * Gets number of coins owned by user from shared resources.
         */
        fun getNumberOfCoins(): Int{
            return sharedPref.getInt("coins", 0)
        }

        /**
         * Adds number of coins owned by user to shared resources.
         */
        fun setNumberOfCoins(newNumber: Int){
            val editor = sharedPref.edit()
            editor.putInt("coins", newNumber)
            editor.apply()
        }

    }

}