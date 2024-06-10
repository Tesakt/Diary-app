package com.example.mydiary.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

enum class MOOD {
    ANGRY, CRYING, LOVE, NEUTRAL, SAD, SMILEY
}

@Parcelize //@Parcelize is an annotation that allows you to make your class Parcelable
data class Entry(
    var isPinned: Boolean,
    val id: String,
    val title: String,
    val date: Date?,
    val description: String,
    val mood: MOOD = MOOD.NEUTRAL
) : Parcelable// Parcelable is an interface that allows you to pass data between activities
                // and fragments