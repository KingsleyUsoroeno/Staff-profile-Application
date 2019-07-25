package com.techgroup.social_vue.auth.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val firstName: String,
    val lastName: String,
    val username: String,
    val email: String,
    val password: String
) : Parcelable

