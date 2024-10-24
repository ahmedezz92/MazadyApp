package com.example.mazadyapp.data.remote.model

data class AllCategoriesResponse(
    val categories: List<Categories>,
)

data class Categories(
    val id: Int,
    val name: String,
    val description: String,
    val slug:String,
    val image: String,
    val children: List<Children>?
)

data class Children(
    val id: Int,
    val name: String,
    val slug:String,
    val children: Children?
)