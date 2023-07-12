package com.proyecto.integrador.descub.data.service

import com.proyecto.integrador.descub.data.model.Mural
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MuralApiService {
    @GET("mural")
    fun getMural(@Query("id") id: Int): Call<Mural>
}
