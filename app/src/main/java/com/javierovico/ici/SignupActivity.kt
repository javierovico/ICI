package com.javierovico.ici

import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.javierovico.ici.extra.Utils
import com.javierovico.ici.extra.entities.AccessToken
import com.javierovico.ici.extra.network.ApiService
import com.javierovico.ici.extra.network.RetrofitBuilder
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.activity_signup.input_email
import kotlinx.android.synthetic.main.activity_signup.input_password
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        btn_signup.setOnClickListener { 
            signup()
        }
        link_login.setOnClickListener { 
            finish()
        }
    }

    fun signup() {
        if (!validate()) {
            onSignupFailed()
            return
        }
        btn_signup.isEnabled = false
        val progressDialog = ProgressDialog(
            this@SignupActivity,
            R.style.AppTheme_Dark_Dialog
        )
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Creando cuenta...")
        progressDialog.show()
        val name: String = input_name.text.toString()
        val apellido = input_apellido.text.toString()
        val cedula: String = input_documento.text.toString()
        val email: String = input_email.text.toString()
        val mobile: String = input_mobile.text.toString()
        val password: String = input_password.text.toString()

        val service = RetrofitBuilder.createService(ApiService::class.java)
        val call = service.signup(email,password,password,name,apellido,cedula,mobile)
        call.enqueue(object : Callback<Any?> {
            override fun onResponse(call: Call<Any?>, response: Response<Any?>) {
                Log.d("javierlog", "onResponse: $response")
                progressDialog.dismiss()
                if (response.isSuccessful) {
                    Toast.makeText(applicationContext,"Se creo exitosamente",Toast.LENGTH_SHORT).show()
                    onSignupSuccess()
                }else if(response.code() == 401){
                    onSignupFailed()
                    Toast.makeText(applicationContext,"Mensaje: "+ Utils.converErrors(response.errorBody()).message,Toast.LENGTH_LONG).show()
                }else{
                    onSignupFailed()
                    Toast.makeText(applicationContext,"Mensaje: "+ Utils.converErrors(response.errorBody()).message,Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Any?>, t: Throwable) {
                progressDialog.dismiss()
                Log.w("javierlog", "onFailure: " + t.message)
                Toast.makeText(applicationContext,"No dispone de internet",Toast.LENGTH_SHORT).show()
            }
        })


        // TODO: Implement your own signup logic here.
//        Handler().postDelayed(
//            {
//                // On complete call either onSignupSuccess or onSignupFailed
//                // depending on success
//                onSignupSuccess()
//                // onSignupFailed();
//                progressDialog.dismiss()
//            }, 3000
//        )
    }


    fun onSignupSuccess() {
        btn_signup.isEnabled = true
        setResult(RESULT_OK, null)
        finish()
    }

    fun onSignupFailed() {
        Toast.makeText(baseContext, "Crecion fallida", Toast.LENGTH_LONG).show()
        btn_signup.isEnabled = true
    }

    fun validate(): Boolean {
        var valid = true
        val name: String = input_name.text.toString()
        val apellido = input_apellido.text.toString()
        val cedula: String = input_documento.text.toString()
        val email: String = input_email.text.toString()
        val mobile: String = input_mobile.text.toString()
        val password: String = input_password.text.toString()
        val reEnterPassword: String = input_reEnterPassword.text.toString()
        if(apellido.isEmpty() || apellido.length<3){
            input_apellido.error = "Al menos 3 caracteres"
        }else{
            input_apellido.error = null
        }
        if (name.isEmpty() || name.length < 3) {
            input_name.error = "Al menos tres caracteres"
            valid = false
        } else {
            input_name.error = null
        }
        if (cedula.isEmpty()) {
            input_documento.error = "No ingresaste tu cedula"
            valid = false
        } else {
            input_documento.error = null
        }
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            input_email.error = "Email invalido"
            valid = false
        } else {
            input_email.error = null
        }
        if (mobile.isEmpty() || mobile.length != 10) {
            input_mobile.error = "Ingresa un telefono valido"
            valid = false
        } else {
            input_mobile.error = null
        }
        if (password.isEmpty() || password.length < 4 || password.length > 10) {
            input_password.error = "Contrasenha de al menos 4 caracteres, por favor"
            valid = false
        } else {
            input_password.error = null
        }
        if (reEnterPassword.isEmpty() || reEnterPassword.length < 4 || reEnterPassword.length > 10 || reEnterPassword != password) {
            input_reEnterPassword.error = "No coincide con la conrasenha anterior"
            valid = false
        } else {
            input_reEnterPassword.error = null
        }
        return valid
    }
}
