package com.fabiojunior.eventsapp.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fabiojunior.eventsapp.R
import com.fabiojunior.eventsapp.data.model.Coupon
import kotlinx.android.synthetic.main.item_coupon.view.*

class CouponAdapter(
    private val coupons: MutableList<Coupon>,
    private val cellClickListener: CouponClickListener
) :
    RecyclerView.Adapter<CouponAdapter.EventsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsViewHolder {
        return EventsViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_coupon, parent, false)
        )
    }

    override fun getItemCount() = coupons.count()

    override fun onBindViewHolder(holder: EventsViewHolder, position: Int) {
        holder.bindView(coupons[position], cellClickListener)
    }

    /**
     * Update list
     */
    fun updateList(itemList: MutableList<Coupon>) {
        this.coupons.clear()
        this.coupons.addAll(itemList)
        notifyDataSetChanged()
    }

    class EventsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val card = itemView.card
        private val icon = itemView.preview

        fun bindView(coupon: Coupon, couponClickListener: CouponClickListener) {

                Glide.with(itemView.context)
                    .load(coupon.image)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(icon)

            card.setOnClickListener {
                couponClickListener.onCouponClickListener(coupon)
            }
        }
    }

    /**
     * Coupon callback
     */
    interface CouponClickListener {
        fun onCouponClickListener(coupon: Coupon)
    }
}