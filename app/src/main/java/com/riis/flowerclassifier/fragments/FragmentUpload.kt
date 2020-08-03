package com.riis.flowerclassifier.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.riis.flowerclassifier.R
import com.riis.flowerclassifier.Utils
import com.riis.flowerclassifier.model.ImageItem
import com.riis.flowerclassifier.tflite.Classifier
import java.io.InputStream

class FragmentUpload: Fragment() {
    companion object {
        private const val REQUEST_CODE = 100
        private const val INPUT_SIZE = 224
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_upload, container, false)

        // Gets id of the FloatingActionButton to upload photos from the gallery
        val fab = view.findViewById<FloatingActionButton>(R.id.fab)

        fab.setOnClickListener{
            // Opens gallery when the button is clicked
            openGallery()
        }

        return view
    }

    private fun openGallery(){
        // Creates intent for selecting an image from the gallery
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"

        // Starts the activity
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // If the request Code is correct and the activity completed correctly
        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            // Gets the uri of the selected image
            val uri = data?.data

            //gets all necessary elements from the layout
            val imageView = view!!.findViewById<ImageView>(R.id.uploadedImage)
            val directionsTextView = view!!.findViewById<TextView>(R.id.directionsTextView)
            val uploadItem = view!!.findViewById<View>(R.id.includeUpload)

            // Sets the image view to the selected image
            imageView.setImageURI(data?.data)

            // Makes the image and text visible and hides the direction text
            directionsTextView.visibility = View.INVISIBLE
            uploadItem.visibility = View.VISIBLE

            //creates an input stream from the selected image's URI
            val inputStream: InputStream? = context!!.contentResolver.openInputStream(uri!!)
            //creates a bitmap of the image
            val image: Bitmap = BitmapFactory.decodeStream(inputStream)
            val item = ImageItem(Utils.resizeToCenter(image, INPUT_SIZE))
            startClassifier(item)

        }

    }

    private fun startClassifier(item: ImageItem){
        val textView = view?.findViewById<TextView>(R.id.uploadedTextView)
        //loads the tflite and label files
        val classifier = Classifier(activity!!.assets, context!!.getString(R.string.model_name), context!!.getString(R.string.label_name), 224)
        val recognition = classifier.recognizeImage(item.image)

        //displays the dog title and confidence
        if(recognition.isNotEmpty()){
            item.confidence = recognition[0].confidence
            item.label = recognition[0].title
            textView!!.text = item.getTitle()
        } else {
            textView!!.text = getString(R.string.unknown)
        }
    }
}