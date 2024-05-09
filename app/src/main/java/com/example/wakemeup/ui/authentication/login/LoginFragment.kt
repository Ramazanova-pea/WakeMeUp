package com.example.wakemeup.ui.authentication.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.wakemeup.MainActivity
import com.example.wakemeup.R
import com.example.wakemeup.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch

/**
 * This is the LoginFragment class which is responsible for the user login process.
 * It handles user input validation and communicates with the LoginViewModel to perform the login.
 */
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel

    /**
     * This method is called to do initial creation of a fragment.
     * This is where you should do all of your normal static set up to create views, bind data to lists, and so on.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]



        setupEyeIconClick(binding.inputPasswordLayout2)
        setupEditTexts()

        // Setting up the click listener for the login button
        binding.loginButton.setOnClickListener {
            val login = binding.inputLogin2.text.toString()
            val password = binding.inputPassword2.text.toString()

            if (login.isEmpty()) setError(binding.inputLoginLayout2, "Login is required")
            if (password.isEmpty()) setError(binding.inputPasswordLayout2, "Password is required")

            if (login.isNotEmpty() && password.isNotEmpty()) {
                lifecycleScope.launch {
                    viewModel.onLoginClick(login, password, requireContext()).collect { state -> processLoginState(state) }
                }
            }
        }

        // Setting up the click listener for the proceed button
        // Why the button is called "Proceed"?
        // @peterkrglv, @RamazanovaMO???
        binding.proceedButtonLoginFragment.setOnClickListener {
            (activity as? MainActivity)?.goToSignup()
        }

        return binding.root
    }

    fun processLoginState(state: LoginState) {
        when(state) {
            LoginState.SUCCESS -> {
                Log.d("LoginFragment", "Logged in successfully")
                findNavController().navigate(R.id.navigation_friends)
            }
            LoginState.ERROR_USER_DOESNT_EXIST -> setError(binding.inputLoginLayout2, "User with this login doesn't exist")
            LoginState.ERROR_WRONG_PASSWORD -> setError(binding.inputPasswordLayout2, "Wrong password")
            LoginState.ERROR -> Snackbar.make(binding.root, "Some error has occurred", Snackbar.LENGTH_SHORT).show()
        }
    }

    /**
     * This method sets up the click listener for the eye icon in the password field.
     */
    private fun setupEyeIconClick(layout: TextInputLayout) {
        layout.setEndIconOnClickListener {
            val isPasswordVisible = layout.editText?.inputType == 129
            layout.endIconDrawable = ResourcesCompat.getDrawable(
                resources,
                if (isPasswordVisible) {
                    R.drawable.open_eye
                }
                else {
                    R.drawable.closed_eye
                },
                null
            )
            layout.editText?.inputType = if (isPasswordVisible) 145 else 129
        }
    }

    /**
     * This method sets up the text watcher for the input field.
     */
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

    /**
     * This method sets up the text watchers for all the input fields.
     */
    private fun setupEditTexts() {
        setEditText(binding.inputLoginLayout2, binding.inputLogin2)
        setEditText(binding.inputPasswordLayout2, binding.inputPassword2)
    }

    /**
     * This method sets the error message for a TextInputLayout.
     */
    private fun setError(layout: TextInputLayout, error: String) {
        layout.error = error
    }
}