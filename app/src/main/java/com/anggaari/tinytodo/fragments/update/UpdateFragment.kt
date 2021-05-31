package com.anggaari.tinytodo.fragments.update

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.anggaari.tinytodo.R
import com.anggaari.tinytodo.data.models.TodoData
import com.anggaari.tinytodo.data.viewmodel.TodoViewModel
import com.anggaari.tinytodo.databinding.FragmentUpdateBinding
import com.anggaari.tinytodo.fragments.SharedViewModel
import com.anggaari.tinytodo.utils.Helpers.hideKeyboard

class UpdateFragment : Fragment() {
    private lateinit var binding: FragmentUpdateBinding
    private val args by navArgs<UpdateFragmentArgs>()
    private val sharedViewModel: SharedViewModel by viewModels()
    private val todoViewModel: TodoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpdateBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)

        binding.currentEditTextTitle.setText(args.currentItem.title)
        binding.currentEditTextDescription.setText(args.currentItem.description)
        binding.currentSpinnerPriorities.setSelection(sharedViewModel.parsePriorityToInt(args.currentItem.priority))
        binding.currentSpinnerPriorities.onItemSelectedListener = sharedViewModel.prioritySelectedListener

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_save -> updateItem()
            R.id.menu_delete -> confirmItemRemoval()
        }
        hideKeyboard()
        return super.onOptionsItemSelected(item)
    }

    private fun confirmItemRemoval() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
            todoViewModel.deleteData(args.currentItem)
            Toast.makeText(
                requireContext(),
                getString(R.string.delete_success, args.currentItem.title),
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setNegativeButton(getString(R.string.No)) { _, _ -> }
        builder
            .setTitle(getString(R.string.delete_title, args.currentItem.title))
            .setMessage(getString(R.string.delete_message, args.currentItem.title))
            .create()
            .show()
    }

    private fun updateItem() {
        val title = binding.currentEditTextTitle.text.toString()
        val description = binding.currentEditTextDescription.text.toString()
        val priority = binding.currentSpinnerPriorities.selectedItem.toString()

        val validation = sharedViewModel.verifyDataFromUser(title, description)
        if (validation) {
            val updatedItem = TodoData(
                args.currentItem.id,
                title,
                sharedViewModel.parsePriority(priority),
                description
            )
            todoViewModel.updateData(updatedItem)
            Toast.makeText(
                requireContext(),
                getString(R.string.successfully_updated),
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.please_fill_out),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}