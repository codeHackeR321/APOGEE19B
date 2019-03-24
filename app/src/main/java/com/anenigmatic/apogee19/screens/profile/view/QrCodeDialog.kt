package com.anenigmatic.apogee19.screens.profile.view

import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.anenigmatic.apogee19.R
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.android.synthetic.main.dia_show_qr_code.view.*

class QrCodeDialog : DialogFragment() {

    interface OnTriggerQrCodeRefreshListener {

        fun onTriggerQrCodeRefresh()
    }


    private val qrCode by lazy {
        arguments!!.getString("QR_CODE")!!
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootPOV = inflater.inflate(R.layout.dia_show_qr_code, container, false)

        rootPOV.qrCodeIMG.setImageBitmap(qrCode.toQrCodeBitmap())

        rootPOV.refreshQrCodeBTN.setOnClickListener {
            (parentFragment as? OnTriggerQrCodeRefreshListener)?.onTriggerQrCodeRefresh()
            dismiss()
        }

        return rootPOV
    }

    // This method has been overriden to make the rounded corners visible.
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }


    private fun String.toQrCodeBitmap(): Bitmap {
        val bitMatrix = QRCodeWriter().encode(this, BarcodeFormat.QR_CODE, 256, 256)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val pixels = IntArray(width*height)
        (0 until height).forEach { y ->
            val offset = y*width
            (0 until width).forEach { x ->
                pixels[offset + x] = if(bitMatrix.get(x, y)) { Color.BLACK } else { Color.WHITE }
            }
        }

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply {
            setPixels(pixels, 0, width, 0, 0, width, height)
        }

        return bitmap
    }
}