package com.anenigmatic.apogee19.screens.menu.data

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import com.anenigmatic.apogee19.screens.menu.data.retrofit.*
import com.anenigmatic.apogee19.screens.menu.data.room.*
import com.anenigmatic.apogee19.screens.shared.data.room.AppDatabase
import com.anenigmatic.apogee19.screens.menu.data.retrofit.StallAndMenu
import com.example.manish.apogeewallet.screens.menu.data.room.PastOrder
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuRepositoryImpl(private val prefs: SharedPreferences, database: AppDatabase, private val apiService: StallsAndMenuApi) : MenuRepository {

    var stallDao = database.stallDao()
    var stallItemDao = database.stallItemDao()
    var cartItemDao = database.cartItemDao()
    var pastOrderDao = database.pastOrderDao()
    var pastOrderItemDao = database.pastOrderItemsDao()

   /* @Volatile
    private var soleInstance : MenuRepositoryImpl? = null



    fun getInstance(context: Context): MenuRepositoryImpl {
        //Double check locking pattern
        if (soleInstance == null) { //Check for the first time

            synchronized(MenuRepositoryImpl::class.java) {
                //Check for the second time.
                //if there is no instance available... create new one
                if (soleInstance == null) soleInstance = MenuRepositoryImpl(context)
            }
        }

        return soleInstance!!
    }*/

    fun getJwt(): String? {
        return prefs.getString("JWT", null)
    }

    override fun getStalls(): LiveData<List<Stall>> {
        return stallDao.getAll()
    }

    override fun getMenu(stall: Stall): LiveData<List<StallItem>> {
        return getMenu(stall.stallId)
    }

    override fun getMenu(stallId: Int): LiveData<List<StallItem>> {
        // Log.d("Test" , "Database Data = ${stallItemDao.getStallItemsList(stallId)}")
        return stallItemDao.getStallItems(stallId)
    }

    override fun refreshStallAndMenu() {
        var call = apiService.getStallsAndMenu()
        Log.d("Apogee19Log", "refreshStallAndMenu()")
        call.enqueue(object : Callback<List<StallAndMenu>> {

            override fun onResponse(call: Call<List<StallAndMenu>>, response: Response<List<StallAndMenu>>) {

                if(!response.isSuccessful){
                    Log.d("Menu call response code", response.code().toString())
                    return
                }

                stallDao.deleteAll()
                stallItemDao.deleteAll()

                var responseBody = response.body()
                var stallList : ArrayList<Stall> = ArrayList(responseBody!!.size)
                var menuList : ArrayList<StallItem> = ArrayList(responseBody.size)
                Log.d("Test" , "Size of response = $responseBody")
                responseBody.forEach {
                    stallList.add(Stall(it.stallId , it.name , it.description , it.isClosed))
                    it.menu.forEach { stallItem ->
                        menuList.add(StallItem(stallItem.itemId , stallItem.stallId, stallItem.name, stallItem.price , stallItem.isAvailable))
                    }
                }

                Log.d("Test" , "Pass variable = $stallList")
                Log.d("Test" , "Pass2 variable = $menuList")

                stallDao.insertAll(stallList)
                stallItemDao.insertAll(menuList)

            }
            override fun onFailure(call: Call<List<StallAndMenu>>, t: Throwable) {
                Log.d("Menu call response" , "" + t.message.toString())
            }

        })
    }

    override fun addItemToCart(stallItem: StallItem, quantity: Int) {
        var cartItem = CartItem(stallItem.itemId, stallItem.stallId, stallItem.name, stallItem.price, quantity)
        addItemToCart(cartItem)
    }

    override fun addItemToCart(newCartItem: CartItem) {
        //TODO: Make it more efficient
        //checks of the current item already exists in the cart and updates it if so
        var cartItemList = cartItemDao.getCart()
        if( cartItemList.any { it.itemId == newCartItem.itemId }) {
            var cartItem = cartItemList.find { it.itemId == newCartItem.itemId }
            cartItemDao.changeQuantity(cartItem!!.itemId, cartItem!!.quantity + newCartItem.quantity)
            return
        }

        cartItemDao.insertCartItem(newCartItem)

    }

    override fun getCartItems(): LiveData<List<CartItem>> {
        return cartItemDao.getAll()
    }

    override fun placeOrder() {
//        var cartItemList = cartItemDao.getCart()
//        var requestBody = JSONObject()
//        requestBody.put("orderdict", JSONObject())
//
//        for(cartItem in cartItemList) {
//            var stallId = cartItem.stallId.toString()
//            if(!requestBody.has(stallId))
//                requestBody.getJSONObject("orderdict").put(stallId,JSONObject())
//            var itemId = cartItem.itemId.toString()
//            requestBody.getJSONObject("orderdict").getJSONObject(stallId).put(itemId, cartItem.quantity)
//        }

        var cartItemList = cartItemDao.getCart()
        var orderItems = HashMap<String,HashMap<String,Int>>()
        var cartItemStallId = ""
        for (cartItem in cartItemList) {
            cartItemStallId = cartItem.stallId.toString()
            if (!orderItems.containsKey(cartItemStallId)) {
                orderItems.put(cartItemStallId,HashMap())
            }
            orderItems.get(cartItemStallId)!!.put(cartItem.itemId.toString(),cartItem.quantity)
        }

        val cartOrder = CartOrder(orderItems)
        Log.d("Test","Cart order: $cartOrder")

        var responseCode = -1
        getJwt()?.let { jwt ->
            var call = apiService.placeOrder(jwt, cartOrder)
            call.enqueue(object : Callback<OrderComfirmation> {

                override fun onResponse(call: Call<OrderComfirmation>, response: Response<OrderComfirmation>) {
                    Log.d("Test",call.request().body().toString())
                    responseCode = response.code()
                    if (responseCode == 200)
                    {
                        cartItemDao.deleteAll()
                        Log.d("Test" , "Order Placed Sucessfully")
                    }
                    else
                        Log.d("Test" , "Order Placed UnSucessfully $responseCode error ${response.errorBody()?.string()}")
                }

                override fun onFailure(call: Call<OrderComfirmation>, t: Throwable) {
                    Log.d("Test" , "Order Placed UnSucessfully $t")
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

            })
        }
    }

    override fun deleteCartItem(item: CartItem) {
        cartItemDao.delete(item)
    }

    override fun getOrders(): LiveData<List<PastOrder>> {

        return pastOrderDao.getAll()
    }

    override fun getOrderItems(orderId: Int): LiveData<List<OrderItem>> {

        return pastOrderItemDao.getPastOrderItemsList(orderId)
    }

    override fun changeOrderOtpStatus(orderId: Int) {

        val requestBody = JSONObject().apply {
            put("order_id", orderId)
        }.toRequestBody()

        getJwt()?.let { jwt ->
            apiService.requestOtpSeen(jwt, requestBody)
                .enqueue(object : Callback<Unit> {

                    override fun onResponse(call: Call<Unit>, response: Response<Unit>) {

                        if(response.code() == 200) {
                            pastOrderDao.changeOtpSeenStatus(orderId, true)
                        }
                    }

                    override fun onFailure(call: Call<Unit>, t: Throwable) {

                        Log.d("OTP Response"," ${t.message}")
                    }
                })
        }
    }

    override fun changeOrderStatus(orderId: Int, status: String) {

        pastOrderDao.changeStatus(orderId, status)
    }

    override fun refreshPastOrders() {

        getJwt()?.let { jwt ->
            var call = apiService.getPastOrders(jwt)
            call.enqueue(object : Callback<List<OrderShell>> {

                override fun onResponse(call: Call<List<OrderShell>>, response: Response<List<OrderShell>>) {

                    if(!response.isSuccessful){
                        Log.d("Menu call response code", response.code().toString())
                        return
                    }

                    pastOrderDao.deleteAll()
                    pastOrderItemDao.deleteAll()

                    var responseBody = response.body()
                    var orderList : ArrayList<PastOrder> = ArrayList(responseBody!!.size)
                    var responseOrderItemList : ArrayList<ResponseOrderItem> = ArrayList(responseBody.size)
                    val orderItemList: ArrayList<OrderItem> = ArrayList(responseBody.size)

                    Log.d("Test" , "Size of response = $responseBody")
                    try {

                        responseBody.forEach {orderShell ->

                            orderShell.order.forEach {order ->

                                order.menu.forEach {
                                    responseOrderItemList.add(ResponseOrderItem(it.itemId, it.name, it.price, it.quantity))
                                    orderItemList.add(OrderItem(0, order.orderId, orderShell.shellId, it.itemId,
                                        it.name, it.price, it.quantity ))
                                }
                                orderList.add(PastOrder(order.orderId , order.vendor.name , order.price, order.otp, order.status, order.showotp))

                            }

                        }

                        Log.d("Test" , "Pass variable = $orderList")
                        Log.d("Test" , "Pass2 variable = $responseOrderItemList")

                        pastOrderDao.insertPastOrders(orderList)
                        pastOrderItemDao.insertOrderItem(orderItemList)

                    }
                    catch (e: Exception){

                        Log.d("Test","${e.message}")
                    }

                }
                override fun onFailure(call: Call<List<OrderShell>>, t: Throwable) {
                    Log.d("Menu call response" , "" + t.message.toString())
                }
            })
        }
    }

    fun JSONObject.toRequestBody(): RequestBody {
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), this.toString())
    }
}