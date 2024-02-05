package com.practicum.shift

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.practicum.shift.data.ContactDataModel
import com.practicum.shift.databinding.ActivityContactInformationBinding
import com.squareup.picasso.Picasso

@Suppress("DEPRECATION")
class ContactInformationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactInformationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val contact = intent.getSerializableExtra("item") as ContactDataModel

        binding.apply {
            Picasso.get().load(contact.photoUrl).into(binding.imageContact)
            contactName.text = contact.name
            contactTelephone.text = "Телефон: " + contact.phoneNumber
            contactEmail.text = "Email-адресс: " + contact.email
            contactLocation.text = "Город проживания: " + contact.address
        }
    }


}