package com.example.pokemon_red_radical

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.pokemon_red_radical.databinding.ActivityItemInfoBinding
import models.ItemInfoViewModel

class ItemInfo : AppCompatActivity() {


    private lateinit var viewModel : ItemInfoViewModel
    private lateinit var binding : ActivityItemInfoBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //INICIALIZO BINDING PARA ACCEDER A LOS ELEMENTOS DIRECTAMENTE DE LA INTERFAZ DE ITEMINFO
            //INFLA O CREA EL DISENO DEL ARCHIVO XML Y LO ENLAZO PARA ACCEDER A SUS VISTAS
        binding = ActivityItemInfoBinding.inflate(layoutInflater) //

        //INDICO EL CONTENIDO DE LA ACTIVIDAD EL CUAL EL ELEMENTO RAIZ DEL BINDING.
            //AL ELEMENTO ROOT SIEMPRE ES EL PADRE GENERAL, ES DECIR EL ELEMENTO PRINCIPAL
        setContentView(binding.root) //

        //ASIGNO MI TOOLBAR COMO LA ACTION BAR DE LA ACTIVIDAD
        setSupportActionBar(findViewById(R.id.my_toolbar))

        //CREA UN VIEWMODEL Y LO ASOCIA CON LA ACTIVIDAD ACTUAL,
            // ESPECIFICA LA ACTIVIDAD, EN ESTE CASO THIS, Y EL MODELO A QUERER ASOCIAR
        viewModel = ViewModelProvider(this,)[ItemInfoViewModel::class.java]

        initUI()//INICIALIZA LA INTERFAZ

        //DEFINO UN LISTENER DE TIPO CLICK AL ICONO DE VUELVO ATRAS
        //REFERENCIAS: https://stackoverflow.com/questions/4038479/android-go-back-to-previous-activity
        binding.returnBackIcon.setOnClickListener {
            finish()//FINALIZA LA ACTIVIDAD ACTUAL PARA RETORNAR LA ANTERIOR
        }
    }

    //
    private fun initUI(){

        //EXTRAER LA ID DEL EXAMEN ANTERIOR QUE FUE ENVIADO A TRAVES DEL INTENT
        val id= intent.getIntExtra("id",0)// EL VALOR POR DEFECTO ES IGUAL A 0
        viewModel.getItemInfo(id)//Y SE LLAMA LA FUNCION PARA EXTRAER LA INFORMACION DEL POKEMON

        //OBSERVAR LOS CAMBIOS OCURRIDO EN LA MUTABLELIVEDATA DE ITEMINFO DEL VIEWMODEL
        viewModel.itemInfo.observe(this) { item ->//
            //IMAGEN
            Glide.with(this) //INDICA EL CONTEXTO
                .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/" //LA URL DE DONDE SE SACA LA IMAGEN
                        + item.name + ".png")
                .error(R.drawable.pokeball_icon)//SI NO SE CONSIGUE LA IMAGEN O DA ERROR, POR DEFECTO PONE UN ICONO DE POKEMON
                .into(binding.itemImage)//INSERTA LA IMAGEN EN EL IMAGEVIEW

            binding.itemName.text = item.name.uppercase()//ESCRIBE EL NOMBRE DEL ITEM EN LA PANTALLA EN EL TEXTVIEW

            //EFECTO
            //VARIABLE QUE VA A ALMACENAR LOS VALORES EN ESPANOL DE LOS EFECTOS DEL ITEM
            var spanishLanguageEffect =
                item.flavorTextEntriesEffects.filter { //FILTRA PARA BUSCAR EL EFECTO EN IDIOMA ESPANO Y LA VERSION SEA DEL ULTIMO JUEGO
                    it.language.name == "es" && it.versionGroup.name == "sword-shield"
                } //

            //VALIDA SI NO SE ENCONTRO NINGUNO EN ESPANOL
            if (spanishLanguageEffect.isEmpty()) {
                spanishLanguageEffect =
                    item.flavorTextEntriesEffects.filter { //SE FILTRA PARA CONSEGUIR UNO EN INGLES
                        it.language.name == "en" && it.versionGroup.name == "sword-shield"
                    }//
            }

            //Y SE UTILIZA EL PRIMER EFECTO QUE APARECE
            val spanishItemEffect = spanishLanguageEffect.firstOrNull()?.text

            //Y SE LO ESCRIBE EN SU RESPECTIVA TEXTVIEW
            binding.itemEffect.text = spanishItemEffect.toString()

            //PRECIO DEL ITEM
            binding.itemCost.text = String.format("¥" + item.cost.toString())
        }
    }
}