package com.example.mazadyapp.data.remote.api

import com.example.mazadyapp.core.data.utils.WrappedResponse
import com.example.mazadyapp.data.remote.model.AllCategoriesResponse
import com.example.mazadyapp.data.remote.model.PropertiesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MazadyApiService {
    @GET("get_all_cats")
    suspend fun getAllCategories(
    ): Response<WrappedResponse<AllCategoriesResponse>>

    @GET("properties")
    suspend fun getProps(
        @Query("cat") id: String
    ): Response<WrappedResponse<List<PropertiesResponse>>>

    @GET("get-options-child/{optionId}")
    suspend fun getOptions(
        @Path("optionId") optionId: Int
    ): Response<WrappedResponse<List<PropertiesResponse>>>
}


