package com.example.viewpager2

import android.R.id
import android.icu.util.UniversalTimeScale.toLong
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
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

        bundle?.let {
            id = it.getString("id")
            val name = it.getString("name")
            val phone = it.getString("phone")

            nameTextView.text = name
            phoneTextView.text = phone

        }

        var deleteButton:Button = findViewById(R.id.delete_btn)
        deleteButton.setOnClickListener {
            id?.let { it -> deleteButtonOnClick(it) }
            finish()
        }
    }

    private fun deleteButtonOnClick(id: String) {
        val rawContactId: Long = id.toLong()

        val contentResolver = contentResolver

        val dataContentUri: Uri = ContactsContract.Data.CONTENT_URI

        // Create data table where clause.

        // Create data table where clause.
        val dataWhereClauseBuf = StringBuffer()
        dataWhereClauseBuf.append(ContactsContract.Data.RAW_CONTACT_ID)
        dataWhereClauseBuf.append(" = ")
        dataWhereClauseBuf.append(rawContactId)

        contentResolver.delete(dataContentUri, dataWhereClauseBuf.toString(), null)

        val rawContactUri: Uri = ContactsContract.RawContacts.CONTENT_URI

        // Create raw_contacts table where clause.

        // Create raw_contacts table where clause.
        val rawContactWhereClause = StringBuffer()
        rawContactWhereClause.append(ContactsContract.RawContacts._ID)
        rawContactWhereClause.append(" = ")
        rawContactWhereClause.append(rawContactId)

        // Delete raw_contacts table related data.

        // Delete raw_contacts table related data.
        contentResolver.delete(rawContactUri, rawContactWhereClause.toString(), null)

        val contactUri: Uri = ContactsContract.Contacts.CONTENT_URI

        // Create contacts table where clause.

        // Create contacts table where clause.
        val contactWhereClause = StringBuffer()
        contactWhereClause.append(ContactsContract.Contacts._ID)
        contactWhereClause.append(" = ")
        contactWhereClause.append(rawContactId)

        // Delete raw_contacts table related data.

        // Delete raw_contacts table related data.
        contentResolver.delete(contactUri, contactWhereClause.toString(), null)

    }
}