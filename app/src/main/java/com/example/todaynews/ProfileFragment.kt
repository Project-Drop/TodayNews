package com.example.todaynews

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.compose.runtime.savedinstancestate.savedInstanceState
import com.example.todaynews.databinding.ActivityMainBinding
import com.example.todaynews.databinding.FragmentProfileBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class ProfileFragment : Fragment() {
    private lateinit var showName: TextView
    private lateinit var showEmail: TextView

    private var db = Firebase.firestore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val fragmentView =  inflater.inflate(R.layout.fragment_profile, container, false)

        showName = fragmentView.findViewById(R.id.user_name)
        showEmail = fragmentView.findViewById(R.id.user_email)
        val edit_profile = fragmentView.findViewById<Button>(R.id.edit)

        val userId = FirebaseAuth.getInstance().currentUser?.email?.split("@")?.get(0)
        if (userId != null) {
            val ref = db.collection("users").document(userId)
            ref.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val name = documentSnapshot.getString("name")
                        val email = documentSnapshot.getString("email")
                        showName.text = name
                        showEmail.text = email
                    } else {
                        Toast.makeText(context, "User data not found!", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Failed to fetch data!", Toast.LENGTH_SHORT).show()
                }
        } else {
            // Handle case when user is not authenticated
            Toast.makeText(context, "User not authenticated!", Toast.LENGTH_SHORT).show()
        }

        edit_profile.setOnClickListener {
            showName.isEnabled = true
            edit_profile.setText("update")
            edit_profile.setOnClickListener {
                val newName = showName.text.toString()
                val firestore = Firebase.firestore
                val documentId = FirebaseAuth.getInstance().currentUser?.email?.split("@")?.get(0).toString()
                firestore.collection("users").document(documentId).update("name", newName)
                showName.isEnabled = false
                edit_profile.setText("edit")
            }
        }








        return fragmentView
    }
}