package com.javierovico.ici.fragmentos


import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.javierovico.ici.LoginActivity
import com.javierovico.ici.R
import com.javierovico.ici.extra.MiCallBack
import com.javierovico.ici.extra.TokenManager
import com.javierovico.ici.extra.entities.PostResponse
import com.javierovico.ici.extra.entities.User
import com.javierovico.ici.extra.network.ApiService
import com.javierovico.ici.extra.network.RetrofitBuilder
import kotlinx.android.synthetic.main.fragmento_perfil_content_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
class FragmentoPerfil : Fragment() {

    val tokenManager: TokenManager by lazy {
        TokenManager.getInstance(context?.getSharedPreferences("prefs", MODE_PRIVATE))
    }

    lateinit var call: Call<User?>
    val service: ApiService by lazy {
        RetrofitBuilder.createServiceWithAuth(ApiService::class.java, tokenManager)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragmento_perfil_content_profile, container, false)
        view.findViewById<Button>(R.id.iv_cerrar_sesion).setOnClickListener {
            service.logout().enqueue(MiCallBack{ s: String?, mutableMap: MutableMap<String, Any?>? ->
                if(s== null){
                    tokenManager.deleteToken()
                    startActivity(Intent(context, LoginActivity::class.java))
                    activity?.finish()
                }else{
                    Toast.makeText(context,"error: $s",Toast.LENGTH_SHORT).show()
                }
            })
        }

        call = service.user()
        call.enqueue(object : Callback<User?> {
            override fun onResponse(call: Call<User?>, response: Response<User?>) {
                Log.d("javierlog", "onResponse: $response")
                if (response.isSuccessful) {
                    val usuario = response.body()?: return
                    view.findViewById<TextView>(R.id.iv_nombre_apellido).text = String.format("%s %s",usuario.name , usuario.apellido)
                    view.findViewById<TextView>(R.id.iv_telefono).text = usuario.telefono
                    view.findViewById<TextView>(R.id.iv_email).text = usuario.email
                    view.findViewById<TextView>(R.id.iv_cedula).text = usuario.cedula.toString()
                    view.findViewById<TextView>(R.id.iv_habilitado).text = if(usuario.habilitado == 0){ "NO"}else{"SI"}

                } else {
                    tokenManager.deleteToken()
                    startActivity(Intent(context, LoginActivity::class.java))
                    activity?.finish()
                }
            }

            override fun onFailure(call: Call<User?>, t: Throwable) {
                Log.d("javierlog", "onFailure: " + t.message)
            }
        })
        return view
    }


}
