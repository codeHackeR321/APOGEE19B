package com.anenigmatic.apogee19.screens.more.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.anenigmatic.apogee19.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fra_map.view.*

class MapFragment : Fragment(), OnMapReadyCallback {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootPOV = inflater.inflate(R.layout.fra_map, container, false)

        (childFragmentManager.findFragmentById(R.id.mapFRA) as SupportMapFragment).getMapAsync(this)

        rootPOV.backBTN.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }



        return rootPOV
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        if(googleMap != null) {
            val locations = mapOf(
                "Rotunda" to LatLng(28.3633546, 75.5871163),
                "FD2 QT" to LatLng(28.364265, 75.588081),
                "FD1" to LatLng(28.364255, 75.589361),
                "FD3 QT" to LatLng(28.363734, 75.586000),
                "NAB Audi" to LatLng(28.362171, 75.587562),
                "Library" to LatLng(28.363734, 75.586000),
                "SR Grounds" to LatLng(28.365908, 75.587813),
                "LTC" to LatLng(28.365136, 75.590246)
            )

            locations.forEach {
                googleMap.addMarker(MarkerOptions().position(it.value).title(it.key)).showInfoWindow()
            }

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locations["Rotunda"], 17.0f))
        }
    }
}