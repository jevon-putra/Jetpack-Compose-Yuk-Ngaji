package com.jop.ngaji.data.local.store

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.jop.ngaji.data.model.LastReadSurah
import com.jop.ngaji.data.model.LastSyncLocation
import com.jop.ngaji.data.model.pray.PrayTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

class DataStoreSetting(private val dataStore: DataStore<Preferences>)  {

    suspend fun setLastReadSurah(lastReadSurah: LastReadSurah){
        dataStore.edit { setting ->
            setting[stringPreferencesKey(DataStoreKey.LAST_READ_SURAH)] = Json.encodeToString(lastReadSurah)
        }
    }

    fun getLastReadSurah(): Flow<LastReadSurah?> {
        return dataStore.data.map {
            val defaultValue = Json.encodeToString(LastReadSurah())
            Json.decodeFromString<LastReadSurah?>(it[stringPreferencesKey(DataStoreKey.LAST_READ_SURAH)] ?: defaultValue)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun setLastSync(lastSyncLocation: LastSyncLocation){
        dataStore.edit { setting ->
            setting[stringPreferencesKey(DataStoreKey.LAST_SYNC)] = Json.encodeToString(lastSyncLocation)
        }
    }

    private fun getLastSync(): Flow<LastSyncLocation?> {
        return dataStore.data.map {
            Json.decodeFromString<LastSyncLocation?>(it[stringPreferencesKey(DataStoreKey.LAST_SYNC)] ?: "null")
        }.flowOn(Dispatchers.IO)
    }

    suspend fun setLastPrayerTime(prayerTime: PrayTime.Timings){
        dataStore.edit { setting ->
            setting[stringPreferencesKey(DataStoreKey.LAST_PRAYER_TIME)] = Json.encodeToString(prayerTime)
        }
    }

    private fun getLastPrayerTime(): Flow<PrayTime.Timings?> {
        return dataStore.data.map {
            Json.decodeFromString<PrayTime.Timings?>(it[stringPreferencesKey(DataStoreKey.LAST_PRAYER_TIME)] ?: "null")
        }.flowOn(Dispatchers.IO)
    }

    fun getLastSyncAndPrayTime(): Flow<Pair<LastSyncLocation?, PrayTime.Timings?>> = flow {
        combine(getLastSync(), getLastPrayerTime()) { token, selectedBranch ->
            Pair(token, selectedBranch)
        }.collect{
            emit(it)
        }
    }
}