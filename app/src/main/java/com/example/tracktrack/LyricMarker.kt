package com.example.tracktrack

import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker

/**
 * Represents a lyric marker in game.
 * Stores the corresponding Marker Object, alongside it's position, and whether or not
 * it is within proximity of the user.
 */
class LyricMarker(var lyric: String, var marker: Marker) {

    private var isClose: Boolean

    /**
     * Constructor - sets isClose attribute to false.
     */
    init {
        this.isClose = false
    }

    /**
     * Returns whether or not the marker is in proximity of player.
     */
    fun isClose(): Boolean {
        return isClose
    }

    /**
     * Sets whether or not the marker is in close proximity of player.
     * If true, marker style is changed.
     */
    fun isClose(close: Boolean){
        isClose = close
        if(isClose){
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.arrow_up))
        } else {
            marker.setIcon(BitmapDescriptorFactory.fromResource(Game.getLyricMarkerIcon()))
        }
    }

}