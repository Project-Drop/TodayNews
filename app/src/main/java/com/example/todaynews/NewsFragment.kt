package com.example.todaynews

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NewsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_news, container, false)

        val sdf = SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
        val currentDate = sdf.format(Date())
        val database = Firebase.firestore
        val newsref = database.collection("news").document(currentDate)

        // Getting the RecyclerView by its ID
        val recyclerview = view.findViewById<RecyclerView>(R.id.recyclerview)

        // This creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(activity)

        // ArrayList of class ItemsViewModel
        val data = ArrayList<ItemViewModel>()

        // Fetching all documents from the "news" collection
        newsref.get().addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                val headlines = document.get("headline") as? List<String> ?: listOf()
                val links = document.get("links") as? List<String> ?: listOf()

                // Combine headlines and links into ItemViewModel instances
                for (i in headlines.indices) {
                    val heading = headlines[i]
                    val link = if (i < links.size) links[i] else ""
                    data.add(ItemViewModel(heading, link))
                    println("Heading: $heading, Link: $link")
                }

                // This will pass the ArrayList to our Adapter
                val adapter = CustomAdapter(data)

                // Setting the Adapter with the RecyclerView
                recyclerview.adapter = adapter
            } else {
                println("No such document")
            }
        }.addOnFailureListener { exception ->
            println("get failed with $exception")
        }
        return view
    }
}
