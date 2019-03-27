package com.anenigmatic.apogee19.screens.menu.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.anenigmatic.apogee19.R
import com.anenigmatic.apogee19.screens.menu.data.room.CartItem
import com.anenigmatic.apogee19.screens.menu.data.room.StallItem

class CartAdapter(var list : List<CartItem> , var fragment : CartDialog) : RecyclerView.Adapter<CartAdapter.MyViewHolder>()
{
    class MyViewHolder(val view: View): RecyclerView.ViewHolder(view){

        var nameLBL: TextView =view.findViewById(com.anenigmatic.apogee19.R.id.nameLBL)
        var infoLBL: TextView =view.findViewById(com.anenigmatic.apogee19.R.id.infoLBL)
        var minusButton : ImageButton = view.findViewById(R.id.removeItem)

    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(com.anenigmatic.apogee19.R.layout.row_cart_items, parent,false) as View
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
       holder.nameLBL.setHorizontallyScrolling(true)
        holder.nameLBL.maxLines=1

        holder.nameLBL.text = list[position].name /*"${list[position].name}\n${list[position].price}"*/
        holder.infoLBL.text= "${list[position].price} X ${list[position].quantity}"

        holder.minusButton.setOnClickListener {
            fragment.removeItem(list[position])
        }

    }
}