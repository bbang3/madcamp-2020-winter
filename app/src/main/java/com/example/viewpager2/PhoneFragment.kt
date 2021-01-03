package com.example.viewpager2

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import kotlinx.android.synthetic.main.fragment_phone.view.*

class PhoneFragment : Fragment() {
    private val phoneViewModel: PhoneViewModel by activityViewModels()
    private val newPhoneAddActivityRequestCode = 1
    private val phoneDeleteRequestCode = 2

    private val permissions = arrayOf(
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.WRITE_CONTACTS,
        Manifest.permission.CALL_PHONE
    ) // permissions required in this fragment
    private val writeContactRequestCode = 100

    private lateinit var adapter: PhoneAdapter
    private var searchText = ""
    private var sortText = "asc"

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_phone, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fab_add.setOnClickListener { fabOnClick(view) }
        setRadioListener(view)

        checkAndStart()
    }

    private fun fabOnClick(view: View) {
        var permissionOperation = false
        val intent = Intent(context, PhoneAddActivity::class.java)
        startActivityForResult(intent, newPhoneAddActivityRequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // 연락처가 추가되거나 삭제된 이후 phoneList를 갱신함
        if (requestCode == newPhoneAddActivityRequestCode || requestCode == phoneDeleteRequestCode) {
            changeList()
        }
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

    private fun setList() {
        phoneViewModel.getPhoneList().addAll(getPhoneNumbers(sortText, searchText))

        adapter = PhoneAdapter(phoneViewModel.getPhoneList())
        adapter.itemClick = object : PhoneAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                val intent = Intent(activity, PhoneDetailActivity::class.java)
                val currentItem = phoneViewModel.getPhoneList()[position]

                intent.putExtra("id", currentItem.id)
                intent.putExtra("name", currentItem.name)
                intent.putExtra("phone", currentItem.phone)
                startActivityForResult(intent, phoneDeleteRequestCode)
            }
        }

        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(getContext())
    }

    private fun changeList() {
        phoneViewModel.getPhoneList().clear()
        phoneViewModel.getPhoneList().addAll(getPhoneNumbers(sortText, searchText))

        adapter.notifyDataSetChanged()
    }

    private fun setRadioListener(view: View) {
        radiogroup_sort_by.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radio_asc -> sortText = "asc"
                R.id.radio_desc -> sortText = "desc"
            }
            changeList()
        }
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

        Log.i("PhoneFragment", "${list.size}")
        return list
    }

    private fun isPermitted(): Boolean {
        return permissions.all { perm ->
            context?.let {
                checkSelfPermission(
                    it,
                    perm
                )
            } == PackageManager.PERMISSION_GRANTED
        }
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
        } else if (requestCode == writeContactRequestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(context, PhoneAddActivity::class.java)
                startActivityForResult(intent, newPhoneAddActivityRequestCode)
                adapter.notifyDataSetChanged()
            }
        }
    }
}