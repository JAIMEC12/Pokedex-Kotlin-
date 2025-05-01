package adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.pokemon_red_radical.R
import com.example.pokemon_red_radical.databinding.RecyclerListLayoutChainEvolutionBinding
import com.example.pokemon_red_radical.databinding.RecyclerListLayoutPoketeamBinding
import pokeApi_Data.Species

class PokeChainEvolutionAdapter (/*private val itemClick: (Int) -> Unit*/) : RecyclerView.Adapter<PokeChainEvolutionAdapter.SearchViewHolder>() {

    private var pokemonChainEvolution = mutableListOf<Species>()

    fun setpokemonChainEvolution(list: MutableList<Species>){
        pokemonChainEvolution = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return pokemonChainEvolution.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = RecyclerListLayoutChainEvolutionBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val binding = holder.binding
        val pokemon = pokemonChainEvolution[position].name.uppercase()
        val image = pokemonChainEvolution[position].url
        val urlParts: List<String> = image.split("/") //SE SEPARA LA URL EN PARTES
        val pokeId :String = urlParts.dropLast(1).last() //LUEGO SE CONSIGUE EL ID DE LA URL PARA SACAR LA IMAGEN

        binding.pokeTeamName.text = pokemon
        Glide.with(holder.itemView.context)
            .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"+pokeId+".png")
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .error(R.drawable.pokeball_icon)
            .into(binding.pokeTeamImage)//SE AGREGA EN LA IMAGEVIEW
    }

    class SearchViewHolder(val binding: RecyclerListLayoutChainEvolutionBinding): RecyclerView.ViewHolder(binding.root)
}