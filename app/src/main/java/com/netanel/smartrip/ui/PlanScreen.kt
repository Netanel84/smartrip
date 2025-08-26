package com.netanel.smartrip.ui

import android.location.Geocoder
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.netanel.smartrip.R
import com.netanel.smartrip.model.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PlanScreen(plan: TripPlan) {
    val context = LocalContext.current
    var windows by remember { mutableStateOf(listOf<StopWindow>()) }
    var driveMinutes by remember { mutableStateOf(0.0) }

    LaunchedEffect(Unit) {
        val geocoder = Geocoder(context, Locale.getDefault())
        val originAddr = geocoder.getFromLocationName(plan.origin, 1)?.firstOrNull()
        val destAddr = geocoder.getFromLocationName(plan.destination, 1)?.firstOrNull()
        if (originAddr != null && destAddr != null) {
            val dist = haversine(originAddr.latitude, originAddr.longitude, destAddr.latitude, destAddr.longitude)
            driveMinutes = dist / 90.0 * 60.0
            val fractions = computeStopFractions(dist, plan.profile, plan.fuelMandatory)
            windows = fractions.map { f ->
                val lat = originAddr.latitude + (destAddr.latitude - originAddr.latitude) * f
                val lng = originAddr.longitude + (destAddr.longitude - originAddr.longitude) * f
                StopWindow(f, lat, lng)
            }
        } else {
            driveMinutes = 0.0
            windows = emptyList()
        }
    }

    val formatter = remember { SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault()) }
    val totalMinutes = driveMinutes + windows.size * 20

    Column(modifier = Modifier.padding(16.dp)) {
        Text(stringResource(R.string.summary, plan.origin, plan.destination))
        Text(stringResource(R.string.planned_time, formatter.format(Date(plan.departureTime))))
        Spacer(Modifier.height(8.dp))
        Text(stringResource(R.string.drive_time, driveMinutes.toInt()))
        Text(stringResource(R.string.total_time, totalMinutes.toInt()))
        Spacer(Modifier.height(8.dp))
        windows.forEachIndexed { index, w ->
            Text(stringResource(R.string.window_label, index + 1))
            Row {
                Button(onClick = { openCategory(context, plan.navApp, stringResource(R.string.cat_fuel), w.lat, w.lng) }) {
                    Text(stringResource(R.string.cat_fuel))
                }
                Spacer(Modifier.width(4.dp))
                Button(onClick = { openCategory(context, plan.navApp, stringResource(R.string.cat_coffee), w.lat, w.lng) }) {
                    Text(stringResource(R.string.cat_coffee))
                }
                Spacer(Modifier.width(4.dp))
                Button(onClick = { openCategory(context, plan.navApp, stringResource(R.string.cat_food), w.lat, w.lng) }) {
                    Text(stringResource(R.string.cat_food))
                }
                Spacer(Modifier.width(4.dp))
                Button(onClick = { openCategory(context, plan.navApp, stringResource(R.string.cat_park), w.lat, w.lng) }) {
                    Text(stringResource(R.string.cat_park))
                }
            }
            Spacer(Modifier.height(8.dp))
        }
        Spacer(Modifier.height(16.dp))
        Button(onClick = { openDestination(context, plan.navApp, plan.destination) }) {
            Text(stringResource(R.string.navigate_dest))
        }
    }
}
