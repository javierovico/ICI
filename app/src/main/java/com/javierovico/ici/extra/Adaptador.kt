package com.javierovico.ici.extra

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView

open class Adaptador<TD>(private val resource: Int, val lista: MutableList<TD>, val factory: (View) -> MyHolderN<TD>): RecyclerView.Adapter<Adaptador.MyHolderN<TD>>() {

    constructor(resource: Int,factory: (View) -> MyHolderN<TD>) : this(resource, mutableListOf<TD>(),factory)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderN<TD> {
        return factory(LayoutInflater.from(parent.context).inflate(resource,parent,false))
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyHolderN<TD>, position: Int) {
        holder.bindView(lista[position]!!,position, this)
    }

    fun agregarTodo(listaR: MutableList<TD>) {
        lista.clear()
        lista.addAll(listaR)
        notifyDataSetChanged()
    }

    fun agregarUno(datoAgregar: TD){
        lista.add(datoAgregar)
        notifyItemInserted(lista.size-1)
    }

    fun borrarUno(borrar: Int){
        lista.removeAt(borrar)
        notifyItemRemoved(borrar)
    }

    fun sacarUno(position: Int){
        lista.removeAt(position)
        notifyItemRemoved(position)
    }

    fun notificarCambio(position: Int){
        notifyItemChanged(position)
    }

    abstract class MyHolderN<TD>(view: View): RecyclerView.ViewHolder(view){
        internal val layout: View = view
        abstract fun bindView(dato: TD,position: Int, adapter: Adaptador<TD>)
    }

//    abstract class MyHolderMaps<TD>(view: View, mapaRecurso: Int,val contexto: Context): MyHolderN<TD>(view), OnMapReadyCallback{
//        private val mapView: MapView = layout.findViewById(mapaRecurso)
//        lateinit var map: GoogleMap
//        public var dato: TD? = null
//
//        abstract fun setMapLocation()
//
//        init {
//            mapView.onCreate(null)
//            mapView.getMapAsync(this)
//        }
//
//        @CallSuper
//        override fun bindView(dato: TD, position: Int, adapter: Adaptador<TD>) {
//            this.dato = dato
//            if(!::map.isInitialized) return
//            setMapLocation()
//        }
//
//        override fun onMapReady(googleMap: GoogleMap?) {
//            MapsInitializer.initialize(contexto)
//            map = googleMap ?: return
//            setMapLocation()
//        }
//
//        fun clearView() {
//            with(map) {
//                clear()
//                mapType = GoogleMap.MAP_TYPE_NONE
//            }
//        }
//    }

    abstract class MyHolderSimple<TD>(view: View): RecyclerView.ViewHolder(view){
        internal val layout: View = view
        abstract fun bindView(dato: TD)
    }
}