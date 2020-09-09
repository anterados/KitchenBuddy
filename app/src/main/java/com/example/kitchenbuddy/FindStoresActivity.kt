package com.example.kitchenbuddy

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException


//dobar url
//https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=45.5613818,18.6407529&radius=2500&type=supermarket&key=AIzaSyATHg-yaSv66WoZ_5tfWSyBDlvtHhhNGjI
class FindStoresActivity : AppCompatActivity(), OnMapReadyCallback, OnMarkerClickListener {

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location

    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private var locationUpdateState = false
    val storesList = ArrayList<Place>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_stores)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)

                lastLocation = p0.lastLocation

                placeMarkerOnMap(LatLng(lastLocation.latitude, lastLocation.longitude))
            }
        }
        createLocationRequest()
        //
        /*
        storesList.forEachIndexed{index, place ->
            val location=place.geometry.location.lat + place.geometry.location.lng
            Log.v("FindStoresActivity", "string!!!!!!!!!!!!!!!!!")
            Log.v("FindStoresActivity", location)

        }

         */
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // Add a marker in Sydney and move the camera
        //val osijek = LatLng(45.560001, 18.675880)
        //map.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        //map.moveCamera(CameraUpdateFactory.newLatLng(osijek))
        //map.moveCamera(CameraUpdateFactory.newLatLngZoom(osijek,12f))
        map.getUiSettings().setZoomControlsEnabled(true)
        map.setOnMarkerClickListener(this)
        setUpMap()
    }

    fun fetchJsonPlaces(location:Location){
        println("Attempting to fetch JSON")

        val urlPlaces = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=${location.latitude},${location.longitude}&radius=2500&type=supermarket&key=AIzaSyATHg-yaSv66WoZ_5tfWSyBDlvtHhhNGjI"
        val request = Request.Builder().url(urlPlaces).build()
        val client = OkHttpClient()

        runOnUiThread {
            client.newCall(request).enqueue(object: Callback {
                override fun onResponse(call: Call, responses: Response) {
                    val body = responses.body?.string()
                    println(body)

                    val gson = GsonBuilder().create()
                    val placeList = gson.fromJson(body, PlaceList::class.java)
                    val latituda= placeList.results[0].geometry.location.lat
                    storesList.addAll(placeList.results)
                    Log.v("FindStoresActivity", latituda)
                    //Log.v("FindStoresActivity", storesList[0].name)
                    for ((i, item) in storesList.withIndex()) {
                        val storeName=storesList[i].name
                        //var latlng:String=(storesList[i].geometry.location.lat +","+storesList[i].geometry.location.lng)

                        //val latlong =latlng.split(",".toRegex()).toTypedArray()
                        //val latitude = latlong[0].toDouble()
                        //val longitude = latlong[1].toDouble()
                        val locationss = LatLng(storesList[i].geometry.location.lat.toDouble(), storesList[i].geometry.location.lng.toDouble())
                        Log.v("FindStoresActivity", locationss.toString())
                        runOnUiThread {
                            placeStoreOnMap(locationss, storeName)


                        }
                        //placeMarkerOnMap(locationss)

                    }



                }
                override fun onFailure(call: Call, e: IOException) {
                    TODO("Not yet implemented")
                }

            })
        }


    }

    override fun onMarkerClick(p0: Marker?)=false

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val REQUEST_CHECK_SETTINGS = 2
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }

        map.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                lastLocation = location

                val currentLatLng = LatLng(location.latitude, location.longitude)
                fetchJsonPlaces(lastLocation)

                placeMarkerOnMap(currentLatLng)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
            }
        }
    }


    private fun placeMarkerOnMap(location: LatLng) {

        val markerOptions = MarkerOptions().position(location).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
        //map.clear()
        map.addMarker(markerOptions)
        //map.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))


    }

    private fun placeStoreOnMap(location:LatLng, name: String) {
        val markerOptions = MarkerOptions().position(location).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).title(name)
        map.addMarker(markerOptions)
        //map.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))


    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null /* Looper */)
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            locationUpdateState = true
            startLocationUpdates()
        }
        task.addOnFailureListener { e ->
            if (e is ResolvableApiException) {
                try {
                    e.startResolutionForResult(this@FindStoresActivity,
                        REQUEST_CHECK_SETTINGS)
                } catch (sendEx: IntentSender.SendIntentException) {
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                locationUpdateState = true
                startLocationUpdates()
            }
        }
    }


    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    public override fun onResume() {
        super.onResume()
        if (!locationUpdateState) {
            startLocationUpdates()
        }
    }


}

class PlaceList(val results: List<Place>)
class Place(val name:String, val geometry:LocationC)
class LocationC(val location:Coordinates)
class Coordinates(val lat:String, val lng:String)

