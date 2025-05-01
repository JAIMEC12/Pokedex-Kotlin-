package com.example.pokemon_red_radical

import adapter.PokeTeamAdapter
import android.content.Intent
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.pokemon_red_radical.databinding.ActivityBossInfoBinding
import models.PokeBossesModel
import models.PokeUserModel

class BossInfo : AppCompatActivity() {

    //
    private lateinit var binding : ActivityBossInfoBinding
    private lateinit var viewModel : PokeBossesModel
    private lateinit var pokeTeamAdapter : PokeTeamAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //ENLACE DE LA VISTA DE ITEMS PARA ACCEDER A SUS ELEMENTOS
        binding = ActivityBossInfoBinding.inflate(layoutInflater)

        //INDICO EL CONTENIDO DE LA ACTIVIDAD EL CUAL EL ELEMENTO RAIZ DEL BINDING.
            //AL ELEMENTO ROOT SIEMPRE ES EL PADRE GENERAL, ES DECIR EL ELEMENTO PRINCIPAL
        setContentView(binding.root)

        //ASIGNO MI TOOLBAR COMO LA ACTION BAR DE LA ACTIVIDAD
        setSupportActionBar(findViewById(R.id.my_toolbar))

        //CREA UN INSTANCIA O OBJETO POKEBOSSMODEL
        viewModel = PokeBossesModel()

        //DEFINO UN LISTENER DE TIPO CLICK AL ICONO DE VUELVO ATRAS
        //REFERENCIAS: https://stackoverflow.com/questions/4038479/android-go-back-to-previous-activity
        binding.returnBackIcon.setOnClickListener {
            finish()//FINALIZA LA ACTIVIDAD ACTUAL PARA RETORNAR A LA ACTIVIDAD ANTERIOR
        }

        //INICIALIZA LA INTERFAZ
        initUI()
    }

    private fun initUI(){

        //SE LLAMA A LA INFORMACION DE LOS JEFES DE GIMNASIO
        viewModel.getBosses(this)

        //SE EXTRAE EL ID ENVIADO POR LA ACTIVIDAD ANTERIOR AL DAR CLICK EN UNO DE LOS ELEMENTOS DE LA LISTA
        val id = intent.getIntExtra("id",0)

        //VARIABLE QUE ALMACENA LA URL DE LA IMAGEN
        val bossImage = viewModel.bossesList[id].url

        //EXTRAER LA IMAGEN DEL BOSS DE LA URL Y AGREGARLA EN EL IMAGEVIEW
        Glide.with(this)
            .load(bossImage)
            .into(binding.BossInfoImage)

        //EXTRAER LA IMAGEN DE LA MEDALLA DEL BOSS DE LA URL (.MEDAL) Y AGREGARLA EN LA IMAGEVIEW
        Glide.with(this)
            .load(viewModel.bossesList[id].medal)
            .into(binding.bossMedalImage)


        //NOMBRE Y TIPO DE GIMNASIO DEL JEFE
        binding.bossName.text = viewModel.bossesList[id].name
        binding.BossType.text = viewModel.bossesList[id].boss_type

        // INICIALIZO EL ADAPTADOR
        pokeTeamAdapter = PokeTeamAdapter{
            val intent= Intent(this, PokemonInfo::class.java)
            intent.putExtra("id",it)
            startActivity(intent)
        }//PASA UNA FUNCION ANONIMA COMO PARAMETRO AL CONSTRUCTOR LO CUAL SE ACTIVA AL CLICK Y ENVIA A LA ACTIVIDAD POKEMONINFO

        //REFERENCIAS https://developer.android.com/develop/ui/views/layout/recyclerview?hl=es-419#:~:text=RecyclerView%20es%20el%20ViewGroup%20que,un%20objeto%20contenedor%20de%20vistas

        //INDICO AL RECYCLER VIEW COMO SE VAN A DISTRIBUIR LOS ELEMENTOS
        binding.bossTeam.layoutManager=LinearLayoutManager(this,
            LinearLayoutManager.HORIZONTAL,false)//SE DISTRIBUIRA DE MANERA HORIZONTAL LINEALMENTE
        binding.bossTeam.adapter = pokeTeamAdapter//VINCULO EL ADAPTER CON EL RECYLCER VIEW
        pokeTeamAdapter.setData(viewModel.bossesList[id].pokemonTeam)//LLENO EL RECYCLER VIEW CON LOS DATOS DE LA LISTA

        //SE CREA UNA INSTANCIA DE POKEUSERMODEL
        val userCollection= PokeUserModel()
        userCollection.getUserInfo(this)//SE EXTRAE LOS DATOS DEL USUARIO

        //VALIDAMOS SI EL USUARIO HA DERROTADO A ESTE JEFE
        if(userCollection.userInfo.medalsGained.contains(viewModel.bossesList[id].medal)){
           binding.btnBossDefeat
               .setText(R.string.MedallaAgrerada)// SI LO HA DERROTADO EL BOTON EN LA PARTE INFERIOR DARA EL MENSAJE QUE LA MEDALLA FUE AGREGADA
        }
        else{
            binding.btnBossDefeat.setOnClickListener { // SI NO HA SIDO DERROTADO, SE AGREGA UN LISTENER AL BOTON
                //ESTO ES PARA VALIDAR UNA VEZ DADO CLICK AL BOTON, NO SE EJECUTE DE NUEVO
                if(!userCollection.userInfo.medalsGained.contains(viewModel.bossesList[id].medal))
                {
                    //SE AGREGA LA INFORMACION DE LA MEDALLA Y TIPO DE JEFE CUAL FUE VENCIDO
                    userCollection.userInfo.medalsGained
                        .add(viewModel.bossesList[id].medal)//

                    userCollection.userInfo.typeMedal
                        .add(viewModel.bossesList[id].boss_type)//
                    userCollection
                        .setUserInfo(this, userCollection.userInfo)// SE AGREGA LOS DATOS AL ALMACENAMIENTO INTERNO

                    binding.btnBossDefeat
                        .setText(R.string.MedallaAgrerada)//Y SE CAMBIA EL TEXTO DE BOTON PARA INDICAR QUE LA MEDALLA YA FUE AGREGADA
                }
            }
        }
    }
}