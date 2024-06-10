package com.example.mydiary

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mydiary.data.MOOD
import com.example.mydiary.data.Entry
import com.example.mydiary.data.Entries
import com.example.mydiary.databinding.FragmentAddEntryBinding
import java.util.*

class AddEntryFragment : Fragment() {
    private val args: AddEntryFragmentArgs by navArgs()

    private lateinit var binding: FragmentAddEntryBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View {
        // Initialize the binding variable
        binding = FragmentAddEntryBinding.inflate(inflater, container, false)
        binding.titleInput.setText(args.entryToEdit?.title)
        binding.descriptionInput.setText(args.entryToEdit?.description)
        when(args.entryToEdit?.mood) {
            MOOD.SMILEY -> binding.happyRadioButton.isChecked = true
            MOOD.NEUTRAL -> binding.neutralRadioButton.isChecked = true
            MOOD.LOVE -> binding.loveRadioButton.isChecked = true
            MOOD.CRYING -> binding.cryingRadioButton.isChecked = true
            MOOD.ANGRY -> binding.angryRadioButton.isChecked = true
            MOOD.SAD -> binding.sadRadioButton.isChecked = true
            else -> binding.neutralRadioButton.isChecked = true
        }
        return binding.root
    }

    private var selectedDate: Date? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Dodajemy nasłuchiwacza zmiany daty do CalendarView
        binding.entryDate.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // Tworzymy obiekt Calendar i ustawiamy wybraną datę
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)

            // Pobieramy datę z obiektu Calendar
            selectedDate = calendar.time
        }

        binding.saveButton.setOnClickListener { saveEntry() }
    }

    private fun saveEntry() {
        // Get the values from data fields on the screen
        var title: String = binding.titleInput.text.toString()
        var description = binding.descriptionInput.text.toString()
        val date = selectedDate ?: Date()
        val mood = when(binding.moodGroup.checkedRadioButtonId) {
            // ANGRY, CRYING, LOVE, NEUTRAL, SAD, SMILEY
            R.id.happy_radioButton -> MOOD.SMILEY
            R.id.neutral_radioButton -> MOOD.NEUTRAL
            R.id.love_radioButton -> MOOD.LOVE
            R.id.crying_radioButton -> MOOD.CRYING
            R.id.angry_radioButton -> MOOD.ANGRY
            R.id.sad_radioButton -> MOOD.SAD
            else -> MOOD.NEUTRAL
        }
        // Handle missing EditText input
        if(title.isEmpty())
            title = getString(R.string.default_diary_title) + "${Entries.list.size + 1}"
        if(description.isEmpty())
            description = getString(R.string.no_desc_msg)
        // Create a new Task item based on input values
        val entryItem = Entry(
            args.entryToEdit?.isPinned ?: false,
            {title + description}.hashCode().toString(),
            title,
            date,
            description,
            mood
        )
        if(!args.edit) {
            // Add the new task to the list of tasks
            Entries.list.add(entryItem)
        } else {
            Entries.updateEntry(oldEntry = args.entryToEdit, newEntry = entryItem)
        }

        // Hide the softwere keyboard with InpudMethodMenager
        val inputMethodManager: InputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)

        // Navigate back to the TaskListFragment, the inclusive parameter is set to false
        findNavController().clearBackStack(R.id.DiaryListFragment)
        findNavController().popBackStack(R.id.DiaryListFragment, false)
    }
}