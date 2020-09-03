package com.fabiojunior.eventsapp.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Coupon model
 */
@Parcelize
data class Coupon(
    val id: String? = null,
    val eventId: String? = null,
    val discount: Int = 0,
    var image: String? = ""
) : Parcelable