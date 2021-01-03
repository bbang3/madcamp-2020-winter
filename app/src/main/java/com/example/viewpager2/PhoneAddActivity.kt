package com.example.viewpager2

import android.content.ContentProviderOperation
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_phone_add.*


class PhoneAddActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_add)
        supportActionBar?.hide()

        delete_btn.setOnClickListener {
            addContact()
            finish()
        }

        cancel_btn.setOnClickListener {
            finish()
        }
    }

    private fun addContact() {
        val name = name.text.toString()
        val phone = phone.text.toString()

        if(name.isNullOrEmpty() || phone.isNullOrEmpty()) {
            Toast.makeText(this, "name or phone must not be null!", Toast.LENGTH_SHORT).show()
            return
        }

            val contact = ArrayList<ContentProviderOperation>()

            contact.add(
                ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                    .build()
            )

        // name
        contact.add(
            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, 0)
                .withValue(
                    ContactsContract.RawContacts.Data.MIMETYPE,
                    ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
                )
                .withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, name)
                .build()
        )

        // phone (Mobile)
        contact.add(
            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, 0)
                .withValue(
                    ContactsContract.Data.MIMETYPE,
                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
                )
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phone)
                .withValue(
                    ContactsContract.CommonDataKinds.Phone.TYPE,
                    ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE
                )
                .build()
        )

        try {
            val results = contentResolver.applyBatch(ContactsContract.AUTHORITY, contact)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}