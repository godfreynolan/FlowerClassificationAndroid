package com.riis.flowerclassifier.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.riis.flowerclassifier.model.ImageItem
import  androidx.recyclerview.widget.RecyclerView
import com.riis.flowerclassifier.R
import com.riis.flowerclassifier.tflite.Classifier

class ImageItemAdapter(val imageItems: List<ImageItem>, val classifier: Classifier):
    RecyclerView.Adapter<ImageItemAdapter.ViewHolder>() {


    inner class ViewHolder(imageItemView: View) : RecyclerView.ViewHolder(imageItemView), View.OnClickListener {
        val sampleNameView = itemView.findViewById<TextView>(R.id.sampleName)
        val sampleImageView = itemView.findViewById<ImageView>(R.id.sampleImage)
        init{
            itemView.setOnClickListener(this)
        }
        // when the user clicks the image display the confidence and the label.
        override fun onClick(view: View?) {
            if(adapterPosition != RecyclerView.NO_POSITION){
                val item: ImageItem = imageItems[adapterPosition]
                val recognitions = classifier.recognizeImage(item.image)
                Log.d("Recognition(Classifier)", recognitions.isEmpty().toString())
                if(recognitions.isNotEmpty()){
                    item.confidence = recognitions[0].confidence
                    item.label = recognitions[0].title
                } else {
                    item.label = view!!.resources.getString(R.string.unknown)
                    item.confidence = 0f
                }
                notifyItemChanged(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val contactView = inflater.inflate(R.layout.image_item, parent, false)
        // Return a new holder instance
        return ViewHolder(contactView)
    }


    // Returns the total count of items in the list
    override fun getItemCount(): Int {
        return imageItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.sampleNameView.text = imageItems[position].getTitle()
        holder.sampleImageView.setImageBitmap(imageItems[position].image)
    }
}