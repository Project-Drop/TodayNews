package com.example.todaynews

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class CustomAdapter(private val mList: List<ItemViewModel>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemViewModel = mList[position]

        // Log the heading value to check for null
        Log.d("CustomAdapter", "Heading: ${itemViewModel.heading}, Link: ${itemViewModel.link}")

        // sets the text to the textview from our itemHolder class
        holder.textView.text = itemViewModel.heading
        holder.published_date.text = itemViewModel.time
        holder.pyblish_by.text = itemViewModel.source
        Glide.with(holder.itemView.context)
            .load(itemViewModel.image)
            .placeholder(R.drawable.kkr)
            .error(R.drawable.kkr2)
            .into(holder.image)
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, WebViewActivity::class.java).apply {
                putExtra("URL", itemViewModel.link)
            }
            context.startActivity(intent)
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val textView: TextView = itemView.findViewById(R.id.Heading)
        val published_date : TextView = itemView.findViewById(R.id.published)
        val  pyblish_by : TextView = itemView.findViewById(R.id.publishedBy)
        val image : ImageView = itemView.findViewById(R.id.title_image)
    }
}