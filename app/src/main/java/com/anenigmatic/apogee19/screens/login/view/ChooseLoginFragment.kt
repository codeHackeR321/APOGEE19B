package com.anenigmatic.apogee19.screens.login.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.anenigmatic.apogee19.R
import com.anenigmatic.apogee19.screens.login.core.ChooseLoginViewModel
import com.anenigmatic.apogee19.screens.login.core.ChooseLoginViewModel.UiOrder
import com.anenigmatic.apogee19.screens.login.core.ChooseLoginViewModelFactory
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.android.synthetic.main.fra_choose_login.view.*

class ChooseLoginFragment : Fragment() {

    private val googleSignInClient by lazy {
        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("765197201085-j6q594d8v56sfmoq401avvmbthorv21s.apps.googleusercontent.com")
            .requestEmail()
            .build()

        GoogleSignIn.getClient(requireActivity(), signInOptions)
    }

    private val reqCode = 112

    private val viewModel by lazy {
        ViewModelProviders.of(this, ChooseLoginViewModelFactory())[ChooseLoginViewModel::class.java]
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootPOV = inflater.inflate(R.layout.fra_choose_login, container, false)

        rootPOV.loginAsBitsianBTN.setOnClickListener {
            startActivityForResult(googleSignInClient.signInIntent, reqCode)
        }

        rootPOV.loginAsOutsteeBTN.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.navHostFRM, OutsteeLoginFragment())
                .commitNowAllowingStateLoss()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == reqCode) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                viewModel.onLoginAction(account!!.idToken!!)
            } catch(e: ApiException) {
                Toast.makeText(context, "Google sign in failed: ${e.statusCode}", Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun showLoadingState() {
        view?.let { view ->
            view.loaderPBR.visibility = View.VISIBLE

            view.loginAsBitsianBTN.visibility = View.GONE
            view.loginAsOutsteeBTN.visibility = View.GONE
        }
    }

    private fun showWorkingState() {
        view?.let { view ->
            view.loaderPBR.visibility = View.GONE

            view.loginAsBitsianBTN.visibility = View.VISIBLE
            view.loginAsOutsteeBTN.visibility = View.VISIBLE
        }
    }
}