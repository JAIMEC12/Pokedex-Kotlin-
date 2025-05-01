package models

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import pokeApi_Data.PokeUser
import java.io.IOException

/*
*AQUI SE VA EXTRAER INFORMACION DE UN ARCHIVO JSON PERO UBICADO EN EL ALMACENAMIENTO INTERNO DE LA APLICACION
* AQUI EL JSON NO SOLO SERA DE LECTURA, POR LO CUAL, SE LO UBICO EN EL ALMACENAMIENTO INTERNO, EN CAMBIO, EL JSON
* SERA DE LECTURA Y ESCRITURA. SU PROCESO ES SIMILAR A LA LECTURA DE UN JSON NORMAL CON UN LIGERO CAMBIO
* */


//SE CREA LA CLASE DE POKEUSERMODEL
class PokeUserModel {

    //SE CREA UN OBJETO COMPANERO
    companion object{
        private const val TAG ="PokeUserModel" // VARIABLE PARA EL LOG
    }

    //VARIABLE QUE VA TENER LA INFORMACION DEL USUARIO
    var userInfo: PokeUser = PokeUser(mutableListOf(), mutableListOf(), mutableListOf(),
        mutableListOf()) //

    //EL CONTENIDO DEL JSON ES CARGADO EN EL OBJETO POKEUSER
    fun getUserInfo(context: Context) {
        userInfo = uploadDataFromJson(context) //SE LLAMA LA FUNCION PARA CARGAR LOS DATOS
    }

    //https://chatgpt.com/share/6716f556-11d8-8001-9977-cd2d3265341d
    //https://www.geeksforgeeks.org/read-from-files-using-bufferedreader-in-kotlin/

    //SE ACTUALIZA O SE ESCRIBE LA INFORMACION EL JSON
    fun setUserInfo(context: Context, user :PokeUser){
        val gson = Gson() //CONSTRUCTOR GSON, HEREDA TODOS SUS METODOS
        val json = gson.toJson(user) //TRANSFORMA EL OBJETO POKEUSER A FORMA JSON
        context.openFileOutput("UserInformation.json", Context.MODE_PRIVATE).use { output -> //ABRE EL ARCHIVO JSON
                //AL PONER OPENFILEOUTPUT, ABRE EL ARCHIVO DEL ALMACENAMIENTO INTERNO PARA ESCRIBIR
                    // Y MODE_PRIVATE INDICA QUE SOLO LA APP TIENE ACCESO A EL
            output.write(json.toByteArray()) //ESCRIBE LOS DATOS EN EL ARCHIVO JSON COMO UN ARREGLO DE BYTES
        }
    }

    //CARGA EL CONTENIDO DE JSON
    private fun uploadDataFromJson(context: Context) : PokeUser{ // CARGA LOS DATOS Y DEVUELVE UN OBJETO POKEUSER
        val gson = Gson() // CONSTRUCTOR GSON Y HEREDA SUS METODOS
        val json = readJson(context) // LEE EL JSON Y ALMACENA SU CONTENIDO EN ESTA VARIABLE

        Log.d(TAG,json.toString()) // SE VERIFICA SU CONTENIDO

        //RETORNA SI
        return if(json != null){
            gson.fromJson(json,PokeUser::class.java) //RETORNA LOS VALORES DE JSON TRANSFORMADO A GSON EN UN OBJETO DE POKEUSER
        }
        //DE LO CONTRARIO RETORNA UN OBJETO CON SU PROPIEDADES VACIAS
        else{
            PokeUser(mutableListOf(),mutableListOf(), mutableListOf(), mutableListOf()) //
        }
    }

    //LEE EL CONTENIDO DE JSON
    private fun readJson(context: Context):String?{
        return try {
            val inputStream = context.openFileInput("UserInformation.json") //ABRE EL ARCHIVO PARA SU LECTURA
            inputStream.bufferedReader().use { it.readText() } // CREA UN LECTOR DE BUFFER EL CUAL DEL
                 // ARCHIVO LECTURA LEE EL CONTENIDO. .USER PERMITE MANEJAR EL FLUJO DEL ARCHIVO AUTOMATICAMENTE
        } catch (e: IOException) { // CAPTURA LA EXPCEPCION
            e.printStackTrace() //IMPRIME EL ERROR EN LA CONSOLA
            null //RETORNA NULL SI SE CAPTURO UN ERROR
        }
    }


}