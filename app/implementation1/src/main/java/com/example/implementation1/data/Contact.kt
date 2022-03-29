package com.example.implementation1.data

import android.os.Parcelable
import androidx.room.PrimaryKey
import com.google.firebase.database.Exclude
import kotlinx.parcelize.Parcelize

@Parcelize
data class Contact(
    @PrimaryKey(autoGenerate = true)
    var id: String? = null,
    val contactImage: Int? = null,
    var fullName: String? = null,
    var contactNumber: String? = null,
    var email: String? = null,

    @get:Exclude
    var isDeleted: Boolean = false

) : Parcelable {
    override fun equals(other: Any?): Boolean {
        return if (other is Contact) {
            other.id == id
        } else false
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (contactImage ?: 0)
        result = 31 * result + (fullName?.hashCode() ?: 0)
        result = 31 * result + (contactNumber?.hashCode() ?: 0)
        result = 31 * result + (email?.hashCode() ?: 0)
        result = 31 * result + isDeleted.hashCode()
        return result
    }
}
