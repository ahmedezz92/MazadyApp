package com.example.mazadyapp.data.repository

import com.example.mazadyapp.base.BaseResult
import com.example.mazadyapp.core.data.utils.WrappedResponse
import com.example.mazadyapp.data.remote.api.MazadyApiService
import com.example.mazadyapp.data.remote.model.AllCategoriesResponse
import com.example.mazadyapp.data.remote.model.PropertiesResponse
import com.example.mazadyapp.domain.repository.MazadyRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MazadyRepositoryImp @Inject constructor(
    private val mazadyApiService: MazadyApiService,
) :
    MazadyRepository {

    override suspend fun getAllCategories(): Flow<BaseResult<WrappedResponse<AllCategoriesResponse>>> {
        return flow {
            val response = mazadyApiService.getAllCategories(
            )
            if (response.isSuccessful) {
                val body = response.body()
                emit(BaseResult.DataState(body))
            } else {
                val type = object : TypeToken<WrappedResponse<AllCategoriesResponse>>() {}.type
                val err: WrappedResponse<AllCategoriesResponse> =
                    Gson().fromJson(response.errorBody()!!.charStream(), type)
                err.code = response.code()
                emit(BaseResult.ErrorState(err.msg))
            }
        }
    }

    override suspend fun getProperties(id: Int): Flow<BaseResult<WrappedResponse<List<PropertiesResponse>>>> {
        return flow {
            val response = mazadyApiService.getProps(
                id = id.toString()
            )
            if (response.isSuccessful) {
                val body = response.body()
                emit(BaseResult.DataState(body))
            } else {
                val type = object : TypeToken<WrappedResponse<PropertiesResponse>>() {}.type
                val err: WrappedResponse<AllCategoriesResponse> =
                    Gson().fromJson(response.errorBody()!!.charStream(), type)
                err.code = response.code()
                emit(BaseResult.ErrorState(err.msg))
            }
        }
    }

    override suspend fun getOptions(id: Int): Flow<BaseResult<WrappedResponse<List<PropertiesResponse>>>> {
        return flow {
            val response = mazadyApiService.getOptions(
                optionId = id
            )
            if (response.isSuccessful) {
                val body = response.body()
                emit(BaseResult.DataState(body))
            } else {
                val type = object : TypeToken<WrappedResponse<PropertiesResponse>>() {}.type
                val err: WrappedResponse<AllCategoriesResponse> =
                    Gson().fromJson(response.errorBody()!!.charStream(), type)
                err.code = response.code()
                emit(BaseResult.ErrorState(err.msg))
            }
        }
    }

}
