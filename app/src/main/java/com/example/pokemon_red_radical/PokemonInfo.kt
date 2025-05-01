package com.example.pokemon_red_radical

import adapter.PokeChainEvolutionAdapter
import adapter.PokeInfoAbilities
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.pokemon_red_radical.databinding.ActivityPokemonInfoBinding
import models.PokeInfoViewModel
import models.PokeUserModel
import pokeApi_Data.AbilityDetails
import pokeApi_Data.Species

class PokemonInfo : AppCompatActivity() {

    //VARIABLES QUE SE VAN A INICIALIZAR POSTERIORMENTE

    private lateinit var viewModel: PokeInfoViewModel// MODELOVISTA DE POKEMONINFO
    private lateinit var binding: ActivityPokemonInfoBinding// ENLACE A LA ACTIVIDAD
    private lateinit var pokeAbilitiesAdapter: PokeInfoAbilities// ADAPTADOR DE LAS HABILIDADES DE LOS POKEMONES
    private lateinit var pokeChainEvolutionAdapter : PokeChainEvolutionAdapter
    private lateinit var  abilityHidden : List<Boolean>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //INICIALIZO BINDING PARA ACCEDER A LOS ELEMENTOS DIRECTAMENTE DE LA INTERFAZ DE POKEMONINFO
            //INFLA O CREA EL DISENO DEL ARCHIVO XML Y LO ENLAZO PARA ACCEDER A SUS VISTAS
        binding = ActivityPokemonInfoBinding.inflate(layoutInflater)//

        //INDICO EL CONTENIDO DE LA ACTIVIDAD EL CUAL EL ELEMENTO RAIZ DEL BINDING.
            //AL ELEMENTO ROOT SIEMPRE ES EL PADRE GENERAL, ES DECIR EL ELEMENTO PRINCIPAL
        setContentView(binding.root)

        //ASIGNO MI TOOLBAR COMO LA ACTION BAR DE LA ACTIVIDAD
        setSupportActionBar(findViewById(R.id.my_toolbar))

        //CREA UN VIEWMODEL Y LO ASOCIA CON LA ACTIVIDAD ACTUAL, ESPECIFICA LA ACTIVIDAD, EN ESTE CASO
            // THIS, Y EL MODELO A QUERER ASOCIAR
        viewModel = ViewModelProvider(this)[PokeInfoViewModel::class.java]//

        //INICIALIZO LA INTERFAZ
        initUI()//


        //DEFINO UN LISTENER DE TIPO CLICK AL ICONO DE VUELVO ATRAS
        //REFERENCIAS: https://stackoverflow.com/questions/4038479/android-go-back-to-previous-activity
        binding.returnBackIcon.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()//FINALIZA LA ACTIVIDAD ACTUAL, VOLVIENDO A LA ANTERIOR
        }

    }

    //FUNCION PRIVADA PARA INICIALIZAR LA ACTIVIDAD
    private fun initUI() {

        val idChainEvolution : Int

        //EXTRAIGO EL ID ENVIADO POR LA ACTIVIDAD ANTERIOR A ESTA
        val id = intent.getIntExtra("id", 0)//VALOR POR DEFECTO ES 0
        Log.d("kda",id.toString())
        viewModel.getPokemonInfo(id)//LLAMO LA FUNCION DEL MODELOVISTA PARA PREPARAR
                // Y CONSEGUIR LOS DATOS DE UN POKEMON ESPECIFCO


        //ESTABLECE A LA ACTIVIDAD ACTUAL COMO UN OBSERVADOR A LA LISTA MUTABLE DEL VIEWMODEL
        //ENTONCES CUANDO OCURRA ALGUN CAMBIO, SE NOTIFICA AL OBSERVADOR Y EJECUTA LA ACCION CORRESPONDIENTE

        viewModel.pokemonInfo.observe(this) { pokemon -> // SI LOS DATOS ESTAN DISPONIBLES O EXITEN SE EJECUTA LO SIGUIENTE

            //IMAGEN DEL POKEMON
            Glide.with(this) // INDICA EL CONTEXTO
                .load(pokemon.sprites.frontDefault) // CARGA LA IMAGEN DESDE LA URL
                .error(R.drawable.pokeball_icon) // SI DA ERROR, POR DEFECTO AGREGA UN IMAGEN DEFINIDA
                .into(binding.PokeInfoImage) //AGREGA LA IMAGEN AL IMAGEVIEW


            //NOMBRE DEL POKEMON
            binding.PokeNameInfo.text = String.format(pokemon.name.uppercase()) //MODIFICA AL TEXTVIEW A MOSTRAR EL NOMBRE DEL POKEMON

            //TIPO DE POKEMON
            val typeNames = pokemon.types.map { it.type.name } // MAPEA LA LISTA TYPES Y ENTREGA EN FORMA DE LISTA
                //LOS NOMBRE DE LOS TIPOS DEL POKEMON
            setTypesUI(typeNames) //LLAMA LA FUNCION LA CUAL DEFINIRA LA POSICION DE LOS CONTENEDORES DEL TIPO DE POKEMON

            //HABILIDADES
            val abilityNames = pokemon.abilities.map { it.ability.name } //MAPEO LA LISTA ABILITIES PARA TENER UNA LISTA DE SOLO NOMBRE DE HABILIDADES
            abilityHidden = pokemon.abilities.map { it.isHidden } //MAPEO LA LISTA ISHIDDEN PARA TENER UNA LISTA DE SOLO INDICADORES SI ES OCULTO O NO UNA HABILIDAD

            //INICIALIZO EL ADAPTER DE LA LISTA DE HABILIDADES
            //ITERO POR CADA NOMBRE DE LA LISTA PARA REALIZAR UNA PETICION HTTP
            abilityNames.forEach { abilityName ->
                viewModel.getPokemonAbility(abilityName) //ASIGNO EL NOMBRE PARA CONSEGUIR INFORMACION DE LAS HABILIDADES DEL POKEMON
                Log.d("Holaaa", abilityName)
            }

            //STATS

            // ESTABLESCO EN CADA TEXT VIEW Y PROGRESS BAR EL VALOR ESTADISTICO RESPECTIVO DE CADA CARACTERISTICA DEL POKEMON
            binding.PokeStatHp.text = pokemon.stats[0].baseStat.toString()
            binding.ProgressHP.progress = pokemon.stats[0].baseStat

            binding.PokeStatAtk.text = pokemon.stats[1].baseStat.toString()
            binding.ProgressAtk.progress = pokemon.stats[1].baseStat

            binding.PokeStatDef.text = pokemon.stats[2].baseStat.toString()
            binding.ProgressDef.progress = pokemon.stats[2].baseStat


            binding.PokeStatSpA.text = pokemon.stats[3].baseStat.toString()
            binding.ProgressSpA.progress = pokemon.stats[3].baseStat

            binding.PokeStatSpD.text = pokemon.stats[4].baseStat.toString()
            binding.ProgressSpD.progress = pokemon.stats[4].baseStat

            binding.PokeStatSpe.text = pokemon.stats[5].baseStat.toString()
            binding.ProgressSpe.progress = pokemon.stats[5].baseStat

            //TOTAL DE LAS ESTADISTICAS
            val suma =
                pokemon.stats[1].baseStat + pokemon.stats[2].baseStat +
                pokemon.stats[3].baseStat + pokemon.stats[4].baseStat +
                pokemon.stats[5].baseStat //

            binding.PokeStatTotal.text = suma.toString()

            val nameSpecieSeparated = pokemon.species.url.split("/")
            viewModel.getPokemonSpecies(nameSpecieSeparated.dropLast(1).last())


            //INICIALIZO UNA VARIABLE CON POKEUSERMODEL - MODELOVISTA DE LOS DATOS DEL USUARIO
            val userCollection = PokeUserModel()//
            userCollection.getUserInfo(this)// SE CONSULTA DATOS DEL USUARIO

            //
            if (userCollection.userInfo.pokemonCaptured.contains(id)) { // SI CONTIENE EL ID DEL POKEMON ESPECIFICADO
                binding.btnPokemonCaptured.setText(R.string.PokemonAgregado) //SE MODIFICA EL MENSAJE DEL BOTON
            } else {
                binding.btnPokemonCaptured.setOnClickListener { // SE ANADE UN LISTENER AL BOTON EN CASO QUE NO CONTENGA EL ID
                    //
                    if (!userCollection.userInfo.pokemonCaptured.contains(id)) {//SI, NO CONTIENE EL ID
                        //
                        userCollection.userInfo.pokemonCaptured.add(id) // SE LO ANADE A LA LISTA DE USUARIO
                        //
                        userCollection.userInfo.name.add(pokemon.name)//SE ANADE A LA LISTA DEL USUARIO
                        //
                        userCollection.setUserInfo(this, userCollection.userInfo)//LUEGO SE LA ENVIA A ESCRIBIR Y GUARDAR EN EL ALMACENAMIENTO INTERNO
                        //
                        binding.btnPokemonCaptured.setText(R.string.PokemonAgregado)//SE AGREGA EL TEXTO DE POKEMONAGREGADO AL BOTON
                    }
                }
            }
        }
        //CHAIN EVOLUTION
        /*viewModel.pokemonChainEvolution.observe(this){ chainEvolution ->
            val chainEvolutionList : MutableList<Species> = mutableListOf()
            chainEvolutionList.add(Species(chainEvolution.chain.species.name, chainEvolution.chain.species.url))
            if(chainEvolution.chain.evolvesTo.isNotEmpty()){
                chainEvolution.chain.evolvesTo.forEach{ pokemonMiddleEvolution ->
                    chainEvolutionList.add(Species(pokemonMiddleEvolution.species.name, pokemonMiddleEvolution.species.url))
                    if(pokemonMiddleEvolution.evolvesTo.isNotEmpty()){
                        pokemonMiddleEvolution.evolvesTo.forEach{pokemonLastEvolution->
                            chainEvolutionList.add(Species(pokemonLastEvolution.species.name,
                                pokemonLastEvolution.species.url))
                        }
                    }
                }
            }
            pokeChainEvolutionAdapter.setpokemonChainEvolution(chainEvolutionList)
        }*/

        /*pokeChainEvolutionAdapter = PokeChainEvolutionAdapter()
        binding.chainEvolutionList.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        binding.chainEvolutionList.adapter = pokeChainEvolutionAdapter*/

        pokeAbilitiesAdapter = PokeInfoAbilities()

        //REFERENCIAS https://developer.android.com/develop/ui/views/layout/recyclerview?hl=es-419#:~:text=RecyclerView%20es%20el%20ViewGroup%20que,un%20objeto%20contenedor%20de%20vistas

        //AL RECYCLER VIEW DE LA ACTIVIDAD LE ASIGNO UN ADMINISTRADOR DE LAYOUT
        //EN ESTE CASO UN LINEAR DE TIPO HORIZONTAL EL CUAL SE ENCARGARA DE DISTRIBUIR LOS ELEMENTOS DE LA LISTA HORIZONTALMENTE
        binding.abilitiesRecyclerList.layoutManager =
            LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false)

        binding.abilitiesRecyclerList.adapter = pokeAbilitiesAdapter// UNO EL ADAPTER CON LA RECYLCER VIEW


        val abilityDetails: MutableList<AbilityDetails> = mutableListOf()//

        //OBSERVO LOS CAMBIOS OCURRIDOS DENTRO DE LA INFOABILITYDETAILS
        viewModel.pokemonInfoAbilityDetails.observe(this) { pokemonA ->

            //FILTROS LOS RESULTADOS DE LOS EFECTOS A SOLO ESPAÑOL
            var spanishLanguageEffect =
                pokemonA.flavorTextEntries.filter {
                    it.language.name == "es"
                }

            //SI LA RESPUESTA ES NULA, LO FILTRO PARA SOLO EFECTOS EN INGLES
            if (spanishLanguageEffect.isEmpty()) {
                spanishLanguageEffect =
                    pokemonA.flavorTextEntries.filter {
                        it.language.name == "en"
                    } //
            }

            //FILTROS LOS RESULTADOS DE NAME A SOLO ESPANOL
            val spanishLanguageName = pokemonA.names.filter { it.language.name == "es" }

            //GUARDO EL PRIMER RESULTADO DE NOMBRE EN ESPANOL
            val nameInSpanish = spanishLanguageName.firstOrNull()?.name

            //GUARDO EL PRIMER RESULTADO DE EFECTO EN ESPANOL
            val effectInSpanish = spanishLanguageEffect.firstOrNull()?.flavorText

            //AGREGO AMBOS A MI LISTA DE HABILIDADES
            abilityDetails.add(AbilityDetails(nameInSpanish, effectInSpanish))
            //ENVIO LOS DATOS A AL ADAPTER DE LA RECYLCER VIEW
            pokeAbilitiesAdapter.setData(abilityDetails, abilityHidden)
        }

        viewModel.pokemonDescription.observe(this) { pokemonD -> //SI HAY DISPONIBILIDAD

            //SE FILTRA PARA SOLO DESCRIPCIONES EN ESPANOL
            var spanishDescription =
                pokemonD.flavorTextEntries.filter {
                    it.language.name == "es"
                } //

            //SI NO EXISTEN, DESCRIPCIONES SOLO EN INGLES
            if (spanishDescription.isEmpty()) {
                spanishDescription =
                    pokemonD.flavorTextEntries.filter {
                        it.language.name == "en"
                    } //

                //AVISO EN LA APLICACION SI PREGUNTAN LA RAZON DE PORQUE ESTA EN INGLES EN ALGUNAS PARTES
                Toast.makeText(
                    this, "NO EXISTE DESCRIPCION TRADUCIDA AL ESPAÑOL",
                    Toast.LENGTH_LONG
                ).show() //MOSTRAR EL TOAST
            }
            //AGREGA LA DESCRIPCION EN EL TEXTVIEW CORRESPONDIENTE
            binding.pokeDescription.text =
                spanishDescription.firstOrNull()?.flavorText.toString()//

            Log.d("description",spanishDescription.toString())

            //CHAIN EVOLUTION
            /*val urlParts: List<String> = pokemonD.evolutionChain.url.split("/")
            val evolutionChainId = urlParts.dropLast(1).last()
            viewModel.getPokemonChainEvolution(evolutionChainId.toInt())*/
        }

    }


    private fun setTypesUI(list: List<String>) {
        //SI EL TAMANO DE LA LISTA ES MAYOR O IGUAL QUE 2 SE EJECUTA LO SIGUIENTE
        if (list.size >= 2) {

            //EL POKEMON TENDRA DOS TIPOS, POR LO TANTO, A CADA CONTENEDOR Y SU RESPECTIVO TEXVIEW
                //SE AGREGA SU RESPECTIVO TIPO
            binding.PokeType1.text = list[0].uppercase()
            binding.PokeType2.text = list[1].uppercase()


            //REFERENICAS: https://chatgpt.com/share/670e0de2-79e4-8001-b6f0-451a4863bd93
            //VARIABLE QUE CONTIENE LOS PARAMETROS DE UNA RELATIVELAYOUT
            val params = binding.PokeType1Container.layoutParams as RelativeLayout.LayoutParams

            //SE AGREGA UNA REGLA AL PARAMETRO DE ALINEARSE CON EL PADRE A LA DERECHA
            params.addRule(RelativeLayout.ALIGN_PARENT_START) //
            binding.PokeType1Container.layoutParams = params //SE ENVIA AL PARAMETRO DEL CONTENEDOR DE TIPO1

            //VARIABLE QUE CONTIENE LOS PARAMETROS DE UNA RELATIVELAYOUT
            val params2 = binding.PokeType2Container.layoutParams as RelativeLayout.LayoutParams
            params2.addRule(RelativeLayout.ALIGN_PARENT_END) // //SE AGREGA UNA REGLA AL PARAMETRO DE ALINEARSE CON EL PADRE A LA IZQUIERDA
            binding.PokeType2Container.layoutParams = params2 //SE ENVIA AL PARAMETRO DEL CONTENEDOR DE TIPO2
            binding.PokeType2Container.visibility = View.VISIBLE //Y SE INDICA QUE DEBE ESTAR VISIBLE EL CONTENEDOR DEL TIPO2

        } else {
            //SI ES MENOR EL TAMANO A 2, SIGNIFICA QUE SOLO HAY UN TIPO PARA EL POKEMON, POR LO TANTO,
                //SOLO SE MODIFICA EL TEXTVIEW
            binding.PokeType1.text = list[0].uppercase()

        }
    }
}