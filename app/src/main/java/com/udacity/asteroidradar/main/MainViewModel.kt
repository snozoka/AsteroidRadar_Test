package com.udacity.asteroidradar.main


import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.PicOfDayApiFilter
import com.udacity.asteroidradar.database.AsteroidsRepository
import com.udacity.asteroidradar.database.getDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

enum class NasaApiStatus { LOADING, ERROR, DONE }
/**
 * The [ViewModel] that is attached to the [OverviewFragment].
 */

class MainViewModel(application: android.app.Application) : AndroidViewModel(application) {
    private val _status = MutableLiveData<NasaApiStatus>()
    val status: LiveData<NasaApiStatus> get() = _status

    private var _asteroids = MutableLiveData<List<Asteroid>>()
    val asteroids: LiveData<List<Asteroid>> get() =  _asteroids

    private val _pictureEntity = MutableLiveData<PictureOfDay>()
    val pictureEntity: LiveData<PictureOfDay> get() = _pictureEntity

    //For testing network call
    private val _response = MutableLiveData<String>()
    val response: LiveData<String> get() = _response

    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid>()
    val navigateToSelectedAsteroid: LiveData<Asteroid> get() = _navigateToSelectedAsteroid

//    private val viewModelJob = SupervisorJob()
//
//    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val database = getDatabase(application)
    private val asteroidsRepository = AsteroidsRepository(database)

    /**
     * Call getNasaAsteroids() on init so we can display status immediately.
     */
    init {
//        getNasaAsteroids()
        viewModelScope.launch {
            asteroidsRepository.convertStringToAsteroidObjects()
            getPicOfDay(PicOfDayApiFilter.MEDIA_TYPE)
            //Log.i("AsteroidList",asteroids.toString())
            _asteroids = asteroidsRepository.asteroids as MutableLiveData<List<Asteroid>>
        }
    }


    private fun getPicOfDay(filter: PicOfDayApiFilter){
        viewModelScope.launch {
            _status.value = NasaApiStatus.LOADING
            try {
                _pictureEntity.value = NasaApi.retrofitService_pic.getPicOfDay(filter.value)
                _status.value = NasaApiStatus.DONE
            }
            catch (e: Exception){
                _status.value = NasaApiStatus.ERROR
                _pictureEntity.value = PictureOfDay("", "", "")
            }
        }
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
     * by calling [getPicOfDay]
     * @param filter the [PicOfDayApiFilter] that is sent as part of the web server request
     */
    fun updateImageFilter(filter: PicOfDayApiFilter){
        getPicOfDay(filter)
    }


    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
