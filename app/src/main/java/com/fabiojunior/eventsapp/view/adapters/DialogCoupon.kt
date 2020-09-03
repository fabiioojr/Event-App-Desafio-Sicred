package com.fabiojunior.eventsapp.view.adapters

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.bumptech.glide.Glide
import com.fabiojunior.eventsapp.R
import com.fabiojunior.eventsapp.data.model.Coupon
import com.fabiojunior.eventsapp.data.model.Event
import kotlinx.android.synthetic.main.dialog_coupon.*

class DialogCoupon(val event: Event, private val callbackDialog: CallbackDialog, context: Context) :
    Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_coupon)
        this.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setupCard()
        yes.setOnClickListener {
            callbackDialog.onClickDialogCoupon(event)
            dismiss()
        }
    }

    /**
     * Configure card
     */
    private fun setupCard() {
        var coupon: Coupon = event.cupons[0]
        coupon.let {
            title_checkin.text = "Obaa! R$${event.cupons[0].discount}"
            text_desc.text =
                """Você ganhou R${"$"}${coupon.discount} de desconto no evento ${event.title}"""

            Glide.with(context)
                .load(event.image)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(preview)

        }
    }

    /**
     * Callback for dialog custom
     */
    interface CallbackDialog {

        fun onClickDialogCoupon(event: Event)

    }
}