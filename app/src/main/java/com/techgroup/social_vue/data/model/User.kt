package com.techgroup.social_vue.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "Staff")
@Parcelize
data class Staff(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var firstName: String,
    var email: String,
    var stateOfOrigin: String,
    var dob: String,
    var imageUrl: String
) : Parcelable {
    constructor() : this(0, "", "", "", "", "")
}

