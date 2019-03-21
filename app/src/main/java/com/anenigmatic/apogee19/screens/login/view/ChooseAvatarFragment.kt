package com.anenigmatic.apogee19.screens.login.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.anenigmatic.apogee19.R
import com.anenigmatic.apogee19.screens.login.core.ChooseAvatarViewModel
import com.anenigmatic.apogee19.screens.login.core.ChooseAvatarViewModel.UiOrder
import com.anenigmatic.apogee19.screens.login.core.ChooseAvatarViewModelFactory
import com.anenigmatic.apogee19.screens.shared.core.Avatar
import kotlinx.android.synthetic.main.fra_choose_avatar.view.*

class ChooseAvatarFragment : Fragment(), AvatarsAdapter.ClickListener {

    private val viewModel by lazy {
        ViewModelProviders.of(this, ChooseAvatarViewModelFactory())[ChooseAvatarViewModel::class.java]
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootPOV = inflater.inflate(R.layout.fra_choose_avatar, container, false)

        rootPOV.avatarsRCY.layoutManager = GridLayoutManager(context, 3)
        rootPOV.avatarsRCY.adapter = AvatarsAdapter(this)

        rootPOV.proceedBTN.setOnClickListener {
            viewModel.onProceedAction()
        }

        viewModel.orderData.observe(viewLifecycleOwner, Observer { order ->
            when(order) {
                is UiOrder.ShowLoadingState  -> showLoadingState()
                is UiOrder.ShowWorkingState  -> showWorkingState(order.avatars, order.chosenAvatarId)
                is UiOrder.GoToDesiredScreen -> {
                    Toast.makeText(context, "Avatar chosen successfully", Toast.LENGTH_SHORT).show()
                    activity!!.supportFragmentManager.popBackStack()
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

    override fun onAvatarClicked(avatarId: Long) {
        viewModel.onChooseAvatarAction(avatarId)
    }


    private fun showLoadingState() {
        view?.let { view ->
            view.loaderPBR.visibility = View.VISIBLE

            view.avatarsRCY.visibility = View.GONE
            view.proceedBTN.visibility = View.GONE
        }
    }

    private fun showWorkingState(avatars: List<Avatar>, chosenAvatarId: Long) {
        view?.let { view ->
            view.loaderPBR.visibility = View.GONE

            view.avatarsRCY.visibility = View.VISIBLE
            view.proceedBTN.visibility = View.VISIBLE

            (view.avatarsRCY.adapter as AvatarsAdapter).also {
                it.avatars = avatars
                it.chosenAvatarId = chosenAvatarId
            }
        }
    }
}