package com.travels.searchtravels

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.api.services.vision.v1.model.Image
import com.google.api.services.vision.v1.model.LatLng
import com.travels.searchtravels.api.OnVisionApiListener
import com.travels.searchtravels.api.VisionApi2
import okhttp3.OkHttpClient
import okhttp3.Request
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
class CategoriesTest {
    final val url = "https://ciphen.net/pictures/"
    private val client = OkHttpClient()

    private final val snow = "snow.jpg"
    private final val sea = "sea.jpg"
    private final val beach = "beach2.jpg"
    private final val ocean = "ocean9.jpg"
    private final val mountain = "mountain.jpg"
    private final val other = "other.jpg"
    private final val tokyo = "tokyo.jpg"

    @Mock
    var listener: OnVisionApiListener = object : OnVisionApiListener {
        override fun onError() {}

        override fun onErrorPlace(category: String?) {}

        override fun onSuccess(latLng: LatLng?) {
            throw IllegalStateException()
        }
    }

    @InjectMocks
    val visionApiUtil = VisionApi2(listener)

    @Before
    fun initMocks(): Unit {
        MockitoAnnotations.initMocks(this)
    }

    private fun getImage(name: String): Image {
        val request = Request.Builder()
            .url(url + name)
            .build()
        val response = client.newCall(request).execute()
        return Image().encodeContent(response.body()!!.bytes())
    }

    @Test
    fun snowTest() {
        visionApiUtil.findLocation(getImage(snow), "")
        verify(listener).onErrorPlace("snow")
    }

    @Test
    fun mountainTest() {
        visionApiUtil.findLocation(getImage(mountain), "")
        verify(listener).onErrorPlace("mountain")
    }

    @Test
    fun seaTest() {
        visionApiUtil.findLocation(getImage(sea), "")
        verify(listener).onErrorPlace("sea")
    }

    @Test
    fun oceanTest() {
        visionApiUtil.findLocation(getImage(ocean), "")
        verify(listener).onErrorPlace("ocean")
    }

    @Test
    fun beachTest() {
        visionApiUtil.findLocation(getImage(beach), "")
        verify(listener).onErrorPlace("beach")
    }

    @Test
    fun otherTest() {
        visionApiUtil.findLocation(getImage(other), "")
        verify(listener).onError()
    }

    @Test
    fun locationTest() {
        visionApiUtil.findLocation(getImage(tokyo), "")
        verify(listener).onSuccess(LatLng().setLatitude(35.66031160000001).setLongitude(139.72900629999998))
    }
}