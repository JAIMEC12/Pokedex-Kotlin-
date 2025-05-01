package com.example.pokemon_red_radical

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pokemon_red_radical.databinding.ActivityBossesBinding
import adapter.PokeBossesAdapter
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import models.PokeBossesModel

class Bosses : AppCompatActivity() {

    //
    private lateinit var binding : ActivityBossesBinding//
    private lateinit var pokeBossListAdapter : PokeBossesAdapter//
    private lateinit var viewModel : PokeBossesModel//

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //ENLACE DE LA VISTA DE ITEMS PARA ACCEDER A SUS ELEMENTOS
        binding = ActivityBossesBinding.inflate(layoutInflater)//

        //INDICO EL CONTENIDO DE LA ACTIVIDAD EL CUAL EL ELEMENTO RAIZ DEL BINDING.
            //AL ELEMENTO ROOT SIEMPRE ES EL PADRE GENERAL, ES DECIR EL ELEMENTO PRINCIPAL
        setContentView(binding.root)

        //ASIGNO MI TOOLBAR COMO LA ACTION BAR DE LA ACTIVIDAD
        setSupportActionBar(findViewById(R.id.my_toolbar))

        //DEFINO AL VIEWMODEL COMO OBJETO POKEBOSSMODEL
        viewModel = PokeBossesModel()

        //REFERENCIAS : https://www.youtube.com/watch?v=MUl19ppdu0o
        //BUSCO LA BARRA DE NAVEGACIÓN EN EL LAYOUT, TAMBIÉN SE PUDO HACER COMO BINDING.NAVIGATOR_BAR_BOTTOM
        val bottomNavBar = findViewById<BottomNavigationView>(R.id.navigator_bar_bottom)//
        //SE INDICA EL ÍTEM QUE DEBE ESTAR SELECCIONADO EN LA ACTIVIDAD ACTUAL
        bottomNavBar.setSelectedItemId(R.id.icon_boss)//


        //DEFINO UN LISTENER DE TIPO CLICK AL ICONO DE VUELVO ATRAS
        //REFERENCIAS: https://stackoverflow.com/questions/4038479/android-go-back-to-previous-activity
        binding.returnBackIcon.setOnClickListener {
            finish()//FINALIZA LA ACTIVIDAD ACTUAL PARA RETORNAR A LA ACTIVIDAD ANTERIOR
        }

        //INDICA SI NO APARECE LAS IMAGENES, ES UN PROBLEMA DE INTERNET
        /*Toast.makeText(this,
            "SI NO APARECE LAS IMAGENES, REVISE SU CONEXION AL INTERNET",
            Toast.LENGTH_LONG).show()*/

        //INICIALIZO LA INTERFAZ
        initUI()

        //AGREGA UN LISTENER QUE INDICA SI UN ITEM DEL MENU FUE SELECCIONADO Y ENVIA HACIA LA ACTIVIDAD ASOCIADA CON EL ITEM
        bottomNavBar.setOnItemSelectedListener { item ->
            when (item.itemId){
                R.id.icon_home ->{
                    val intent = Intent(this,Home::class.java)
                    startActivity(intent)
                    return@setOnItemSelectedListener true
                }
                R.id.icon_item ->{
                    val intent = Intent(this,Items::class.java)
                    startActivity(intent)
                    return@setOnItemSelectedListener true
                }
                R.id.icon_pokedex ->{
                    val intent = Intent(this,Pokedex::class.java)
                    startActivity(intent)
                    return@setOnItemSelectedListener true
                }
                else ->
                    return@setOnItemSelectedListener  true
            }
        }
    }


    private fun initUI(){

        //EXTRAIGO INFORMACION ACERCA DE LOS JEFES DE GIMNASIO
        viewModel.getBosses(this)

        //REFERENCIAS https://developer.android.com/develop/ui/views/layout/recyclerview?hl=es-419#:~:text=RecyclerView%20es%20el%20ViewGroup%20que,un%20objeto%20contenedor%20de%20vistas

        //SE INDICA COMO SE VA A DISTRIBUIR LA RECYCLER VIEW
        binding.bossList.layoutManager= GridLayoutManager(this,2)//LOS ELEMENTOS VAN ESTAR UBICADO EN FORMA DE GRILLAS
        pokeBossListAdapter = PokeBossesAdapter {
            val intent = Intent(this,BossInfo::class.java)//SE DEFINE EL INTENT
            intent.putExtra("id",it)//SE INICIALIZA
            startActivity(intent)//COMIENZA LA ACTIVIDAD
        }//INICALIZA EL ADAPTADOR, PASA UNA FUNCION ANONIMA COMO PARAMETRO AL CONSTRUCTOR LO CUAL SE ACTIVA AL CLIC
        binding.bossList.adapter = pokeBossListAdapter//SE ENLAZA EL ADAPTADOR CON LA RECYCLER VIEW
        pokeBossListAdapter.setData(viewModel.bossesList)//SE LLENA LA RECYCLER VIEW CON DATOS
    }
}