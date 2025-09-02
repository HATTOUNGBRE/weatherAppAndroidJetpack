package com.example.weatherapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.api.Constants
import com.example.weatherapp.api.NetworkResponse
import com.example.weatherapp.api.RetrofitInstance
import com.example.weatherapp.api.WeatherApi
import com.example.weatherapp.api.WeatherModel
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
// private seule veut dire accessible uniquement dans cette classe
    private val weatherApi = RetrofitInstance.weatherApi
    // MutableLiveData pour stocker la réponse du réseau
    // _variable privée pour encapsulation donc on mets un _
    private val _weatherResult = MutableLiveData<NetworkResponse<WeatherModel>>()
    // LiveData publique pour exposer les données
    public val weatherResult : LiveData<NetworkResponse<WeatherModel>> = _weatherResult

    fun getData( city: String){
        _weatherResult.value = NetworkResponse.Loading
        viewModelScope.launch {
           val response = weatherApi.getWeather(Constants.apiKey,city)
            try {
                if (response.isSuccessful){
                    response.body()?.let {
                        _weatherResult.value = NetworkResponse.Success(it)
                    }

                }else{
                    _weatherResult.value = NetworkResponse.Error("failed to load")
                }
            } catch (e: Exception){
                _weatherResult.value = NetworkResponse.Error("failed to load")

            }

        }
    }
}