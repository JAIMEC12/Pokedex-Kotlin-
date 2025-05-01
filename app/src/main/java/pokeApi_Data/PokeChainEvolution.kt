package pokeApi_Data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PokeChainEvolution(
    @Expose @SerializedName("baby_trigger_item") val babyTriggerItem: Item,
    @Expose @SerializedName("chain") val chain: EvolutionChain,
    @Expose @SerializedName("id") val id: Int
)

data class EvolutionChain(
    @Expose @SerializedName("evolution_details") val evolutionDetails : List<EvolutionDetails>,
    @Expose @SerializedName("evolves_to") val evolvesTo : List<EvolvesTo>,
    @Expose @SerializedName("is_baby") val isBaby :Boolean,
    @Expose @SerializedName("species") val species : Species,
)

data class EvolutionDetails(
    @Expose @SerializedName("gender") val gender : Int,
    @Expose @SerializedName("held_item") val heldItem : Item,
    @Expose @SerializedName("item") val item: Item,
    @Expose @SerializedName("known_move") val knownMove: String?,
    @Expose @SerializedName("known_move_type") val knownMoveType: String?,
    @Expose @SerializedName("location") val location: String?,
    @Expose @SerializedName("min_affection") val minAffection: Int?,
    @Expose @SerializedName("min_beauty") val minBeauty: Int?,
    @Expose @SerializedName("min_happiness") val minHappiness: Int?,
    @Expose @SerializedName("min_level") val minLevel: Int,
    @Expose @SerializedName("needs_overworld_rain") val needsOverworldRain: Boolean,
    @Expose @SerializedName("party_species") val partySpecies: Species,
    @Expose @SerializedName("party_type") val partyType: String?,
    @Expose @SerializedName("relative_physical_stats") val relativePhysicalStats: Int?,
    @Expose @SerializedName("time_of_day") val timeOfDay: String?,
    @Expose @SerializedName("trade_species") val tradeSpecies: Species,
    @Expose @SerializedName("trigger") val trigger: EvolutionTrigger?,
    @Expose @SerializedName("turn_upside_down") val turnUpsideDown: Boolean
)

data class EvolutionTrigger(
    @Expose @SerializedName("name") val name: String?,
    @Expose @SerializedName("url") val url: String?
)

data class EvolvesTo(
    @Expose @SerializedName("evolution_details") val evolutionDetails : List<EvolutionDetails>,
    @Expose @SerializedName("evolves_to") val evolvesTo : List<EvolvesTo>,
    @Expose @SerializedName("is_baby") val isBaby :Boolean,
    @Expose @SerializedName("species") val species : Species,
)