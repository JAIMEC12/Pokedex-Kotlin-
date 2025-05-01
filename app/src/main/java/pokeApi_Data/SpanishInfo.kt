package pokeApi_Data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

//REFERENCIAS DE https://www.youtube.com/watch?v=oKuqqM5oolk&list=LL&index=1&t=137s
//REFERENCIA DE LA ESTRUCTURA DEL OBJETO : https://pokeapi.co/docs/v2#pokemon

/**/
data class Language(
@Expose  @SerializedName("name") val name:String,
@Expose  @SerializedName("url") val url: String
)

/**/
data class NamesLanguage(
    @Expose @SerializedName("name") val name: String,
    @Expose @SerializedName("language") val language: Language
)
