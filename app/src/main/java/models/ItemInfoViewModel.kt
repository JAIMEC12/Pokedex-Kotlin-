package models

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pokeApi_Data.Item
import pokeApi_Data.PokeApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


//REFERENCIAS DE https://www.youtube.com/watch?v=oKuqqM5oolk&list=LL&index=1&t=137s

/*SE CREA LA CLASE ITEMINFOVIEWMODEL LA CUAL VA HEREDAR DE LA CLASE VIEWMODEL
ESTO NOS VA A PERMITIR TRABAJAR DE MANERA SEPARADA, ES DECIR, LA LOGICA DE LOS DATOS SERA GESTIONADA APARTE
PARA LUEGO SER MANDADA CUANDO ES SOLICITADA A LA INTERFAZ.
POR LO TANTO, AL ESTAR APARTADO, SON CAPACES DE RESISTIR A LOS CAMBIOS DE CONFIGURACIONES
    Y LOGRAR MANTENER LOS DATOS SIN AFECTAR A LA INTERFAZ O ALGUN COMPONENTE QUE HAGAN USO DE ELLOS
* */

class ItemInfoViewModel: ViewModel() {

    //PARA CREAR VARIABLES ESTATICAS - SE CREA UN OBJETO COMPANERO
    companion object{
        private const val TAG = "ItemInfoViewModel"
        //VARIABLE PRIVADA Y CONSTANTE QUE VA SER USADA PARA IDENTIFICAR MENSAJES EN EL DEPURADOR
    }

    //VARIABLE EN LA CUAL SE VA ALMACENAR UNA INSTANCIA DE RETROFIT PARA LAS SOLICITUDES HTTP
    private val retrofit = Retrofit.Builder()//CONSTRUCTOR DE LA INSTANCIA
        .baseUrl("https://pokeapi.co/api/v2/") //LA URL BASE DE LA POKEAPI PARA LAS LLAMADAS , NO EL ENDPOINT!
        .addConverterFactory(GsonConverterFactory.create()) //AGREGA UN CONVERTIDOR GSON
            // PARA TRANSFORMAR LOS DATOS JSON A OBJETOS DE KOTLIN, ES DECIR, GSON
        .build() //CONSTRUCCION DE LA INSTANCIA

    //VARIABLE EN LA CUAL SE VA UNIR LOS METODOS DEFINIDOS EN LA INTERFAZ DE POKEAPISERVICE CON LA INSTANCIA RETROFIT
    private val service : PokeApiService = retrofit.create(PokeApiService ::class.java)
    //RETROFIT SE ENCARGARA DE UNIR Y MANEJAR LOS ENDPOINTS EN CONJUNTO CON LOS PARAMETROS CON LA URL
        // Y TRANSFORMAR EL RESULTADO EN UN GSON. BASICAMENTE FACILITARA LA ADMINISTRACION DE LAS SOLICITUDES HTTP

    //VARIABLE LA CUAL SE DEFINE Y ALMACENA UN OBJETO DE TIPO MUTABLELIVEDATA
        //SU VALORES PUEDEN CAMBIAR O ACTUALIZARSE DESDE CUALQUIER ACTIVIDAD QUE ESTE USANDO U OBSERVANDOLA
    val itemInfo = MutableLiveData<Item>()

    //FUNCION DEL MODELOVISTA PARA OBTENER LA INFORMACION DEL ITEM
    fun getItemInfo(id : Int?){
        //VARIABLE CALL LA CUAL VA A TENER LA SOLICITUD HTTP YA ESTRUCTURADA Y FORMULADA
            //ESTO ENTREGABA UNA INVOCACION CON RESPUESTA DE ENTREGA DE UN TIPO OBJETO ITEM
        val call = service.getItemInfo(id)
        call.enqueue(object : Callback<Item> { // SE REALIZA LA LLAMADA DE MANERA ASINCRONA PASANDO COMO PARAMETRO LA COMUNICACION DE LA RESPUESTA
            //SE SOBREESCRIBE LA FUNCION DE SI FUE RESPONDIDA LA LLAMADA
            override fun onResponse(call: Call<Item>, response: Response<Item>) { //LA LLAMADA, RESPUESTA - AMBOS ENTREGAN UN OBJETO ITEM
                response.body()?.let { item -> //LA RESPUESTA YA ESTA DESERIALIZADA Y SE VERIFICA SI NO ES NULA
                        //Y ENVIA LOS VALORES.
                    itemInfo.postValue(item) //PUBLICA LOS VALORES EN EL OBJETO ITEMINFO Y SE ACTUALIZA
                    //PARA SABER DESDE EL LADO PROGRAMADOR SI FUE RESPONDIDA
                    Log.d(TAG,response.toString())
                }
            }
            //O SI FUE LA RESPUESTA DE LA LLAMADA FUE FALLIDA
            override fun onFailure(call: Call<Item>, t: Throwable) {
                call.cancel() //CANCELLA LA SOLICITUD PARA GARANTIZAR NO SE EJECUTE NUEVAMENTE
                //PARA SABER DEL LADO PROGRAMADOR SI HUBO ALGUN ERROR Y CUAL FUE
                Log.e(TAG,t.toString())
            }
        })
    }
}