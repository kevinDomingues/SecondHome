package ipvc.estg.secondhome.api

import ipvc.estg.secondhome.models.DefaultResponse
import ipvc.estg.secondhome.models.Evaluation
import ipvc.estg.secondhome.models.Advertisements
import ipvc.estg.secondhome.models.User
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

    @Multipart
    @POST("announcement/registerAnnouncement")
    fun insertAdd( @Header("x-access-token") token: String,
//        @Part file : List<MultipartBody.Part>,
        @Part("type") type: RequestBody,
        @Part("netArea") netArea: RequestBody,
        @Part("rooms") rooms: RequestBody,
        @Part("bathrooms") bathrooms: RequestBody,
        @Part("price") price: RequestBody,
        @Part("location") location: RequestBody,
        @Part("lat") lat: RequestBody,
        @Part("lng") lng: RequestBody,
        @Part("constructionYear") constructionYear: RequestBody,
        @Part("accessibility") accessibility: RequestBody,
        @Part("wifi") wifi: RequestBody,
        @Part("email") email: RequestBody,
        @Part("contact") contact: RequestBody,
        @Part("name") name: RequestBody
    ): Call<DefaultResponse>


    @GET("announcement")
    fun getAnnouncement(@Header("x-access-token") token: String) : Call<ArrayList<Advertisements>>

    @GET("getMyAnnouncements")
    fun getMyAnnouncements(@Header("x-access-token") token: String) : Call<ArrayList<Advertisements>>

    @GET("announcement/{id}")
    fun getAnnouncementById(@Header("x-access-token") token: String, @Path("id") id: String) : Call<Advertisements>

    @FormUrlEncoded
    @PATCH("announcement/update/{id}")
    fun updateAnnoucenement(@Header("x-access-token") token: String, @Path("id") id: String,
        @Field("type") type: Int,
        @Field("netArea") netArea: Int,
        @Field("rooms") rooms: Int,
        @Field("bathrooms") bathrooms: Int,
        @Field("price") price: Int,
        @Field("location") location: String,
        @Field("lat") lat: Double,
        @Field("lng") lng: Double,
        @Field("constructionYear") constructionYear: Int,
        @Field("accessibility") accessibility: Boolean,
        @Field("wifi") wifi: Boolean,
        @Field("email") email: String,
        @Field("contact") contact: String,
        @Field("name") name: String
    ) : Call<DefaultResponse>

    @FormUrlEncoded
    @POST("announcement/delete")
    fun deleteAnnouncement(@Header("x-access-token") token: String,
        @Field("id") id: String
    ) : Call<DefaultResponse>
}
