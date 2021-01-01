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
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_one.*
import android.content.ContentResolver;
import android.provider.CalendarContract.Attendees.*
import android.provider.CalendarContract.Reminders.*
import androidx.core.content.ContextCompat.checkSelfPermission

data class Phone(val id:String?, val name:String?, val phone:String?)

class OneFragment : Fragment() {
    val permissions = arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE)
    lateinit var adapter: PhoneAdapter
    var list = mutableListOf<Phone>()
    var searchText = ""
    var sortText = "asc"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_one, container, false)
        checkAndStart()
        return view
    }

    fun startProcess() {
        setList()
        setSearchListener()
//        setRadioListener()
    }

    fun setList() {
        list.addAll(getPhoneNumbers(sortText, searchText))
        adapter = PhoneAdapter(list)
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(getContext())
    }

    fun setSearchListener() {
        editSearch.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchText = s.toString()
                changeList()
            }
        })
    }

    /*fun setRadioListener() {
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId) {
                R.id.radioAsc -> sortText = "asc"
                R.id.radioDsc -> sortText = "desc"
            }
            changeList()
        }
    }*/

    fun changeList() {
        val newList = getPhoneNumbers(sortText, searchText)
        this.list.clear()
        this.list.addAll(newList)
        this.adapter.notifyDataSetChanged()
    }

    fun getPhoneNumbers(sort:String, searchName:String?) : List<Phone> {
        val list = mutableListOf<Phone>()
        // 1. 전화번호 Uri
        val phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        // 2.1 전화번호에서 가져올 컬럼 정의
        val projections = arrayOf(
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID
            , ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
            , ContactsContract.CommonDataKinds.Phone.NUMBER)
        var wheneClause:String? = null
        var whereValues:Array<String>? = null
        // searchName에 값이 있을 때만 검색을 사용한다
        if(searchName?.isNotEmpty() ?: false) {
            wheneClause = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " like ?"
            whereValues = arrayOf("%$searchName%")
        }
        // 2.3 정렬쿼리 사용
        val optionSort = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " $sort"
        // 3. 테이블에서 주소록 데이터 쿼리
        val cursor = getActivity()?.contentResolver?.query(phoneUri, projections, wheneClause, whereValues, optionSort)

        // 4. 반복문으로 아이디와 이름을 가져오면서 전화번호 조회 쿼리를 한번 더 돌린다.
        while(cursor?.moveToNext()?:false) {
            val id = cursor?.getString(0)
            val name = cursor?.getString(1)
            var number = cursor?.getString(2)
            // 개별 전화번호 데이터 생성
            val phone = Phone(id, name, number)
            // 결과목록에 더하기
            list.add(phone)
        }
        // 결과목록 반환
        return list
    }

    // 권한처리 코드
    fun checkAndStart() {
        if( isLower23() || isPermitted()) {
            startProcess()
        } else {
            requestPermissions( permissions, 99)
        }
    }

    fun isLower23() : Boolean{
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun isPermitted():Boolean {
        for(perm in permissions) {
            if(getContext()?.let { checkSelfPermission(it, perm) } != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == 99) {
            var check = true
            for(grant in grantResults) {
                if(grant != PackageManager.PERMISSION_GRANTED) {
                    check = false
                    break
                }
            }
            if(check) startProcess()
            else {
                Toast.makeText(getContext(), "권한 승인을 하셔야지만 앱을 사용할 수 있습니다.", Toast.LENGTH_LONG).show()
//                finish()
            }
        }
    }
}