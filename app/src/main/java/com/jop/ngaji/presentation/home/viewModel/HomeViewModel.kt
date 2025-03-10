package com.jop.ngaji.presentation.home.viewModel

import android.app.Application
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.jop.ngaji.data.Resource
import com.jop.ngaji.data.local.store.DataStoreSetting
import com.jop.ngaji.data.model.LastReadSurah
import com.jop.ngaji.data.model.LastSyncLocation
import com.jop.ngaji.data.repo.PrayRepository
import com.jop.ngaji.presentation.home.view.HomeScreenEvent
import com.jop.ngaji.presentation.home.view.HomeScreenState
import com.jop.ngaji.util.LocationManager
import com.jop.ngaji.util.getCountryCodeFromCountryName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HomeViewModel(application: Application, private val repository: PrayRepository, private val dataStore: DataStoreSetting): ViewModel() {
    private val _state = MutableStateFlow(HomeScreenState())
    val state: StateFlow<HomeScreenState> = _state

    private val geocoder = Geocoder(application, Locale.getDefault())
    private var address: Address? = null
    private var lastSyncLocation: LastSyncLocation? = null
    private var dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    private val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(application)
    private val locationManager = LocationManager(context = application, fusedLocationProviderClient = fusedLocationProviderClient)
    private val locationRequest =  LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 50)
        .setWaitForAccurateLocation(false)
        .build()

    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            val location = result.lastLocation!!

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                geocoder.getFromLocation(location.latitude, location.longitude, 1) {
                    address = it[0]
                    getTime()
                }
            } else {
                val listAddress = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                if (listAddress != null && listAddress.size != 0) {
                    address = listAddress[0]
                    getTime()
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            dataStore.getLastSyncAndPrayTime().first{
                if(it.first != null && it.second != null){
                    lastSyncLocation = it.first
                    _state.value = _state.value.copy(prayerTimes = it.second)
                }
                true
            }
        }

        viewModelScope.launch {
            dataStore.getLastReadSurah().collect {
                _state.value = _state.value.copy(lastReadSurah = it ?: LastReadSurah())
            }
        }
    }

    fun onEvent(event: HomeScreenEvent){
        when(event){
            is HomeScreenEvent.GetPrayTime -> {
                if(event.defaultLocation) getTime()
                else locationManager.getLiveLocation(locationRequest, locationCallback)
            }
        }
    }

    private fun getTime() = viewModelScope.launch {
        locationManager.stopLiveLocation(locationCallback)
        val currentDate = Calendar.getInstance()
        val addressData = address?.getAddressLine(0)?.split(", ")
        val city = addressData?.get(4) ?: "Surabaya"
        val country = addressData?.last() ?: "Indonesia"

        if((lastSyncLocation != null && (lastSyncLocation!!.city != city || lastSyncLocation!!.lastSync <= currentDate.time.time)) || lastSyncLocation == null){
            try {
                repository.getTime(dateFormat.format(currentDate.time), city, country.getCountryCodeFromCountryName()).collect {
                    when(it){
                        is Resource.Loading -> {
                            _state.value = _state.value.copy(isLoading = true)
                        }
                        is Resource.Success -> {
                            currentDate.add(Calendar.DAY_OF_MONTH, 1)
                            dataStore.setLastSync(LastSyncLocation(city, country, currentDate.time.time))
                            dataStore.setLastPrayerTime(it.data!!)
                            _state.value = _state.value.copy(prayerTimes = it.data)
                        }
                        is Resource.Error -> {
                            println("HELLO ${it.message.toString()}")
//                        _state.value = _state.value.copy(errorMessage = "Terjadi Kesalahan", isLoading = false)
                        }
                    }
                }
            } catch (e: Exception){
                println("HELLO ${e.message.toString()}")
//            _state.value = _state.value.copy(errorMessage = "Terjadi Kesalahan", isLoading = false)
            }
        }
    }
}