package com.riis.flowerclassifier.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.riis.flowerclassifier.R

class LabelListAdapter(private val data: List<String>): RecyclerView.Adapter<LabelListAdapter.ListItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemHolder {
        // Initializes the recycler view with the correct layout for each recycler item
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_list_item, parent, false)
        return ListItemHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: LabelListAdapter.ListItemHolder, position: Int) {
        // Sets the text in the textView to the position in the array
        holder.label.text = data[position]
    }

    inner class ListItemHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener{
        // Gets the id of the textView in fragment_list_item layout
        internal var label = view.findViewById<View>(R.id.labelView) as TextView

        override fun onClick(v: View?) {

        }
    }
}