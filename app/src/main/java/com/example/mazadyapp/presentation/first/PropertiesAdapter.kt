package com.example.mazadyapp.presentation.first

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mazadyapp.data.remote.model.PropertiesResponse
import com.example.mazadyapp.databinding.ItemSubcategoryBinding
import com.example.mazadyapp.databinding.LayoutBottomsheetBinding
import com.example.mazadyapp.utils.layoutInflater
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class PropertiesAdapter(private val viewModel: MainViewModel) :
    ListAdapter<PropertiesResponse, RecyclerView.ViewHolder>(
        diffCallback
    ) {
    private var selectedPosition = -1
    private var selectedProcessType: String? = null
    private var isOtherSelected = false
    private var currentOptionChildren: List<PropertiesResponse> = emptyList()
    private val otherTextInputs = mutableMapOf<Int, String>()

    fun setOtherText(position: Int, text: String) {
        otherTextInputs[position] = text
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val binding =
            ItemSubcategoryBinding.inflate(parent.layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        getItem(position)?.let {
            (holder as ViewHolder).bind(it)
        }
    }

    private inner class ViewHolder(private val binding: ItemSubcategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PropertiesResponse) {
            binding.tvProperty.hint = item.slug
            binding.tvProperty.text =
                selectedProcessType.takeIf { layoutPosition == selectedPosition } ?: ""
            binding.tvProperty.setOnClickListener {
                showCategoriesPropsBottomSheet(item, layoutPosition)
            }

            if (layoutPosition == selectedPosition && isOtherSelected) {
                binding.etOther.visibility = View.VISIBLE
                binding.etOther.hint = "Please specify your ${item.slug}"
                setOtherText(layoutPosition, binding.etOther.text.toString())
            } else {
                binding.etOther.visibility = View.GONE
            }

        }

        private fun showCategoriesPropsBottomSheet(item: PropertiesResponse, position: Int) {
            val bottomSheetDialog = BottomSheetDialog(binding.root.context)
            val bottomSheetBinding =
                LayoutBottomsheetBinding.inflate(LayoutInflater.from(binding.root.context))
            bottomSheetDialog.setContentView(bottomSheetBinding.root)

            val optionAdapter = OptionsAdapter { optionName ->
                val selectedOption = item.options.find { it.slug == optionName }

                selectedPosition = position
                selectedProcessType = optionName
                isOtherSelected = optionName == "Other"
                selectedOption?.let {
                    if (it.child) {
                        viewModel.getOptionChild(it.id)
                    }
                }
                binding.tvProperty.text = optionName
                bottomSheetDialog.dismiss()
                notifyItemChanged(position)
            }

            bottomSheetBinding.apply {
                processTypeRecyclerView.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = optionAdapter
                }
                tvPropertyTitle.text = item.slug
                closeButton.setOnClickListener {
                    bottomSheetDialog.dismiss()
                }

                optionAdapter.submitList(listOf("Other") + item.options.map { it.slug })


                // Observe option child state
                viewModel.optionChildState.flowWithLifecycle(
                    (binding.root.context as AppCompatActivity).lifecycle,
                    Lifecycle.State.STARTED
                ).onEach { state ->
                    when (state) {
                        is OptionChildState.Success -> {
                            state.data?.let { children ->
                                currentOptionChildren = children
                                // Update the options list with child options
                                optionAdapter.submitList(listOf("Other") + children.map { it.slug })
                            }
                        }

                        else -> {}
                    }
                }.launchIn((binding.root.context as AppCompatActivity).lifecycleScope)
                // Set up search functionality
                searchEditText.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        val filteredList = item.options
                            .map { it.slug }
                            .filter { it.contains(s.toString(), true) }
                        optionAdapter.submitList(filteredList)
                    }

                    override fun afterTextChanged(s: Editable?) {}
                })
            }
            bottomSheetDialog.show()
        }
    }

    fun getSelectedData(): List<Pair<String, String>> {
        val selectedData = mutableListOf<Pair<String, String>>()

        currentList.forEachIndexed { index, property ->
            val value = if (index == selectedPosition && isOtherSelected) {
                // Get the "Other" text input value from our map
                otherTextInputs[index] ?: "Not specified"
            } else if (index == selectedPosition) {
                selectedProcessType ?: "Not selected"
            } else {
                "Not selected"
            }

            selectedData.add(Pair(property.slug, value))
        }

        return selectedData
    }


    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<PropertiesResponse>() {
            override fun areItemsTheSame(
                oldItem: PropertiesResponse,
                newItem: PropertiesResponse
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: PropertiesResponse,
                newItem: PropertiesResponse
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}