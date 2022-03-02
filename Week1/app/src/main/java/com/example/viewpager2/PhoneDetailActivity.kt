package com.example.viewpager2

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract.PhoneLookup
import android.provider.ContactsContract.RawContacts
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class PhoneDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_detail)
        supportActionBar?.hide()

        var nameTextView: TextView = findViewById<TextView>(R.id.name)
        var phoneTextView: TextView = findViewById<TextView>(R.id.phone)


        val bundle = intent.extras
        var id: String? = null
        var name: String?
        var phone: String?

        bundle?.let {
            id = it.getString("id")
            name = it.getString("name")
            phone = it.getString("phone")

            nameTextView.text = name
            phoneTextView.text = phone
        }

        var deleteButton: Button = findViewById(R.id.delete_btn)
        deleteButton.setOnClickListener {
            id?.let { deleteButtonOnClick(it) }
            finish()
        }
    }

    private fun deleteButtonOnClick(id: String) {
        deleteContactFromRawContactID(id.toLong())
    }

    private fun deleteContactFromRawContactID(CONTACT_ID: Long) {
        val where = RawContacts.CONTACT_ID + " = " + CONTACT_ID.toString()
        contentResolver.delete(RawContacts.CONTENT_URI, where, null)
    }


    private fun getContactIdFromNameAndNumber(display_name: String, number: String?): Long {
        var rawContactId: Long = -1
        val contactUri: Uri =
            Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number))
        val projection = arrayOf(PhoneLookup._ID, PhoneLookup.TYPE, PhoneLookup.DISPLAY_NAME)
        var cursor: Cursor? = null
        try {
            cursor = contentResolver.query(contactUri, projection, null, null, null)
            if (cursor?.moveToFirst() == true) {
                do {
                    val phoneName: String =
                        cursor.getString(cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME))
                    if (display_name == phoneName) {
                        rawContactId = cursor.getLong(cursor.getColumnIndex(PhoneLookup._ID))
                    }
                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        return rawContactId
    }

}