package com.fabiojunior.eventsapp.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Even model
 */
@Parcelize
data class Event(
    val id: String? = null,
    val title: String? = "",
    val date: Long? = null,
    val description: String = "",
    val image: String? = null,
    val price: Double = 0.0,
    val longitude: String? = null,
    val latitude: String? = null,
    val people: List<People>,
    val cupons: List<Coupon>

) : Parcelable