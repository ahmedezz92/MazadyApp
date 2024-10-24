package com.example.mazadyapp.data.remote.model

data class PropertiesResponse(
    val id: Int,
    val name: String,
    val description: String?,
    val slug: String,
    val parent: Int?,
    val list: Boolean,
    val type: String?,
    val value: String,
    val options: List<Option>
)

data class Option(
    val id: Int,
    val name: String,
    val slug: String,
    val parent: Int,
    val child: Boolean
)
