package com.example.pokemon_red_radical

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokemon_red_radical.databinding.ActivityPokedexBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import adapter.PokeListAdapter
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.lifecycle.Observer
import models.PokeListViewModel

//REFERENCIAS DE https://www.youtube.com/watch?v=oKuqqM5oolk&list=LL&index=1&t=137s

class Pokedex : AppCompatActivity() {

    //
    private lateinit var viewModel: PokeListViewModel//
    private lateinit var binding: ActivityPokedexBinding//
    private lateinit var pokeListAdapter: PokeListAdapter//

    private var limitList : Int = 0//


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ////CREA UN ENLACE CON LA VISTA DEL ACTIVITYPOKEDEX
        binding =ActivityPokedexBinding.inflate(layoutInflater)

        ////INDICA QUE EL CONTENIDO VA A SER EL RAIZ DE LA VISTA ENLAZADA
        setContentView(binding.root)

        ////ASIGNO MI TOOLBAR COMO LA ACTION BAR DE LA ACTIVIDAD
        setSupportActionBar(findViewById(R.id.my_toolbar))

        //CREA UN VIEWMODEL Y LO ASOCIA CON LA ACTIVIDAD ACTUAL, ESPECFICA LA ACTIVIDAD, EN ESTE CASO THIS, Y EL MODELO A QUERER ASOCIAR
        viewModel = ViewModelProvider(this)[PokeListViewModel::class.java]//

        //REFERENCIAS : https://www.youtube.com/watch?v=MUl19ppdu0o
        //BUSCO LA BARRA DE NAVEGACIÓN EN EL LAYOUT, TAMBIÉN SE PUDO HACER COMO BINDING.NAVIGATOR_BAR_BOTTOM
        val bottomNavBar = findViewById<BottomNavigationView>(R.id.navigator_bar_bottom)
        //SE INDICA EL ÍTEM QUE DEBE ESTAR SELECCIONADO EN LA ACTIVIDAD ACTUAL
        bottomNavBar.setSelectedItemId(R.id.icon_pokedex)


        //DEFINO UN LISTENER DE TIPO CLICK AL ICONO DE VUELVO ATRAS
        //REFERENCIAS: https://stackoverflow.com/questions/4038479/android-go-back-to-previous-activity
        binding.returnBackIcon.setOnClickListener {
            finish()//FINALIZA LA ACTIVIDAD ACTUAL, VOLVIENDO A LA ANTERIOR
        }

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
                R.id.icon_boss ->{
                    val intent = Intent(this,Bosses::class.java)
                    startActivity(intent)
                    return@setOnItemSelectedListener true
                }
                else ->
                    return@setOnItemSelectedListener  true
            }
        }

        checkInternet()

    }


    private fun initUI(){

        //REALIZA EL LLAMADO A LA API PARA SACAR LA LISTA DE POKEMONES CON UN LIMITE EN ESPECIFICO DE RESULTADOS
        viewModel.getPokemonList(limitList)

        //REFERENCIAS https://developer.android.com/develop/ui/views/layout/recyclerview?hl=es-419#:~:text=RecyclerView%20es%20el%20ViewGroup%20que,un%20objeto%20contenedor%20de%20vistas

        //SE LE INDICA AL RECYCLER VIEW COMO VAN A ESTAR DISTRIBUIDOS SUS ELEMTNOS
        val layoutManager= GridLayoutManager(this,2)//EN FORMATO DE GRILLAS CON 2 COLUMNAS
        binding.pokedexList.layoutManager = layoutManager//SE LO PASA AL ADAPTER DEL RECYCLER VIEW
        pokeListAdapter = PokeListAdapter {
            val intent = Intent(this,PokemonInfo::class.java)
            intent.putExtra("id",it) //SE PASA EL CLICK DEL POKEMON IDENTIFICADO
            startActivity(intent)
        }//PASA UNA FUNCION ANONIMA COMO PARAMETRO AL CONSTRUCTOR LO CUAL SE ACTIVA AL CLICK

        //VINCULAR EL RECYCLER VIEW CON EL ADAPTADOR
        binding.pokedexList.adapter = pokeListAdapter

        //ESTABLECE A LA ACTIVIDAD ACTUAL COMO UN OBSERVADOR A LA LISTA MUTABLE DEL VIEWMODEL
        //ENTONCES CUANDO OCURRA ALGUN CAMBIO, SE NOTIFICA AL OBSERVADOR Y EJECUTA LA ACCION CORRESPONDIENTE
        //ACTUALIZAR EL ADAPTADOR DE LA RECYCLER VIEW CON LOS NUEVOS DATOS AÑADIDOS
        viewModel.pokemonList.observe(this) { list ->
            pokeListAdapter.setData(list)
        }


        //LISTENER PARA EL DESPLAZAMIENTO DE LA RECYCLER
        binding.pokedexList.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(dy>0){ //SI SE DESPLAZA DEL COMIENZO
                    val visiblePokemons= layoutManager.childCount //TOTAL DE ITEMS EN PANTALLA
                    val pastPokemons = layoutManager.findFirstVisibleItemPosition()//PRIMERA POSICION DEL ITEM
                    val totalPokemons= layoutManager.itemCount//TOTAL DE ITEMS DE LA RECYCLER
                    if(visiblePokemons+pastPokemons>=totalPokemons){ //SI YA LLEGO AL MAXIMO DE ITEMS AL DESPLAZARSE
                        limitList+=20 //SE AUMENTA EL LIMITE DE RESULTADOS
                        viewModel.getPokemonList(limitList)//Y SE VUELVE A LLAMAR A LA POKEAPI PARA EXTRAER NUEVAMENTE RESULTADOS
                            //CON EL NUEVO LIMITE. AGREGA NUEVO POKEMONES
                    }
                }
            }
        })


        binding.searchPokemon.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    viewModel.getPokemonListFilter(newText!!)
                }
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.getPokemonListFilter(query!!)
                }
                return true
            }
        })

        viewModel.pokemonFilterList.observe(this, Observer { pokeFilterList ->
            if (pokeFilterList.isNotEmpty()){
                pokeListAdapter.setData(pokeFilterList)
            }
            else{
                viewModel.getPokemonList(limitList)
            }
        })
    }

    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)

        return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun checkInternet(){
        //INICIALIZO LA INTERFAZ
        if (isInternetAvailable(this)) {
            initUI()
        } else {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("No se ha podido conectar con el internet. Reintente de nuevo")
            builder.setTitle("Conexion fallida")
            builder.setPositiveButton("Reintentar"){ dialog , which ->
                checkInternet()
                dialog.dismiss()
            }
            builder.setNegativeButton("Volver"){ dialog , which ->
                dialog.cancel()
            }
            val alertDialog = builder.create()
            alertDialog.show()
        }
    }

    //REVISAR PORQUE ALGUNAS EVOLUCIONES NO COGEN
    //ESPERANZA DE QUE COJAN TODAS
    //TERMINAR LO DE LA CONEXION A INTERNET
    
}

