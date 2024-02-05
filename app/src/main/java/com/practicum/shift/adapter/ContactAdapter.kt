package com.practicum.shift.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.practicum.shift.R
import com.practicum.shift.data.App
import com.practicum.shift.data.ContactDataModel
import com.practicum.shift.data.ContactEntity
import com.practicum.shift.data.toContactDataModel
import com.practicum.shift.databinding.ActivityMainBinding.inflate
import com.practicum.shift.databinding.ContactItemBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.ArrayList

class ContactAdapter (val listener: Listener):
    RecyclerView.Adapter <ContactAdapter.ContactHolder>() {

    val contactList = ArrayList<ContactEntity>()


    class ContactHolder(item: View): RecyclerView.ViewHolder(item){

        private val binding = ContactItemBinding.bind(item)

        fun setData(contact: ContactEntity, listener: Listener)= with(binding){

            contactName.text = "Имя: " + contact.name

            userAdress.text = "Адресс: " + contact.address

            emailAdress.text = "Почта: " + contact.email

            telephoneNumber.text = "Телефон: " + contact.phoneNumber

            Picasso.get().load(contact.photoUrl).into(binding.avatar)

            itemView.setOnClickListener {
                listener.onClick(contact.toContactDataModel()) // Преобразовываем в ContactDataModel
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.contact_item, parent, false)
        return ContactHolder(view)
    }

    override fun onBindViewHolder(holder: ContactHolder, position: Int) {

        holder.setData(contactList[position], listener)
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    fun addContact(contact: ContactEntity) {

        contactList.add(contact)
        notifyItemInserted(contactList.size - 1)

        // Сохраняем контакт в базу данных Room
        CoroutineScope(Dispatchers.IO).launch {
            App.database.contactDao().insertContact(contact)
        }
    }

    interface Listener{
        fun onClick(contact: ContactDataModel)
    }

    fun deleteAllContacts() {

        contactList.clear()
        notifyDataSetChanged()

        // Удаляем все контакты из базы данных Room
        CoroutineScope(Dispatchers.IO).launch {
            App.database.contactDao().deleteAllContacts()
        }
    }


}