package com.example.mydiary

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.example.mydiary.data.MOOD
import com.example.mydiary.data.Entry
import com.example.mydiary.databinding.FragmentDiaryItemBinding

// Adapter is responsible for managing the display of the list binding data with the views
class MyEntryRecyclerViewAdapter(
    private val values: List<Entry>,
    private val eventListener: DiaryListListener
): RecyclerView.Adapter<MyEntryRecyclerViewAdapter.ViewHolder>()
{
    // The ViewHolder class is a container for the views in the recycler view item
    class ViewHolder(binding: FragmentDiaryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val pinView: ImageView = binding.pin
        val imgView: ImageView = binding.itemImg
        val dateView: TextView = binding.date
        val contentView: TextView = binding.content
        val itemContainer: View = binding.root
        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

    // onCreateViewHolder creates the ViewHolder objects
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyEntryRecyclerViewAdapter.ViewHolder {
        // create the view holders for the recycler view items
        // no data is bound to the views yet
        return ViewHolder(
            FragmentDiaryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    @SuppressLint("DefaultLocale")
    override fun onBindViewHolder(holder: MyEntryRecyclerViewAdapter.ViewHolder, position: Int) {
        val entry = values[position]

        val moodImage = when(entry.mood) {
            MOOD.ANGRY -> R.drawable.angry_face
            MOOD.CRYING -> R.drawable.crying_face
            MOOD.LOVE -> R.drawable.love_face
            MOOD.NEUTRAL -> R.drawable.neutral_face
            MOOD.SAD -> R.drawable.sad_face
            MOOD.SMILEY -> R.drawable.happy_face
        }

        holder.imgView.setImageResource(moodImage)
        val formattedDate = String.format("Date: %02d.%02d.%d",
            entry.date?.getDate() ?: 0,
            entry.date?.getMonth()?.plus(1) ?: 0,
            (entry.date?.year ?: 0) + 1900
        )
        holder.dateView.text = formattedDate
        holder.contentView.text = entry.title

        holder.pinView.visibility = if (entry.isPinned) View.VISIBLE else View.INVISIBLE

        holder.itemContainer.setOnClickListener {
            eventListener.onEntryClick(position)
        }
        holder.itemContainer.setOnLongClickListener {
            eventListener.onEntryLongClick(position)
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int {
        return values.size
    }

}