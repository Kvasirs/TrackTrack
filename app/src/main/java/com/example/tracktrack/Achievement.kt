package com.example.tracktrack

/**
 * Base class that defines the attributes of all activity types.
 */
open class Achievement(var name: String, var description: String) {

    var resource = R.drawable.achievement_g

    /**
     * If achievement has been achieved by the player,
     * Change the achievement icon to a gold icon.
     */
    fun setAchieved(achieved: Boolean){
        if (achieved) {
            resource = R.drawable.achievement
        }
    }

}

