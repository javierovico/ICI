package com.javierovico.ici.fragmentos


import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.javierovico.ici.R
import com.javierovico.ici.extra.Adaptador
import com.javierovico.ici.extra.MiCallBack
import com.javierovico.ici.extra.TokenManager
import com.javierovico.ici.extra.entities.Especialidad
import com.javierovico.ici.extra.entities.User
import com.javierovico.ici.extra.network.ApiService
import com.javierovico.ici.extra.network.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
class EspecialidadFragment : Fragment() {

    val tokenManager: TokenManager by lazy{
        TokenManager.getInstance(context?.getSharedPreferences("prefs", Context.MODE_PRIVATE))
    }

    val service: ApiService by lazy {
        RetrofitBuilder.createServiceWithAuth(ApiService::class.java, tokenManager)
    }

    val adaptador = Adaptador<Especialidad>(R.layout.item_lista_usuarios){
        object: Adaptador.MyHolderN<Especialidad>(it){
            val nombre = it.findViewById<TextView>(R.id.iv_nombre)
            val correo = it.findViewById<TextView>(R.id.iv_correo)
            val more = it.findViewById<ImageView>(R.id.av_more)
            override fun bindView(dato: Especialidad, position: Int, adapter: Adaptador<Especialidad>) {
                nombre.text = String.format("%s",dato.nombre)
                correo.text = ""
//                more.setOnClickListener{
//                    val popup = PopupMenu(context,more)
//                    popup.menu.add(Menu.NONE,1,1,"Eliminar")
//                    popup.menu.add(Menu.NONE,2,2,if(dato.doctor == null){"Establecer como Doctor"}else{"Quitar como doctor"})
//                    popup.setOnMenuItemClickListener {menuPulsado ->
//                        if(menuPulsado.itemId == 1){ //eliminar
//                            service.borrar(dato.id!!).enqueue(object: Callback<MutableMap<String, Any?>?> {
//                                override fun onFailure(call: Call<MutableMap<String, Any?>?>, t: Throwable) {
//                                    Toast.makeText(context,t.message, Toast.LENGTH_SHORT).show()
//                                }
//
//                                override fun onResponse(call: Call<MutableMap<String, Any?>?>, response: Response<MutableMap<String, Any?>?>) {
//                                    if(response.isSuccessful){
//                                        val respuesta = response.body()
//                                        Log.d("javierlog",respuesta.toString())
//                                        if(respuesta!= null && respuesta["deleted"]!=null){
//                                            adapter.borrarUno(position)
//                                        }else{
//                                            Toast.makeText(context,"error: codigo: "+respuesta.toString(),
//                                                Toast.LENGTH_SHORT).show()
//                                        }
//                                    }else{
//                                        Toast.makeText(context,"error: codigo: "+response.code(),
//                                            Toast.LENGTH_SHORT).show()
//                                    }
//                                }
//
//                            })
//                            adapter.notifyItemRemoved(position)
//                        }else if(menuPulsado.itemId == 2){
//                            if(dato.doctor == null){
//                                service.crearDoctor(dato.cedula!!.toLong()).enqueue(MiCallBack { err: String?, respuesta: MutableMap<String, Any?>? ->
//                                    if(err == null){
//                                        Toast.makeText(context,"REALIZADO CORRECTAMENTE", Toast.LENGTH_SHORT).show()
//                                        dato.doctor = "doctor"  //para marcar mientras
//                                        adapter.notifyItemChanged(position)
//                                    }else{
//                                        Toast.makeText(context,"NO se pudo realizar", Toast.LENGTH_SHORT).show()
//                                    }
//                                })
//                            }else{
//                                service.sacarDoctor(dato.cedula!!.toLong()).enqueue(MiCallBack { err: String?, respuesta: MutableMap<String, Any?>? ->
//                                    if(err == null){
//                                        Toast.makeText(context,"REALIZADO CORRECTAMENTE", Toast.LENGTH_SHORT).show()
//                                        dato.doctor = null //para marcar mientras
//                                        adapter.notifyItemChanged(position)
//                                    }else{
//                                        Toast.makeText(context,"NO se pudo realizar", Toast.LENGTH_SHORT).show()
//                                    }
//                                })
//                            }
//                        }
//                        true
//                    }
//                    popup.show()
//                }
            }

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root= inflater.inflate(R.layout.fragment_especialidad, container, false)

        root.findViewById<RecyclerView>(R.id.iv_lista).run{
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
            adapter = adaptador
        }
        service.especialidades(tokenManager.token.cedula).enqueue(object: Callback<MutableList<Especialidad>>{
            override fun onFailure(call: Call<MutableList<Especialidad>>, t: Throwable) {
                Toast.makeText(context,t.message,Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<MutableList<Especialidad>>, response: Response<MutableList<Especialidad>>) {
                if(response.isSuccessful){
                    adaptador.agregarTodo(response.body()?: mutableListOf())
                }else{
                    Toast.makeText(context,"error: codigo: "+response.code(),Toast.LENGTH_SHORT).show()
                }
            }
        })
        return root
    }


}
