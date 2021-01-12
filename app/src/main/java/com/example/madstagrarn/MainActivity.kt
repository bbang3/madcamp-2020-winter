package com.example.madstagrarn

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.madstagrarn.dataclass.User
import com.example.madstagrarn.network.DataService
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
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
        val forgot_password = findViewById<TextView>(R.id.forgot_password)
        val login_button = findViewById<Button>(R.id.login_button)
        val login_button_facebook = findViewById<LinearLayout>(R.id.login_button_facebook)
        val signup_button = findViewById<TextView>(R.id.signup_button)

        forgot_password.setOnClickListener {
            Toast.makeText(this@MainActivity, "Don't forget password.", Toast.LENGTH_LONG).show()
        }

        login_button.setOnClickListener {
            dataService.service.loginRequest(false, login_id.text.toString(), login_password.text.toString()).enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        val intent = Intent(this@MainActivity, InsideActivity::class.java)
                        intent.putExtra("User", response.body())
                        startActivity(intent)
                        this@MainActivity.finish()
                    } else {
                        Toast.makeText(this@MainActivity, "User not found", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }

        login_button_facebook.setOnClickListener(View.OnClickListener {
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
        })

        signup_button.setOnClickListener{
            startActivity(Intent(this@MainActivity, SignupActivity::class.java))
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
                dataService.service.loginRequest(true, facebook_id, "").enqueue(object : Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        if (response.isSuccessful) {
                            val intent = Intent(this@MainActivity, InsideActivity::class.java)
                            intent.putExtra("User", response.body())
                            startActivity(intent)
                            this@MainActivity.finish()
                        } else {
                            Toast.makeText(this@MainActivity, "User not found", Toast.LENGTH_LONG).show()
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