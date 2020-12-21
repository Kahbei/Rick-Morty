package com.rickandmorty.network

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import com.rickandmorty.model.CharacterModel
import com.rickandmorty.model.EpisodeModel
import com.rickandmorty.model.ErrorResponse
import com.rickandmorty.model.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyService {

    // Set-up the api call
    companion object {
        fun build(): RickAndMortyService {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://rickandmortyapi.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(NetworkResponseAdapterFactory())
                .build()

            return retrofit.create(RickAndMortyService::class.java)
        }
    }

    /**
     * These functions will get a page contained a list of character or episode. If an id is specified, it will display the character following the id
     */

    @GET("character/")
    suspend fun characters(@Query("page") page: Int): NetworkResponse<Response<CharacterModel>, ErrorResponse>

    @GET("character/{id}")
    suspend fun character(@Path("id") id: Int): NetworkResponse<CharacterModel, ErrorResponse>

    @GET("episode/{id}")
    suspend fun episode(@Path("id") id: Int): NetworkResponse<EpisodeModel, ErrorResponse>

    @GET("episode/")
    suspend fun episodes(@Query("page") page: Int): NetworkResponse<Response<EpisodeModel>, ErrorResponse>
}
