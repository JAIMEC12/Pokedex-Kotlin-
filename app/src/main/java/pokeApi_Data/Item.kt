package pokeApi_Data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


//REFERENCIAS DE https://www.youtube.com/watch?v=oKuqqM5oolk&list=LL&index=1&t=137s
//REFERENCIA DE LA ESTRUCTURA DEL OBJETO : https://pokeapi.co/docs/v2#pokemon

/*
* */

data class Item(
    @Expose @SerializedName("id") val id: Int,
    @Expose @SerializedName("name") val name: String,
    @Expose @SerializedName("cost") val cost: Int,
    @Expose @SerializedName("fling_power") val flingPower: Int,
    @Expose @SerializedName("fling_effect") val flingEffect: FlingEffect,
    @Expose @SerializedName("effect_entries") val effectEntries : List<EffectEntries>,
    @Expose @SerializedName("attributes") val attributes: List<ItemAttribute>,
    @Expose @SerializedName("category") val category: ItemCategory,
    @Expose @SerializedName("names") val names : List<NamesLanguage>,
    @Expose @SerializedName ("flavor_text_entries") val flavorTextEntriesEffects : List<FlavorTextEntriesEffects>,
)


/*
* */

data class FlingEffect(
    @Expose @SerializedName("name") val name: String,
    @Expose @SerializedName("url") val url:String
)

/*
* */

data class ItemAttribute(
    @Expose @SerializedName("name") val name: String,
    @Expose @SerializedName("url") val url:String
)

/*
* */

data class ItemCategory(
    @Expose @SerializedName("name") val name: String,
    @Expose @SerializedName("url") val url:String
)

/*
* */

data class  EffectEntries(
    @Expose @SerializedName("effect") val effect: String,
    @Expose @SerializedName("short_effect") val shortEffect:String
)

/*
* */

data class FlavorTextEntriesEffects(
    @Expose @SerializedName("text") val text: String,
    @Expose @SerializedName("language") val language : Language,
    @Expose @SerializedName("version_group") val versionGroup : VersionGroup
)

/*
* */

data class VersionGroup(
    @Expose @SerializedName("name") val name: String
)