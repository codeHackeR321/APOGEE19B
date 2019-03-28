package com.anenigmatic.apogee19.screens.more.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.Fragment

class SponsorsFragment : Fragment() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootPOV = WebView(context).apply {
            settings.javaScriptEnabled = true
            loadUrl("https://bits-apogee.org/sponsors.html")
        }

        return rootPOV
    }
}