package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.Asteroid

enum class NasaApiStatus { LOADING, ERROR, DONE }
/**
 * The [ViewModel] that is attached to the [OverviewFragment].
 */

class MainViewModel : ViewModel() {
    private val _status = MutableLiveData<NasaApiStatus>()
    val status: LiveData<NasaApiStatus> get() = _status

    private val _asteroids = MutableLiveData<List<Asteroid>>()
    val asteroids: LiveData<List<Asteroid>> get() = _asteroids

    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid>()
    val navigateToSelectedAsteroid: LiveData<Asteroid> get() = _navigateToSelectedAsteroid

    /**
     * Call getNasaAsteroids() on init so we can display status immediately.
     */
//    init {
//        getNasaAsteroids(NasaApiFilter.SHOW_ALL)
//    }

//    private fun getNasaAsteroids(filter: NasaApiFilter) {
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
//    }

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
//    fun updateFilter(filter: NasaApiFilter){
//        getNasaAsteroids(filter)
//    }

}
