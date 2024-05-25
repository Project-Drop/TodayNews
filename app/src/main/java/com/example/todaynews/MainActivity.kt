package com.example.todaynews

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.todaynews.databinding.ActivityMainBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.actionCodeSettings
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Initialize Firebase Auth
        auth = Firebase.auth
        binding.register.setOnClickListener {
//            Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show()
            val name = binding.name.text.toString()
            val email = binding.mail.text.toString()
            val password = binding.password.text.toString()
            val users = Users(name, email)

//            val alert = AlertDialog.Builder(this)
//            alert.setTitle("Alert")
//            alert.setMessage("Check email to verify")
//            alert.setPositiveButton("Yes", DialogInterface.OnClickListener{dialog, i ->
//                // code
//            })
//            alert.show()


//            creating user object using Users class => Users.kt file

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val documentId = email.split("@")[0]
//                        val user = auth.currentUser
//                        updateUI(user)
                        val db = Firebase.firestore
                        db.collection("users")
                            .document(
                                documentId.toString()
                            )
                            .set(users)
                            .addOnSuccessListener { documentReference ->
                                sendVerification()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        // If sign in fails, display a message to the user.
//                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
//                        updateUI(null)
                    }
                }
        }

        binding.sign.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
    }
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, HomePageActivity::class.java)
                .apply {
                    Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
            startActivity(intent)
        }
    }

    private fun clearFormFields(name: TextInputEditText, mail: TextInputEditText, password: TextInputEditText) {
        name.text?.clear()
        mail.text?.clear()
        password.text?.clear()
    }

    private fun sendVerification() {
        Toast.makeText(applicationContext, "Function Executed", Toast.LENGTH_SHORT).show()
        val mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser

        user?.sendEmailVerification()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Please verify email")
                    .setMessage("Email sent")
                    .setCancelable(false) // Optional: Set whether dialog can be canceled by tapping outside

                // Set positive button and its click listener
                builder.setPositiveButton("OK") { dialog, which ->
                    if(Firebase.auth.currentUser?.isEmailVerified == true) {
                        // redirect to HomePageActivity
                        Toast.makeText(this, "User Registered", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, HomePageActivity::class.java).apply {
                            Intent.FLAG_ACTIVITY_CLEAR_TOP
                        }
                        startActivity(intent)
                    } else {
                        builder.show()
                    }
                }
                val alertDialog = builder.create()
                alertDialog.show()
                // Email verification link sent successfully
                Toast.makeText(applicationContext, "Verification email sent", Toast.LENGTH_SHORT).show()
            } else {
                // Failed to send verification email
                Toast.makeText(applicationContext, "Failed to send verification email", Toast.LENGTH_SHORT).show()
            }
        }
    }

}