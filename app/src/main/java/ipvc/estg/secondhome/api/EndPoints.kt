package ipvc.estg.secondhome.api

import ipvc.estg.secondhome.models.DefaultResponse
import ipvc.estg.secondhome.models.Habitacoes
import ipvc.estg.secondhome.models.Evaluation
import ipvc.estg.secondhome.models.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.util.*

interface EndPoints {

    @FormUrlEncoded
    @POST("user/signUp")
    fun signUp(
        @Field("username") username:String,
        @Field("name") name:String,
        @Field("email") email:String,
        @Field("password") password:String,
        @Field("birthdayDate") birthdayDate: String,
        @Field("contact") contact:Int
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("user/login")
    fun login(
        @Field("username") username:String,
        @Field("password") password: String
    ): Call<User>

    @GET("user/me")
    fun getUserById(@Header("x-access-token") token: String) : Call<User>

    @FormUrlEncoded
    @PUT("user/update")
    fun updateUser(
        @Field("token") token:String,
        @Field("username") username:String,
        @Field("name") name:String,
        @Field("email") email:String,
        @Field("birthdayDate") birthdayDate: Date,
        @Field("contact") contact:Int
    ): Call<DefaultResponse>
  
    @FormUrlEncoded
    @POST("evaluation/createEvaluation")
    fun sendEvaluation(
        @Field("evaluationText") evaluationText:String,
        @Field("amountOfStars") amountOfStars: Float?
    ): Call<Evaluation>
  
    @POST("announcement/registerAnnouncement")
    fun insertAdd(
        @PartMap partMap: HashMap<String?, RequestBody?>,
        @Part file : List<MultipartBody.Part>
    ): Call<DefaultResponse>

    @GET("announcement")
    fun getAnnouncement(@Header("x-access-token") token: String) : Call<ArrayList<Habitacoes>>

}

