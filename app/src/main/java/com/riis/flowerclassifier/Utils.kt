package com.riis.flowerclassifier

import android.graphics.Bitmap

class Utils {
    companion object {
        fun resizeToCenter(srcBmp: Bitmap, inputSize: Int): Bitmap {
            if (srcBmp.width >= srcBmp.height) {

                val finalBitmap = Bitmap.createBitmap(
                    srcBmp,
                    srcBmp.width / 2 - srcBmp.height / 2,
                    0,
                    srcBmp.height,
                    srcBmp.height
                )
                return Bitmap.createScaledBitmap(finalBitmap, inputSize, inputSize, false)

            } else {

                val finalBitmap = Bitmap.createBitmap(
                    srcBmp,
                    0,
                    srcBmp.height / 2 - srcBmp.width / 2,
                    srcBmp.width,
                    srcBmp.width
                )
                return Bitmap.createScaledBitmap(finalBitmap, inputSize, inputSize, false)
            }
        }
    }
}