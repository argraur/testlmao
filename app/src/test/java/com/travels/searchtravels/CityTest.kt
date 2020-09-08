package com.travels.searchtravels

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.api.services.vision.v1.model.Image
import com.google.api.services.vision.v1.model.LatLng
import com.travels.searchtravels.api.GeolocationApi
import com.travels.searchtravels.api.OnVisionApiListener
import com.travels.searchtravels.api.VisionApi2
import junit.framework.Assert.assertTrue
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
class CityTest {
    private val url = "https://ciphen.net/pictures/"
    private val client = OkHttpClient()

    // Filenames
    private val tokyo = "tokyo.jpg"
    private val moscow = "moscow.jpg"

    // Expected coordinates
    private val tokyo_latlng = LatLng().setLatitude(35.66031160000001).setLongitude(139.72900629999998)
    private val moscow_latlng = LatLng().setLatitude(55.8263081).setLongitude(37.6376375)

    @Mock
    val listener = object : OnVisionApiListener {
        override fun onError() {}
        override fun onErrorPlace(category: String?) {}
        override fun onSuccess(latLng: LatLng?) {}
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
    fun tokyoTest() {
        visionApiUtil.findLocation(getImage(tokyo), "")
        // Validate that we get same coordinates as expected
        verify(listener).onSuccess(tokyo_latlng)
        val latLngString = tokyo_latlng.latitude.toString() + "," + tokyo_latlng.longitude.toString()
        GeolocationApi.create().getCity(latLngString, "en-GB")
            .subscribe { result ->
                //assertTrue(result.asJsonObject.getAsJsonObject("plus_code").get("compound_code").asString.toLowerCase().contains("tokyo"))
                // Assert that GeoAPI result contains "tokyo"
                assertTrue(result.asJsonObject.getAsJsonArray("results")[0].asJsonObject.get("formatted_address").asString.toLowerCase().contains("tokyo"))
            }
    }

    @Test
    fun moscowTest() {
        visionApiUtil.findLocation(getImage(moscow), "")
        // Validate that we get same coordinates as expected
        verify(listener).onSuccess(moscow_latlng)
        val latLngString = moscow_latlng.latitude.toString() + "," + moscow_latlng.longitude.toString()
        GeolocationApi.create().getCity(latLngString, "en-GB")
            .subscribe { result ->
                // Assert that GeoAPI result contains our city.
                assertTrue(result.asJsonObject.getAsJsonArray("results")[0].asJsonObject.get("formatted_address").asString.toLowerCase().contains("moscow"))
            }
    }
}