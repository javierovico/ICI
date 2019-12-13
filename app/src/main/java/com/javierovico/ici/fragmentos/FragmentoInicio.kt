package com.javierovico.ici.fragmentos

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.javierovico.ici.R
import com.javierovico.ici.extra.Adaptador
import com.javierovico.ici.extra.RandomColors
import com.javierovico.ici.tipodato.ItemInicio
import de.hdodenhof.circleimageview.CircleImageView


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [FragmentoInicio.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [FragmentoInicio.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentoInicio : Fragment() {
    private var listener: OnFragmentInteractionListener? = null

    val adaptador = Adaptador<ItemInicio>(R.layout.item_lista_negocios_2, mutableListOf(ItemInicio("Internacion",R.drawable.icono8_1),ItemInicio("Ambulatorio",R.drawable.icono8_2),ItemInicio("Guardia",R.drawable.icono8_3),ItemInicio("Informes",R.drawable.icono8_4))){
        object: Adaptador.MyHolderN<ItemInicio>(it){
            val fondo = it.findViewById<ConstraintLayout>(R.id.iv_fondo)
            val titulo = it.findViewById<TextView>(R.id.iv_titulo)
            val foto = layout.findViewById<CircleImageView>(R.id.iv_foto)
            override fun bindView(dato: ItemInicio, position: Int, adapter: Adaptador<ItemInicio>) {
                fondo.setBackgroundColor(RandomColors().color)
                titulo.text = dato.titulo
                foto.setImageResource(dato.imagenID)

            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_fragmento_inicio, container, false)
        val recycler = view.findViewById<RecyclerView>(R.id.iv_lista).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = adaptador
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        fun interaccion(uri: String)
    }
}
