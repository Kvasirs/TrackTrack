package com.example.tracktrack

/**
 * Child class of Achievement that represents a guess achievement.
 * Contains attributes that stores the number of correct guesses needed.
 */
class GuessAchievement(name: String, description: String, var correctGuesses: Int):
    Achievement(name, description)