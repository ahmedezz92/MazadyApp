package com.example.mazadyapp

import com.example.mazadyapp.base.BaseResult
import com.example.mazadyapp.core.data.utils.WrappedResponse
import com.example.mazadyapp.data.remote.model.Option
import com.example.mazadyapp.data.remote.model.PropertiesResponse
import com.example.mazadyapp.domain.repository.MazadyRepository
import com.example.mazadyapp.domain.usecase.GetPropsUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetPropsUseCaseTest {
    private lateinit var useCase: GetPropsUseCase
    private lateinit var repository: MazadyRepository

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetPropsUseCase(repository)
    }

    @Test
    fun `execute returns success result when repository returns data`() = runBlocking {
        // Arrange
        val testId = 1
        val mockProperties = listOf(
            PropertiesResponse(
                1, "model", "", "model", 12, true, "car", "BMW", listOf(

                )
            )
        )
        val wrappedResponse = WrappedResponse(data = mockProperties, msg = "Success", code = 200)
        coEvery { repository.getProperties(testId) } returns flowOf(
            BaseResult.DataState(
                wrappedResponse
            )
        )

        // Act
        val result = useCase.execute(testId).first()

        // Assert
        assertTrue(result is BaseResult.DataState)
        assertEquals(mockProperties, (result as BaseResult.DataState).items?.data)

        // Verify repository was called with correct parameter
        coVerify(exactly = 1) { repository.getProperties(testId) }
    }

    @Test
    fun `execute returns error result when repository returns error`() = runBlocking {
        // Arrange
        val testId = 1
        val errorMessage = "Network error"
        coEvery { repository.getProperties(testId) } returns flowOf(
            BaseResult.ErrorState(
                errorMessage
            )
        )

        // Act
        val result = useCase.execute(testId).first()

        // Assert
        assertTrue(result is BaseResult.ErrorState)
        assertEquals(errorMessage, (result as BaseResult.ErrorState).errorMessage)
    }

    @Test
    fun `execute propagates repository response without transformation`() = runBlocking {
        // Arrange
        val testId = 1
        val mockProperties = listOf(
            PropertiesResponse(
                1, "model", "", "model", 12, true, "car", "BMW", listOf(

                )
            )
        )
        val wrappedResponse = WrappedResponse(data = mockProperties, msg = "Success", code = 200)
        val repositoryResult = BaseResult.DataState(wrappedResponse)
        coEvery { repository.getProperties(testId) } returns flowOf(repositoryResult)

        // Act
        val result = useCase.execute(testId).first()

        // Assert
        assertEquals(repositoryResult, result)
    }
}