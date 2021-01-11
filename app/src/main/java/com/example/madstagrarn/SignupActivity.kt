package com.example.madstagrarn

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.madstagrarn.network.DataService
import com.facebook.*
import com.facebook.login.Login
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class SignupActivity : AppCompatActivity() {
    private val dataService: DataService = DataService()
    private var callbackManager: CallbackManager? = null
    lateinit var signup_id: EditText
    lateinit var signup_password: EditText
    lateinit var signup_name: EditText
    lateinit var signup_phonenumber: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        val back_button = findViewById<ImageView>(R.id.back_button)
        val signup_button_facebook = findViewById<LinearLayout>(R.id.signup_button_facebook)
        val signup_button = findViewById<Button>(R.id.signup_button)
        signup_id = findViewById(R.id.signup_id)
        signup_password = findViewById(R.id.signup_password)
        signup_name = findViewById(R.id.signup_name)
        signup_phonenumber = findViewById(R.id.signup_phonenumber)

        back_button.setOnClickListener {
            finish()
        }

        signup_button_facebook.setOnClickListener {
            if (signup_name.text.toString().replace(" ", "").equals("") || signup_phonenumber.text.toString().replace(" ", "").equals("")) {
                Toast.makeText(this@SignupActivity, "Name and Phone number are needed", Toast.LENGTH_LONG).show()
            } else {
                callbackManager = CallbackManager.Factory.create()
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"))
                LoginManager.getInstance().registerCallback(callbackManager,
                        object : FacebookCallback<LoginResult> {
                            override fun onSuccess(loginResult: LoginResult) {
                                val accessToken = loginResult.accessToken
                                requestMe(accessToken)
                            }

                            override fun onCancel() {
                            }

                            override fun onError(error: FacebookException?) {
                            }
                        }
                )
            }
        }

        signup_button.setOnClickListener {
            if (signup_id.text.toString().replace(" ", "").equals("")
                    || signup_password.text.toString().replace(" ", "").equals("")
                    || signup_name.text.toString().replace(" ", "").equals("")
                    || signup_phonenumber.text.toString().replace(" ", "").equals("")) {
                Toast.makeText(this@SignupActivity, "Fill every field", Toast.LENGTH_LONG).show()
            } else {
                dataService.service.signupRequest(false, signup_id.text.toString(), signup_password.text.toString(), signup_name.text.toString(), signup_phonenumber.text.toString()).enqueue(object : Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        if (response.isSuccessful) {
                            startActivity(Intent(this@SignupActivity, MainActivity::class.java))
                            this@SignupActivity.finish()
                        } else {
                            Toast.makeText(this@SignupActivity, "Id already exist", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode, resultCode, data)
    }

    fun requestMe(accessToken: AccessToken) {
        val request = GraphRequest.newMeRequest(accessToken) { `object`, response ->
            try {
                var facebook_id = `object`.getString("id")
                Toast.makeText(this@SignupActivity, facebook_id, Toast.LENGTH_LONG).show()
                dataService.service.signupRequest(true, facebook_id, "", signup_name.text.toString(), signup_phonenumber.text.toString()).enqueue(object : Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        if (response.isSuccessful) {
                            startActivity(Intent(this@SignupActivity, MainActivity::class.java))
                            this@SignupActivity.finish()
                        } else {
                            Toast.makeText(this@SignupActivity, "Account already exist", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
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