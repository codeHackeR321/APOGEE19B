package com.anenigmatic.apogee19.screens.shared.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.anenigmatic.apogee19.R
import com.anenigmatic.apogee19.screens.events.view.EventListFragment
import com.anenigmatic.apogee19.screens.more.view.MoreFragment
import com.anenigmatic.apogee19.screens.profile.view.ProfileFragment
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter
import kotlinx.android.synthetic.main.act_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_main)

        if(savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.navHostFRM, EventListFragment())
                .commitNow()

            val eventsTabPosition = 3
            setupBottomNavigation(eventsTabPosition, true)
        } else {
            setupBottomNavigation(savedInstanceState.getInt("BOTTOM_NAV_CURRENT_ITEM"), false)
        }
    }

    override fun onSaveInstanceState(saveInstanceState: Bundle) {
        super.onSaveInstanceState(saveInstanceState)

        saveInstanceState.putInt("BOTTOM_NAV_CURRENT_ITEM", bottomNavAHB.currentItem)
    }

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount == 1) {
            bottomNavAHB?.colorize(3)
            bottomNavAHB?.setCurrentItem(3, false)
        }

        super.onBackPressed()
    }

    private fun setupBottomNavigation(currentItem: Int, useCallback: Boolean) {
        val navigationAdapter = AHBottomNavigationAdapter(this, R.menu.mn_bottom_nav)
        navigationAdapter.setupWithBottomNavigation(bottomNavAHB)

        bottomNavAHB.defaultBackgroundColor = ContextCompat.getColor(this, R.color.wht01)

        bottomNavAHB.titleState = AHBottomNavigation.TitleState.ALWAYS_HIDE

        bottomNavAHB.setOnTabSelectedListener { position, wasSelected ->

            // This removes all the Fragments except the EventListFragment(which is the root)
            supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

            if(!wasSelected) {
                when(position) {
                    0 -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.navHostFRM, ProfileFragment())
                            .addToBackStack(null)
                            .commitAllowingStateLoss()
                    }
                    1 -> {
                        Toast.makeText(this, "Menu", Toast.LENGTH_SHORT).show()
                    }
                    2 -> {
                        Toast.makeText(this, "Orders", Toast.LENGTH_SHORT).show()
                    }
                    3 -> {

                    }
                    4 -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.navHostFRM, MoreFragment())
                            .addToBackStack(null)
                            .commitAllowingStateLoss()
                    }
                }
            }
            bottomNavAHB.colorize(position)
            true
        }

        bottomNavAHB.setCurrentItem(currentItem, useCallback)
        bottomNavAHB.colorize(currentItem)
    }

    // Sets the bottom navigation's accent and inactive colors depending upon which is the current item.
    private fun AHBottomNavigation.colorize(currentItem: Int) {
        when(currentItem) {
            0 -> {
                bottomNavAHB.accentColor = ContextCompat.getColor(this@MainActivity, R.color.blu02)
                bottomNavAHB.inactiveColor = ContextCompat.getColor(this@MainActivity, R.color.blu01)
            }
            1 -> {

            }
            2 -> {

            }
            3 -> {
                bottomNavAHB.accentColor = ContextCompat.getColor(this@MainActivity, R.color.vio07)
                bottomNavAHB.inactiveColor = ContextCompat.getColor(this@MainActivity, R.color.pnk01)
            }
            4 -> {
                bottomNavAHB.accentColor = ContextCompat.getColor(this@MainActivity, R.color.yel02)
                bottomNavAHB.inactiveColor = ContextCompat.getColor(this@MainActivity, R.color.yel01)
            }
        }
    }
}
