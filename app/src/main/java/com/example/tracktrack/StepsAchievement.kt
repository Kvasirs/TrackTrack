package com.example.tracktrack

/**
 * Child class of Achievement that represents a walking achievement.
 * Contains attribute that stores the number of steps needed.
 */
class StepsAchievement(name: String, description: String, var stepsNeeded: Int):
    Achievement(name, description)