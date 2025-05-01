package models

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pokeApi_Data.PokeApiService
import pokeApi_Data.PokeItemList
import pokeApi_Data.PokeItemListResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


//REFERENCIAS DE https://www.youtube.com/watch?v=oKuqqM5oolk&list=LL&index=1&t=137s
//
class PokeItemListViewModel : ViewModel(){

    //
    companion object{
        private const val TAG = "PokeItemListViewModel"
    }

    //
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/") //
        .addConverterFactory(GsonConverterFactory.create()) //
        .build() //


    //
    private val service : PokeApiService = retrofit.create(PokeApiService::class.java)

    //
    val pokemonItemList = MutableLiveData<List<PokeItemListResult>>()

    //
    fun getPokemonItemList(limit:Int){

        //
        val call = service.getItemList(limit,0)

        //
        call.enqueue(object : Callback<PokeItemList>{
            override fun onResponse(call: Call<PokeItemList>, response: Response<PokeItemList>) { //
                response.body()?.results?.let{list-> //
                    pokemonItemList.postValue(list) //
                    //
                    Log.d(TAG,response.toString()) //
                }
            }

            //
            override fun onFailure(call: Call<PokeItemList>, t: Throwable) {
                call.cancel() //
                Log.e(TAG, t.toString()) //
            }
        })
    }

    val pokeItemListFiltered = MutableLiveData<List<PokeItemListResult>>()

    fun getPokemonItemListFiltered(filterString : String){
        val call = service.getItemList(2180,0)
        pokeItemListFiltered.value = emptyList()
        call.enqueue(object : Callback<PokeItemList>{
            override fun onResponse(call: Call<PokeItemList>, response: Response<PokeItemList>) {
                response.body()?.results?.let {
                    val filterList = it.filter { pokemon ->
                        pokemon.name.contains(filterString,ignoreCase = true)
                    }
                    pokeItemListFiltered.postValue(filterList)
                }
            }

            override fun onFailure(call: Call<PokeItemList>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}