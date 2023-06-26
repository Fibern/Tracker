package com.example.tracker.Fragments

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.tracker.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = binding.root
        firebaseAuth = FirebaseAuth.getInstance()

        binding.loginButton.setOnClickListener{
            (activity as NavigationHost).navigateTo(LoginFragment(), false)
        }

        binding.registerButton.setOnClickListener {
            val mail = binding.emailEditText.text
            val pass = binding.passEditText.text
            val confPass = binding.confPassEditText.text

            if (!isValid(mail, pass, confPass)){

            }else if(pass.toString() == confPass.toString()){
                firebaseAuth.createUserWithEmailAndPassword(mail.toString(), pass.toString()).addOnCompleteListener { 
                    if(it.isSuccessful){
                        (activity as NavigationHost).navigateTo(LoginFragment(), false)
                    }else{
                        Toast.makeText(requireContext(), it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(requireContext(), "", Toast.LENGTH_SHORT).show()
            }

        }

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun isValid(log: Editable?, pass: Editable?, confPass: Editable?): Boolean {
        return log != null && pass != null && confPass != null
    }

}