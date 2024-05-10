package com.example.wakemeup.ui.signup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.wakemeup.R
import com.example.wakemeup.databinding.FragmentSignupBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class SignupFragment : Fragment() {

    private lateinit var binding: FragmentSignupBinding
    private lateinit var viewModel: SignupViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[SignupViewModel::class.java]

        setupEyeIconClick(binding.inputPasswordLayout)
        setupEyeIconClick(binding.inputPasswordRepeatLayout)

        setupEditTexts()


        viewModel.registrationState.observe(viewLifecycleOwner) { state ->
            when (state) {
                RegistrationState.SUCCESS -> {
                    Log.d("SignupFragment", "User created successfully")
                    findNavController().navigate(R.id.action_signupFragment_to_navigation_friends)

                }
                RegistrationState.ERROR_INVALID_EMAIL -> setError(binding.inputLoginLayout, "Invalid email adress")
                RegistrationState.ERROR_USER_ALREADY_EXISTS -> setError(binding.inputLoginLayout, "User with this login already exists")
                RegistrationState.ERROR_WEAK_PASSWORD -> setError(binding.inputPasswordLayout, "Password is too weak")
                RegistrationState.ERROR_INVALID_CREDENTIALS -> setError(binding.inputLoginLayout, "Invalid credentials")
                RegistrationState.ERROR -> Snackbar.make(binding.root, "Some error has occurred", Snackbar.LENGTH_SHORT).show()
            }
        }


        binding.signupButton.setOnClickListener {
            val name = binding.inputName.text.toString()
            val login = binding.inputLogin.text.toString()
            val password = binding.inputPassword.text.toString()
            val passwordConfirm = binding.inputPasswordRepeat.text.toString()

            if (name.isEmpty()) setError(binding.inputNameLayout, "Name is required")
            if (login.isEmpty()) setError(binding.inputLoginLayout, "Login is required")
            if (password.isEmpty()) setError(binding.inputPasswordLayout, "Password is required")
            if (passwordConfirm.isEmpty()) setError(binding.inputPasswordRepeatLayout, "Password confirmation is required")

            if (name.isNotEmpty() && login.isNotEmpty() && password.isNotEmpty() && passwordConfirm.isNotEmpty()) {
                if (password != passwordConfirm) {
                    setError(binding.inputPasswordRepeatLayout, "Passwords do not match")
                } else {
                    viewModel.onSignUpClick(name, login, password)
                }
            }
        }

        binding.proceedButtonSignupFragment.setOnClickListener {
            findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
        }

//        binding.inputLogin.addTextChangedListener(viewModel.loginTextWatcher)
        return binding.root
    }

    private fun setupEyeIconClick(layout: TextInputLayout) {
        layout.setEndIconOnClickListener {
            val isPasswordVisible = layout.editText?.inputType == 129
            layout.endIconDrawable = resources.getDrawable(if (isPasswordVisible) R.drawable.open_eye else R.drawable.closed_eye)
            layout.editText?.inputType = if (isPasswordVisible) 145 else 129
        }
    }

    fun setEditText(layout: TextInputLayout, editText: TextInputEditText) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                layout.error = null
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                layout.error = null
            }
        })
    }

    private fun setupEditTexts() {
        setEditText(binding.inputNameLayout, binding.inputName)
        setEditText(binding.inputLoginLayout, binding.inputLogin)
        setEditText(binding.inputPasswordLayout, binding.inputPassword)
        setEditText(binding.inputPasswordRepeatLayout, binding.inputPasswordRepeat)
    }

    private fun setError(layout: TextInputLayout, error: String) {
        layout.error = error
    }
}