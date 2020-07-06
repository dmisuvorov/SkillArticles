package ru.skillbranch.skillarticles.data.delegates

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import ru.skillbranch.skillarticles.data.local.PrefManager
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

@Suppress("UNCHECKED_CAST")
class PrefDelegate<T>(private val defaultValue: T) : ReadWriteProperty<PrefManager, T?> {
    private var value: T? = null

    override fun getValue(thisRef: PrefManager, property: KProperty<*>): T? {
        if (value == null) {
            val key = property.name
            value = when (defaultValue) {
                is Boolean -> thisRef.preferences.getBoolean(key, defaultValue) as? T
                is String -> thisRef.preferences.getString(key, defaultValue) as? T
                is Float -> thisRef.preferences.getFloat(key, defaultValue) as? T
                is Int -> thisRef.preferences.getInt(key, defaultValue) as? T
                is Long -> thisRef.preferences.getLong(key, defaultValue) as? T
                else -> throw IllegalArgumentException("Value must be primitive type")
            }
        }
        return value
    }

    override fun setValue(thisRef: PrefManager, property: KProperty<*>, value: T?) {
        thisRef.preferences.edit().run {
            val key = property.name
            when (value) {
                is Boolean -> putBoolean(key, value)
                is String -> putString(key, value)
                is Float -> putFloat(key, value)
                is Int -> putInt(key, value)
                is Long -> putLong(key, value)
                else -> throw IllegalArgumentException("Value must be primitive type")
            }
            apply()
        }
        this.value = value
    }
}

class PrefLiveDataDelegate<T>(private val defaultValue: T) :
    ReadOnlyProperty<PrefManager, LiveData<T>> {
    private lateinit var value: LiveData<T>
    override fun getValue(thisRef: PrefManager, property: KProperty<*>): LiveData<T> {
        return if (!::value.isInitialized) PrefLiveData(thisRef, property.name, defaultValue)
        else value
    }

}

@Suppress("UNCHECKED_CAST")
class PrefLiveData<T>(
    prefManager: PrefManager,
    private val key: String,
    private val defaultValue: T
) : LiveData<T>() {

    private val preferences = prefManager.preferences
    private val prefChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, changeKey ->
            if (changeKey == key) {
                val newValue = readValue()
                if (newValue != value) value = newValue
            }
        }

    override fun onActive() {
        super.onActive()
        value = readValue()
        preferences.registerOnSharedPreferenceChangeListener(prefChangeListener)
    }

    override fun onInactive() {
        super.onInactive()
        preferences.unregisterOnSharedPreferenceChangeListener(prefChangeListener)
    }

    private fun readValue(): T = when (defaultValue) {
        is Boolean -> preferences.getBoolean(key, defaultValue) as T
        is String -> preferences.getString(key, defaultValue) as T
        is Float -> preferences.getFloat(key, defaultValue) as T
        is Int -> preferences.getInt(key, defaultValue) as T
        is Long -> preferences.getLong(key, defaultValue) as T
        else -> throw IllegalArgumentException("Value must be primitive type")
    }
}