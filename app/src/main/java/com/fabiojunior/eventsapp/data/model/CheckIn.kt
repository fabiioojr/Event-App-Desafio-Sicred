package com.fabiojunior.eventsapp.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Check in model
 */
@Parcelize
class CheckIn(
    val eventId: String? = null,
    val name: String? = "",
    val email: String? = ""
) : Parcelable