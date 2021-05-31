package com.anggaari.tinytodo.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.*
import com.anggaari.tinytodo.R
import com.anggaari.tinytodo.data.models.TodoData
import com.anggaari.tinytodo.data.viewmodel.TodoViewModel
import com.anggaari.tinytodo.databinding.FragmentListBinding
import com.anggaari.tinytodo.fragments.SharedViewModel
import com.anggaari.tinytodo.fragments.list.adapter.ListAdapter
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator

class ListFragment : Fragment(), SearchView.OnQueryTextListener {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private val todoViewModel: TodoViewModel by viewModels()
    private val adapter: ListAdapter by lazy { ListAdapter() }
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        // val rootView = inflater.inflate(R.layout.fragment_list, container, false)

        _binding = FragmentListBinding.inflate(inflater, container, false)

        /* Move to data binding see: BindingAdapters.kt
        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }*/

        binding.lifecycleOwner = this
        binding.sharedViewModel = sharedViewModel

        setupRecyclerView()

        todoViewModel.getAllData.observe(viewLifecycleOwner, Observer { data ->
            sharedViewModel.checkIfDatabaseEmpty(data)
            adapter.setData(data)
        })

        /* Move to data binding see: BindingAdapters.kt
        sharedViewModel.isEmptyDatabase.observe(viewLifecycleOwner, Observer { isEmptyDatabase ->
            if (isEmptyDatabase) {
                binding.imageViewNoData.visibility = View.VISIBLE
                binding.textViewNoData.visibility = View.VISIBLE
            } else {
                binding.imageViewNoData.visibility = View.INVISIBLE
                binding.textViewNoData.visibility = View.INVISIBLE
            }
        })*/

        // Set top right menu
        setHasOptionsMenu(true)

        return binding.root;
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        //recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.itemAnimator = SlideInUpAnimator().apply {
            addDuration = 300
        }
        swipeToDelete(recyclerView)
    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallback = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val itemToDelete = adapter.dataList[viewHolder.adapterPosition]

                // Delete item
                todoViewModel.deleteData(itemToDelete)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)

                // Restore deleted item
                restoreDeletedData(viewHolder.itemView, itemToDelete, viewHolder.adapterPosition)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoreDeletedData(view: View, deletedItem: TodoData, position: Int) {
        val snackBar = Snackbar.make(
            view, getString(R.string.delete_success, deletedItem.title),
            Snackbar.LENGTH_LONG
        )
        snackBar.setAction(getString(R.string.undo)) {
            todoViewModel.insertData(deletedItem)
        }
        snackBar.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)

        val search: MenuItem = menu.findItem(R.id.menu_search)
        val searchView: SearchView? = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete_all -> confirmRemoval()
            R.id.menu_priority_high -> todoViewModel.sortByHighPriority.observe(this, Observer { adapter.setData(it) })
            R.id.menu_priority_low -> todoViewModel.sortByLowPriority.observe(this, Observer { adapter.setData(it) })
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchThroughDatabase(query)
        }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null) {
            searchThroughDatabase(query)
        }
        return true
    }

    private fun searchThroughDatabase(query: String) {
        val searchQuery = "%$query%"
        todoViewModel.search(searchQuery).observe(this, {
            adapter.setData(it)
        })
    }

    private fun confirmRemoval() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
            todoViewModel.deleteAll()
            Toast.makeText(
                requireContext(),
                getString(R.string.delete_all_success),
                Toast.LENGTH_SHORT
            ).show()
        }
        builder.setNegativeButton(getString(R.string.No)) { _, _ -> }
        builder
            .setTitle(getString(R.string.delete_all_title))
            .setMessage(getString(R.string.delete_all_message))
            .create()
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}