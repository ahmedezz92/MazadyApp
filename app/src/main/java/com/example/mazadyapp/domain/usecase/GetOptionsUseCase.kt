package com.example.mazadyapp.domain.usecase

import com.example.mazadyapp.base.BaseResult
import com.example.mazadyapp.core.data.utils.WrappedResponse
import com.example.mazadyapp.data.remote.model.PropertiesResponse
import com.example.mazadyapp.domain.repository.MazadyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetOptionsUseCase @Inject constructor(private val mazadyRepository: MazadyRepository) {
    suspend fun execute(id: Int): Flow<BaseResult<WrappedResponse<List<PropertiesResponse>>>> {
        return mazadyRepository.getOptions(id = id)
    }
}