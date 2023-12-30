package tn.esprit.greenworld.API


import androidx.room.Delete
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import tn.esprit.greenworld.models.Dechets
import tn.esprit.greenworld.models.DechetsItem
import tn.esprit.greenworld.models.DechetsList
import tn.esprit.greenworld.models.TypeList
import tn.esprit.greenworld.models.User
import tn.esprit.greenworld.models.User5


interface DechetsApi {
   @POST("/dechets")
   fun addDechets(@Body dechets: DechetsItem): Call<Dechets>



   @GET("dechets/user/{userId}")
   fun getDechetsForUser(@Path("userId") userId: String): Call<DechetsList>

   @DELETE("dechets/{id}")
   fun deleteDechets(@Path("id") id: String): Call<ResponseBody>

}