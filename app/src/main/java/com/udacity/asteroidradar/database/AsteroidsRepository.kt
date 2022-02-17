package com.udacity.asteroidradar.database


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.NetworkAsteroidContainer
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await

class AsteroidsRepository(private val database: AsteroidsDatabase) {
    private val _asteroidsRepository = MutableLiveData<ArrayList<Asteroid>>()
    private val _responseRepository = MutableLiveData<String>()

    val asteroids: LiveData<List<Asteroid>> = Transformations.map(database.asteroidDao.getAsteroids()) {
        it.asDomainModel()
    }

    suspend fun refreshAsteroid() {
        withContext(Dispatchers.IO) {
            val asteroidlist = convertStringToAsteroidObjects()
            database.asteroidDao.insertAll(*asteroidlist.asDatabaseModel())
        }
    }

    fun convertStringToAsteroidObjects(): LiveData<ArrayList<Asteroid>> {
        NasaApi.retrofit_service.getNetworkAsteriods().enqueue(object: Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                _responseRepository.value = response.body()
                _asteroidsRepository.value = parseAsteroidsJsonResult(JSONObject(_responseRepository.value))
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                _asteroidsRepository.value = ArrayList()
            }

        })
        return  _asteroidsRepository
    }
}

