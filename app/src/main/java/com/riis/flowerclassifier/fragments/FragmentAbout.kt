package com.riis.flowerclassifier.fragments

import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.riis.flowerclassifier.R

class FragmentAbout(): Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_about, container, false)

        val dataset = view.findViewById<TextView>(R.id.dataset)
        val github = view.findViewById<TextView>(R.id.github)
        dataset.autoLinkMask = 0
        github.autoLinkMask = 0

        val datasetHtml: Spanned = Html.fromHtml(getString(R.string.about_DataSet))
        dataset.text = datasetHtml
        dataset.movementMethod = LinkMovementMethod.getInstance()

        val githubHtml: Spanned = Html.fromHtml(getString(R.string.about_GitHub))
        github.text = githubHtml
        github.movementMethod = LinkMovementMethod.getInstance()

        return view
    }
}