package ru.skillbranch.skillarticles.data.local

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.preference.PreferenceManager
import ru.skillbranch.skillarticles.App
import ru.skillbranch.skillarticles.data.delegates.PrefLiveDataDelegate
import ru.skillbranch.skillarticles.data.models.AppSettings

object PrefManager {

    val preferences: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(App.applicationContext())
    }
    val isAuth by PrefLiveDataDelegate(false)
    private val isDarkMode by PrefLiveDataDelegate(false)
    private val isBigText by PrefLiveDataDelegate(false)

    fun clearAll() {
        preferences.edit().clear().apply()
    }

    fun getAppSettings(): LiveData<AppSettings> {
        return MediatorLiveData<AppSettings>().apply {
            value = AppSettings()
            addSource(isDarkMode) {
                val newValue = value?.copy(isDarkMode = it)
                if (newValue != value) value = newValue
            }
            addSource(isBigText) {
                val newValue = value?.copy(isBigText = it)
                if (newValue != value) value = newValue
            }
        }
    }

    fun updateSettings(appSettings: AppSettings) {
        preferences.edit().apply {
            putBoolean(::isDarkMode.name, appSettings.isDarkMode)
            putBoolean(::isBigText.name, appSettings.isBigText)
            apply()
        }
        isDarkMode.updateValue()
        isBigText.updateValue()
    }

    fun setAuth(auth: Boolean) {
        preferences.edit().apply {
            putBoolean(::isAuth.name, auth)
            apply()
        }
        isAuth.updateValue()
    }
}

