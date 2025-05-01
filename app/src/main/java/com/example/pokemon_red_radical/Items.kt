package com.example.pokemon_red_radical

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokemon_red_radical.databinding.ActivityItemsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import adapter.PokeItemListAdapter
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import models.PokeItemListViewModel


//REFERENCIAS DE https://www.youtube.com/watch?v=oKuqqM5oolk&list=LL&index=1&t=137s


class Items : AppCompatActivity() {

    //
    private lateinit var viewModel : PokeItemListViewModel
    private lateinit var binding : ActivityItemsBinding
    private lateinit var pokeItemListAdapter: PokeItemListAdapter
    private var limit : Int =20

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //ENLACE DE LA VISTA DE ITEMS PARA ACCEDER A SUS ELEMENTOS
        binding = ActivityItemsBinding.inflate(layoutInflater)

        //INDICO EL CONTENIDO DE LA ACTIVIDAD EL CUAL EL ELEMENTO RAIZ DEL BINDING.
            //AL ELEMENTO ROOT SIEMPRE ES EL PADRE GENERAL, ES DECIR EL ELEMENTO PRINCIPAL
        setContentView(binding.root)//

        //ASIGNO MI TOOLBAR COMO LA ACTION BAR DE LA ACTIVIDAD
        setSupportActionBar(findViewById(R.id.my_toolbar))//


        //REFERENCIAS : https://www.youtube.com/watch?v=MUl19ppdu0o
        //BUSCO LA BARRA DE NAVEGACIÓN EN EL LAYOUT, TAMBIÉN SE PUDO HACER COMO BINDING.NAVIGATOR_BAR_BOTTOM
        val bottomNavBar = findViewById<BottomNavigationView>(R.id.navigator_bar_bottom)
        //SE INDICA EL ÍTEM QUE DEBE ESTAR SELECCIONADO EN LA ACTIVIDAD ACTUAL
        bottomNavBar.setSelectedItemId(R.id.icon_item)

        ////CREA UN VIEWMODEL Y LO ASOCIA CON LA ACTIVIDAD ACTUAL, ESPECIFICA LA ACTIVIDAD, EN ESTE CASO THIS, Y EL MODELO A QUERER ASOCIAR
        viewModel = ViewModelProvider(this)[PokeItemListViewModel::class.java]

        //DEFINO UN LISTENER DE TIPO CLICK AL ICONO DE VUELVO ATRAS
        //REFERENCIAS: https://stackoverflow.com/questions/4038479/android-go-back-to-previous-activity
        binding.returnBackIcon.setOnClickListener {
            finish()//FINALIZA LA ACTIVIDAD ACTUAL PARA RETORNAR A LA ACTIVIDAD ANTERIOR
        }


        //INDICA AL USUARIO SI LA PANTALLA APARECE EN BLANCO, SIGNIFICA QUE NO HAY CONEXION AL INTERNET
        //Toast.makeText(this,
            //"SI NO APARECE NADA, REVISE SU CONEXION AL INTERNET",
            //Toast.LENGTH_LONG).show()


        //AGREGA UN LISTENER QUE INDICA SI UN ITEM DEL MENU FUE SELECCIONADO Y ENVIA HACIA LA ACTIVIDAD ASOCIADA CON EL ITEM
        bottomNavBar.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.icon_home -> {
                    val intent = Intent(this, Home::class.java)
                    startActivity(intent)
                    return@setOnItemSelectedListener true
                }

                R.id.icon_pokedex -> {
                    val intent = Intent(this, Pokedex::class.java)
                    startActivity(intent)
                    return@setOnItemSelectedListener true
                }

                R.id.icon_boss -> {
                    val intent = Intent(this, Bosses::class.java)
                    startActivity(intent)
                    return@setOnItemSelectedListener true
                }

                else ->//
                    return@setOnItemSelectedListener true
            }
        }
        checkInternet()

    }



    private fun initUI(){

        //HACE EL LLAMADO A LA API PARA CONSEGUIR LA LISTA DE ITEMS
            //SE ENTREGA EL VALOR DE LIMIT COMO PARAMETRO PARA INDICAR EL LIMITE DE RESULTADOS DESEADOS
        viewModel.getPokemonItemList(limit)

        //CREA EL ADAPTER PASANDO COMO PARAMETRO UNA FUNCION LAMBDA
        pokeItemListAdapter = PokeItemListAdapter {
            val intent = Intent(this,ItemInfo::class.java)// SI SE DA CLICK, ENVIA A ESTA ACTIVIDAD
            intent.putExtra("id",it)//ENVIA EL VALOR DE RETORNO DE LA FUNCION, EL ID
            startActivity(intent)//INICIA LA ACTIVIDAD
        }//PASA UNA FUNCION ANONIMA COMO PARAMETRO AL CONSTRUCTOR LO CUAL SE ACTIVA AL CLICK

        //REFERENCIAS https://developer.android.com/develop/ui/views/layout/recyclerview?hl=es-419#:~:text=RecyclerView%20es%20el%20ViewGroup%20que,un%20objeto%20contenedor%20de%20vistas

        //INDICA COMO SE VAN A DISTRIBUIR LOS ELEMENTOS DENTRO DE LA RECYCLER VIEW
        val layoutManager = LinearLayoutManager(this)// SE DISTRIBUIRA DE MANERA LINEAL, POR DEFECTO VERTICAL
        binding.pokeItemList.layoutManager=layoutManager //INDICA CUAL VA A SER LA DISTRIBUCION DEL RECYCLER VIEW
        binding.pokeItemList.adapter = pokeItemListAdapter//CONECTA EL ADAPTER CON EL RECYCLER VIEW

        //ESTABLECE A LA ACTIVIDAD ACTUAL COMO UN OBSERVADOR A LA LISTA MUTABLE DEL VIEWMODEL
        //ENTONCES CUANDO OCURRA ALGUN CAMBIO, SE NOTIFICA AL OBSERVADOR Y EJECUTA LA ACCION CORRESPONDIENTE
        //ACTUALIZAR EL ADAPTADOR DE LA RECYCLER VIEW CON LOS NUEVOS DATOS AÑADIDOS
        viewModel.pokemonItemList.observe(this) { list ->
            pokeItemListAdapter.setData(list) //SI EXISTEN DATOS DISPONIBLES SE LO AGREGA A LA RECYCLER VIEW
        }

        //SE AGREGA UN LISTENER DE DESPLAZAMIENTO PARA REALIZAR ACCIONES CUANDO EL USUARIO SE DESPLAZE
        binding.pokeItemList.addOnScrollListener(
            object : RecyclerView.OnScrollListener(){
            override fun onScrolled(//SOBREESCRIBE LA FUNCION ONSCROLLED QUE SE LLAMA CADA VEZ QUE EL USUARIO SE DESPLAZA
                recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)//
                if(dy>0){//SI EL USUARIO SE ESTA DESPLAZANDO HACIA ABAJO
                    val visibleItems= layoutManager.childCount// CUENTA LOS ELEMENTOS VISIBLES DE LA LISTA
                    val pastItems = layoutManager.findFirstVisibleItemPosition()//RETORNA LA POSICION DEL PRIMER ITEM VISIBLE DE LA LISTA
                    val totalItems= layoutManager.itemCount//RETORNA EL TOTAL DE ITEMS DE LA LISTA
                    if(visibleItems+pastItems>=totalItems){//SI SE LLEGA AL FINAL DE LA LISTA
                        limit+=20//SE AUMENTA EL LIMITE DE LOS RESULTADOS
                        viewModel.getPokemonItemList(limit)//Y SE VUELVE HACER UN LLAMADO A LA API PARA OBTENER LA LISTA DE ITEMS
                    }
                }
            }
        })


        binding.searchItem.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    viewModel.getPokemonItemListFiltered(newText)
                }
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.getPokemonItemListFiltered(query)
                }
                return true
            }
        })

        viewModel.pokeItemListFiltered.observe(this, Observer { pokeFilterList->
            if(pokeFilterList.isNotEmpty()){
                pokeItemListAdapter.setData(pokeFilterList)
            }
            else{
                viewModel.getPokemonItemList(limit)
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


}