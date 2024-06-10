package com.example.mydiary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mydiary.data.Entries
import com.example.mydiary.databinding.FragmentDiaryListBinding
import com.google.android.material.snackbar.Snackbar

class DiaryListFragment : Fragment(), DiaryListListener {

    // connect the fragment_task_list.xml with TaskListFragment class
    private lateinit var binding: FragmentDiaryListBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDiaryListBinding.inflate(inflater, container, false)

        // Set the adapter and layout manager for the RecyclerView
        // "with" is a Kotlin extension function that allows you to call
        // the methods of an object without explicitly calling the object itself
        with(binding.list) {
            layoutManager = LinearLayoutManager(context)
            adapter = MyEntryRecyclerViewAdapter(
                Entries.list,
                this@DiaryListFragment
            ) // adapter is responsible for displaying the data
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Set the action for the FAB
        binding.addButton.setOnClickListener {
            // Navigate to the AddTaskFragment with action id
            findNavController().navigate(R.id.action_diaryListFragment_to_addEntryFragment)
        }
    }

    override fun onEntryClick(entryPosition: Int) {
        val actionEntryListFragmentToDisplayEntryFragment =
            DiaryListFragmentDirections.actionDiaryListFragmentToDisplayEntryFragment(
                Entries.list[entryPosition])
        findNavController().navigate(actionEntryListFragmentToDisplayEntryFragment)
    }

    override fun onEntryLongClick(entryPosition: Int) {
        showDeleteDialog(entryPosition)
    }

    private fun showDeleteDialog(entryPosition: Int) {
        val entryToDelete = Entries.list[entryPosition]
        val dialogBuilder =  android.app.AlertDialog.Builder(requireContext())
        dialogBuilder.setTitle("Delete Task?")
        dialogBuilder.setMessage(entryToDelete.title)
            .setPositiveButton("Confirm") { _, _ ->
                deleteDialogPositiveClick(entryPosition)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
                deleteDialogNegativeClick(entryPosition)
            }
        val alert = dialogBuilder.create()
        alert.show()
    }

    private fun deleteDialogPositiveClick(entryPosition: Int) {
        Entries.list.removeAt(entryPosition)
        Snackbar.make(binding.root, "Task Deleted", Snackbar.LENGTH_SHORT).show()
        binding.list.adapter?.notifyItemRemoved(entryPosition)
    }

    private fun deleteDialogNegativeClick(entryPosition: Int) {
        Snackbar.make(binding.root, "Deletion Cancelled", Snackbar.LENGTH_SHORT)
            .setAction("Redo") {
                showDeleteDialog(entryPosition)
            }.show()
    }
}