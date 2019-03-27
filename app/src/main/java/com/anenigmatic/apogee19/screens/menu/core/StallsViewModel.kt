package com.anenigmatic.apogee19.screens.menu.core

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anenigmatic.apogee19.ApogeeApp
import com.anenigmatic.apogee19.screens.menu.data.MenuRepository
import com.anenigmatic.apogee19.screens.menu.data.MenuRepositoryImpl
import com.anenigmatic.apogee19.screens.menu.data.room.CartItem
import com.anenigmatic.apogee19.screens.menu.data.room.Stall
import com.anenigmatic.apogee19.screens.menu.data.room.StallItem
import com.anenigmatic.apogee19.screens.menu.view.StallListFragment
import com.anenigmatic.apogee19.screens.shared.util.asMut

class StallsViewModel(private val repository: MenuRepository) : ViewModel()
{
    var stallList : LiveData<List<Stall>> = MutableLiveData()
    var menuList : LiveData<List<StallItem>> = MutableLiveData()
    var cartList : LiveData<List<CartItem>> = MutableLiveData()

    init
    {
       /* Log.d("Test" , "Entered init")
        try {
            repository = MenuRepositoryImpl(instance.currentContext!!)
        }
        catch (e : Exception)
        {
            repository = MenuRepositoryImpl(instance.currentContext!!).getInstance()
        }*/
        Log.e("Test" , "Repository Created")
        repository.listenStatus()
        //repository = MenuRepositoryImpl(instance.currentContext!!)
    }

    /**
     * Makes the REST Api call through the repository to get new data regarding the list of stalls and their menus
     */
    fun getStallListFromServer()
    {
        Log.d("Test" , "Entered getStallList of view model")
        repository.refreshStallAndMenu()
    }

    /**
     * Till the time new data arises, display data that is displayed in the cache
     */
    fun loadDataFromCache()
    {
        stallList = repository.getStalls()
    }

    /**Once data has been added to room this method updates the live data variable so that if there are any changes, they can be reflected*/
    fun onStallDataAddedToLocalDatabase(list : List<Stall>)
    {
        Log.d("Test" , "onStallDataAdded called $list")
        stallList.asMut().value = list
    }

    /**Runs a Query on the room database to fetch the menu of the corresponding stall is*/

    fun getMenuListForStall(stall_id : Int)
    {
        menuList = repository.getMenu(stall_id)
    }

    fun addItemToCart(item : StallItem , quantity : Int)
    {
        repository.addItemToCart(item , quantity)
    }

    fun cartItemsGet()
    {
        cartList =  repository.getCartItems()
    }


}