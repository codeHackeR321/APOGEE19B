package com.anenigmatic.apogee19.screens.more.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.anenigmatic.apogee19.R
import kotlinx.android.synthetic.main.fra_contacts.view.*

class ContactsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootPOV = inflater.inflate(R.layout.fra_contacts, container, false)

        rootPOV.contactsRCY.adapter = ContactsAdapter()

        rootPOV.backBTN.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        return rootPOV
    }
}