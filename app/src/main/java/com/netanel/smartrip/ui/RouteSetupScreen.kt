package com.netanel.smartrip.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.netanel.smartrip.R
import com.netanel.smartrip.data.PrefState
import com.netanel.smartrip.data.UserPreferencesRepository
import com.netanel.smartrip.model.NavApp
import com.netanel.smartrip.model.PartyProfile
import com.netanel.smartrip.model.TripPlan
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun RouteSetupScreen(onPlan: (TripPlan) -> Unit) {
    val context = LocalContext.current
    val repo = remember { UserPreferencesRepository(context) }
    val prefs by repo.preferences.collectAsState(initial = PrefState("", "", PartyProfile(), NavApp.MAPS))

    var origin by remember { mutableStateOf(TextFieldValue(prefs.origin)) }
    var destination by remember { mutableStateOf(TextFieldValue(prefs.destination)) }
    var profile by remember { mutableStateOf(prefs.profile) }
    var navApp by remember { mutableStateOf(prefs.navApp) }
    var fuelMandatory by remember { mutableStateOf(false) }
    var departureTime by remember { mutableStateOf(System.currentTimeMillis()) }

    val scope = rememberCoroutineScope()
    val formatter = remember { SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault()) }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = origin,
            onValueChange = { origin = it },
            label = { Text(stringResource(R.string.origin)) },
            modifier = Modifier.fillMaxWidth().semantics { contentDescription = "origin" }
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = destination,
            onValueChange = { destination = it },
            label = { Text(stringResource(R.string.destination)) },
            modifier = Modifier.fillMaxWidth().semantics { contentDescription = "destination" }
        )
        Spacer(Modifier.height(8.dp))
        Button(onClick = {
            val cal = Calendar.getInstance()
            DatePickerDialog(context, { _, y, m, d ->
                cal.set(y, m, d)
                TimePickerDialog(context, { _, h, min ->
                    cal.set(Calendar.HOUR_OF_DAY, h)
                    cal.set(Calendar.MINUTE, min)
                    departureTime = cal.timeInMillis
                }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }) {
            Text(stringResource(R.string.pick_departure))
        }
        Text(formatter.format(Date(departureTime)))
        Spacer(Modifier.height(8.dp))
        Text(stringResource(R.string.party_profile))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = profile.infants, onCheckedChange = { profile = profile.copy(infants = it) })
            Text(stringResource(R.string.infants))
            Spacer(Modifier.width(8.dp))
            Checkbox(checked = profile.stroller, onCheckedChange = { profile = profile.copy(stroller = it) })
            Text(stringResource(R.string.stroller))
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = profile.wheelchair, onCheckedChange = { profile = profile.copy(wheelchair = it) })
            Text(stringResource(R.string.wheelchair))
            Spacer(Modifier.width(8.dp))
            Checkbox(checked = profile.kids, onCheckedChange = { profile = profile.copy(kids = it) })
            Text(stringResource(R.string.kids))
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = profile.adults, onCheckedChange = { profile = profile.copy(adults = it) })
            Text(stringResource(R.string.adults))
        }
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = fuelMandatory, onCheckedChange = { fuelMandatory = it })
            Text(stringResource(R.string.fuel_mandatory))
        }
        Spacer(Modifier.height(8.dp))
        Text(stringResource(R.string.choose_nav))
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(selected = navApp == NavApp.MAPS, onClick = { navApp = NavApp.MAPS })
            Text(stringResource(R.string.nav_maps))
            Spacer(Modifier.width(8.dp))
            RadioButton(selected = navApp == NavApp.WAZE, onClick = { navApp = NavApp.WAZE })
            Text(stringResource(R.string.nav_waze))
        }
        Spacer(Modifier.height(16.dp))
        Button(onClick = {
            val plan = TripPlan(
                origin.text,
                destination.text,
                departureTime,
                profile,
                fuelMandatory,
                navApp
            )
            scope.launch { repo.save(PrefState(origin.text, destination.text, profile, navApp)) }
            onPlan(plan)
        }, enabled = origin.text.isNotBlank() && destination.text.isNotBlank()) {
            Text(stringResource(R.string.plan))
        }
    }
}
