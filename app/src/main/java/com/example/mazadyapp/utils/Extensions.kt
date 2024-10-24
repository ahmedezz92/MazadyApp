package com.example.mazadyapp.utils

import android.view.LayoutInflater
import android.view.ViewGroup


val ViewGroup.layoutInflater: LayoutInflater get() = LayoutInflater.from(this.context)
