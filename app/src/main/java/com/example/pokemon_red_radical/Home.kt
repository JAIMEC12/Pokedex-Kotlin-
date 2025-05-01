package com.example.pokemon_red_radical

import adapter.MedalsUserAdapter
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pokemon_red_radical.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import models.PokeUserModel


class Home : AppCompatActivity() {

    //VARIABLES QUE SE VAN A INICIALIZAR LUEGO
    private lateinit var binding : ActivityMainBinding
    private lateinit var userMedal : MedalsUserAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // REFERENCIA: https://developer.android.com/training/wearables/apps/splash-screen
        //LLAMA A LA PANTALLA SPLASHSCREEN
        installSplashScreen()
        //GENERA UN ENLACE CON LA VISTA DEL ACTIVITYMAIN
        binding = ActivityMainBinding.inflate(layoutInflater)

        //INDICA QUE EL CONTENIDO VA A SER EL RAIZ DE LA VISTA ENLAZADA
        setContentView(binding.root)

        //ASIGNO MI TOOLBAR COMO LA ACTION BAR DE LA ACTIVIDAD
        setSupportActionBar(findViewById(R.id.my_toolbar))


        //REFERENCIAS : https://www.youtube.com/watch?v=MUl19ppdu0o
        //BUSCO LA BARRA DE NAVEGACIÓN EN EL LAYOUT, TAMBIÉN SE PUDO HACER COMO BINDING.NAVIGATOR_BAR_BOTTOM
        val bottomNavBar = findViewById<BottomNavigationView>(R.id.navigator_bar_bottom)
        //SE INDICA EL ÍTEM QUE DEBE ESTAR SELECCIONADO EN LA ACTIVIDAD ACTUAL
        bottomNavBar.setSelectedItemId(R.id.icon_home)


        //LANZA UN MENSAJE EN LA APLICACION PARA INDICAR QUE LA APLICACION FUNCIONA CON INTERNET
        //Toast.makeText(this,"LA APLICACION FUNCIONA CON INTERNET", Toast.LENGTH_SHORT).show()

        //INICIALIZA LA INTERFAZ
        initUI()

        //REFERENCIAS : https://www.youtube.com/watch?v=MUl19ppdu0o
        //SE AGREGA UN LISTENER A LA BARRA DE NAVEGACIÓN PARA DETECTAR QUE ITEM FUE CLICKEADO
        bottomNavBar.setOnItemSelectedListener { item ->
            when (item.itemId){
                R.id.icon_home ->
                    return@setOnItemSelectedListener true // SI FUE CLICKEADO HOME, NO PASA NINGUNA ACCION

                R.id.icon_item ->{
                    val intent = Intent(this,Items::class.java)//SI FUE CLICKEADO EL ICONO ITEM,
                        // INICIA SU ACTIVIDAD
                    startActivity(intent)//LA INICIALIZA
                    return@setOnItemSelectedListener true// Y SE RETORNA TRUE AL LISTERNER
                }
                R.id.icon_pokedex ->{
                    val intent = Intent(this,Pokedex::class.java)//SI FUE CLICKEADO POKEDEX,
                        // SE INICIA LA ACTIVIDAD POKEDEX
                    startActivity(intent)//LA INICIALIZA
                    return@setOnItemSelectedListener true//Y SE RETORNA TRUE AL LISTENER
                }
                R.id.icon_boss ->{
                    val intent = Intent(this,Bosses::class.java)// SI FUE CLICKEADO BOSS
                        // SE INICIA LA ACTIVIDAD BOSSES
                    startActivity(intent)//LA INICIALIZA
                    return@setOnItemSelectedListener true//Y SE RETORNA TRUE
                }
                else -> //SI NO FUE CLICKEADO NADA, SE RETORNA FALSE
                    return@setOnItemSelectedListener  false
            }
        }
    }

    //FUNCION PARA INICIALIZAR LA INTERFAZ
    private fun initUI(){

        //SE INICIALIZA MODELOVISTA CON LOS DATOS DEL USUARIO ENCONTRADO EN EL ALMACENAMIENTO INTERNO
        val viewModel = PokeUserModel()// VIEWMODEL SERA UN OBJETO DE LA CLASE POKEUSERMODEL
        viewModel.getUserInfo(this)// LLAMA A SU METODO CONSEGUIR INFORMACION DEL USUARIO

        //VARIABLE QUE ALMACENA LA CANTIDAD DE MEDALLAS Y POKEMONES CAPTURADOS
        val totalMedals=viewModel.userInfo.medalsGained.size// CANTIDAD DE MEDALLAS
        val totalPokemon = viewModel.userInfo.pokemonCaptured.size// CANTIDAD DE POKEMONES CAPTURADO

        //VARIABLE QUE ALMACENA EL VALOR DEL PROGRESO DE JUEGO DEL USUARIO DEPENDIENDO
            // DE SU CANTIDAD DE MEDALLAS Y POKEMONES CAPTURADOS
        val progressNumber = (((totalMedals.toDouble()/8)/2) + ((totalPokemon.toDouble()/1300)/2)) *100 //

        //EXTRACCION DE TEXTO PARA CONCATENERLO
        val medalsGained= binding.medalsGained.text // EXTRAE EL TEXTO DEL TEXTVIEW
        val pokemonsCaptured = binding.pokemonCaptured.text // EXTRAE EL TEXTO DEL TEXTVIEW

        //INICIALIZA EL ADAPTER PARA LA LISTA DE MEDALLAS
        userMedal = MedalsUserAdapter()

        //REFERENCIAS https://developer.android.com/develop/ui/views/layout/recyclerview?hl=es-419#:~:text=RecyclerView%20es%20el%20ViewGroup%20que,un%20objeto%20contenedor%20de%20vistas

        //INDICA EL RECYCLER VIEW COMO SE VA A DISTRIBUIR SUS ITEMS
        binding.userMedalList.layoutManager =
            GridLayoutManager(this,2,
                GridLayoutManager.HORIZONTAL,false) // AL SER GRID SE DISTRIBUIRAN ENTRE COLUMNAS Y FILAS

        binding.userMedalList.adapter=userMedal //ENLAZA EL RECYCLER VIEW CON EL ADAPTER

        //SE ENVIA LOS DATOS A LA RECYCLER VIEW. EL NOMBRE E IMAGEN DE LA MEDALLA
        userMedal.setData(viewModel.userInfo.medalsGained, viewModel.userInfo.typeMedal)


        //SI LA CANTIDAD DE MEDALLAS ES SUPERIOR A 0
        if(totalMedals>0){
            binding.lackMedalsTitle.visibility = View.INVISIBLE //EL MENSAJE DE "SIN MEDALLAS" DESAPARECE
        }


        //A CADA TEXT VIEW SE LE AGREGA SU TEXTO RESPECTIVO
        var mensajeComplete = "$medalsGained $totalMedals"

        binding.medalsGained.text =  mensajeComplete

        mensajeComplete = pokemonsCaptured.toString() + " " + viewModel.userInfo.pokemonCaptured.size.toString()

        binding.pokemonCaptured.text= mensajeComplete

        binding.progressNumber.text= String.format("%.2f%%",progressNumber)//COMO SOLO QUIERO PRESENTAR DOS DECIMALES,
            //SE LE DA UN FORMATO AL TEXTO (STRING)

        //ASIGNO EL PROGRESO DE LA BARRA
        binding.progressBar.progress =  progressNumber.toInt()//

    }


    override fun onDestroy() {
        super.onDestroy()
    }


}