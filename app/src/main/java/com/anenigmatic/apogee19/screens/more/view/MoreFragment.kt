package com.anenigmatic.apogee19.screens.more.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.anenigmatic.apogee19.R
import kotlinx.android.synthetic.main.fra_more.view.*

class MoreFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootPOV = inflater.inflate(R.layout.fra_more, container, false)

        rootPOV.showContactsBTN.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.navHostFRM, ContactsFragment())
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }

        rootPOV.showAboutBTN.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.navHostFRM, AboutFragment())
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }

        rootPOV.showMapBTN.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.navHostFRM, MapFragment())
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }

        rootPOV.showSponsorsBTN.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.navHostFRM, SponsorsFragment())
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }

        rootPOV.showEpcBlogBTN.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.navHostFRM, EpcBlogFragment())
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }

        return rootPOV
    }
}