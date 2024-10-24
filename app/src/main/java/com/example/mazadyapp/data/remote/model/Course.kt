package com.example.mazadyapp.data.remote.model

data class Course(
    val title: String,
    val duration: String,
    val isFree: Boolean,
    val instructor: Instructor,
    val lessonsCount: String
)

data class Instructor(val name: String, val role: String)