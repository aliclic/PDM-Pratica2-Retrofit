package com.example.navegacao1.model.dados

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UsuarioServiceIF {

    @GET("usuarios")
    suspend fun listar(): List<Usuario>

    @POST("usuarios")
    suspend fun adicionar(@Body usario: Usuario): Usuario

    @DELETE("usuarios/{id}")
    suspend fun remover(@Path("id") id: String)

    @GET("usuarios/{id}")
    suspend fun buscar(@Path("id") id: String): Usuario

    @GET("58013240/json/")
    suspend fun getEndereco(): Endereco
}