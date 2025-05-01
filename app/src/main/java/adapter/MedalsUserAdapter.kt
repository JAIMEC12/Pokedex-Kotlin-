package adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pokemon_red_radical.R
import com.example.pokemon_red_radical.databinding.RecyclerListLayoutUsermedalsBinding


//REFERENCIAS DE https://www.youtube.com/watch?v=oKuqqM5oolk&list=LL&index=1&t=137s
//REFERENCIAS DE https://developer.android.com/develop/ui/views/layout/recyclerview?hl=es-419#:~:text=RecyclerView%20es%20el%20ViewGroup%20que,un%20objeto%20contenedor%20de%20vistas.


class MedalsUserAdapter : RecyclerView.Adapter<MedalsUserAdapter.SearchViewHolder> (){

    //
    private var userMedals = mutableListOf<String>()
    //
    private var typeMedals =  mutableListOf<String>()


    //
    fun setData(list:MutableList<String> , list2 : MutableList<String>){
        userMedals=list //
        typeMedals=list2 //
        notifyDataSetChanged()
    }

    //
    override fun getItemCount(): Int {
        return userMedals.size//
    }

    //
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = RecyclerListLayoutUsermedalsBinding
            .inflate(LayoutInflater.from(parent.context), parent, false) //
        return SearchViewHolder(binding) //
    }

    //
    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val binding = holder.binding //
        val medal = userMedals[position] //
        val type = typeMedals[position] //

        //
        binding.UserMedal.text = type //

        //
        Glide.with(holder.itemView.context) //
            .load(medal) //
            .error(R.drawable.pokeball_icon) //
            .into(binding.medalImage) //
    }

    //
    class SearchViewHolder(val binding: RecyclerListLayoutUsermedalsBinding) //
        : RecyclerView.ViewHolder(binding.root) //

}