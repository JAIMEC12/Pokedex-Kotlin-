package pokeApi_Data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


//REFERENCIAS DE https://www.youtube.com/watch?v=oKuqqM5oolk&list=LL&index=1&t=137s
//REFERENCIA DE LA ESTRUCTURA DEL OBJETO : https://pokeapi.co/docs/v2#pokemon

/*

* */

data class PokeListResponse(
    @Expose @SerializedName("count") val count: Int,
    @Expose @SerializedName("next") val next : String?,
    @Expose @SerializedName("previous") val previous: String?,
    @Expose @SerializedName("results") val results : List<PokemonResult>
)


/*
* */

data class PokemonResult(
    @Expose @SerializedName("name") val name : String,
    @Expose @SerializedName("url") val url : String
)