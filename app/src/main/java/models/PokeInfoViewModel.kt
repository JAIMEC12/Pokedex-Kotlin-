package models

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pokeApi_Data.PokeApiService
import pokeApi_Data.PokeChainEvolution
import pokeApi_Data.Pokemon
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


//REFERENCIAS DE https://www.youtube.com/watch?v=oKuqqM5oolk&list=LL&index=1&t=137s
//
class PokeInfoViewModel : ViewModel() {

    //
    companion object{
        private const val TAG = "PokeInfoViewModel" //
    }

    //
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/") //
        .addConverterFactory(GsonConverterFactory.create()) //
        .build() //

    //
    private val service : PokeApiService = retrofit.create(PokeApiService::class.java)

    //
    val pokemonInfo = MutableLiveData<Pokemon>()

    //
    fun getPokemonInfo(id:Int?){
        val call =service.getPokemonInfo(id) //

        call.enqueue(object: Callback<Pokemon>{ //
            //
            override fun onResponse(call: Call<Pokemon>, response: Response<Pokemon>) { //
                response.body()?.let {pokemon -> //
                    pokemonInfo.postValue(pokemon) //
                    //
                    Log.d(TAG,response.toString())
                }
            }

            //
            override fun onFailure(call: Call<Pokemon>, t: Throwable) { //
                call.cancel() //
                //
                Log.e(TAG, t.toString())
            }
        })
    }


    //
    val pokemonInfoAbilityDetails = MutableLiveData<Pokemon>()

    //
    fun getPokemonAbility(name : String) {
        //
        val call = service.getPokemonAbility(name)
        call.enqueue(object : Callback<Pokemon> { //
            //
            override fun onResponse(call: Call<Pokemon>, response: Response<Pokemon>) { //
                response.body()?.let { pokemon -> //
                    pokemonInfoAbilityDetails.setValue(pokemon) //
                    Log.d(TAG,response.toString())
                    Log.d(TAG,pokemonInfoAbilityDetails.toString())
                }
            }

            //
            override fun onFailure(call: Call<Pokemon>, t: Throwable) {
                call.cancel() //
                Log.e(TAG,t.toString()) //
            }
        })
    }

    //
    val pokemonDescription = MutableLiveData<Pokemon>()

    //
    fun getPokemonSpecies(name: String){

        //
        val call = service.getPokemonSpecies(name)

        call.enqueue(object: Callback<Pokemon>{ //
            //
            override fun onResponse(call: Call<Pokemon>, response: Response<Pokemon>) { //
                response.body()?.let { pokemon -> //
                    pokemonDescription.postValue(pokemon) //
                    Log.d(TAG, response.toString())
                }

            }

            //
            override fun onFailure(call: Call<Pokemon>, t: Throwable) {
                //
                call.cancel()
                //
                Log.e(TAG,t.toString())
            }
        })
    }

    val pokemonChainEvolution = MutableLiveData<PokeChainEvolution>()

    fun getPokemonChainEvolution(id : Int?){
        val call = service.getPokemonChainEvolution(id)

        call.enqueue(object: Callback<PokeChainEvolution>{
            override fun onResponse(call: Call<PokeChainEvolution>, response: Response<PokeChainEvolution>) {
                response.body()?.let { chainEvolution ->
                    pokemonChainEvolution.postValue(chainEvolution)
                }
            }

            override fun onFailure(call: Call<PokeChainEvolution>, t: Throwable) {
                Log.d("ERROR CHAIN EVOLUTION",  t.toString())
            }
        })
    }

}