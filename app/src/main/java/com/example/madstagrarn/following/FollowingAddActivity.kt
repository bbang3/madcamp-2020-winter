package com.example.madstagrarn.following

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madstagrarn.network.DataService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.appcompat.app.ActionBar
import com.example.madstagrarn.*
import com.example.madstagrarn.dataclass.Phone
import com.example.madstagrarn.dataclass.User

class FollowingAddActivity : AppCompatActivity() {
    private val readContactRequestCode = 100
    private val phoneList: ArrayList<Phone> = ArrayList()
    private val contactUserList: ArrayList<User> = ArrayList()
    private val dataService: DataService = DataService()

    private var signedUserList: ArrayList<User> = ArrayList()
    private var unsignedPhoneList: ArrayList<Phone> = ArrayList()

    private lateinit var currentUser: User
    private lateinit var rvSignedUser: RecyclerView
    private lateinit var rvUnsignedUser: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_following_add)
        val actionBar: ActionBar? = getSupportActionBar()
        actionBar?.hide()

        currentUser = intent.extras!!.get("User") as User
        loadCurrentUserInfo()

        getContactWithPermission()

        rvSignedUser = findViewById<RecyclerView>(R.id.rv_user_signed)
        rvSignedUser.setHasFixedSize(true)
        rvSignedUser.layoutManager = LinearLayoutManager(this)

        rvUnsignedUser = findViewById<RecyclerView>(R.id.rv_user_unsigned)
        rvUnsignedUser.setHasFixedSize(true)
        rvUnsignedUser.layoutManager = LinearLayoutManager(this)
    }

    override fun onBackPressed() {
        this.finish()
    }

    private fun getContactUsers() {
        Log.i("FollwingAddActivity", "phoneList size: ${phoneList.size}")
        dataService.service.getUsersByContact(phoneList).enqueue(object :
            Callback<ArrayList<User>> {
            override fun onResponse(
                call: Call<ArrayList<User>>,
                response: Response<ArrayList<User>>
            ) {
                if(response.isSuccessful) {
                    val receivedUserList = response.body()!!
                    Log.i("getContactUsers", receivedUserList.toString())
                    for(phone in phoneList) {
                        var isFound: Boolean = false
                        for(user in receivedUserList) {
                            if(phone.phoneNumber == user.phoneNumber) {
                                signedUserList.add(user)
                                isFound = true
                                break
                            }
                        }
                        if(!isFound) unsignedPhoneList.add(phone)
                    }
                    Log.i("getContactUsers", signedUserList.toString())
                    Log.i("getContactUsers", unsignedPhoneList.toString())

                    rvSignedUser.adapter =
                        SignedUserAdapter(
                            signedUserList,
                            currentUser.followingIds,
                            currentUser
                        )
                    rvUnsignedUser.adapter =
                        UnsignedUserAdapter(
                            unsignedPhoneList
                        )
                }
            }

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun getAllContacts() {
        val cr = contentResolver
        val cur: Cursor? = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
        if ((cur?.getCount() ?: 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                val id: String = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID))
                val name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY))

                if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    val pCur: Cursor? = cr.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id),
                        null
                    )
                    if (pCur != null) {
                        while (pCur.moveToNext()) {
                            val phoneNumber: String =
                                pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            Log.e("phone", "$phoneNumber phone")
                            phoneList.add(
                                Phone(
                                    name,
                                    phoneNumber
                                )
                            )
                        }
                    }
                    pCur?.close()
                }

            }
        }
        cur?.close()
        getContactUsers()
    }


    private fun getContactWithPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            // only for gingerbread and newer versions
            val permission: String = Manifest.permission.WRITE_CONTACTS
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_CONTACTS),
                    readContactRequestCode
                )
            } else {
                getAllContacts()
            }
        } else {
            getAllContacts()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            readContactRequestCode -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getAllContacts()
                } else {
                    val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
                    alertDialogBuilder.setTitle("Permissions Required")
                        .setMessage(
                            "You have forcefully denied some of the required permissions " +
                                    "for this action. Please open settings, go to permissions and allow them."
                        )
                        .setPositiveButton("Settings") { dialog, which ->
                            val intent = Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", packageName, null)
                            )
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                        .setNegativeButton("Cancel") { dialog, which ->
                            Toast.makeText(
                                application,
                                "Permission required",
                                Toast.LENGTH_LONG
                            ).show()
                            this.finish()
                        }
                        .setCancelable(false)
                        .create()
                        .show()
                }
                return
            }
        }
    }

    private fun loadCurrentUserInfo() {
        dataService.service.getUser(currentUser._id).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if(response.isSuccessful) {
                    Log.i("loadCurrentUserInfo", response.body()!!.toString())
                    currentUser = response.body()!!
                }
            }
            override fun onFailure(call: Call<User>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}