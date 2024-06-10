package com.example.mydiary.data

import java.util.Date


object Entries {
    // This is the list that will be used to store the tasks
    val list: MutableList<Entry> = ArrayList()

    // This is the number of placeholder tasks that will be added to the list
    private var COUNT = 8

    // This is the init block that will be used to add the placeholder tasks to the list
    init {
        for (i in 1..COUNT) {
            addEntry(createPlaceholderEntry(i))
        }
    }

    // This function is used to add a task to the list
    private fun addEntry(entry: Entry) {
        list.add(entry)
    }

    fun togglePinEntry(entry: Entry) {
        if (!entry.isPinned) {
            list.remove(entry)
            entry.isPinned = true
            // remove the entry from the list
            list.add(0, entry)
        } else {
            list.remove(entry)
            entry.isPinned = false
            // remove the entry from the list
            // place the entry at the end of pinned entries
            if (list.find { !it.isPinned } != null) {
                list.find { !it.isPinned }?.let { temp -> list.indexOf(temp).let { list.add(it, entry) }
                }
            } else {
                list.add(entry)
            }
        }
    }

    fun updateEntry(oldEntry: Entry?, newEntry: Entry) {
        oldEntry?.let { old ->
            val indexOfOld = list.indexOf(old)
            if (indexOfOld != -1) {
                list[indexOfOld] = newEntry
            }
        }
    }

    // This function is used to create a placeholder task so the initial list is not empty
    private fun createPlaceholderEntry(position: Int): Entry {
        return Entry(false, position.toString(), "Entry $position", Date(), makeDetails(position))
    }

    // This function is used to create a placeholder description for the tasks
    // it takes in an integer position and returns a string created with the StringBuilder
    private fun makeDetails(position: Int): String {
        val builder = StringBuilder()
        builder.append("Details about Entry: ").append(position)
        for (i in 0..<position) {
            builder.append("\nMore details information here.")
        }
        return builder.toString()
    }
}