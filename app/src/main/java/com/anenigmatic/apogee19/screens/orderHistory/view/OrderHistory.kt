package com.anenigmatic.apogee19.screens.orderHistory.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.anenigmatic.apogee19.R
import com.anenigmatic.apogee19.screens.login.view.ChooseLoginFragment
import com.anenigmatic.apogee19.screens.menu.data.room.OrderItem
import com.anenigmatic.apogee19.screens.orderHistory.core.OrderHistoryViewModel
import com.anenigmatic.apogee19.screens.orderHistory.core.OrderHistoryViewModelFactory
import com.example.manish.apogeewallet.screens.menu.data.room.PastOrder
import kotlinx.android.synthetic.main.fra_order_history.*
import kotlinx.android.synthetic.main.fra_order_history.view.*

class OrderHistory : Fragment() {

    private var currentContext : Context? = null
    val model: OrderHistoryViewModel by lazy {
        ViewModelProviders.of(this, OrderHistoryViewModelFactory())[OrderHistoryViewModel::class.java]
    }
    var observer : Observer<List<OrderItem>>? = null
    var list : ArrayList<PastOrder> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fra_order_history, container, false)

        if(activity!!.getSharedPreferences("apogee.sp", Context.MODE_PRIVATE).getString("JWT", null) == null) {
            activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.navHostFRM, ChooseLoginFragment())
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }

        view.recyViewMenu.adapter = OrderHistoryAdapter(this)

        currentContext = view.context
        return view
    }

    override fun onStart() {

        //TODO To be checked
        if (observer != null)
            model.orderItemList.removeObserver(observer!!)

        model.getOrderListFromServer()

        model.orderList.observe(this, Observer {

            Log.d("Testing","model.orderList observed $it:")
            (recyViewMenu.adapter as OrderHistoryAdapter).dataset = it.reversed()

            list.clear()
            list.addAll(ArrayList(it))
        })

        model.otpStatus.observe(this , Observer {
            if (it)
            {
                progressBarHistory.bringToFront()
                progressBarHistory.visibility = View.VISIBLE
                activity!!.window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE , WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
            else
            {
                progressBarHistory.visibility = View.GONE
                activity!!.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
        })

        model.message.observe(this , Observer {
            Toast.makeText(context , it , Toast.LENGTH_LONG).show()
        })

        super.onStart()
    }

    fun onOrderClicked(orderId : Int , position : Int)
    {

        //TODO To be checked
        if (observer != null)
            model.orderItemList.removeObserver(observer!!)

        Log.d("Testing","onOrderClicked invoked")
        //To be implemented later
        model.getOrderListForOrder(orderId)

        observer = Observer {

            Log.d("Test" , "Array List $it")
            var name = "Domino's"
            var totalPrice = 0
            it.forEach {item ->
                totalPrice += item.price * item.quantity
            }

            OrderDetailDialog().apply {
                arguments = bundleOf(
                    "Stall Name" to list[position].name,
                    "Total Price" to totalPrice,
                    "OTP" to list[position].otp,
                    "Status" to list[position].status ,
                    "Order List" to it.map { item -> "${item.orderId}<|>${item.name}<|>${item.price}<|>${item.quantity}" }
                )
            }.show(childFragmentManager , "OrderDetailDialog")
        }
        model.orderItemList.observe(this, observer!!)

    }

    override fun onResume() {

        //TODO To be checked
        if (observer != null)
            model.orderItemList.removeObserver(observer!!)
        super.onResume()
    }

}
