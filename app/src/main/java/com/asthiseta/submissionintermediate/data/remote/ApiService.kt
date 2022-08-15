package com.asthiseta.submissionintermediate.data.remote

import com.asthiseta.submissionintermediate.data.model.auth.UserLoginResponse
import com.asthiseta.submissionintermediate.data.model.auth.UserRegisterResponse
import com.asthiseta.submissionintermediate.data.model.stories.AddStoryResponse
import com.asthiseta.submissionintermediate.data.model.stories.StoryListResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    fun userRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ) : Call<UserRegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun userLogin(
        @Field("email") email: String,
        @Field("password")password: String
    ): Call<UserLoginResponse>

    @GET("stories")
    fun getAllStory(
        @Header("Authorization") auth: String
    ): Call<StoryListResponse>

    @Multipart
    @POST("stories")
    fun uploadStory(
        @Header("Authorization") auth: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<AddStoryResponse>
}