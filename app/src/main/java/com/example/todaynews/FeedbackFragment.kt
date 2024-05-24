package com.example.todaynews

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FeedbackFragment : Fragment() {

    // Define EditText and Button variables
    private lateinit var nameEt: EditText
    private lateinit var locationEt: EditText
    private lateinit var experienceEt: EditText
    private lateinit var improveEt: EditText
    private lateinit var submit: Button

    // Create a Retrofit instance for API communication
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://docs.google.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Create an API interface using Retrofit
    private val api = retrofit.create(GoogleFormApi::class.java)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feedback, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view) // Initialize UI elements

        submit.setOnClickListener {
            // Get user input from EditText fields
            val name = nameEt.text.toString()
            val location = locationEt.text.toString()
            val experience = experienceEt.text.toString()
            val improvements = improveEt.text.toString()

            // Create a call to send form data
            val call = api.sendFormData(name, location, experience, improvements)

            // Execute the API call asynchronously
            call.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    try {
                        if (response.isSuccessful) {
                            Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show()
                            clearText()
                        } else {
                            Toast.makeText(context, "Unsuccessful", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    try {
                        Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
    }

    private fun clearText() {
        nameEt.text.clear()
        locationEt.text.clear()
        experienceEt.text.clear()
        improveEt.text.clear()
    }

    // Initialize UI elements from Your xml
    private fun init(view: View) {
        nameEt = view.findViewById(R.id.name)
        locationEt = view.findViewById(R.id.location)
        experienceEt = view.findViewById(R.id.experience)
        improveEt = view.findViewById(R.id.improve)
        submit = view.findViewById(R.id.submit)
    }
}

