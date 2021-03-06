package com.anenigmatic.apogee19.screens.profile.view

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.anenigmatic.apogee19.R
import com.anenigmatic.apogee19.screens.login.view.ChooseLoginFragment
import com.anenigmatic.apogee19.screens.profile.core.ProfileViewModel
import com.anenigmatic.apogee19.screens.profile.core.ProfileViewModel.UiOrder
import com.anenigmatic.apogee19.screens.profile.core.ProfileViewModelFactory
import com.anenigmatic.apogee19.screens.shared.core.User
import com.anenigmatic.apogee19.screens.tickets.view.TicketsFragment
import kotlinx.android.synthetic.main.fra_profile.view.*

class ProfileFragment : Fragment(), QrCodeDialog.OnTriggerQrCodeRefreshListener, TransferMoneyDialog.OnEnterTransferDetailsListener, AddMoneyDialog.OnEnterAmountListener {

    private val viewModel by lazy {
        ViewModelProviders.of(this, ProfileViewModelFactory())[ProfileViewModel::class.java]
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootPOV = inflater.inflate(R.layout.fra_profile, container, false)

        rootPOV.signingsRCY.adapter = SigningsAdapter()

        rootPOV.showQrCodeBTN.setOnClickListener {
            (viewModel.orderData.value as? UiOrder.ShowWorkingState)?.user?.let { user ->
                QrCodeDialog().apply { arguments = bundleOf("QR_CODE" to user.qrCode) }
                    .show(childFragmentManager, "QrCodeDialog")
            }
        }

        rootPOV.refreshDetailsBTN.setOnClickListener {
            viewModel.onFetchDetailsAction()
        }

        rootPOV.addMoneyBTN.setOnClickListener {
            (viewModel.orderData.value as? UiOrder.ShowWorkingState)?.user?.let { user ->
                if(!user.isBitsian) {
                    Toast.makeText(context, "Please go to the teller's stall to add money", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            AddMoneyDialog().show(childFragmentManager, "AddMoneyDialog")
        }

        rootPOV.transferMoneyBTN.setOnClickListener {
            TransferMoneyDialog().show(childFragmentManager, "TransferMoneyDialog")
        }

        rootPOV.purchaseTicketsBTN.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.navHostFRM, TicketsFragment())
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }

        viewModel.orderData.observe(viewLifecycleOwner, Observer { order ->
            when(order) {
                is UiOrder.ShowLoadingState -> showLoadingState()
                is UiOrder.ShowWorkingState -> showWorkingState(order.user)
                is UiOrder.GoToLoginScreen  -> {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.navHostFRM, ChooseLoginFragment())
                        .addToBackStack(null)
                        .commitAllowingStateLoss()
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

    override fun onTriggerQrCodeRefresh() {
        viewModel.onQrCodeRefreshAction()
    }

    override fun onEnterAmount(amount: Int) {
        viewModel.onAddMoneyAction(amount)
    }

    override fun onEnterTransferDetails(recipientId: Long, amount: Int) {
        viewModel.onTransferMoneyAction(recipientId, amount)
    }


    private fun showLoadingState() {
        view?.let { view ->
            view.loaderPBR.visibility = View.VISIBLE
            view.contentGRP.visibility = View.GONE
        }
    }

    private fun showWorkingState(user: User) {
        view?.let { view ->
            view.loaderPBR.visibility = View.GONE
            view.contentGRP.visibility = View.VISIBLE

            view.avatarIMG.setImageURI(null)
            view.avatarIMG.setImageURI(Uri.parse(user.avatar.picUri))
            view.nameLBL.text = user.name
            view.userIdLBL.text = "User Id: ${user.id}"
            view.balanceLBL.text = "₹ ${user.balance}"
            view.coinsLBL.text = user.coins.toString()

            (view.signingsRCY.adapter as SigningsAdapter).signings = user.signings
        }
    }
}