package com.anggaari.tinytodo.fragments.add

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.anggaari.tinytodo.R
import com.anggaari.tinytodo.data.models.TodoData
import com.anggaari.tinytodo.data.viewmodel.TodoViewModel
import com.anggaari.tinytodo.databinding.FragmentAddBinding
import com.anggaari.tinytodo.fragments.SharedViewModel

class AddFragment : Fragment() {
    private lateinit var binding: FragmentAddBinding
    private val todoViewModel: TodoViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        //val rootView = inflater.inflate(R.layout.fragment_add, container, false)

        binding = FragmentAddBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)

        binding.spinnerPriorities.onItemSelectedListener = sharedViewModel.listener

        return binding.root;
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_add) {
            insertDataToDB()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun insertDataToDB() {
        val title = binding.editTextTitle.text.toString()
        val priority = binding.spinnerPriorities.selectedItem.toString()
        val description = binding.editTextDescription.text.toString()

        val isValid = sharedViewModel.verifyDataFromUser(title, description)
        if (isValid) {
            val newTodo = TodoData(0, title, sharedViewModel.parsePriority(priority), description)
            todoViewModel.insertData(newTodo)

            Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        }
    }
}