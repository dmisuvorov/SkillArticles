package ru.skillbranch.skillarticles.data.delegates

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
    ReadOnlyProperty<PrefManager, PrefLiveData<T>> {
    private lateinit var value: PrefLiveData<T>
    override fun getValue(thisRef: PrefManager, property: KProperty<*>): PrefLiveData<T> {
        if (!::value.isInitialized) value = PrefLiveData(thisRef, property.name, defaultValue)
        return value
    }

}

@Suppress("UNCHECKED_CAST")
class PrefLiveData<T>(
    prefManager: PrefManager,
    private val key: String,
    private val defaultValue: T
) : LiveData<T>() {

    private val preferences = prefManager.preferences

    fun updateValue() {
        val newValue = readValue()
        if (newValue != value)
            value = newValue
    }

    override fun onActive() {
        super.onActive()
        value = readValue()
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