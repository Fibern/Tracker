package com.example.tracker.Fragments

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.tracker.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private var _binding:FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        firebaseAuth = FirebaseAuth.getInstance()

        binding.registerButton.setOnClickListener{
            (activity as NavigationHost).navigateTo(RegisterFragment(), false)
        }

        binding.loginButton.setOnClickListener {
            val mail = binding.emailEditText.text
            val pass = binding.passEditText.text

            if(!isValid(mail, pass)){
                (activity as NavigationHost).navigateTo(MovieFragment(), false)
            }else{
                firebaseAuth.signInWithEmailAndPassword(mail.toString(), pass.toString()).addOnCompleteListener {
                    if (it.isSuccessful){
                        (activity as NavigationHost).navigateTo(MovieFragment(), false)
                    }else{
                        Toast.makeText(requireContext(), it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun isValid(log: Editable?, pass: Editable?): Boolean {
        return log != null && pass != null
    }

}