package com.example.mazadyapp.presentation.first

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.mazadyapp.R
import com.example.mazadyapp.data.remote.model.Categories
import com.example.mazadyapp.data.remote.model.Children
import com.example.mazadyapp.data.remote.model.PropertiesResponse
import com.example.mazadyapp.databinding.ActivityMainBinding
import com.example.mazadyapp.presentation.second.SecondActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()
    private var categories: List<Categories> = emptyList()
    private var subCategoriesList: List<PropertiesResponse> = emptyList()

    private val propertiesAdapter: PropertiesAdapter by lazy { PropertiesAdapter(viewModel) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        observeStateFlow()
        getAllCategories()
        setupListeners()
    }

    private fun initViews() {
        binding.rvSubcategories.adapter = propertiesAdapter
    }

    private fun getAllCategories() {
        viewModel.getCategories()
    }

    private fun observeStateFlow() {
        viewModel.getMainState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { state -> handleGetAllCategoriesState(state) }.launchIn(lifecycleScope)
        viewModel.getPropsState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { state -> handleGetSubCategories(state) }.launchIn((lifecycleScope))
    }

    private fun handleGetAllCategoriesState(state: GetMainActivityState) {
        when (state) {
            is GetMainActivityState.IsLoading -> {}
            is GetMainActivityState.Success -> {
                state.data?.let {
                    categories = it.categories
                    setupMainCategorySpinner()
                }
            }

            is GetMainActivityState.Error -> {}
        }
    }

    private fun setupMainCategorySpinner(
    ) {
        setupSpinner(binding.mainCategoryDropdown, categories.map { it.slug })
    }

    private fun setupSpinner(
        autoCompleteTextView: AutoCompleteTextView, items: List<String>
    ) {
        val adapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_dropdown_item, items
        )
        autoCompleteTextView.setAdapter(adapter)
        subCategoriesList.toMutableList().clear()
    }

    private fun setupListeners() {
        binding.mainCategoryDropdown.setOnItemClickListener { _, _, position, _ ->
            val selectedCategory = categories[position]
            selectedCategory.children?.let { setupSubCategorySpinner(it) }
            // Clear dependent spinners
            binding.subCategoryDropdown.text.clear()
            propertiesAdapter.submitList(emptyList()) // This will clear the RecyclerView
            subCategoriesList = emptyList()
        }

        binding.subCategoryDropdown.setOnItemClickListener { _, _, position, _ ->
            val mainCategoryPosition = categories.indexOfFirst {
                it.slug == binding.mainCategoryDropdown.text.toString()
            }
            if (mainCategoryPosition != -1) {
                val selectedSubCategory = categories[mainCategoryPosition].children?.get(position)
                selectedSubCategory?.let { viewModel.getSubCategory(it.id) }

            }
        }

        binding.submitButton.setOnClickListener {
            submitForm()
        }
        binding.btnScreenTwo.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }
    }

    private fun submitForm() {
        val selectedData = mutableListOf<Pair<String, String>>()

        // Add main category
        selectedData.add(Pair("Main Category", binding.mainCategoryDropdown.text.toString()))

        // Add sub category
        selectedData.add(Pair("Sub Category", binding.subCategoryDropdown.text.toString()))

        // Add properties with their selected values
        propertiesAdapter.getSelectedData().forEach { (property, value) ->
            selectedData.add(Pair(property, value))
        }

        showSelectedDataDialog(selectedData)
    }

    private fun showSelectedDataDialog(selectedData: List<Pair<String, String>>) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_selected_data)

        val tableLayout = dialog.findViewById<TableLayout>(R.id.tableLayout)

        // Add data rows
        selectedData.forEach { (field, value) ->
            val tableRow = TableRow(this).apply {
                layoutParams = TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
                )
                setPadding(8, 8, 8, 8)
            }

            // Add field name
            tableRow.addView(TextView(this).apply {
                text = field
                setTextColor(Color.BLACK)
            })

            // Add value
            tableRow.addView(TextView(this).apply {
                text = value
                setTextColor(Color.BLACK)
            })

            tableLayout.addView(tableRow)

            // Add separator
            tableLayout.addView(View(this).apply {
                layoutParams = TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    1
                )
                setBackgroundColor(Color.LTGRAY)
            })
        }

        dialog.show()
    }

    private fun setupSubCategorySpinner(subCategories: List<Children>) {
        val subCategoryNames = subCategories.map { it.slug }
        setupSpinner(binding.subCategoryDropdown, subCategoryNames)
    }

    private fun handleGetSubCategories(state: GetPropsState) {
        when (state) {
            is GetPropsState.IsLoading -> {}
            is GetPropsState.Error -> {}
            is GetPropsState.Success -> {
                state.data?.let {
                    subCategoriesList = it
                    handleSubCategoriesResponse(state.data)
                }
            }
        }
    }

    private fun handleSubCategoriesResponse(subCategories: List<PropertiesResponse>) {
        propertiesAdapter.submitList(subCategories)
    }
}
