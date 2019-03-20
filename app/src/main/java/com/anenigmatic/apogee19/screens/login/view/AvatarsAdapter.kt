package com.anenigmatic.apogee19.screens.login.view

import android.content.res.ColorStateList
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.anenigmatic.apogee19.R
import com.anenigmatic.apogee19.screens.shared.core.Avatar
import kotlinx.android.synthetic.main.box_avatar.view.*

class AvatarsAdapter(private val listener: ClickListener) : RecyclerView.Adapter<AvatarsAdapter.AvatarVHolder>() {

    interface ClickListener {

        fun onAvatarClicked(avatarId: Long)
    }


    var avatars = listOf<Avatar>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var chosenAvatarId = 0L
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun getItemCount() = avatars.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvatarVHolder {
        val inflater = LayoutInflater.from(parent.context)
        return AvatarVHolder(inflater.inflate(R.layout.box_avatar, parent, false))
    }

    override fun onBindViewHolder(holder: AvatarVHolder, position: Int) {
        val avatar = avatars[position]

        val uri = Uri.parse(avatar.picUri)
        // We set URI to null first, due to a bug in Android.
        holder.avatarIMG.setImageURI(null)
        holder.avatarIMG.setImageURI(uri)

        val context = holder.avatarIMG.context
        holder.avatarIMG.backgroundTintList = if(avatar.id == chosenAvatarId) {
            ColorStateList.valueOf(ContextCompat.getColor(context, R.color.blu05))
        } else {
            ColorStateList.valueOf(ContextCompat.getColor(context, R.color.clear))
        }

        holder.avatarIMG.setOnClickListener {
            listener.onAvatarClicked(avatar.id)
        }
    }


    class AvatarVHolder(rootPOV: View) : RecyclerView.ViewHolder(rootPOV) {

        val avatarIMG: ImageView = rootPOV.avatarIMG
    }
}