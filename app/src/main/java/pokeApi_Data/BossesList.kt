package pokeApi_Data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


//REFERENCIAS DE https://www.youtube.com/watch?v=oKuqqM5oolk&list=LL&index=1&t=137s
/*
EN ESTA PARTE SE CREAN LAS CLASES DE LOS OBJETOS QUE VAN ALMACENAR LOS VALORES EXTRADIOS DEL JSON
SE UTILIZA DATA CLASS QUE SU FUNCION PRINCIPAL VA A SER ALAMCENAR DATOS
SE CREA LA ESTRUCTURA DEL OBJETO, LOS TIPOS DE DATOS QUE VA A TENER DEBE COINCIDIR CON LOS DEL JSON.
SE UTILIZA @EXPOSE Y @SERIALIZEDNAME PARA LLEVAR A CABO LA TRANSFORMACION DEL DATO JSON A GSON
@EXPOSE INDICA QUE EL CAMPO ESPECIFICADO DEBE SER INCLUIDO PARA LA SERIALIZIACION O DESERIALIZACION,
    EN ESTE CASO, PARA DESERIALIZAR
@SERIALIZEDNAME ESPECIFICA EL NOMBRE DEL CAMPO DE JSON QUE ES EL EQUIVALENTE A LA DE LA PROPIEDAD DE LA CLASE
    Y AMBOS DEBEN SER IGUALES, ES DECIR, SI BOSS_NAME ES UN STRING, LA PROPIEDAD DEBE SER UN STRING
* */

//CLASE BOSSESLIST, LOS OBJETOS VAN TENER LAS SIGUIENTES PROPIEDADES
data class BossesList(
    @Expose @SerializedName("boss_name") val name : String, //NOMBRE
    @Expose @SerializedName("boss_image") val url : String, //IMAGEN EN FORMATO URL
    @Expose @SerializedName("pokemon_team") val pokemonTeam : List<PokemonTeam>, //LA LISTA DE SU POKEMONES
    @Expose @SerializedName("boss_medal") val medal : String, //LAS MEDALLAS
    @Expose @SerializedName("boss_type") val boss_type : String //EL TIPO DE JEFE DE GIMNASIO QUE ES
)


//CLASE POKEMONTEAM, LOS OBJETOS VAN A TENER LAS SIGUIENTES PROPIEDADES
data class PokemonTeam(
    @Expose @SerializedName("id") val id : Int, //ID
    @Expose @SerializedName("name") val name: String, //NOMBRE DEL POKEMON
    @Expose @SerializedName("url") val url: String, //IMAGEN EN FORMATO URL
    @Expose @SerializedName("level") val level: Int //NIVEL DEL POKEMON
)
