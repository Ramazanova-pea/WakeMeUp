package com.example.wakemeup.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.wakemeup.R
import com.example.wakemeup.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        setupEyeIconClick(binding.inputPasswordLayout2)
        setupEditTexts()


        viewModel.loginState.observe(viewLifecycleOwner) { state ->
            when(state) {
                LoginState.SUCCESS -> {
                    Log.d("LoginFragment", "Logged in successfully")
                    findNavController().navigate(R.id.action_loginFragment_to_navigation_friends)

                }
                LoginState.ERROR_INVALID_EMAIL -> setError(binding.inputLoginLayout2, "Invalid email adress")
                LoginState.ERROR_USER_DOESNT_EXIST -> setError(binding.inputLoginLayout2, "User with this login doesn't exist")
                LoginState.ERROR_WRONG_PASSWORD -> setError(binding.inputPasswordLayout2, "Wrong password")
                LoginState.ERROR -> Snackbar.make(binding.root, "Some error has occurred", Snackbar.LENGTH_SHORT).show()

            }
        }

        binding.loginButton.setOnClickListener {
            val login = binding.inputLogin2.text.toString()
            val password = binding.inputPassword2.text.toString()

            if (login.isEmpty()) setError(binding.inputLoginLayout2, "Login is required")
            if (password.isEmpty()) setError(binding.inputPasswordLayout2, "Password is required")

            if (!viewModel.checkEmail(login)) {
                // setError()
            }
            else if (login.isNotEmpty() && password.isNotEmpty()) {
                viewModel.onLoginClick(login, password)
            }
        }

        binding.proceedButtonLoginFragment.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }

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
        setEditText(binding.inputLoginLayout2, binding.inputLogin2)
        setEditText(binding.inputPasswordLayout2, binding.inputPassword2)
    }

    private fun setError(layout: TextInputLayout, error: String) {
        layout.error = error
    }
}