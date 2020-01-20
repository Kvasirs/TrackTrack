@file:Suppress("DEPRECATION")

package com.example.tracktrack

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.SensorEventListener
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlin.random.Random
import androidx.appcompat.app.AlertDialog
import android.widget.ImageButton
import android.hardware.SensorManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.net.ConnectivityManager
import android.net.NetworkInfo

/**
 * Maps Activity - Game screen which is started when the player selects one of the two game modes
 * within the main menu.
 */
@Suppress("DEPRECATION")
class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener, SensorEventListener {

    private val permissionId = 42
    private lateinit var mMap: GoogleMap
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var userMarker: Marker

    var sensorManager: SensorManager? = null
    var running = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        //Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //Obtain last known location and request new location data.
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()
        requestNewLocationData()

        //Check if new game or if game is carrying on.
        if (Game.selectedLyrics.isEmpty()) {
            Game.selectedLyrics = selectRandomSong()
        }

        //Event handler for guessing icon. Will navigate to guessing screen.
        val guessButton = findViewById<ImageButton>(R.id.makeGuessButton)
        guessButton.setOnClickListener {
            val intent = Intent(applicationContext, GuessActivity::class.java)
            startActivity(intent)
        }

        //Event handler for equip icon. Will navigate to inventory screen.
        val equipButton = findViewById<ImageButton>(R.id.equipButton)
        equipButton.setOnClickListener {
            val intent = Intent(applicationContext, InventoryActivity::class.java)
            startActivity(intent)
        }

        //Event handler for lyric icon. Will navigate to lyric screen.
        val lyricsButton = findViewById<ImageButton>(R.id.findLyricsButton)
        lyricsButton.setOnClickListener {
            val intent = Intent(applicationContext, LyricActivity::class.java)
            startActivity(intent)
        }

        //Creates sensor object for tracking number of steps taken.
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        //ConnectivityManager object for determining if there is an internet connection or not.
        val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnected == true

        //If there is no internet connection, display a dialog asking the user to enable internet.
        if(!(isConnected)){
            val builder = AlertDialog.Builder(this)
            builder.setCancelable(false)
            builder.setMessage("Please connect to the internet before playing.")
            builder.setPositiveButton("OK") { _, _ ->
                val intent = Intent(applicationContext, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)

            }
            val alert = builder.create()
            alert.show()
        }

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {

        Log.i("myLocation", "Map Ready")
        mMap = googleMap
        googleMap.setOnMarkerClickListener(this)

        mMap.uiSettings.isMapToolbarEnabled = false
        mMap.uiSettings.isZoomControlsEnabled = false

        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(
            this, R.raw.style_json))

        val lastLoc = LatLng(-33.8688, 151.2093)
        updateMarker(lastLoc)
        updateCamera(lastLoc)
    }

    /**
     * Called on activity restart.
     * Will refresh user and lyric markers.
     */
    override fun onRestart() {
        super.onRestart()
        mMap.clear()
        updateMarker(userMarker.position)
        generateLyricMarkers(userMarker.position)
    }

    /**
     * Called when activity is resumed.
     * Will register sensor listener for steps tracking.
     */
    override fun onResume(){
        super.onResume()
        running = true
        val stepsSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if(stepsSensor == null){
            Toast.makeText(this, "No Step Counter Sensor!", Toast.LENGTH_LONG).show()
        } else {
            sensorManager?.registerListener(this, stepsSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    /**
     * Called when activity is paused.
     * Will unregister sensor for steps tracking.
     */
    override fun onPause(){
        super.onPause()
        running = false
        sensorManager?.unregisterListener(this)
    }

    /**
     * Called when sensor input is changed.
     * Will set user achievements for step count.
     */
    override fun onSensorChanged(event: SensorEvent){
        if(running){
            Game.inGameSteps++
        }
    }

    /**
     * Function definition is required to use Sensor object.
     */
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    /**
     * Updates camera position depending on new location.
     */
    private fun updateCamera(location: LatLng){

        val cameraPosition = CameraPosition.Builder()
            .target(location)
            .zoom(18f)
            .bearing(0f)
            .tilt(40f)
            .build()

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

    }

    /**
     * Alert box will be displayed when user presses back button.
     */
    override fun onBackPressed() {

        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setMessage("Are you sure you want to quit?")
        builder.setPositiveButton("Yes") { dialog, which ->
            Game.resetValues()
            finish()
        }
        builder.setNegativeButton("No") { dialog, which ->
            dialog.cancel()
        }
        val alert = builder.create()
        alert.show()

    }

    /**
     * Set name and artist of song.
     */
    private fun setSongDetails(trackString: String){

        val detailsString = trackString.replace("_", " ").toLowerCase()
        val artistsString = detailsString.substringBefore("(")
        val artists = artistsString.split(" : ")

        for(artist in artists){
            Game.songArtists.add(artist)
        }

        Game.songName = detailsString.substringAfter("(").replace(").txt", "")
    }

    /**
     * Event handler for collecting lyricMarkers.
     */
    override fun onMarkerClick(marker: Marker): Boolean {
        for (lyric in Game.lyricMarkers) {
            if (marker == lyric.marker && lyric.isClose()) {
                Game.lyricMarkers.remove(lyric)
                lyric.marker.remove()
                Game.collectedLyrics.add(lyric.lyric)
                return true
            }
        }
        return false
    }

    /**
     * Parses individual music file.
     */
    private fun getSongLyrics(filename: String): ArrayList<String> {
        val directory = "lyrics/${Game.songType}/$filename"
        val lyrics = this.assets.open(directory).bufferedReader().use {
            it.readText()
        }
        val songLyrics = ArrayList<String>()
        lyrics.split("\n").forEach {
            songLyrics.add(it)
        }
        return songLyrics
    }

    /**
     * Parse through all music file names in directory and select random song.
     */
    private fun selectRandomSong(): ArrayList<String> {

        //Load all potential song names.
        val allSongNames = ArrayList<String>()
        val directory = "lyrics/${Game.songType}"
        this.assets.list(directory)!!.forEach {
            allSongNames.add(it)
        }

        //Selected random song name from songs.
        val selectedSongName = allSongNames.shuffled().take(1)[0]

        //Set randomly selected song.
        setSongDetails(selectedSongName)
        //Get song lyricMarkers for randomly selected song.
        val selectedSong = getSongLyrics(selectedSongName)

        //Remove duplicate lyricMarkers and return.
        val set = HashSet(selectedSong)
        selectedSong.clear()
        selectedSong.addAll(set)
        return selectedSong
    }

    /**
     *  Generates lyric markers for each lyric in selected song.
     *  Will place markers around either user or Bay Campus coordinates.
     */
    private fun generateLyricMarkers(location: LatLng) {

        //Will generate new random points if none are present.
        if(Game.lyricMarkers.isEmpty()){

            val long = location.longitude
            val lat = location.latitude
            for (lyric in Game.selectedLyrics) {

                //Ensure that lyric is within 0.001 of the given longitude and latitude.
                val lyricLong = Random.nextDouble(long - 0.001, long + 0.001)
                val lyricLat = Random.nextDouble(lat - 0.001, lat + 0.001)
                val markerLocation = LatLng(lyricLat, lyricLong)

                //Create lyric marker object for each lyric.
                val lyricMarker = mMap.addMarker(MarkerOptions()
                    .title("Lyric is too far away!")
                    .position(markerLocation)
                    .icon(BitmapDescriptorFactory.fromResource(Game.getLyricMarkerIcon()))
                )
                Game.lyricMarkers.add(LyricMarker(lyric, lyricMarker))
            }

        } else {
            //Sets existing points if already present.
            for(lyric in Game.lyricMarkers){
                lyric.marker = mMap.addMarker(MarkerOptions()
                    .position(lyric.marker.position).title("Lyric is too far away!")
                    .icon(BitmapDescriptorFactory.fromResource(Game.getLyricMarkerIcon())))
            }
        }

    }

    /**
     * Gets distance between a given location and a marker.
     */
    private fun distanceBetween(location: LatLng, location2: LatLng): Float {
        val distance = FloatArray(2)
        Location.distanceBetween(
            location2.latitude, location2.longitude,
            location.latitude, location.longitude, distance
        )
        return distance[0]
    }


    /**
     * Check if lyric has been collected during each location update.
     */
    private fun hasFoundLyric(location: LatLng) {
        //for each lyric marker, check distance between user marker.
        for (lyric in Game.lyricMarkers) {
            val marker = lyric.marker
            val distance = distanceBetween(location, marker.position)
            //if lyric is within 10 meters, turn green.
            lyric.isClose(distance < 20)
        }

    }

    /**
     * Updates marker position depending on new location.
     */
    private fun updateMarker(location: LatLng){
        userMarker = mMap.addMarker(MarkerOptions()
            .position(location)
            .title("You")
            .icon(BitmapDescriptorFactory.fromResource(Game.getUserMarkerIcon())))
    }

    /**
     * Update marker and camera to last known location.
     */
    private fun getLastLocation() {

        //If permissions are given and location is enabled
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                //Get last known location, if any.
                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        //If last location is found, move user marker and camera there.
                        var lastLoc = LatLng(location.latitude, location.longitude)
                        updateMarker(lastLoc)
                        updateCamera(lastLoc)

                        //Generate lyric markers.
                        generateLyricMarkers(lastLoc)
                        hasFoundLyric(lastLoc)
                    }
                }
            } else {
                //If location is not enabled, display dialog box.
                val builder = AlertDialog.Builder(this)
                builder.setCancelable(false)
                builder.setMessage("Please turn on your location before playing.")
                builder.setPositiveButton("OK") { _, _ ->
                    val intent = Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }
                val alert = builder.create()
                alert.show()
            }
        } else {
            //if permissions are not given, request permissions.
            requestPermissions()
        }
    }

    /**
     * Requests new location data.
     */
    private fun requestNewLocationData() {
        Log.i("myLocation", "request")
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 5000
        mLocationRequest.fastestInterval = 3000

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    /**
     * Will update camera and marker positions once new location is obtained.
     */
    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            //get updated coordinate position.
            Log.i("myLocation", "Callback")
            val mLastLocation: Location = locationResult.lastLocation
            val lat = mLastLocation.latitude
            val long = mLastLocation.longitude
            val lastLoc = LatLng(lat, long)
            //update marker and camera position.
            userMarker.remove()
            updateMarker(lastLoc)
            updateCamera(lastLoc)
            hasFoundLyric(lastLoc)
        }
    }

    /**
     * Checks if location is enabled on device.
     */
    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    /**
     * Check if appropriate location permissions have been given.
     */
    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    /**
     * Request permissions from user if location permissions have not been given.
     */
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            permissionId
        )
    }

    /**
     * Will get last known location once user grants location permissions.
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == permissionId) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }
}