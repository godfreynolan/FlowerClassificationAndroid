package com.riis.flowerclassifier.fragments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.riis.flowerclassifier.R
import com.riis.flowerclassifier.adapter.ImageItemAdapter
import com.riis.flowerclassifier.model.ImageItem
import com.riis.flowerclassifier.tflite.Classifier
import java.io.InputStream

class FragmentSample: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sample, container, false)

        // Sets up the classifier, recycler view, and labels
        val classifier = Classifier(context!!.assets, context!!.getString(R.string.model_name), context!!.getString(R.string.label_name), 224)
        val imageListView: RecyclerView = view.findViewById(R.id.image_list_view)
        val items = getImageItems()

        val adapter = ImageItemAdapter(items, classifier)

        val staggeredGridLayoutManager = GridLayoutManager(
            activity?.applicationContext,
            2,
            GridLayoutManager.VERTICAL,
            false
        )

        imageListView.layoutManager = staggeredGridLayoutManager
        imageListView.adapter = adapter

        return view
    }

    // Adds all text in the labels file to the list
    private fun getImageItems(): MutableList<ImageItem>{
        val items = mutableListOf<ImageItem>()
        val fileNames = context!!.assets.list("images")
        if (fileNames != null) {
            for (name in fileNames){
                if(name.matches(context!!.getString(R.string.sample_image_type).toRegex())){
                    val stream: InputStream = context!!.assets.open("images/$name")
                    val image: Bitmap = BitmapFactory.decodeStream(stream)
                    val item = ImageItem(image)
                    items.add(item)
                }
            }
        }
        return items
    }
}