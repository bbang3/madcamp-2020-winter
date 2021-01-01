package com.example.viewpager2

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_phone.*
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PhoneFragment : Fragment() {
    private val phoneViewModel: PhoneViewModel by activityViewModels()

    val permissions = arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE) // permissions required in this fragment
    lateinit var adapter: PhoneAdapter
    var searchText = ""
    var sortText = "asc"

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_phone, container, false)
        view.findViewById<FloatingActionButton>(R.id.fab_add).setOnClickListener {
            it ->
            Toast.makeText(context, "!!!", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        checkAndStart()
    }

    private fun checkAndStart() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && isPermitted()) {
            startProcess()
        } else {
            requestPermissions(permissions, 99)
        }
    }

    private fun startProcess() {
        setList()
        setSearchListener()
    }

    fun setList() {
        phoneViewModel.getPhoneList().addAll(getPhoneNumbers(sortText, searchText))
        adapter = PhoneAdapter(phoneViewModel.getPhoneList())

        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(getContext())
    }

    fun changeList() {
        val newList = getPhoneNumbers(sortText, searchText)
        phoneViewModel.getPhoneList().clear()
        phoneViewModel.getPhoneList().addAll(newList)

        adapter.notifyDataSetChanged()
    }

    private fun setSearchListener() {
        editSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s != null && s.toString() != ("")) {
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchText = s.toString()
                changeList()
            }
        })
    }

    private fun getPhoneNumbers(sort: String, searchName: String?): MutableList<Phone> {
        val list = mutableListOf<Phone>()
        val phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val projections = arrayOf(
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )
        var whenClause: String? = null
        var whereValues: Array<String>? = null
        if (searchName?.isNotEmpty() == true) {
            whenClause = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " like ?"
            whereValues = arrayOf("%$searchName%")
        }
        val optionSort = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " $sort"
        val cursor = activity?.contentResolver?.query(
            phoneUri,
            projections,
            whenClause,
            whereValues,
            optionSort
        )

        while (cursor?.moveToNext() == true) {
            val id = cursor.getString(0)
            val name = cursor.getString(1)
            var number = cursor.getString(2)
            val phone = Phone(id, name, number)
            list.add(phone)
        }
        return list
    }

    private fun isPermitted(): Boolean {
        return permissions.all { perm -> context?.let { checkSelfPermission(it, perm) } == PackageManager.PERMISSION_GRANTED}
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 99) {
            var check = true
            for (grant in grantResults) {
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    check = false
                    break
                }
            }
            if (check) startProcess()
            else {
                Toast.makeText(context, "권한 승인을 하셔야지만 앱을 사용할 수 있습니다.", Toast.LENGTH_LONG)
                    .show()
                activity?.finish()
            }
        }
    }
}