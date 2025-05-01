package adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.pokemon_red_radical.databinding.RecyclerListLayoutPokebossBinding
import pokeApi_Data.BossesList

//REFERENCIAS DE https://www.youtube.com/watch?v=oKuqqM5oolk&list=LL&index=1&t=137s
//REFERENCIAS DE https://developer.android.com/develop/ui/views/layout/recyclerview?hl=es-419#:~:text=RecyclerView%20es%20el%20ViewGroup%20que,un%20objeto%20contenedor%20de%20vistas.


//
class PokeBossesAdapter(private val pokemonClick: (Int) -> Unit): RecyclerView.Adapter<PokeBossesAdapter.SearchViewHolder>() {

    //
    private var pokeBossList = emptyList<BossesList>()

    //
    fun setData(list:List<BossesList>){
        pokeBossList=list //
        notifyDataSetChanged() //
    }

    //
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = RecyclerListLayoutPokebossBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    //
    override fun getItemCount(): Int {
        return pokeBossList.size
    }

    //
    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val binding = holder.binding//
        val bosses = pokeBossList[position] //

        //
        binding.PokeBoss.text= bosses.name

        //
        Glide.with(holder.itemView.context)
            .load(bosses.url) //
            .centerCrop() //
            .diskCacheStrategy(DiskCacheStrategy.ALL) //
            .into(binding.BossImage) //

        //
        holder.itemView.setOnClickListener { pokemonClick(position) }
    }

    //
    class SearchViewHolder(val binding: RecyclerListLayoutPokebossBinding): RecyclerView.ViewHolder(binding.root)
}


