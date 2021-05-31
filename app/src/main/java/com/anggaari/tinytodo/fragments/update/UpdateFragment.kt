package com.anggaari.tinytodo.fragments.update

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.anggaari.tinytodo.R
import com.anggaari.tinytodo.data.models.Priority
import com.anggaari.tinytodo.databinding.FragmentUpdateBinding
import com.anggaari.tinytodo.fragments.SharedViewModel

class UpdateFragment : Fragment() {
    private lateinit var binding: FragmentUpdateBinding
    private val args by navArgs<UpdateFragmentArgs>()
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentUpdateBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)

        binding.currentEditTextTitle.setText(args.currentItem.title)
        binding.currentEditTextDescription.setText(args.currentItem.description)
        binding.currentSpinnerPriorities.setSelection(parsePriority(args.currentItem.priority))
        binding.currentSpinnerPriorities.onItemSelectedListener = sharedViewModel.prioritySelectedListener

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_fragment_menu, menu)
    }

    private fun parsePriority(priority: Priority): Int {
        return when (priority) {
            Priority.HIGH -> 0
            Priority.MEDIUM -> 1
            Priority.LOW -> 2
        }
    }

}