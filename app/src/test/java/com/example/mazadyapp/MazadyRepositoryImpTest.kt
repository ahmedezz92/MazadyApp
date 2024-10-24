package com.example.mazadyapp

import com.example.mazadyapp.base.BaseResult
import com.example.mazadyapp.core.data.utils.WrappedResponse
import com.example.mazadyapp.data.remote.api.MazadyApiService
import com.example.mazadyapp.data.remote.model.AllCategoriesResponse
import com.example.mazadyapp.data.remote.model.PropertiesResponse
import com.example.mazadyapp.data.repository.MazadyRepositoryImp
import com.google.gson.Gson
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class MazadyRepositoryImpTest {
    private lateinit var repository: MazadyRepositoryImp
    private lateinit var apiService: MazadyApiService
    private val gson = Gson()

    @Before
    fun setup() {
        apiService = mockk()
        repository = MazadyRepositoryImp(apiService)
    }

    @Test
    fun `getAllCategories returns success result when API call is successful`() = runBlocking {
        // Arrange
        val mockResponse = AllCategoriesResponse(emptyList())
        val wrappedResponse = WrappedResponse(data = mockResponse, msg = "Success", code = 200)
        coEvery { apiService.getAllCategories() } returns Response.success(wrappedResponse)

        // Act
        val result = repository.getAllCategories().first()

        // Assert
        assertTrue(result is BaseResult.DataState)
        assertEquals(mockResponse, (result as BaseResult.DataState).items?.data)
    }

    @Test
    fun `getProperties returns success result when API call is successful`() = runBlocking {
        // Arrange
        val testId = 1
        val mockProperties = listOf(
            PropertiesResponse(
                1, "model", "", "model", 12, true, "car", "BMW", listOf(

                )
            )
        )
        val wrappedResponse = WrappedResponse(data = mockProperties, msg = "Success", code = 200)
        coEvery { apiService.getProps(testId.toString()) } returns Response.success(wrappedResponse)

        // Act
        val result = repository.getProperties(testId).first()

        // Assert
        assertTrue(result is BaseResult.DataState)
        assertEquals(mockProperties, (result as BaseResult.DataState).items?.data)
    }

    @Test
    fun `getProperties returns error result when API call fails`() = runBlocking {
        // Arrange
        val testId = 1
        val errorResponse = WrappedResponse<AllCategoriesResponse>(
            data = null,
            msg = "Error occurred",
            code = 404
        )
        val errorResponseJson = gson.toJson(errorResponse)
        val errorResponseBody =
            errorResponseJson.toResponseBody("application/json".toMediaTypeOrNull())

        coEvery { apiService.getProps(testId.toString()) } returns Response.error(
            404,
            errorResponseBody
        )

        // Act
        val result = repository.getProperties(testId).first()

        // Assert
        assertTrue(result is BaseResult.ErrorState)
        assertEquals("Error occurred", (result as BaseResult.ErrorState).errorMessage)
    }
}