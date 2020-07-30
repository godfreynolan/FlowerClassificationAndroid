package com.riis.flowerclassifier.tflite

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.test.platform.app.InstrumentationRegistry
import com.riis.flowerclassifier.R
import com.riis.flowerclassifier.tflite.Classifier
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import java.io.InputStream


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class ClassifierTest {


    private val appContext = InstrumentationRegistry.getInstrumentation().targetContext
    private val testContext = InstrumentationRegistry.getInstrumentation().context


    @Test
    fun testClassifyImage(){
        val classifier = Classifier(
            appContext.assets,
            "flower_model.tflite",
            "labels.txt",
            224
        )
        val testImageStream: InputStream = appContext.assets.open("images/clematis_83.jpg")
        val testImage:Bitmap = BitmapFactory.decodeStream(testImageStream)
        val recognitions = classifier.recognizeImage(testImage)
        assertNotEquals(0, recognitions.size)
        assertEquals(recognitions[0].title, "clematis")
    }
}
