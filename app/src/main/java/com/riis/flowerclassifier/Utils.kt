package com.riis.flowerclassifier

import android.graphics.Bitmap

class Utils {
    companion object{
        fun resize(image: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
            var temp = image
            //checks if the maxWidth and height are less than 0
            return if (maxHeight > 0 && maxWidth > 0) {
                val width = temp.width
                val height = temp.height

                //gets the ratio of width to height
                val ratioBitmap = width.toFloat() / height.toFloat()
                val ratioMax = maxWidth.toFloat() / maxHeight.toFloat()

                //saves the maxWidth and maxHeight for use for the if statement output
                var finalWidth = maxWidth
                var finalHeight = maxHeight

                //checks the ratio and determines what width and height to use
                if (ratioMax > ratioBitmap) {
                    finalWidth = (maxHeight.toFloat() * ratioBitmap).toInt()
                } else {
                    finalHeight = (maxWidth.toFloat() / ratioBitmap).toInt()
                }
                //creates the scaled down version for the tflite model
                temp = Bitmap.createScaledBitmap(temp, maxWidth, maxHeight, false)
                temp
            } else {
                image
            }
        }
    }
}