package com.practicum.shift

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.loader.content.AsyncTaskLoader
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.shift.adapter.ContactAdapter
import com.practicum.shift.data.ContactDataModel
import com.practicum.shift.databinding.ActivityMainBinding
import android.widget.Toast
import com.practicum.shift.data.App
import com.practicum.shift.data.ContactEntity
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.net.MalformedURLException
import java.net.URL
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(), ContactAdapter.Listener {

    private lateinit var binding: ActivityMainBinding
    private val adapter = ContactAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Загружаем контакты из базы данных Room при запуске
                CoroutineScope(Dispatchers.IO).launch {
                    val contacts = App.database.contactDao().getAllContacts()
                    withContext(Dispatchers.Main) {
                        adapter.contactList.addAll(contacts)
                        adapter.notifyDataSetChanged()
                    }
                }

        init()
    }

    private fun init() {
        binding.apply {
            rcView.layoutManager = LinearLayoutManager(this@MainActivity)
            rcView.adapter = adapter

            butAddContact.setOnClickListener {
                val nt = NetworkTask(adapter)
                nt.execute()
            }
        }
    }

    // AsyncTask для выполнения операций в фоновом режиме
    class NetworkTask(private val adapter: ContactAdapter) :
        AsyncTask<Unit, ContactEntity, Unit>() {

        override fun doInBackground(vararg params: Unit) {
            try {
                val url = URL("https://randomuser.me/api/")
                val inputStream: InputStream = url.openStream()
                val buffer = ByteArray(4096)
                val sb = StringBuilder("")

                while (inputStream.read(buffer) != -1) {
                    sb.append(String(buffer))
                }

                val obj = JSONObject(sb.toString())
                val results = obj.getJSONArray("results")
                val user = results.getJSONObject(0)
                val nameObj = user.getJSONObject("name")

                val name = nameObj.getString("title") + ". " + nameObj.getString("first") + " " + nameObj.getString("last")
                val email = user.getString("email")
                val image_url = user.getJSONObject("picture").getString("medium")
                val telephoneNumber = user.getString("phone")
                val address = user.getJSONObject("location").getString("country") + ", " +  user.getJSONObject("location").getString("city")

                // Создаем новый контакт
                val contact = ContactEntity(
                    name = name,
                    address = address,
                    photoUrl = image_url,
                    email = email,
                    phoneNumber = telephoneNumber
                )

                // Публикуем контакт в основном потоке
                publishProgress(contact)

            } catch (e: Exception) {

                // Выводим сообщение об ошибке в лог
                Log.e("NetworkTask", "Error in doInBackground", e)
            }
        }

        override fun onProgressUpdate(vararg values: ContactEntity) {

            // Обновляем UI с новым контактом
            adapter.addContact(values[0])
        }
    }

    override fun onClick(contact: ContactDataModel) {

        startActivity(Intent(this, ContactInformationActivity::class.java).apply {
            putExtra("item", contact)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){

            R.id.delete -> {
                adapter.deleteAllContacts()
                Toast.makeText(this, "Список контактов очищен", Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }


}
