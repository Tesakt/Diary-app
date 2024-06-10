package com.example.mydiary

interface DiaryListListener {
    fun onEntryClick(entryPosition: Int)
    fun onEntryLongClick(entryPosition: Int)
}