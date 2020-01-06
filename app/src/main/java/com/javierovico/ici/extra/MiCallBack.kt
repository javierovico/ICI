package com.javierovico.ici.extra
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MiCallBack(val resultado: (String?,MutableMap<String,Any?>?)->(Unit)) : Callback<MutableMap<String,Any?>> {
    override fun onFailure(call: Call<MutableMap<String, Any?>>, t: Throwable) {
        resultado.invoke(t.message?:"",null)
    }

    override fun onResponse(call: Call<MutableMap<String, Any?>>, response: Response<MutableMap<String, Any?>>) {
        if(response.isSuccessful){
            resultado.invoke(null,response.body())
        }else{
            resultado.invoke("code: ${response.code()}",response.body())
        }

    }
}