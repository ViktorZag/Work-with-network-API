package com.viktor_zet.downloadwebcontent

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import java.io.InputStream
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    private val url =
        "https://www.aton.ru/upload/iblock/68d/68d9ad01bd1d588c0af0fb975ab739de.png"

    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = findViewById(R.id.imageView)
    }

    fun onClickDownloadImage(view: View) {
        Log.i("URL", "onClickDownload")
        val task = DownloadImageTask()
        val bitmap: Bitmap? = task.execute(url).get()
        imageView.setImageBitmap(bitmap)
    }

    private class DownloadImageTask : AsyncTask<String?, Void?, Bitmap?>() {
        override fun doInBackground(vararg arg: String?): Bitmap? {
            var url: URL? = null
            var urlConnection: HttpURLConnection? = null
            try {
                url = URL(arg[0])
                urlConnection = url.openConnection() as HttpURLConnection
                val inputStream: InputStream = urlConnection.getInputStream()
                return BitmapFactory.decodeStream(inputStream)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                urlConnection?.disconnect()

            }
            return null
        }
    }
}

