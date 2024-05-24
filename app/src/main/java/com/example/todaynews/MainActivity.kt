package com.example.todaynews

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


            val actionCodeSettings = actionCodeSettings {
                // URL you want to redirect back to. The domain (www.example.com) for this
                // URL must be whitelisted in the Firebase Console.
                url = "https://www.example.com/finishSignUp?cartId=1234"
                // This must be true
                handleCodeInApp = true
                setIOSBundleId("com.example.ios")
                setAndroidPackageName(
                    "com.example.android",
                    true, // installIfNotAvailable
                    "12", // minimumVersion
                )
            }
            Firebase.auth.sendSignInLinkToEmail(email, actionCodeSettings)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val alertDialog: AlertDialog? = null
                        alertDialog?.setMessage("Email sent to your mail please check for authentication")
                        alertDialog?.show()
                    }
                }
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
                                Toast.makeText(this, "User Registered", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this,HomePageActivity::class.java).apply {
                                    Intent.FLAG_ACTIVITY_CLEAR_TOP
                                }
                                startActivity(intent)
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


}