package com.anenigmatic.apogee19.screens.menu.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.anenigmatic.apogee19.R
import com.anenigmatic.apogee19.screens.menu.data.room.StallItem

class MenuListAdapter(var list : List<StallItem> , var fragment : StallListFragment) : RecyclerView.Adapter<MenuListAdapter.MyViewHolder>()
{
    class MyViewHolder(val view: View): RecyclerView.ViewHolder(view){

        var nameLBL: TextView =view.findViewById(com.anenigmatic.apogee19.R.id.nameLBL)
        var infoLBL: TextView =view.findViewById(com.anenigmatic.apogee19.R.id.infoLBL)
        var starBTN : ImageView= view.findViewById(com.anenigmatic.apogee19.R.id.starBTN)
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(com.anenigmatic.apogee19.R.layout.row_menu_item, parent,false) as View
        // set the view's size, margins, paddings and layout parameters

        return MyViewHolder(view)
    } // create a new view

    override fun getItemCount(): Int = list.size


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        /**
         * Since recycler view works in a way such that it only recycles the information that is displayed in the views
         * of the recycler view, images of groups are also visible as currently we had not specified what image to overwrite
         * the previous image
         */

        holder.nameLBL.text = list[position].name /*"${list[position].name}\n${list[position].price}"*/
        holder.infoLBL.text= list[position].price.toString()
        holder.starBTN.setImageResource(R.drawable.ic_add)
        holder.starBTN.setOnClickListener{
            fragment.showQuantitySelectDialog(list[position])
        }

    }
}