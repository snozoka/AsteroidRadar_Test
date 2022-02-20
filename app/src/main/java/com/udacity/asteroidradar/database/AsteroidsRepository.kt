package com.udacity.asteroidradar.database


import android.net.Network
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.NetworkAsteroidContainer
import com.udacity.asteroidradar.api.asDatabaseModel
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import java.util.*

class AsteroidsRepository(private val database: AsteroidsDatabase) {
    var asteroidlist =  ArrayList<Asteroid>()

    val asteroids: LiveData<List<Asteroid>> = Transformations.map(database.asteroidDao.getAsteroids()) {
        it.asDomainModel()
    }



    suspend fun convertStringToAsteroidObjects() {
        try {
            asteroidlist = parseAsteroidsJsonResult(JSONObject(NasaApi.retrofit_service.getNetworkAsteriods()))
            Log.i("AsteroidListRepository:", asteroidlist.size.toString())
            database.asteroidDao.insertAll(*asteroidlist.asDatabaseModel())
            Log.i("AsteroidDBrepository:", asteroids.value?.size.toString())
        } catch (ex: Exception) {
            asteroidlist = ArrayList()
        }
    }
}

