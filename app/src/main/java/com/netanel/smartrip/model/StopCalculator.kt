package com.netanel.smartrip.model

import kotlin.math.*

private const val AVG_SPEED_KMH = 90.0

/**
 * Calculate list of stop window fractions (0-1) along route
 */
fun computeStopFractions(distanceKm: Double, profile: PartyProfile, fuelMandatory: Boolean): List<Double> {
    val interval = if (profile.infants || profile.stroller) 100.0 else 150.0
    val totalMinutes = distanceKm / AVG_SPEED_KMH * 60.0
    val count = (totalMinutes / interval).toInt().coerceAtMost(3)
    val fractions = mutableListOf<Double>()
    for (i in 1..count) {
        val f = i * interval / totalMinutes
        if (f < 1.0) fractions.add(f)
    }
    if (fuelMandatory) {
        if (fractions.none { abs(it - 0.5) < 0.1 }) {
            fractions.add(0.5)
        }
    }
    return fractions.sorted().take(3)
}

fun haversine(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val R = 6371e3 // meters
    val phi1 = lat1.toRadians()
    val phi2 = lat2.toRadians()
    val dPhi = (lat2 - lat1).toRadians()
    val dLambda = (lon2 - lon1).toRadians()
    val a = sin(dPhi/2).pow(2.0) + cos(phi1)*cos(phi2)*sin(dLambda/2).pow(2.0)
    val c = 2 * atan2(sqrt(a), sqrt(1-a))
    return R * c / 1000.0 // km
}

private fun Double.toRadians() = this / 180.0 * Math.PI
