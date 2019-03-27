package com.anenigmatic.apogee19.screens.menu.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.anenigmatic.apogee19.R
import com.anenigmatic.apogee19.screens.menu.data.room.Stall

class StallListAdapter(var dataset :List<Stall> , val fragment : StallListFragment) : RecyclerView.Adapter<StallListAdapter.MyViewHolder>() {

        class MyViewHolder(val view: View): RecyclerView.ViewHolder(view){

        var nameLBL: TextView =view.findViewById(R.id.nameLBL)
        var parent : ConstraintLayout = view.findViewById(R.id.rootPOV)

    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(com.anenigmatic.apogee19.R.layout.row_menu, parent,false) as View
        // set the view's size, margins, paddings and layout parameters

        return MyViewHolder(view)
    } // create a new view

    override fun getItemCount(): Int = dataset.size


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.nameLBL.text =  dataset[position].name
        holder.parent.setOnClickListener {
            Log.d("Test" , "onClick of menu item called ${dataset[position].stallId}")
            fragment.onStallSelected(dataset[position])
        }
    }



}