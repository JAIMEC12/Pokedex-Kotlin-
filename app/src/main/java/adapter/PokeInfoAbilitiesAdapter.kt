    package adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.pokemon_red_radical.R
import com.example.pokemon_red_radical.databinding.RecyclerListLayoutInfoabilitiesBinding
import pokeApi_Data.AbilityDetails


//REFERENCIAS DE https://www.youtube.com/watch?v=oKuqqM5oolk&list=LL&index=1&t=137s
//REFERENCIAS DE https://developer.android.com/develop/ui/views/layout/recyclerview?hl=es-419#:~:text=RecyclerView%20es%20el%20ViewGroup%20que,un%20objeto%20contenedor%20de%20vistas.


//
class PokeInfoAbilities : RecyclerView.Adapter<PokeInfoAbilities.SearchViewHolder>(){
    //
    private var pokemonAbilities : MutableList<AbilityDetails> = mutableListOf()
    //
    private var pokemonAbilityHidden = listOf<Boolean>()


    //
    fun setData(list: MutableList<AbilityDetails>, listHidden : List<Boolean>){
        pokemonAbilities=list //
        pokemonAbilityHidden=listHidden //
        notifyDataSetChanged() //
    }

    //
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding =RecyclerListLayoutInfoabilitiesBinding
            .inflate(LayoutInflater.from(parent.context), parent, false) //
        return SearchViewHolder(binding) //
    }

    //
    override fun getItemCount(): Int {
       return pokemonAbilities.size//
    }

    //
    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val binding = holder.binding //
        val abilityInfo=pokemonAbilities[position] //
        val hidden = pokemonAbilityHidden[position] //

        //
        binding.abilityName.text=abilityInfo.name
        binding.abilityEffect.text=abilityInfo.effect

        //
        if(hidden){
         binding.abilityName //
             .setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.red_700))
            binding.abilityEffect //
                .setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.red_700))
        }
    }

    //
    class SearchViewHolder(val binding: RecyclerListLayoutInfoabilitiesBinding)
        : RecyclerView.ViewHolder(binding.root) //
}