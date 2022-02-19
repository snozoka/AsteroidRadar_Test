package com.udacity.asteroidradar.api
import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.database.DatabaseAsteroid

/**
 * DataTransferObjects go in this file. These are responsible for parsing responses from the server
 * or formatting objects to send to the server. You should convert these to domain objects before
 * using them.
 */

/**
 * VideoHolder holds a list of Asteroids.
 *
 * This is to parse first level of our network result which looks like
 *
 * [Asteroid(id=2388189, codename=388189 (2006 DS14), closeApproachDate=2022-02-17, absoluteMagnitude=20.42, estimatedDiameter=0.4898239078, relativeVelocity=15.6849703878, distanceFromEarth=0.2228399586, isPotentiallyHazardous=false),
 * Asteroid(id=3696459, codename=(2014 VP35), closeApproachDate=2022-02-17, absoluteMagnitude=23.3, estimatedDiameter=0.130028927, relativeVelocity=20.704573902, distanceFromEarth=0.4903214633, isPotentiallyHazardous=false)
 */
@JsonClass(generateAdapter = true)
data class NetworkAsteroidContainer(val asteroids: List<AsteroidVideo>)

/**
 * Asteroid represent an asteroid that can be viewed.
 */
@JsonClass(generateAdapter = true)
data class AsteroidVideo(
    val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean)

/**
 * Convert Network results to database objects
 */
fun NetworkAsteroidContainer.asDomainModel(): List<Asteroid> {
    return asteroids.map {
        Asteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous)
    }
}

fun List<Asteroid>.asDatabaseModel(): Array<DatabaseAsteroid> {
    return map {
        DatabaseAsteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }.toTypedArray()
}