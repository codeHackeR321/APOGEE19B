package com.anenigmatic.apogee19.screens.menu.view

import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.anenigmatic.apogee19.R
import com.anenigmatic.apogee19.StateClass
import com.anenigmatic.apogee19.screens.login.view.ChooseLoginFragment
import com.anenigmatic.apogee19.screens.menu.core.StallsViewModel
import com.anenigmatic.apogee19.screens.menu.core.StallsViewModelFactory
import com.anenigmatic.apogee19.screens.menu.data.room.Stall
import com.anenigmatic.apogee19.screens.menu.data.room.StallItem
import kotlinx.android.synthetic.main.fra_stall_list.*
import kotlinx.android.synthetic.main.row_quantity_select.*

class StallListFragment : Fragment() {

    var currentContext : Context? = null
    private val model : StallsViewModel by lazy {
        ViewModelProviders.of(this, StallsViewModelFactory())[StallsViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view  = inflater.inflate(R.layout.fra_stall_list, container, false)

        if(activity!!.getSharedPreferences("apogee.sp", Context.MODE_PRIVATE).getString("JWT", null) == null) {
            activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.navHostFRM, ChooseLoginFragment())
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }

        currentContext = view.context
        return view
    }

    override fun onStart() {
        super.onStart()

        Log.e("Test" , "ViewModel Created")
        model.loadDataFromCache()
        model.cartItemsGet()

        // buttonCart.setColorFilter(view!!.resources.getColor(R.color.vio01),PorterDuff.Mode.SRC_IN)


        var connectivityManager : ConnectivityManager = currentContext!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo.isConnectedOrConnecting) {
            model.getStallListFromServer()
            model.stallList.observe(this@StallListFragment , Observer { updatedList ->
                Log.d("Test" , "Obsreved correctly $updatedList")
                recyViewMenu.apply {
                    adapter=StallListAdapter(updatedList.filter { !it.isClosed } , this@StallListFragment)
                    layoutManager=LinearLayoutManager(currentContext)
                }
                recyViewMenu.adapter!!.notifyDataSetChanged()
            })

            model.cartList.observe(this , Observer {
                if (it.size == 0)
                    cartStatus.visibility = View.INVISIBLE
                else
                    cartStatus.visibility = View.VISIBLE
            })

        } else {
            Toast.makeText(currentContext , "Please Check your Internet Connection" , Toast.LENGTH_LONG).show()
        }

        // to check the cart layout

        buttonCart.setOnClickListener { CartDialog().show(childFragmentManager , "CartDialog") }

        backButton.setOnClickListener {
            startAnimationForRecyclerView(true)
        }


    }

    /**
     * This function is called when the user clicks on one of the stalls in the list
     */

    fun onStallSelected(stall : Stall)
    {
        /**Code to start animation will come here*/
        /*recyViewMenu.visibility = View.INVISIBLE
        recyViewMenuItems.visibility = View.VISIBLE*/
        startAnimationForRecyclerView(false)
        model.getMenuListForStall(stall.stallId)
        model.menuList.observe(this@StallListFragment , Observer {stallList ->
            Log.d("Test" , "Observed correctly $stallList")
            recyViewMenuItems.apply {
                adapter = MenuListAdapter(stallList!!.filter { it.isAvailable } , this@StallListFragment)
                layoutManager = LinearLayoutManager(currentContext)
            }
            recyViewMenuItems.adapter!!.notifyDataSetChanged()
        })
        textStallNAme.text = stall.name
        //recyViewMenuItems.adapter!!.notifyDataSetChanged()
    }

    /**
     * This method is called to start the animation between recycler views when a stall is clicked
     */

    fun startAnimationForRecyclerView(isReverse : Boolean)
    {
        StateClass.state = 1
        if (!isReverse)
        {

            linearViewMenuItems.x = backgroundPatternIMG.width.toFloat()
            linearViewMenuItems.visibility = View.VISIBLE
            recyViewMenuItems.visibility=View.VISIBLE

            ObjectAnimator.ofFloat(recyViewMenu , "translationX" , -backgroundPatternIMG.width.toFloat()).apply {
                duration = 500
                start()
            }

            ObjectAnimator.ofFloat(linearViewMenuItems, "translationX" , (backgroundPatternIMG.width - linearViewMenuItems.width).toFloat()/128).apply {
                duration = 500
                start()
            }
            StateClass.state = 2

        }
        else
        {
            ObjectAnimator.ofFloat(linearViewMenuItems , "translationX" , backgroundPatternIMG.width.toFloat()).apply {
                duration = 500
                start()
            }

            recyViewMenu.visibility = View.VISIBLE

            ObjectAnimator.ofFloat(recyViewMenu, "translationX" , (backgroundPatternIMG.width - recyViewMenu.width).toFloat()/128).apply {
                duration = 500
                start()
            }
            StateClass.state = 0
        }

    }

    override fun onDestroyOptionsMenu() {
        if (recyViewMenuItems.isVisible)
        {
            startAnimationForRecyclerView(true)
            model.menuList.removeObservers(this)
        }

        else
            activity!!.onBackPressed()
    }

    fun showQuantitySelectDialog(item : StallItem)
    {
        var itemcount = 1
        var alertBox = AlertDialog.Builder(currentContext).setPositiveButton("Add" , DialogInterface.OnClickListener { dialog, which ->
                model.addItemToCart(item , itemcount)
            Toast.makeText(currentContext , "Added  $itemcount ${item.name} to cart successfully" , Toast.LENGTH_LONG).show()
            })
            .setNegativeButton("Cancel" , DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
            })
            .setTitle(item.name)
            .setView(R.layout.row_quantity_select)
            .create()
        alertBox.show()
        alertBox.AddButton.setOnClickListener {
            alertBox.TextQuantity.text  = (Integer.parseInt(alertBox.TextQuantity.text.toString()) + 1).toString()
            itemcount = Integer.parseInt(alertBox.TextQuantity.text.toString())
        }

        alertBox.SubtractButton.setOnClickListener {
            if (Integer.parseInt(alertBox.TextQuantity.text.toString()) > 1)
            {
                alertBox.TextQuantity.text  = (Integer.parseInt(alertBox.TextQuantity.text.toString()) - 1).toString()
                itemcount = Integer.parseInt(alertBox.TextQuantity.text.toString())
            }
            else
            {
                Toast.makeText(currentContext , "Atleast One item must be selected" , Toast.LENGTH_LONG).show()
                itemcount = Integer.parseInt(alertBox.TextQuantity.text.toString())
            }
        }

    }



}