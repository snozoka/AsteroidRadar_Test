package com.udacity.asteroidradar.main


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.NasaApiFilter
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

enum class NasaApiStatus { LOADING, ERROR, DONE }
/**
 * The [ViewModel] that is attached to the [OverviewFragment].
 */

class MainViewModel : ViewModel() {
    private val _status = MutableLiveData<NasaApiStatus>()
    val status: LiveData<NasaApiStatus> get() = _status

    private val _asteroids = MutableLiveData<List<Asteroid>>()
    val asteroids: LiveData<List<Asteroid>> get() = _asteroids

    //For testing network call
    private val _response = MutableLiveData<String>()
    val response: LiveData<String> get() = _response

    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid>()
    val navigateToSelectedAsteroid: LiveData<Asteroid> get() = _navigateToSelectedAsteroid

    /**
     * Call getNasaAsteroids() on init so we can display status immediately.
     */
    init {
        getNasaAsteroids()
    }


    private fun getNasaAsteroids() {
        NasaApi.retrofit_service.getNetworkAsteriods().enqueue(object: Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                _response.value = response.body()
                Log.i("AsteroidMessage", _response.value.toString())
                _asteroids.value = parseAsteroidsJsonResult(JSONObject(_response.value))
                Log.i("AsteroidMessage", _asteroids.value.toString())
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                _asteroids.value = ArrayList()
            }

        })


//        viewModelScope.launch {
//            _status.value = NasaApiStatus.LOADING
//            try {
//                _asteroids.value = NasaApi.retrofit_service.getNetworkAsteriods(filter.value)
//                _status.value = NasaApiStatus.DONE
//            }
//            catch (e: Exception){
//                _status.value = NasaApiStatus.ERROR
//                _asteroids.value = ArrayList()
//            }
//        }
    }

    /**
     * When the property is clicked, set the [_navigateToSelectedProperty] [MutableLiveData]
     * @param marsProperty The [MarsProperty] that was clicked on.
     */
    fun displayAsteroidDetails(asteroid: Asteroid){
        _navigateToSelectedAsteroid.value = asteroid
    }

    /**
     * After the navigation has taken place, make sure navigateToSelectedProperty is set to null
     */
    fun displayAsteroidDetailsComplete(){
        _navigateToSelectedAsteroid.value = null
    }
    /**
     * Updates the data set filter for the web services by querying the data with the new filter
     * by calling [getMarsRealEstateProperties]
     * @param filter the [MarsApiFilter] that is sent as part of the web server request
     */
    fun updateFilter(){
        getNasaAsteroids()
    }

}
