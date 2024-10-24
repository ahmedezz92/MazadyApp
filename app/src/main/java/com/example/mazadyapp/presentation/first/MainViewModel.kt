package com.example.mazadyapp.presentation.first

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mazadyapp.base.BaseResult
import com.example.mazadyapp.data.remote.model.AllCategoriesResponse
import com.example.mazadyapp.data.remote.model.PropertiesResponse
import com.example.mazadyapp.domain.usecase.GetCategoriesUseCase
import com.example.mazadyapp.domain.usecase.GetOptionsUseCase
import com.example.mazadyapp.domain.usecase.GetPropsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val allCategoriesUseCase: GetCategoriesUseCase,
    private val getPropsUseCase: GetPropsUseCase,
    private val getOptionsUseCase: GetOptionsUseCase
) : ViewModel() {

    private val _getMainState =
        MutableStateFlow<GetMainActivityState>(GetMainActivityState.IsLoading)
    val getMainState: StateFlow<GetMainActivityState> get() = _getMainState

    private val _getPropsState = MutableStateFlow<GetPropsState>(GetPropsState.IsLoading)
    val getPropsState: StateFlow<GetPropsState> get() = _getPropsState

    private val _optionChildState = MutableStateFlow<OptionChildState>(OptionChildState.IsLoading)
    val optionChildState: StateFlow<OptionChildState> get() = _optionChildState

    fun getCategories() {
        viewModelScope.launch {
            allCategoriesUseCase.execute().catch { exception ->
                Log.e("exp", exception.message.toString())
            }.collect {
                when (it) {
                    is BaseResult.ErrorState -> {}
                    is BaseResult.DataState -> {
                        _getMainState.value = it.items?.let { it1 ->
                            GetMainActivityState.Success(
                                it1.data
                            )
                        }!!
                    }
                }
            }
        }
    }

    fun getSubCategory(id: Int) {
        viewModelScope.launch {
            getPropsUseCase.execute(id).catch { exception ->
                Log.e("exp", exception.message.toString())
            }.collect {
                when (it) {
                    is BaseResult.ErrorState -> {}
                    is BaseResult.DataState -> {
                        _getPropsState.value = it.items?.let { it1 ->
                            GetPropsState.Success(
                                it1.data
                            )
                        }!!
                    }
                }
            }
        }
    }

    fun getOptionChild(optionId: Int) {
        viewModelScope.launch {
            getOptionsUseCase.execute(optionId).catch { exception ->
                Log.e("exp", exception.message.toString())
            }.collect {
                when (it) {
                    is BaseResult.ErrorState -> {
                        _optionChildState.value = OptionChildState.Error(it.errorMessage)
                    }

                    is BaseResult.DataState -> {
                        _optionChildState.value = it.items?.let { response ->
                            OptionChildState.Success(response.data)
                        }!!
                    }
                }
            }
        }
    }

}

sealed class GetMainActivityState {
    object IsLoading : GetMainActivityState()

    data class Success(val data: AllCategoriesResponse?) : GetMainActivityState()

    data class Error(val errorCode: Int) : GetMainActivityState()
}

sealed class GetPropsState {
    object IsLoading : GetPropsState()

    data class Success(val data: List<PropertiesResponse>?) : GetPropsState()

    data class Error(val errorCode: Int) : GetPropsState()
}

sealed class OptionChildState {
    object IsLoading : OptionChildState()

    data class Success(val data: List<PropertiesResponse>?) : OptionChildState()

    data class Error(val errorCode: String?) : OptionChildState()
}