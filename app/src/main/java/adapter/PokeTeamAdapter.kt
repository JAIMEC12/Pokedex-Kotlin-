package adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.example.pokemon_red_radical.databinding.RecyclerListLayoutPoketeamBinding
import pokeApi_Data.PokemonTeam


//REFERENCIAS DE https://www.youtube.com/watch?v=oKuqqM5oolk&list=LL&index=1&t=137s
//REFERENCIAS DE https://developer.android.com/develop/ui/views/layout/recyclerview?hl=es-419#:~:text=RecyclerView%20es%20el%20ViewGroup%20que,un%20objeto%20contenedor%20de%20vistas.


/*
AQUI SE CREA UN ADAPTADOR PERSONALIZADO PARA LA RECYCLERVIEW.
* * */
// LA CLASE HEREDA DE RECYCLERVIEW.ADAPTER Y RECIBE COMO PARAMETROS UNA FUNCION LAMBDA
    // CON RESPECTO A LOS CLICK QUE SE DE AL ITEM DE LA LISTA, RETORNA UN VALOR INT

class PokeTeamAdapter (private val pokemonClick: (Int) -> Unit): RecyclerView.Adapter<PokeTeamAdapter.SearchViewHolder>() {

    //VARIABLE PARA ALMACENAR LA LISTA DE POKEMONES DEL JEFE DE GIMNASIO
    private var pokeBossList= emptyList<PokemonTeam>()

    //FUNCION PARA LLENAR LA LISTA
    fun setData(list : List<PokemonTeam>){
        pokeBossList=list //POKEBOSSLIST SE LLENA CON LA LISTA DE ELEMENTOS
        notifyDataSetChanged() //NOTIFICA AL ADAPTADOR PARA ACTUALIZAR LOS DATOS Y MUESTRE LA NUEVA LISTA
    }

    //FUNCION DEL RECYCLER VIEW PARA SABER EL TAMANO DE LA LISTA ES SOBREESCRITA Y SE AGREGA LO SIGUIENTE
    override fun getItemCount(): Int {
        return pokeBossList.size // RETORNA EL TAMANO DE LA LISTA
    }

    //CREA LA VISTA INDIVIDUAL CUANDO EL RECYCLER VIEW NECESITA UNA PARA LISTA
            //BASICAMENTE, CREA EL DISENO QUE VA A TENER EL ITEM DENTRO DE LA LISTA
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        //SE CREA EL DISEÑO DEL ELEMENTO DE LA LISTA UTILIZANDO UN LAYOUT YA HECHO. AQUI SE SE INICIALIZA
                // PERO NO SE AGREGA TODAVIA A LA RECYCLER VIEW
        val binding = RecyclerListLayoutPoketeamBinding //BINDING VA A SER UNA REPRESENTACION DE LA VISTA,
                // UN ENLACE DE ELLA CREADA
            .inflate(LayoutInflater.from(parent.context),parent,false) //

        return SearchViewHolder(binding) //DEVUELVE LA INSTANCIA QUE SE ENCARGARA DEL MANEJO DE LA VISTA
    }


    //AQUI ES CUANDO LA RECYCLERVIEW AGREGA EL ELEMENTO ANTERIORMENTE CREADO A LA LISTA
    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {

        //VARIABLE QUE VA ALMACENAR LA VISTA CREADA, POR LO TANTO, VA A TENER ACCESO A LOS ELEMENTOS DE ELLA
        val binding= holder.binding

        //VARIABLE QUE VA ALMACENAR EL POKEMON UBICADO EN LA POSICION DEL ESPACIO DONDE SE UBICARA LA VISTA
        val pokemon = pokeBossList[position]

        //ASIGNO AL ELEMENTO DE TEXTVIEW EL NOMBRE DEL POKEMON
        binding.pokeTeamName.text = pokemon.name

        //ASIGNO DE IGUAL MANERA AL OTRO TEXTVIEW EL NIVEL DE POKEMON
        binding.pokeTeamLevel.text=String.format("Lv"+ pokemon.level.toString())

        //GLIDE PERMITE CARGAR IMAGENES DEL INTERNET A TRAVES DE UNA URL DE MANERA EFICIENTE
        Glide.with(holder.itemView.context) //INDICA EL CONTEXTO DE LA VISTA ACTUAL
            .load(pokemon.url) // URL DE DONDE DEBERA CONSEGUIR Y CARGAR LA IMAGEN
            .centerCrop() //
            .into(binding.pokeTeamImage) //AGREGA LA IMAGEN EN EL IMAGEVIEW

        //SE ANADE A CADA VISTA UN LISTENER DE TIPO CLICK A LO CUAL LLAMARA LA FUNCION POKEMONCLICK RETORNANDO EL
            // PARAMETRO, EN ESTE CASO SERIA EL ID DEL POKEMON
        holder.itemView.setOnClickListener { pokemonClick(pokemon.id) }
    }

    //ES UNA CLASE PARA MANEJAR Y MANIPULAR LAS VISTAS DEL RECYCLER VIEW
    //HEREDA DE LA SUBCLASE VIEWHOLDER POR LO TANTO, TENDRA LAS FUNCIONES PARA MANJERA LOS ELEMENTOS DE LA VISTAS
    class SearchViewHolder(val binding: RecyclerListLayoutPoketeamBinding): RecyclerView.ViewHolder(binding.root)
    //RECIBE COMO PARAMETRO DE CONSTRUCTOR LA VISTA INDIVIDUAL DEL ELEMENTO DEL RECYLER VIEW
}