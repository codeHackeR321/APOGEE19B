package com.anenigmatic.apogee19.screens.login.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.anenigmatic.apogee19.R
import com.anenigmatic.apogee19.screens.login.core.OutsteeLoginViewModel
import com.anenigmatic.apogee19.screens.login.core.OutsteeLoginViewModel.UiOrder
import com.anenigmatic.apogee19.screens.login.core.OutsteeLoginViewModelFactory
import kotlinx.android.synthetic.main.fra_outstee_login.view.*

class OutsteeLoginFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProviders.of(this, OutsteeLoginViewModelFactory())[OutsteeLoginViewModel::class.java]
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootPOV = inflater.inflate(R.layout.fra_outstee_login, container, false)

        rootPOV.loginBTN.setOnClickListener {
            val username = rootPOV.usernameTXT.text.toString()
            val password = rootPOV.passwordTXT.text.toString()

            viewModel.onLoginAction(username, password)
        }

        viewModel.orderData.observe(viewLifecycleOwner, Observer { order ->
            when(order) {
                is UiOrder.ShowLoadingState -> showLoadingState()
                is UiOrder.ShowWorkingState -> showWorkingState()
                is UiOrder.GoToChooseAvatar -> {
                    Toast.makeText(context, "Logged-in successfully", Toast.LENGTH_SHORT).show()
                    activity!!.supportFragmentManager.beginTransaction()
                        .replace(R.id.navHostFRM, ChooseAvatarFragment())
                        .commitNowAllowingStateLoss()
                }
            }
        })

        viewModel.toastData.observe(viewLifecycleOwner, Observer { toast ->
            if(toast != null) {
                Toast.makeText(context, toast, Toast.LENGTH_SHORT).show()
            }
        })

        return rootPOV
    }


    private fun showLoadingState() {
        view?.let { view ->
            view.loaderPBR.visibility = View.VISIBLE

            view.usernameTXT.visibility = View.GONE
            view.passwordTXT.visibility = View.GONE
            view.loginBTN.visibility = View.GONE
        }
    }

    private fun showWorkingState() {
        view?.let { view ->
            view.loaderPBR.visibility = View.GONE

            view.usernameTXT.visibility = View.VISIBLE
            view.passwordTXT.visibility = View.VISIBLE
            view.loginBTN.visibility = View.VISIBLE
        }
    }
}