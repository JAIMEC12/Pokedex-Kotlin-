package pokeApi_Data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

//REFERENCIAS DE https://www.youtube.com/watch?v=oKuqqM5oolk&list=LL&index=1&t=137s

/**/
data class PokeUser(
    @Expose @SerializedName ("type_medal") var typeMedal : MutableList<String>,
    @Expose @SerializedName("medals_gained") var medalsGained: MutableList<String>,
    @Expose @SerializedName("pokemon_captured") var pokemonCaptured: MutableList<Int>,
    @Expose @SerializedName("pokemon_Name") var name : MutableList<String>
)
