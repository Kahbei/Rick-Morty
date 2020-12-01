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

    @GET("character/")
    suspend fun characters(@Query("page") page: Int): NetworkResponse<Response<CharacterModel>, ErrorResponse>

    @GET("character/{id}")
    suspend fun character(@Path("id") id: Int): NetworkResponse<CharacterModel, ErrorResponse>

    // TODO Implementer la méthode pour récupérer les informations d'un épisode
    @GET("episode/{id}")
    suspend fun episode(@Path("id") id: Int): NetworkResponse<EpisodeModel, ErrorResponse>

    @GET("episode/")
    suspend fun episodes(@Query("page") page: Int): NetworkResponse<Response<EpisodeModel>, ErrorResponse>
}
