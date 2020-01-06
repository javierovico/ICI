package com.javierovico.ici

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.javierovico.ici.extra.TokenManager
import com.javierovico.ici.extra.Utils
import com.javierovico.ici.extra.entities.AccessToken
import com.javierovico.ici.extra.network.ApiService
import com.javierovico.ici.extra.network.RetrofitBuilder
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    lateinit var tokenManager: TokenManager
    lateinit var call: Call<AccessToken>
    val service: ApiService by lazy {
        RetrofitBuilder.createService(ApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btn_login.setOnClickListener {
            login()
        }
        link_signup.setOnClickListener {
            startActivity(Intent(this,SignupActivity::class.java))
        }
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE))
        if (tokenManager.token.accessToken != null) {
            if(tokenManager.token?.esAdmin == true){
                startActivity(Intent(this@LoginActivity, AdminActivity::class.java))
            }else if(tokenManager.token?.esDoctor == true){
                startActivity(Intent(this@LoginActivity, MainDoctores::class.java))
            }else{
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            }
            finish()
        }
    }


    fun login() {
        if (!validate()) {
            onLoginFailed()
            return
        }
        btn_login.isEnabled = false
        val progressDialog = ProgressDialog(
            this@LoginActivity,
            R.style.AppTheme_Dark_Dialog
        )
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Autenticando...")
        progressDialog.show()
        val email: String = input_email.text.toString()
        val password: String = input_password.text.toString()

        call = service.login(email, password)
        call.enqueue(object : Callback<AccessToken?> {
            override fun onResponse(call: Call<AccessToken?>, response: Response<AccessToken?>) {
                Log.d("javierlog", "onResponse: $response")
                progressDialog.dismiss()
                if (response.isSuccessful) {
                    tokenManager.saveToken(response.body())
                    onLoginSuccess(response.body())
                }else if(response.code() == 401){
                    Toast.makeText(applicationContext,"Mensaje: "+Utils.converErrors(response.errorBody()).message,Toast.LENGTH_LONG).show()
                    input_email.error = "correo no coincide"
                    input_password.error = "contrasenha no coincide"
                    onLoginFailed()
                }else{
                    onLoginFailed()
                }
            }

            override fun onFailure(call: Call<AccessToken?>, t: Throwable) {
                progressDialog.dismiss()
                Log.w("javierlog", "onFailure: " + t.message)
                Toast.makeText(applicationContext,"No dispone de internet",Toast.LENGTH_SHORT).show()
                onLoginFailed()
            }
        })

        // TODO: Implement your own authentication logic here.
//        Handler().postDelayed(
//            {
//                // On complete call either onLoginSuccess or onLoginFailed
//                onLoginSuccess()
//                // onLoginFailed();
//                progressDialog.dismiss()
//            }, 3000
//        )
    }



    fun validate(): Boolean {
        var valid = true
        val email: String = input_email.text.toString()
        val password: String = input_password.text.toString()
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            input_email.error = "Introduci tu correo"
            valid = false
        } else {
            input_email.error = null
        }
        if (password.isEmpty() || password.length < 4 || password.length > 10) {
            input_password.error = "Debe tener entre 4 y 10 caracteres"
            valid = false
        } else {
            input_password.error = null
        }
        return valid
    }

    fun onLoginSuccess(access: AccessToken?) {
        btn_login.isEnabled = true
        startActivity(Intent(this,if(access?.esAdmin == true){AdminActivity::class.java}else{MainActivity::class.java}))
        finish()
    }

    fun onLoginFailed() {
        Toast.makeText(baseContext, "Fallo el inicio de sesion", Toast.LENGTH_LONG).show()
        btn_login.isEnabled = true
    }
}
