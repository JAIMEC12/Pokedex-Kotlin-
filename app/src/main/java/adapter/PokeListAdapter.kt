package adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.pokemon_red_radical.R
import com.example.pokemon_red_radical.databinding.RecyclerListLayoutPokedexBinding
import pokeApi_Data.PokemonResult

//REFERENCIAS DE https://www.youtube.com/watch?v=oKuqqM5oolk&list=LL&index=1&t=137s
//REFERENCIAS DE https://developer.android.com/develop/ui/views/layout/recyclerview?hl=es-419#:~:text=RecyclerView%20es%20el%20ViewGroup%20que,un%20objeto%20contenedor%20de%20vistas.



//CREA UN ADAPTADOR PARA LA RECYCLER VIEW. UNA CLASE QUE HEREDA DE RECYCLERVIEW.ADAPTER
//TIENE COMO ARGUMENTO DE CONSTRUCTOR UNA FUNCION QUE RETORNA UN UNIT, UN VALOR NO SIGNIFICATIVO, Y RECIBE COMO PARAMETRO UN INT

class PokeListAdapter(private val pokemonClick: (Int) -> Unit): RecyclerView.Adapter<PokeListAdapter.SearchViewHolder>() {

    //ALMACENARA LOS ELEMENTOS QUE SE VAN A MOSTRAR EN LA RECYCLER VIEW
    private var pokemonList: List<PokemonResult> = emptyList()

    fun setData(list: List<PokemonResult>) { //SE PASA LOS ELEMENTOS
        pokemonList = list //LOS ALMACENA EN LA LISTA
        notifyDataSetChanged() //NOTIFICA AL ADAPTADOR PARA ACTUALIZAR LOS DATOS Y MUESTRE LA NUEVA LISTA
    }


    //CREA UN VISTA INDIVIDUAL CUANDO EL RECYCLER VIEW NECESITA UNO VIEWHOLDER NUEVO
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        //SE CREA EL DISEÑO DEL ELEMENTO DE LA LISTA UTILIZANDO UNA VISTA. AQUI SE SE INICIALIZA PERO NO SE AGREGA TODAVIA A LA RECYCLER VIEW
        val binding = RecyclerListLayoutPokedexBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        //SE RETORNA EL VIEWHOLDER
        return SearchViewHolder(binding)
    }

    //EL TAMAÑO DE LA LISTA
    override fun getItemCount(): Int {
        return pokemonList.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val binding = holder.binding//PERMITE REFERIRSE DIRECTAMENTE A LOS ELEMENTOS DE LA VISTA
        val pokemon = pokemonList[position]//ALMACENA EN POKEMON LOS DATOS DEL OBJETO UBICADO EN ESA POSICION EN LA LISTA

        binding.PokeName.text= pokemon.name //SE REFERENCIA AL TEXTVIEW A TRAVES DEL BINDING Y SE AGREGA EL TEXTO CORRESPONDIENTE


        val urlParts: List<String> = pokemon.url.split("/") //SE SEPARA LA URL EN PARTES
        val pokeId :String = urlParts.dropLast(1).last() //LUEGO SE CONSIGUE EL ID DE LA URL PARA SACAR LA IMAGEN


        Glide.with(holder.itemView.context) //PERMITE CARGAR LAS IMAGENES DE MANERA EFICIENTE DE UNA URL
            .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"+pokeId+".png")//SE REEMPLAZA EL ID
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .error(R.drawable.pokeball_icon)
            .into(binding.PokeImage)//SE AGREGA EN LA IMAGEVIEW


            holder.itemView.setOnClickListener { pokemonClick(pokeId.toInt()) }//CUANDO SE DA CLICK A UN ELEMETNO SE LLAMA LA FUNCION POKEMONCLICK EN CONJUNTO CON LA POSICION RESPECTIVA DEL ELEMENTO
    }

    //ES UNA CLASE PARA MANEJAR Y MANIPULAR LAS VISTAS DEL RECYCLER VIEW
    //HEREDA DE LA SUBCLASE VIEWHOLDER POR LO TANTO, TENDRA LAS FUNCIONES PARA MANJERA LOS ELEMENTOS DE LA VISTAS DEL RECYLCER VIEW
    class SearchViewHolder(val binding: RecyclerListLayoutPokedexBinding): RecyclerView.ViewHolder(binding.root)//INDICO LA VISTA CON LA CUAL SE VA A REPRESENTAR EL OBJETO
    //RECIBE COMO PARAMETRO DE CONSTRUCTOR BINDING PARA TENER ACCESOS A LAS VISTAS DE DISEÑO
}