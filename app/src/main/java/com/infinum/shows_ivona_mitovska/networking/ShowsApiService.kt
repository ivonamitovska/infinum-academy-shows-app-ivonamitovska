package com.infinum.shows_ivona_mitovska.networking

import RegisterResponse
import com.infinum.shows_ivona_mitovska.data.request.CreateReviewRequest
import com.infinum.shows_ivona_mitovska.data.request.LoginRequest
import com.infinum.shows_ivona_mitovska.data.request.RegisterRequest
import com.infinum.shows_ivona_mitovska.data.response.CreateReviewResponse
import com.infinum.shows_ivona_mitovska.data.response.LoginResponse
import com.infinum.shows_ivona_mitovska.data.response.ReviewsListResponse
import com.infinum.shows_ivona_mitovska.data.response.ShowDetailsResponse
import com.infinum.shows_ivona_mitovska.data.response.ShowsResponse
import com.infinum.shows_ivona_mitovska.data.response.TopRatedResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ShowsApiService {

    @POST("/users")
    fun register(
        @Body request: RegisterRequest
    ): Call<RegisterResponse>

    @POST("/users/sign_in")
    fun signIn(
        @Body request: LoginRequest
    ): Call<LoginResponse>

    @GET("/shows")
    fun getShows(
        @Header("access-token") accessToken: String,
        @Header("client") client: String,
        @Header("token-type") tokenType: String,
        @Header("expiry") expiry: String,
        @Header("uid") uid: String
    ): Call<ShowsResponse>

    @GET("/shows/{id}")
    fun getShowDetails(
        @Header("access-token") accessToken: String,
        @Header("client") client: String,
        @Header("token-type") tokenType: String,
        @Header("expiry") expiry: String,
        @Header("uid") uid: String,
        @Path("id") searchById: String): Call<ShowDetailsResponse>

    @POST("/reviews")
    fun createReview(
        @Header("access-token") accessToken: String,
        @Header("client") client: String,
        @Header("token-type") tokenType: String,
        @Header("expiry") expiry: String,
        @Header("uid") uid: String,
        @Body request: CreateReviewRequest
    ): Call<CreateReviewResponse>

    @GET("/shows/{show_id}/reviews")
    fun getReviews(
        @Header("access-token") accessToken: String,
        @Header("client") client: String,
        @Header("token-type") tokenType: String,
        @Header("expiry") expiry: String,
        @Header("uid") uid: String,
        @Path("show_id") showId: String): Call<ReviewsListResponse>

    @GET("/shows/top_rated")
    fun getTopRated(  @Header("access-token") accessToken: String,
                      @Header("client") client: String,
                      @Header("token-type") tokenType: String,
                      @Header("expiry") expiry: String,
                      @Header("uid") uid: String):Call<TopRatedResponse>
}