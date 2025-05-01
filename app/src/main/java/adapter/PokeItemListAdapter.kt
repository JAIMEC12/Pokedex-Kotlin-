package adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.pokemon_red_radical.R
import com.example.pokemon_red_radical.databinding.RecyclerListLayoutPokeitemBinding
import pokeApi_Data.PokeItemListResult


//REFERENCIAS DE https://www.youtube.com/watch?v=oKuqqM5oolk&list=LL&index=1&t=137s
//REFERENCIAS DE https://developer.android.com/develop/ui/views/layout/recyclerview?hl=es-419#:~:text=RecyclerView%20es%20el%20ViewGroup%20que,un%20objeto%20contenedor%20de%20vistas.



//
class PokeItemListAdapter (private val itemClick: (Int) -> Unit) : RecyclerView.Adapter<PokeItemListAdapter.SearchViewHolder>() {
   //
    private var pokemonItemList: List<PokeItemListResult> = emptyList()

    //
    fun setData(list:List<PokeItemListResult>){
        pokemonItemList=list //
        notifyDataSetChanged() //
    }

    //
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = RecyclerListLayoutPokeitemBinding
            .inflate(LayoutInflater.from(parent.context),parent,false) //
        return SearchViewHolder(binding) //
    }

    //
    override fun getItemCount(): Int {
        return pokemonItemList.size //
    }

    //
    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {

        val binding =  holder.binding //
        val item = pokemonItemList[position] //

        //
        binding.itemText.text= item.name

        //
        val urlParts = item.url.split("/")
        //
        val itemId=urlParts.dropLast(1).last().toInt()

        //
        Glide.with(holder.itemView.context)
            .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/"+item.name+".png")
            .centerCrop() //
            .diskCacheStrategy(DiskCacheStrategy.ALL) //
            .error(R.drawable.pokeball_icon) //
            .into(binding.ItemImage) //

        //
        holder.itemView.setOnClickListener{ itemClick(itemId)}
    }

    //
    class SearchViewHolder(val binding: RecyclerListLayoutPokeitemBinding)
        : RecyclerView.ViewHolder(binding.root)//
}


