package com.jyotsna.yourtvweatherapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var etCityName: EditText
    private lateinit var btnFetchWeather: Button
    private lateinit var tvWeatherData: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etCityName = findViewById(R.id.etCityName)
        btnFetchWeather = findViewById(R.id.btnFetchWeather)
        tvWeatherData = findViewById(R.id.tvWeatherData)

        btnFetchWeather.setOnClickListener {
            val city = etCityName.text.toString()
            fetchWeatherData(city)
        }
    }

    private fun fetchWeatherData(city: String) {
        val apiKey = "7c19a9fb15d91245a704842c7ab9005c"
        val call = RetrofitClient.instance.getCurrentWeather(city, apiKey)

        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                if (response.isSuccessful) {
                    val weatherResponse = response.body()
                    weatherResponse?.let {
                        val weatherInfo = """
                            Temperature: ${it.main.temp}°C
                            Humidity: ${it.main.humidity}%
                            Description: ${it.weather[0].description}
                        """.trimIndent()
                        tvWeatherData.text = weatherInfo
                    }
                } else {
                    tvWeatherData.text = "City not found!"
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                tvWeatherData.text = "Error: ${t.message}"
            }
        })
    }
}
