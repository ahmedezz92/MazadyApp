package com.example.mazadyapp

import app.cash.turbine.test
import com.example.mazadyapp.base.BaseResult
import com.example.mazadyapp.core.data.utils.WrappedResponse
import com.example.mazadyapp.data.remote.model.PropertiesResponse
import com.example.mazadyapp.domain.usecase.GetCategoriesUseCase
import com.example.mazadyapp.domain.usecase.GetOptionsUseCase
import com.example.mazadyapp.domain.usecase.GetPropsUseCase
import com.example.mazadyapp.presentation.first.GetPropsState
import com.example.mazadyapp.presentation.first.MainViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {
    private lateinit var viewModel: MainViewModel
    private lateinit var getCategoriesUseCase: GetCategoriesUseCase
    private lateinit var getPropsUseCase: GetPropsUseCase
    private lateinit var getOptionsUseCase: GetOptionsUseCase
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getCategoriesUseCase = mockk()
        getPropsUseCase = mockk()
        getOptionsUseCase = mockk()
        viewModel = MainViewModel(getCategoriesUseCase, getPropsUseCase, getOptionsUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getSubCategory should emit Success state when useCase returns data`() = runTest {
        // Arrange
        val testId = 1
        val mockProperties = listOf( PropertiesResponse(
            1, "model", "", "model", 12, true, "car", "BMW", listOf(

            )
        ))
        val wrappedResponse = WrappedResponse(data = mockProperties, msg = "Success", code = 200)

        coEvery { getPropsUseCase.execute(testId) } returns flowOf(BaseResult.DataState(wrappedResponse))

        // Act & Assert
        viewModel.getPropsState.test {
            assertEquals(GetPropsState.IsLoading, awaitItem())

            viewModel.getSubCategory(testId)

            assertEquals(GetPropsState.Success(mockProperties), awaitItem())
        }
    }

    @Test
    fun `getSubCategory should maintain loading state when useCase throws exception`() = runTest {
        // Arrange
        val testId = 1
        coEvery { getPropsUseCase.execute(testId) } returns flowOf(
            BaseResult.ErrorState("Network Error")
        )

        // Act & Assert
        viewModel.getPropsState.test {
            assertEquals(GetPropsState.IsLoading, awaitItem())

            viewModel.getSubCategory(testId)

            // Should stay in loading state since we're not handling errors in the ViewModel
            assertEquals(GetPropsState.IsLoading, expectMostRecentItem())
        }
    }

    @Test
    fun `getSubCategory should emit correct sequence of states`() = runTest {
        // Arrange
        val testId = 1
        val mockProperties = listOf( PropertiesResponse(
            1, "model", "", "model", 12, true, "car", "BMW", listOf(

            )
        ))
        val wrappedResponse = WrappedResponse(data = mockProperties, msg = "Success", code = 200)

        coEvery { getPropsUseCase.execute(testId) } returns flowOf(BaseResult.DataState(wrappedResponse))

        // Act & Assert
        viewModel.getPropsState.test {
            // Initial state
            assertEquals(GetPropsState.IsLoading, awaitItem())

            // Trigger the action
            viewModel.getSubCategory(testId)

            // Verify state transition
            assertEquals(GetPropsState.Success(mockProperties), awaitItem())

            // Ensure no more emissions
            expectNoEvents()
        }
    }
}