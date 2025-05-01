package pokeApi_Data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

//GLOBAL - SE VAN A USAR EN OTRAS ESTRUCTURA DE DATOS

//REFERENCIAS DE https://www.youtube.com/watch?v=oKuqqM5oolk&list=LL&index=1&t=137s
//REFERENCIA DE LA ESTRUCTURA DEL OBJETO : https://pokeapi.co/docs/v2#pokemon


//POKEMON CARACTERISTICAS
data class Pokemon(
    @Expose @SerializedName("id") val id: Int,
    @Expose @SerializedName("name") val name: String,
    @Expose @SerializedName("abilities") val abilities: List<AbilitiesList>,
    @Expose @SerializedName("held_items") val heldItems: List<PokemonHeldItems>,
    @Expose @SerializedName("moves") val moves: List<PokemonMoves>,
    @Expose @SerializedName("sprites") val sprites: Sprites,
    @Expose @SerializedName("species") val species: Species,
    @Expose @SerializedName("stats") val stats: List<PokemonStats>,
    @Expose @SerializedName("types") val types: List<PokemonTypes>,

    //HABILIDADES Y DESCRIPCIONES EN ESPANOL
    @Expose @SerializedName ("evolution_chain") val evolutionChain : EvolutationPath,
    @Expose @SerializedName ("flavor_text_entries") val flavorTextEntries : List<FlavorTextEntries>,
    @Expose @SerializedName ("names") val names : List<NamesLanguage>
)

data class EvolutationPath(
    @Expose @SerializedName ("url") val url: String
)
/**/
data class FlavorTextEntries(
    @Expose @SerializedName("flavor_text") val flavorText : String,
    @Expose @SerializedName("language") val language: Language
)

/**/
data class AbilitiesList(
    @Expose @SerializedName("is_hidden") val isHidden: Boolean,
    @Expose @SerializedName("slot") val slot: Int,
    @Expose @SerializedName("ability") val ability: AbilityInfo
)

/**/
data class AbilityInfo(
    @Expose @SerializedName("name") val name: String,
    @Expose @SerializedName("url") val url: String,
)

/**/
data class PokemonHeldItems(
    @Expose @SerializedName("item") val item: ItemInfo
)

/**/
data class ItemInfo(
    @Expose @SerializedName("name") val name: String,
    @Expose @SerializedName("url") val url: String
)

/**/
data class PokemonMoves(
    @Expose @SerializedName("move") val move: MoveInfo
)

/**/
data class MoveInfo(
    @Expose @SerializedName("name") val name: String,
    @Expose @SerializedName("url") val url: String
)

/**/
data class  PokemonStats(
    @Expose @SerializedName("base_stat") val baseStat:Int,
    @Expose @SerializedName("effort") val effort:Int,
    @Expose @SerializedName("stat") val stat:StatInfo
)

/**/
data class StatInfo(
    @Expose @SerializedName("name") val name: String,
    @Expose @SerializedName("url") val url: String
)


/**/
data class PokemonTypes(
    @Expose @SerializedName("slot") val slot: Int,
    @Expose @SerializedName("type") val type: TypeInfoPokemon
)


/**/
data class TypeInfoPokemon(
    @Expose @SerializedName("name") val name: String,
    @Expose @SerializedName("url") val url: String
)


/**/
data class Sprites(
    @Expose @SerializedName("front_default") val frontDefault: String?,
    @Expose @SerializedName("front_female") val frontFemale: String?,
    @Expose @SerializedName("front_shiny") val frontShiny: String?,
    @Expose @SerializedName("front_shiny_female") val frontShinyFemale: String?
)


/**/
data class Species(
    @Expose @SerializedName("name") val name: String,
    @Expose @SerializedName("url") val url: String
)


/**/
data class AbilityDetails(
    val name: String?,
    val effect: String?
)