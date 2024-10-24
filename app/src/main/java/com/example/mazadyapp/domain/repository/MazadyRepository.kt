package com.example.mazadyapp.domain.repository

import com.example.mazadyapp.base.BaseResult
import com.example.mazadyapp.core.data.utils.WrappedResponse
import com.example.mazadyapp.data.remote.model.AllCategoriesResponse
import com.example.mazadyapp.data.remote.model.PropertiesResponse
import kotlinx.coroutines.flow.Flow

interface MazadyRepository {
    suspend fun getAllCategories(): Flow<BaseResult<WrappedResponse<AllCategoriesResponse>>>
    suspend fun getProperties(id: Int): Flow<BaseResult<WrappedResponse<List<PropertiesResponse>>>>
    suspend fun getOptions(id: Int): Flow<BaseResult<WrappedResponse<List<PropertiesResponse>>>>

}