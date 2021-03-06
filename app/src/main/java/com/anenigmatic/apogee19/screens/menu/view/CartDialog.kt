package com.anenigmatic.apogee19.screens.menu.view

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.anenigmatic.apogee19.R
import com.anenigmatic.apogee19.screens.menu.core.CartViewModel
import com.anenigmatic.apogee19.screens.menu.core.CartViewModelFactory
import com.anenigmatic.apogee19.screens.menu.data.room.CartItem
import com.anenigmatic.apogee19.screens.shared.util.asMut
import kotlinx.android.synthetic.main.dia_cart.*
import kotlinx.android.synthetic.main.dia_cart.view.*
import kotlinx.android.synthetic.main.row_order_list.view.*
import java.util.*

class CartDialog: DialogFragment() {

    var trialList= ArrayList<String>()
    var currentContect : Context? = null
    val viewModel : CartViewModel by lazy {
        ViewModelProviders.of(this, CartViewModelFactory())[CartViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view  = inflater.inflate(R.layout.dia_cart, container, false)
        currentContect = view.context

        var cartAdapter= CartAdapter(listOf(), this)
        view!!.recyViewCart.apply {
            adapter= cartAdapter
            layoutManager= LinearLayoutManager(view!!.context)
        }

        viewModel.placeOrderStatus.observe( viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it) {
                Log.d("place order",it.toString())
                recyViewCart.visibility = View.GONE
                textViewTotal.visibility = View.GONE
                buttonPay.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
            } else {
                Log.d("place order",it.toString())
                recyViewCart.visibility = View.VISIBLE
                textViewTotal.visibility = View.VISIBLE
                buttonPay.visibility = View.VISIBLE
                progressBar.visibility = View.GONE

            }
        })

        viewModel.message.observe(this , androidx.lifecycle.Observer {
            Toast.makeText(context , it!! , Toast.LENGTH_LONG).show()
        })

        return view
    }



    override fun onStart() {
        cartLayout.minHeight = ((parentFragment!!.view!!.height)*0.85).toInt()
        cartLayout.minWidth = ((parentFragment!!.view!!.width)*0.85).toInt()// Height of dialog is 85% of the fragment width
        viewModel.getCartItems()

        viewModel.cartList.observe(this , androidx.lifecycle.Observer {
            var total = 0

            it.forEach {
                total += it.quantity*it.price
            }
            textViewTotal.text = "Total : \u20B9 $total"

            var cartAdapter= CartAdapter(it, this)
            view!!.recyViewCart.apply {
                adapter= cartAdapter
            }
        })

        view!!.closeDialog.setOnClickListener {
            dismiss()
        }

        view!!.buttonPay.setOnClickListener {
            viewModel.placeOrder()
        }

        super.onStart()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    fun removeItem(item : CartItem)
    {
        /**Think how to implement this function*/
        viewModel.removeItemFromCart(item)
        view!!.recyViewCart.adapter!!.notifyDataSetChanged()
    }

    /*trialList.add("Curry of life")
        trialList.add("Double chicken roll")
        trialList.add("Double chicken roll with")
        trialList.add("Double chicken roll with extra ")
        trialList.add("Double chicken roll cheese")
        trialList.add("Double chicken roll with")
        trialList.add("Double chicken roll with extra ")
        trialList.add("Double chicken roll cheese")*/



}