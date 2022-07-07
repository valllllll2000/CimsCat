package com.vaxapp.cims

import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.util.Log
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken

class Api {

    fun orderByDistance(applicationContext: Context, currentLocation: Location): MutableList<DistancedMountainTop> {
        val originalList = loadApi(applicationContext)
       // Log.d("MainActivity", originalList.toString())
        val distancedList = mutableListOf<DistancedMountainTop>()

        for (top in originalList) {
            val name = top.name ?: ""
            val location = Location(LocationManager.GPS_PROVIDER)
            location.latitude = top.lat?.toDoubleOrNull() ?: 0.0
            location.longitude = top.lon?.toDoubleOrNull() ?: 0.0
            distancedList.add(DistancedMountainTop(name, location, top.url ?: "", currentLocation.distanceTo(location) / 1000))
        }
        distancedList.sortBy { it.distanceFromUsInKm }
        return distancedList
    }

    private fun loadApi(applicationContext: Context): List<MountainTop> {
        val topsString = applicationContext.assets.open("data.json").bufferedReader().use { it.readText() }
        return Gson().fromJson(topsString, object : TypeToken<List<MountainTop?>?>() {}.type)
    }
}

data class MountainTop(
    @SerializedName("nombre") var name: String? = null,
    @SerializedName("lat") var lat: String? = null,
    @SerializedName("lang") var lon: String? = null,
    @SerializedName("url") var url: String? = null
)

data class DistancedMountainTop(
    var name: String,
    var location: Location,
    var url: String,
    var distanceFromUsInKm: Float
)
