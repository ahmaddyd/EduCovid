package com.capstoneproject.csd190.ui.fragment

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.capstoneproject.csd190.databinding.FragmentSignUpBinding
import com.capstoneproject.csd190.ui.activity.MainActivity
import com.google.firebase.auth.FirebaseAuth

class SignUpFragment : Fragment() {
    private lateinit var signUpBinding: FragmentSignUpBinding
    private lateinit var progressDialog: ProgressDialog
    private lateinit var firebaseAuth: FirebaseAuth
    private var email = ""
    private var password = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        signUpBinding = FragmentSignUpBinding.inflate(layoutInflater, container, false)
        return signUpBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressDialog = ProgressDialog(activity)
        progressDialog.setTitle("Silahkan Tunggu")
        progressDialog.setMessage("Membuat Akun...")
        progressDialog.setCanceledOnTouchOutside(false)

        firebaseAuth = FirebaseAuth.getInstance()

        signUpBinding.btnSignup.setOnClickListener {
            checkData()
        }
    }

    private fun checkData() {
        email = signUpBinding.etUsername.text.toString().trim()
        password = signUpBinding.etPassword.text.toString().trim()

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signUpBinding.etUsername.error = "Masukkan email dengan benar!"
        } else if (TextUtils.isEmpty(password)) {
            signUpBinding.etPassword.error = "Kolom password harus diisi"
        } else if (password.length < 6) {
            signUpBinding.etPassword.error = "Password minimum 8 karakter"
        } else {
            signUpFirebase()
        }
    }

    private fun signUpFirebase() {
        progressDialog.show()

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            val firebaseUser = firebaseAuth.currentUser
            val email = firebaseUser!!.email

            Toast.makeText(activity, "Mendaftar sebagai $email", Toast.LENGTH_SHORT).show()

            startActivity(Intent(activity, MainActivity::class.java))

            activity?.finish()
        }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(activity, "Gagal Mendaftar ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}