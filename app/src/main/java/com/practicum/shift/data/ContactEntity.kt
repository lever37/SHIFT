package com.practicum.shift.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class ContactEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val address: String,
    val email: String,
    val phoneNumber: String,
    val photoUrl: String
)
fun ContactEntity.toContactDataModel(): ContactDataModel {
    return ContactDataModel(
        name = this.name,
        address = this.address,
        photoUrl = this.photoUrl,
        email = this.email,
        phoneNumber = this.phoneNumber
    )
}
