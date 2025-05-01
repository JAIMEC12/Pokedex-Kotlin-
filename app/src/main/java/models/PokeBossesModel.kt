package models

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import pokeApi_Data.BossesList
import java.io.IOException


/*
* AQUI LOS DATOS SON EXTRAIDOS DE UN JSON GUARDADO EN LA CARPETA ASSETS, EL ARCHIVO ENCONTRADO AQUI SOLO
* SERA DE LECTURA EN TIEMPO  DE EJECUCION. LOS DATOS EXTRAIDOS DE ESTE JSON VAN A SER GUARDADOS EN UNA LISTA
* DE OBJETOS BOSSESLIST
* */

//SE CREA LA CLASE POKEBOSSESMODEL
class PokeBossesModel{

    //SE CREA UN OBJETO COMPANERO
    companion object{
        private const val TAG ="PokeUserModel" // SE DEFINE UNA VARIABLE ESTATICA QUE SERA USADA PARA EL LOG
    }


    //VARIABLE DE TIPO LISTA DE OBJETOS BOSSESLIST
    var bossesList: List<BossesList> = emptyList()

    //FUNCION PARA CONSEGUIR LOS BOSSES Y ALMACENARLOS EN LA LISTA
    fun getBosses(context: Context) {
        //
        bossesList = uploadDataFromJson(context) //SE ALMACENA DE LO QUE SE RECUPERAR DEL JSON
    }

    //https://chatgpt.com/share/6716f556-11d8-8001-9977-cd2d3265341d
    //https://www.geeksforgeeks.org/read-from-files-using-bufferedreader-in-kotlin/

    //FUNCION PARA CARGAR LOS DATOS Y TRANSFORMAR DE JSON A GSON
    private fun uploadDataFromJson(context: Context): List<BossesList> {
        val gson = Gson() //CONSTRUCTO GSON, VA A TENER SUS METODOS
        val json = readDataFromJson(context) // LEE LA INFORMACION DEL JSON Y LA ALMACENA EN JSON

        Log.d(TAG,json.toString()) //VERIFICO EL CONTENIDO DEL JSON EN EL DEPURADOR

        //RETORNA SI
        return if (json != null) {
            gson.fromJson(json,Array<BossesList>::class.java).toList() // PARSEA EL JSON TO GSON Y
                // LO GUARDA EN UN ARREGLO DE OBJETO BOSSESLIST Y LO TRANSFORMA A UNA LISTA
        }
        //DE LO CONTRARIO
        else {
            listOf() // SI LOS DATOS LEIDOS DEL JSON FUERON NULOS, RETORNA UNA LISTA VACIA
        }
    }

    //FUNCION PARA LEER EL JSON
    private fun readDataFromJson(context: Context): String? {
        //INTENTAMOS CON UN TRY Y CATCH, SI SE NO EXISTE UN ERROR SE RETORNA LO LEIDO DEL JSON
        return try {
            val inputStream = context.assets.open("BossesPokemon.json") //ABRIMOS EL ARCHIVO JSON DE ASSESTS
            inputStream.bufferedReader().use { it.readText() } //SE UTILIZA UN LECTOR DE BUFFER PARA LEER EL CONTENIDO DEL JSON
                //.USE PERMITE MANEJAR EL FLUJO DE ARCHIVO AUTOMATICAMENTE, SU APERTURA Y CIERRE
        }
        //DE LO CONTRARIO CAPTURA Y LEVANTA UNA EXCEPCION
        catch (e: IOException) {
            e.printStackTrace() //SIMILAR AL LOG, AQUI SE IMPRIME EN CONSOLA EL ERROR Y EL MENSAJE RESPECTIVO
            null //RETORNA UN NULL SI OCURRE ALGUN ERROR
        }
    }
}