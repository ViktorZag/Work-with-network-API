package com.viktor_zet.download_json

import android.annotation.SuppressLint
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

private const val URL =
    "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=5b09406fe702903ba70a42721f758b23&lang=ua&units=metric"

class MainActivity : AppCompatActivity() {

    private lateinit var editTextCity: EditText
    private lateinit var textViewWeather: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextCity = findViewById(R.id.editTextCity)
        textViewWeather = findViewById(R.id.textViewWeather)

    }

    @SuppressLint("StaticFieldLeak")
    private inner class DownloadWeatherTask :
        AsyncTask<String?, Void?, String?>() {
        override fun doInBackground(vararg strings: String?): String? {
            var urlConnection: HttpURLConnection? = null
            val result = StringBuilder()
            try {
                val url = URL(strings[0])
                urlConnection = url.openConnection() as HttpURLConnection
                val inputStream: InputStream = urlConnection.getInputStream()
                val inputStreamReader = InputStreamReader(inputStream)
                val reader = BufferedReader(inputStreamReader)
                var line: String? = reader.readLine()
                while (line != null) {
                    result.append(line)
                    line = reader.readLine()
                }
                return result.toString()
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                urlConnection?.disconnect()
            }
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            try {
                val jsonObject = JSONObject(result)
                val city = jsonObject.getString("name")
                val temperature = jsonObject.getJSONObject("main").getString("temp")+"°c"
                val weatherDescription =
                    jsonObject.getJSONArray("weather")
                        .getJSONObject(0).getString("description")
                val weather = "$city\nTemperature: $temperature\nOutside: $weatherDescription"
                textViewWeather.text = weather
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    fun onClickShowWeather(view: View) {
        val city = editTextCity.text.toString().trim()
        if (city.isNotEmpty()){
            val task=DownloadWeatherTask()
            val url=String.format(URL,city)
            task.execute(url)
        }

    }
}
