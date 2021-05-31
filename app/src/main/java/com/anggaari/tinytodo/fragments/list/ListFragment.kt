package com.anggaari.tinytodo.fragments.list

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.anggaari.tinytodo.R
import com.anggaari.tinytodo.data.viewmodel.TodoViewModel
import com.anggaari.tinytodo.databinding.FragmentListBinding

class ListFragment : Fragment() {
    private lateinit var binding: FragmentListBinding
    private val todoViewModel: TodoViewModel by viewModels()
    private val adapter: ListAdapter by lazy { ListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        // val rootView = inflater.inflate(R.layout.fragment_list, container, false)

        binding = FragmentListBinding.inflate(inflater, container, false)

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())

        todoViewModel.getAllData.observe(viewLifecycleOwner, Observer { data ->
            adapter.setData(data)
        })

        // Set top right menu
        setHasOptionsMenu(true)

        return binding.root;
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)
    }
}