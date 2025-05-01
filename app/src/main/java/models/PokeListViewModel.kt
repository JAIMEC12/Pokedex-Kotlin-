package models

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pokeApi_Data.PokeApiService
import pokeApi_Data.PokeListResponse
import pokeApi_Data.PokemonResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//REFERENCIAS DE https://www.youtube.com/watch?v=oKuqqM5oolk&list=LL&index=1&t=137s

//UN MODELO DE VISTA PARA MANEJAR LA INFORMACION OBTENIDA DE LA LLAMADA A LA API
//CLASE QUE HEREDA DE VIEWMODEL PARA LA PERSISTENCIA DE DATOS
//BASICAMENTE ALMACENA DATOS LOS CUALES NO SERAN AFECTADOS SI LAS ACTIVIDADES REALIZAN CAMBIOS EN LA CONFIGURACION

class PokeListViewModel(): ViewModel() {
    private val retrofit= Retrofit.Builder() // CONSTRUYE UN OBJETO RETROFIT INDICANDO EL COMPORTAMIENTO A TENER
        .baseUrl("https://pokeapi.co/api/v2/") //DEFINE LA URL BASE
        .addConverterFactory(GsonConverterFactory.create()) //CONVIERTE LOS DATOS EN FORMATO JSON A GSON
        .build() //CREA LA INSTANCIA RETROFIT

    private val service : PokeApiService = retrofit.create(PokeApiService::class.java)
    //CREA UNA CONEXION IMPLEMENTANDO LOS METODOS ANTES DEFINIDO EN LA INTERFAZ DE POKEAPISERVICE


    val pokemonList= MutableLiveData<List<PokemonResult>>()//UNA LISTA DE TIPO OBJETO POKEMONRESULT
    //AL SER MUTABLELIVEDATA SIGNIFICA QUE LOS VALORES PUEDEN CAMBIAR Y LIVEDATA QUE CUALQUIER ACTIVIDAD QUE ESTE USANDO U OBSERVANDOLA SE ACTUALICE SUS DATOS

    fun getPokemonList(limit :Int){
        val call = service.getPokemonList(limit, 0) //LLAMA AL METODO PARA REALIZAR LA SOLICITUD HTTP DE TODOS LOS POKEMONES Y LO ALMACENA EN CALL

        call.enqueue(object : Callback<PokeListResponse>{ //CALL REALIZA UNA PETICION HTTP ASINCRONA
            override fun onResponse( call: Call<PokeListResponse>, response: Response<PokeListResponse>
            ) { //SI TIENE EXITO LA LLAMADA SE EJECUTA LO SIGUIENTO
                response.body()?.results?.let{list -> //SI CONTIENE DATOS DEL CAMPO RESULTS, SE LO AGREGA Y ACTUALIZA EN EL PROCESO A LA POKEMONLIST
                    pokemonList.postValue(list)
                }
            }

            //SI LA LLAMADA NO TUVO, EXITO SE EJECUTA LO SIGUIENTE
            override fun onFailure(call: Call<PokeListResponse>, t: Throwable) {
                call.cancel() //CANCELLA LA SOLICITUD PARA GARANTIZAR NO SE EJECUTE NUEVAMENTE
            }
        })
    }

    val pokemonFilterList= MutableLiveData<List<PokemonResult>>()

    fun getPokemonListFilter(filterString: String){
        val call = service.getPokemonList(1302,0)
        pokemonFilterList.value= emptyList<PokemonResult>()
        call.enqueue(object : Callback<PokeListResponse>{
            override fun onResponse(call: Call<PokeListResponse>, response: Response<PokeListResponse>) {
                response.body()?.let {
                    val filterList = it.results.filter { pokemon ->
                        pokemon.name.contains(filterString, ignoreCase = true)
                    }
                    pokemonFilterList.postValue(filterList)

                }
            }
            override fun onFailure(call: Call<PokeListResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

}