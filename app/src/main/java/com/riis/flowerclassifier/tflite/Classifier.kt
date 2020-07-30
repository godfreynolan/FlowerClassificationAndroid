package com.riis.flowerclassifier.tflite

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.util.Log
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.util.ArrayList
import java.util.PriorityQueue
import kotlin.Comparator
import kotlin.math.min

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class Classifier(assetManager: AssetManager, modelPath: String, labelPath: String, inputSize: Int) {
    private var interpreter: Interpreter
    private var labelList: List<String>
    private val INPUT_SIZE: Int = inputSize
    private val PIXEL_SIZE: Int = 3
    private val IMAGE_MEAN:Float = 128.0f
    private val IMAGE_STD:Float = 128.0f
    private val MAX_RESULTS = 1
    private val THRESHOLD = 0.3f // 30% confidence

    data class Recognition(
        var id: String = "",
        var title: String = "",
        var confidence: Float = 0F
    ) {
        override fun toString(): String {
            return "Title = $title, Confidence = $confidence)"
        }
    }

    init {
        val tfliteOptions = Interpreter.Options()
        tfliteOptions.setNumThreads(4)
        tfliteOptions.setUseNNAPI(true)
        interpreter = Interpreter(loadModelFile(assetManager, modelPath),tfliteOptions)
        labelList = loadLabelList(assetManager, labelPath)
    }

    private fun loadModelFile(assetManager: AssetManager, modelPath: String): MappedByteBuffer {
        val fileDescriptor = assetManager.openFd(modelPath)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    private fun loadLabelList(assetManager: AssetManager, labelPath: String): List<String> {
        return assetManager.open(labelPath).bufferedReader().useLines { it.toList() }

    }

    fun recognizeImage(bitmap: Bitmap): List<Recognition> {
        // scale the bitmap to the input size of our input tensor
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, false)
        val byteBuffer = convertBitmapToByteBuffer(scaledBitmap)
        // Float array because were using a non Quantized Model
        val result = Array(1) { FloatArray(labelList.size) }
        // run the model
        interpreter.run(byteBuffer, result)

        return getSortedResult(result)
    }


    /** Writes Image data into a `ByteBuffer`.  */
    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        // This application uses a float model which means that we need RGB data for each pixel
        // in floats. INPUT_SIZE * INPUT_SIZE for the number of pixels in the image
        // PIXEL size for the number of color channels in this case 3 for RGB
        // And * 4 because a float takes 4 bytes.
        val imgData = ByteBuffer.allocateDirect(INPUT_SIZE * INPUT_SIZE * PIXEL_SIZE * 4)
        imgData.order(ByteOrder.nativeOrder())
        val intValues = IntArray(INPUT_SIZE * INPUT_SIZE)

        imgData.rewind()
        // stores the bitmap data into the int array
        // Each pixel is represented as an integer in the intValues array.
        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        for (i in 0 until INPUT_SIZE) {
            for (j in 0 until INPUT_SIZE) {
                val pixelValue:Int = intValues[i * INPUT_SIZE + j] // value of the current pixel as an int
                // Extract the RGB data from the integer value of the pixel as floats.
                // Takes the binary data of the integer for the given pixel and extracts RGB float data.
                imgData.putFloat(((pixelValue shr 16 and 0xFF) - IMAGE_MEAN) / IMAGE_STD)
                imgData.putFloat(((pixelValue shr 8 and 0xFF) - IMAGE_MEAN) / IMAGE_STD)
                imgData.putFloat(((pixelValue and 0xFF) - IMAGE_MEAN) / IMAGE_STD)
            }
        }
        return imgData
    }


    private fun getSortedResult(labelProbArray: Array<FloatArray>): List<Recognition> {
        // Priority Queue Used to sort the results to get the largest probability.
        val pq = PriorityQueue(
            MAX_RESULTS,
            Comparator<Recognition> { (_, _, confidence1), (_, _, confidence2)
                ->
                confidence1.compareTo(confidence2) * -1
            })

        // Loop through our probability list of labels and only keep results that are
        // greater than the threshold
        for (i in labelList.indices) {
            val confidence = labelProbArray[0][i]
            if (confidence >= THRESHOLD) {
                Log.d("confidence value:", "" + confidence)
                pq.add(
                    Recognition(
                        "" + i,
                        if (labelList.size > i) labelList[i] else "Unknown",
                        confidence
                    )
                )
            }
        }
        // take only the max number of results and put it into or array
        val recognitions = ArrayList<Recognition>(MAX_RESULTS)
        val recognitionsSize = min(pq.size, MAX_RESULTS)
        for (i in 0 until recognitionsSize) {
            recognitions.add(pq.poll())
        }
        // recognitions are a string containing the label and the confidence
        Log.d("Recognition", recognitions.isEmpty().toString())
        return recognitions
    }

}