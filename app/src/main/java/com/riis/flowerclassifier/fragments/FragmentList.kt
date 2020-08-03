package com.riis.flowerclassifier.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.riis.flowerclassifier.R
import com.riis.flowerclassifier.adapter.LabelListAdapter
import java.io.IOException

class FragmentList(): Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        // Gets the id of the recycler view
        val recyclerView = view.findViewById<RecyclerView>(R.id.labelListRecyclerView)

        try {
            // Opens the labels file from the assets folder and puts it into a List
            val labelArr =
                context!!.assets.open(context!!.getString(R.string.label_name)).bufferedReader().useLines { it.toList() }
            // Creates the adapter with the labels
            val adapter = LabelListAdapter(labelArr)

            // Sets up the recycler view
            val layoutManager = LinearLayoutManager(activity!!.applicationContext)
            recyclerView.addItemDecoration(DividerItemDecoration(activity!!.applicationContext, LinearLayoutManager.VERTICAL))
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = adapter

        } catch (e: IOException){
            Log.i("Error", e.localizedMessage)
        }
        return view
    }
}