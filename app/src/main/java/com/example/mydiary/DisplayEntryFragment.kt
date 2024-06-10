package com.example.mydiary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mydiary.data.Entries
import com.example.mydiary.data.MOOD
import com.example.mydiary.databinding.FragmentDisplayEntryBinding

class DisplayEntryFragment : Fragment() {
    private val args: DisplayEntryFragmentArgs by navArgs()
    private lateinit var binding: FragmentDisplayEntryBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDisplayEntryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val entry = args.entry
        binding.displayTitle.text = entry.title
        binding.displayDescription.text = entry.description
        val moodDrawable = when (entry.mood) {
            MOOD.SMILEY -> R.drawable.happy_face
            MOOD.SAD -> R.drawable.sad_face
            MOOD.ANGRY -> R.drawable.angry_face
            MOOD.NEUTRAL -> R.drawable.neutral_face
            MOOD.LOVE -> R.drawable.love_face
            MOOD.CRYING -> R.drawable.crying_face
        }
        binding.displayMood.setImageResource(moodDrawable)

        binding.displayEdit.setOnClickListener {
            val actionEditEntry =
                DisplayEntryFragmentDirections.actionDisplayEntryFragmentToAddEntryFragment()
            with(actionEditEntry) {
                entryToEdit = entry
                edit = true
            }
            findNavController().navigate(actionEditEntry)
        }

        binding.displayPin.setOnClickListener {
            Entries.togglePinEntry(entry)
            findNavController().clearBackStack(R.id.DiaryListFragment)
            findNavController().popBackStack(R.id.DiaryListFragment, false)
        }
    }
}