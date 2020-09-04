package com.fabiojunior.eventsapp.view.adapters

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import com.fabiojunior.eventsapp.R
import com.fabiojunior.eventsapp.data.model.Event
import kotlinx.android.synthetic.main.dialog_checkin.*

class DialogCheckin(
    val event: Event,
    private val callbackDialog: CallbackDialog,
    context: Context
) :
    Dialog(context) {

    private var status: String = READY
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_checkin)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        setCancelable(false)
        setReady()

        yes.setOnClickListener {
            when (status) {
                READY -> {
                    callbackDialog.onCheckinRequest()
                }
                SUCCESS -> {
                    callbackDialog.onCheckinSuccess()
                    dismiss()
                }
                ERROR -> {
                    callbackDialog.onCheckinError()
                    dismiss()
                }
            }
        }

        no.setOnClickListener {
                callbackDialog.onCheckinCancel()
                dismiss()
        }
    }

    /**
     * Set status ready
     */
    fun setReady() {
        status = READY
        title_checkin.text = context.getString(R.string.title_ready)
        animation_view.setAnimation(R.raw.animation_money)
        text_desc.text = context.getString(R.string.confirm_checkin)
        yes.visibility = VISIBLE
        no.visibility = VISIBLE
    }

    /**
     * Set status loading
     */
    fun setLoading() {
        status = LOADING
        title_checkin.text = context.getText(R.string.title_loading)
        animation_view.setAnimation(R.raw.animation_loading)
        text_desc.text = context.getString(R.string.wait)
        yes.visibility = GONE
        no.visibility = GONE
        animation_view.playAnimation()
    }

    /**
     * Set status success
     */
    fun setSuccess() {
        status = SUCCESS
        title_checkin.text = context.getString(R.string.title_sucess)
        text_desc.text = context.getString(R.string.sucess_message)
        animation_view.setAnimation(R.raw.animation_sucess)
        yes.visibility = VISIBLE
        yes.text = context.getString(R.string.ok)
        no.visibility = GONE
        animation_view.playAnimation()
    }

    /**
     * Set status error
     */
    fun setError() {
        status = ERROR
        animation_view.setAnimation(R.raw.animation_error)
        title_checkin.text = context.getString(R.string.title_error)
        yes.visibility = VISIBLE
        text_desc.text =context.getString(R.string.error_message)
        yes.text = context.getString(R.string.ok)
        no.visibility = GONE
        animation_view.playAnimation()
    }

    /**
     * Callback to custom dialog
     */
    interface CallbackDialog {

        fun onCheckinSuccess()

        fun onCheckinRequest()

        fun onCheckinCancel()

        fun onCheckinError()
    }

    /**
     * Constants of status
     */
    companion object {
        const val READY = "ready"
        const val LOADING = "loading"
        const val SUCCESS = "success"
        const val ERROR = "error"
    }
}