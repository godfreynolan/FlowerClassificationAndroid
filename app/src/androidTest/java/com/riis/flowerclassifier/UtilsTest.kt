package com.riis.flowerclassifier

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.*
import org.junit.Test
import java.io.InputStream

class UtilsTest{
    private val appContext = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun test_Resize(){
        val testImageStream: InputStream = appContext.assets.open("test/test-petunia.jpg")
        val testImage: Bitmap = BitmapFactory.decodeStream(testImageStream)
        val scaled = Utils.resize(testImage, 224, 224)
        assertEquals("Image Width: ",224, scaled.width)
        assertEquals("Image Height: ",224, scaled.height)
    }
}