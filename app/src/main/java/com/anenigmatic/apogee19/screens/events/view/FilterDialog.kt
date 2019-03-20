package com.anenigmatic.apogee19.screens.events.view

import android.app.Dialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.anenigmatic.apogee19.R
import com.anenigmatic.apogee19.screens.events.core.EventFilterViewModel
import com.anenigmatic.apogee19.screens.events.core.EventFilterViewModelFactory
import kotlinx.android.synthetic.main.dia_filter_menu_state.view.*

class FilterDialog : DialogFragment(), FiltersAdapter.ClickListener {

    private enum class DialogState {
        ShowingMenu, ShowingTypeFilters, ShowingSpotFilters
    }


    private var dialogState = DialogState.ShowingMenu

    private val viewModel by lazy {
        ViewModelProviders.of(this, EventFilterViewModelFactory())[EventFilterViewModel::class.java]
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layoutResource = when(dialogState) {
            DialogState.ShowingMenu -> R.layout.dia_filter_menu_state
            else                    -> R.layout.dia_filter_list_state
        }

        val rootPOV = inflater.inflate(layoutResource, container, false)

        (rootPOV as ConstraintLayout).loadLayoutDescription(R.xml.dia_filter_states)

        rootPOV.typeFiltersRCY.adapter = FiltersAdapter(this)

        rootPOV.spotFiltersRCY.adapter = FiltersAdapter(this)

        rootPOV.closeBTN.setOnClickListener {
            dismiss()
        }

        // This button is only visible in the filter list state. So, clicking it takes the user back to menu state.
        rootPOV.backBTN.setOnClickListener {
            rootPOV.setState(R.id.menuState, 0, 0)
            rootPOV.dialogTitleLBL.text = "Filters"

            dialogState = DialogState.ShowingMenu
        }

        rootPOV.showTypeFiltersBTN.setOnClickListener {
            rootPOV.setState(R.id.listState, 0, 0)
            rootPOV.dialogTitleLBL.text = "Categories"
            rootPOV.typeFiltersRCY.visibility = View.VISIBLE
            rootPOV.spotFiltersRCY.visibility = View.GONE

            dialogState = DialogState.ShowingTypeFilters
        }

        rootPOV.showSpotFiltersBTN.setOnClickListener {
            rootPOV.setState(R.id.listState, 0, 0)
            rootPOV.dialogTitleLBL.text = "Venues"
            rootPOV.typeFiltersRCY.visibility = View.GONE
            rootPOV.spotFiltersRCY.visibility = View.VISIBLE

            dialogState = DialogState.ShowingSpotFilters
        }

        viewModel.showOnlyOngoingEventsData.observe(viewLifecycleOwner, Observer { value ->
            when(value) {
                true  -> {
                    TextViewCompat.setTextAppearance(rootPOV.showOnlyOngoingToggleBTN, R.style.ActiveFilterButton)
                    rootPOV.showOnlyOngoingToggleBTN.setBackgroundResource(R.drawable.sh_rounded_rectangle_08dp)

                    val color = ContextCompat.getColor(requireContext(), R.color.vio07)
                    rootPOV.showOnlyOngoingToggleBTN.backgroundTintList = ColorStateList.valueOf(color)

                    rootPOV.showOnlyOngoingToggleBTN.setOnClickListener {
                        viewModel.onSetShowOnlyOngoingEvents(false)
                    }
                }
                false -> {
                    TextViewCompat.setTextAppearance(rootPOV.showOnlyOngoingToggleBTN, R.style.InactiveFilterButton)
                    rootPOV.showOnlyOngoingToggleBTN.setBackgroundResource(R.drawable.sh_rounded_rectangle_thin_outline_08dp)

                    rootPOV.showOnlyOngoingToggleBTN.backgroundTintList = null

                    rootPOV.showOnlyOngoingToggleBTN.setOnClickListener {
                        viewModel.onSetShowOnlyOngoingEvents(true)
                    }
                }
            }
        })

        viewModel.showOnlyStarredEventsData.observe(viewLifecycleOwner, Observer { value ->
            when(value) {
                true  -> {
                    TextViewCompat.setTextAppearance(rootPOV.showOnlyStarredToggleBTN, R.style.ActiveFilterButton)
                    rootPOV.showOnlyStarredToggleBTN.setBackgroundResource(R.drawable.sh_rounded_rectangle_08dp)

                    val color = ContextCompat.getColor(requireContext(), R.color.vio07)
                    rootPOV.showOnlyStarredToggleBTN.backgroundTintList = ColorStateList.valueOf(color)

                    rootPOV.showOnlyStarredToggleBTN.setOnClickListener {
                        viewModel.onSetShowOnlyStarredEvents(false)
                    }
                }
                false -> {
                    TextViewCompat.setTextAppearance(rootPOV.showOnlyStarredToggleBTN, R.style.InactiveFilterButton)
                    rootPOV.showOnlyStarredToggleBTN.setBackgroundResource(R.drawable.sh_rounded_rectangle_thin_outline_08dp)

                    rootPOV.showOnlyStarredToggleBTN.backgroundTintList = null

                    rootPOV.showOnlyStarredToggleBTN.setOnClickListener {
                        viewModel.onSetShowOnlyStarredEvents(true)
                    }
                }
            }
        })

        viewModel.typeFilterData.observe(viewLifecycleOwner, Observer { types ->
            (rootPOV.typeFiltersRCY.adapter as FiltersAdapter).filters = types
        })

        viewModel.spotFilterData.observe(viewLifecycleOwner, Observer { spots ->
            (rootPOV.spotFiltersRCY.adapter as FiltersAdapter).filters = spots
        })

        return rootPOV
    }

    // This method has been overriden to make the rounded corners visible.
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }


    override fun onFilterSet(name: String) {
        when(dialogState) {
            DialogState.ShowingTypeFilters -> viewModel.onSetTypeFilter(name)
            DialogState.ShowingSpotFilters -> viewModel.onSetSpotFilter(name)
            else                           -> Toast.makeText(context, "Error!!", Toast.LENGTH_SHORT).show()
        }
    }
}