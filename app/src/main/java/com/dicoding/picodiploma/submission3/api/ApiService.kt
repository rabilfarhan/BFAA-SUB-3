package com.dicoding.submission2.api

import com.dicoding.picodiploma.submission3.api.UserResponse
import com.dicoding.picodiploma.submission3.api.UserResponseItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @Headers("Authorization: token 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")
    @GET("users")
    fun getUsers(): Call<List<UserResponseItem>>

    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getSearchUser(
        @Query("q") query: String
    ): Call<UserResponse>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<UserResponseItem>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getFollowers(
        @Path("username") username: String
    ): Call<List<UserResponseItem>>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getFollowing(
        @Path("username") username: String
    ): Call<List<UserResponseItem>>


}