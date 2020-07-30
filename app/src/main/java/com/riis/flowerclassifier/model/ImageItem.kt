package com.riis.flowerclassifier.model

import android.graphics.Bitmap

class ImageItem(var image:Bitmap){
    var label:String = ""
    var confidence:Float = 0.0f

    fun getTitle():String{
        if(label != ""){
            val percent = confidence * 100
            val percentString = String.format("%.2f", percent)
            return "$label $percentString%"
        }
        else{
            return "Click to classify"
        }
    }
}