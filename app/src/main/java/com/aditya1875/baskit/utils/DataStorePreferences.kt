package com.aditya1875.baskit.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("baskit_prefs")

class PreferencesManager(private val context: Context) {

    private val ONBOARDING_DONE_KEY = booleanPreferencesKey("onboarding_done")

    val isOnboardingDone: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[ONBOARDING_DONE_KEY] ?: false
    }

    suspend fun setOnboardingDone() {
        context.dataStore.edit { prefs ->
            prefs[ONBOARDING_DONE_KEY] = true
        }
    }
}