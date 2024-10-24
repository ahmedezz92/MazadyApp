package com.example.mazadyapp.presentation.second

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mazadyapp.R
import com.example.mazadyapp.data.remote.model.Course
import com.example.mazadyapp.data.remote.model.Instructor
import com.example.mazadyapp.data.remote.model.Story
import com.example.mazadyapp.databinding.ActivityScreentwoBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SecondActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScreentwoBinding

    private val storiesAdapter = StoriesAdapter()
    private val coursesAdapter = CoursesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScreentwoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerViews()
        setupCategoryChips()
        setupBottomNavigation()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupRecyclerViews() {
        binding.storiesRecyclerView.apply {
            adapter = storiesAdapter
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        }

        binding.coursesRecyclerView.apply {
            adapter = coursesAdapter
//            layoutManager = LinearLayoutManager(context)
//            addItemDecoration(
//                DividerItemDecoration(
//                    context,
//                    DividerItemDecoration.HORIZONTAL
//                )
//            )
        }
    }

    private fun setupCategoryChips() {
        binding.categoryChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            checkedIds.firstOrNull()?.let { chipId ->
                when (chipId) {
//                    R.id.chipAll -> viewModel.setCategory("All")
//                    R.id.chipUiUx -> viewModel.setCategory("UI/UX")

                }
            }
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> true
                R.id.profile -> {
                    false
                }

                R.id.messages -> {
                    false
                }

                R.id.explore -> {
                    false
                }

                else -> false
            }
        }
    }

    private fun setupClickListeners() {
    }

    private fun observeViewModel() {
        val courseList = listOf(
            Course("English", "12", true, Instructor("Ahmed", "Team Leader"), "12"),
            Course("Arabic", "12", true, Instructor("Mohamed", "Team Leader"), "12"),
            Course(
                "French", "12", true, Instructor("Mahmoud", "Team Leader"), "12",
            )
        )
        coursesAdapter.submitList(courseList)

        val storiesList = listOf(
            Story(resources.getResourceName(R.drawable.img_story), true),
            Story(resources.getResourceName(R.drawable.img_story), true),
            Story(resources.getResourceName(R.drawable.img_story), true)
        )
        storiesAdapter.submitList(storiesList)
        binding.userPoints.text = "+$1600 Points"


    }

    companion object {
        const val EXTRA_COURSE_ID = "extra_course_id"
    }
}