package ipvc.estg.secondhome.api

import ipvc.estg.secondhome.models.Announcement
import ipvc.estg.secondhome.models.DefaultResponse
import ipvc.estg.secondhome.models.ListUser
import ipvc.estg.secondhome.models.User
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
    @POST("user/loginWeb")
    fun login(
        @Field("email") username:String,
        @Field("password") password: String
    ): Call<User>

    @FormUrlEncoded
    @PUT("user/update")
    fun updateUser(
        @Field ("token") token:String,
        @Field("username") username:String,
        @Field("name") name:String,
        @Field("email") email:String,
        @Field("birthdayDate") birthdayDate: Date,
        @Field("contact") contact:Int
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("user/me")
    fun me(  @Field ("token") token:String,): Call<User>


    @GET("user/getUsersAndNAnnouncements")
    fun allUsers(  ): Call<List<User>>


    @FormUrlEncoded
    @POST("announcement/getAnnouncements")
    fun annoucements(  @Field ("id") token:String,): Call<List<Announcement>>


    @FormUrlEncoded
    @POST("announcement/delete")
    fun deleteAnnouncement(  @Field ("id") id:String): Call<DefaultResponse>


}