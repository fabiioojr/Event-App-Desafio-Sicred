package com.fabiojunior.eventsapp.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * People model
 */
@Parcelize
data class People(
    val id: String? = null,
    val eventId: String? = null,
    val name: String = "",
    val picture: String? = null
) : Parcelable