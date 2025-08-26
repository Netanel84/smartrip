package com.netanel.smartrip.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.netanel.smartrip.model.NavApp
import com.netanel.smartrip.model.PartyProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.userPrefsDataStore by preferencesDataStore(name = "user_prefs")

class UserPreferencesRepository(private val context: Context) {
    private object Keys {
        val ORIGIN = stringPreferencesKey("origin")
        val DEST = stringPreferencesKey("destination")
        val INFANTS = booleanPreferencesKey("infants")
        val STROLLER = booleanPreferencesKey("stroller")
        val WHEELCHAIR = booleanPreferencesKey("wheelchair")
        val KIDS = booleanPreferencesKey("kids")
        val ADULTS = booleanPreferencesKey("adults")
        val NAV_APP = stringPreferencesKey("nav_app")
    }

    val preferences: Flow<PrefState> = context.userPrefsDataStore.data.map { p ->
        PrefState(
            origin = p[Keys.ORIGIN] ?: "",
            destination = p[Keys.DEST] ?: "",
            profile = PartyProfile(
                infants = p[Keys.INFANTS] ?: false,
                stroller = p[Keys.STROLLER] ?: false,
                wheelchair = p[Keys.WHEELCHAIR] ?: false,
                kids = p[Keys.KIDS] ?: false,
                adults = p[Keys.ADULTS] ?: true
            ),
            navApp = NavApp.valueOf(p[Keys.NAV_APP] ?: NavApp.MAPS.name)
        )
    }

    suspend fun save(state: PrefState) {
        context.userPrefsDataStore.edit { p ->
            p[Keys.ORIGIN] = state.origin
            p[Keys.DEST] = state.destination
            p[Keys.INFANTS] = state.profile.infants
            p[Keys.STROLLER] = state.profile.stroller
            p[Keys.WHEELCHAIR] = state.profile.wheelchair
            p[Keys.KIDS] = state.profile.kids
            p[Keys.ADULTS] = state.profile.adults
            p[Keys.NAV_APP] = state.navApp.name
        }
    }
}

data class PrefState(
    val origin: String,
    val destination: String,
    val profile: PartyProfile,
    val navApp: NavApp
)
