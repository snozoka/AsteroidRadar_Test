package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.AsteroidsRepository
import com.udacity.asteroidradar.database.getDatabase
import retrofit2.HttpException

class RefreshDataWorker(appContext: Context, params: WorkerParameters): CoroutineWorker(appContext, params){
    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val respository = AsteroidsRepository(database)

        return try {
            respository.convertStringToAsteroidObjects()
            Result.success()
        }catch (e: HttpException){
            Result.retry()
        }
    }

}