package com.travels.searchtravels

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.api.services.vision.v1.model.LatLng
import com.google.errorprone.annotations.DoNotMock
import com.travels.searchtravels.api.OnVisionApiListener
import com.travels.searchtravels.api.VisionApi
import okhttp3.OkHttpClient
import okhttp3.Request
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.runners.MockitoJUnitRunner

@RunWith(AndroidJUnit4::class)
class CategoriesUnitTest {
    final val url = "https://ciphen.net/pictures/"
    private val client = OkHttpClient()
    private var finished = false
    private var result = ""

    @Test
    fun snowTest() {
        val listener = object : OnVisionApiListener {
            override fun onSuccess(latLng: LatLng?) {
                throw IllegalStateException()
            }

            override fun onError() {
                throw IllegalStateException()
            }

            @Test
            override fun onErrorPlace(category: String?) {
                assertEquals("snow", category)
            }
        }

        val imageUrl = url + "snow.jpg"

        val request = Request.Builder()
            .url(imageUrl)
            .build()

        val response = client.newCall(request).execute()
        val inputStream = response.body()!!.byteStream()
        val bitmap = BitmapFactory.decodeStream(inputStream)

        VisionApi.findLocation(bitmap, null, listener)
    }
}