package com.fabiojunior.eventsapp.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Profile model
 */
@Parcelize
class Profile(
    val name: String = "",
    val email: String = ""
) : Parcelable