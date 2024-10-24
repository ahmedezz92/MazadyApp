package com.example.mazadyapp.presentation.second

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import coil.transform.CircleCropTransformation
import com.example.mazadyapp.R
import com.example.mazadyapp.data.remote.model.Course
import com.example.mazadyapp.databinding.ItemCourseBinding

class CoursesAdapter : RecyclerView.Adapter<CoursesAdapter.CourseViewHolder>() {
    private var courses = listOf<Course>()
    private var onCourseClickListener: ((Course) -> Unit)? = null

    class CourseViewHolder(private val binding: ItemCourseBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(course: Course, onClick: ((Course) -> Unit)?) {
            binding.apply {
                courseTitle.text = course.title
                lessonCount.text = "${course.lessonsCount} lessons"
                duration.text = course.duration
                freeTag.isVisible = course.isFree
                courseThumbnail.setImageResource(R.drawable.img_course)

                instructorName.text = course.instructor.name
                instructorRole.text = course.instructor.role

                root.setOnClickListener {
                    onClick?.invoke(course)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        return CourseViewHolder(
            ItemCourseBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        holder.bind(courses[position], onCourseClickListener)
    }

    override fun getItemCount() = courses.size

    fun submitList(newCourses: List<Course>) {
        courses = newCourses
        notifyDataSetChanged()
    }

    fun setOnCourseClickListener(listener: (Course) -> Unit) {
        onCourseClickListener = listener
    }
}