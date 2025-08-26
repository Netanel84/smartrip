package com.netanel.smartrip.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PartyProfile(
    val infants: Boolean = false,
    val stroller: Boolean = false,
    val wheelchair: Boolean = false,
    val kids: Boolean = false,
    val adults: Boolean = true
) : Parcelable

@Parcelize
enum class NavApp : Parcelable { MAPS, WAZE }

@Parcelize
enum class StopCategory : Parcelable { FUEL, COFFEE, FOOD, PARK }

@Parcelize
data class TripPlan(
    val origin: String,
    val destination: String,
    val departureTime: Long,
    val profile: PartyProfile,
    val fuelMandatory: Boolean,
    val navApp: NavApp
) : Parcelable

@Parcelize
data class StopWindow(
    val fraction: Double,
    val lat: Double,
    val lng: Double
) : Parcelable
