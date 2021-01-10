package com.example.madstagrarn

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.example.madstagrarn.network.DataService
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.material.tabs.TabLayout
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity() {
    private val dataService: DataService = DataService()
    private var callbackManager: CallbackManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var login_id = findViewById<EditText>(R.id.login_id)
        var login_password = findViewById<EditText>(R.id.login_password)
        val login_button = findViewById<Button>(R.id.login_button)
        val login_button_facebook = findViewById<LinearLayout>(R.id.login_button_facebook)

        login_button.setOnClickListener {
            dataService.service.loginRequest(login_id.text.toString(), login_password.text.toString()).enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if(response.isSuccessful) {
                        val intent = Intent(this@MainActivity, InsideActivity::class.java)
                        intent.putExtra("User", response.body())
                        startActivity(intent)
                        this@MainActivity.finish()
                    }
                    else {
                        Toast.makeText(this@MainActivity, "User not found", Toast.LENGTH_LONG).show()
                    }
                }
                override fun onFailure(call: Call<User>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }

        login_button_facebook.setOnClickListener(View.OnClickListener{
            callbackManager = CallbackManager.Factory.create()
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"))
            LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        val accessToken = loginResult.accessToken
                        requestMe(accessToken)
                        Toast.makeText(this@MainActivity,accessToken.toString(), Toast.LENGTH_SHORT).show()
//                        val jsonObject = JSONObject()
//                        jsonObject.put("LogIn", true)
//                        this@MainActivity.openFileOutput("user.json", Context.MODE_PRIVATE).use { output ->
//                            output.write(jsonObject.toString().toByteArray())
//                        }
                        startActivity(Intent(this@MainActivity, InsideActivity::class.java))
                        this@MainActivity.finish()
                    }
                    override fun onCancel() {
                    }
                    override fun onError(error: FacebookException?) {
                        error?.printStackTrace()
                    }
                }
            )
        })

//        if(check_login()){
//            startActivity(Intent(this, InsideActivity::class.java))
//            finish()
//        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode, resultCode, data)
    }

    private fun check_login(): Boolean {
        var json: String = "{}"
        try {
            val inputStream = this.openFileInput("user.json")
            json = inputStream.bufferedReader().use { it.readText() }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        if(json == "{}")  return false
        else{
            return JSONObject(json).getBoolean("LogIn")
        }
    }

    private fun requestMe(accessToken: AccessToken) {
        val request = GraphRequest.newMeRequest(accessToken) { `object`, response ->
            try {
                Log.e("TAGG", `object`.toString())
                val userEmail = `object`.getString("email")
                Log.e("TAGG", userEmail)
                val userName = `object`.getString("name")
                Log.e("TAGG", userName)
                val jobj1 = `object`.optJSONObject("picture")
                Log.e("TAGG", jobj1.toString())
                val jobj2 = jobj1.optJSONObject("data")
                Log.e("TAGG", jobj2.toString())
                val userPicture = jobj2.getString("url")
                Log.e("TAGG", userPicture)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        val parameters = Bundle()
        parameters.putString("fields", "name,email,picture")
        request.parameters = parameters
        request.executeAsync()
    }
}