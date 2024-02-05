package com.practicum.shift.data

import java.io.Serializable

data class ContactDataModel(
    val name: String,
    val address: String,
    val photoUrl: String,
    val email: String,
    val phoneNumber: String,
): Serializable
