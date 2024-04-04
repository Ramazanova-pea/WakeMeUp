package com.example.wakemeup.ui.signup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.wakemeup.R
import com.example.wakemeup.databinding.FragmentSignupBinding
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

        binding.inputPasswordLayout.setEndIconOnClickListener {
            if (binding.inputPasswordLayout.editText?.inputType == 129) {
                binding.inputPasswordLayout.endIconDrawable =
                    resources.getDrawable(R.drawable.open_eye)
                binding.inputPasswordLayout.editText?.inputType = 145
            } else {
                binding.inputPasswordLayout.endIconDrawable =
                    resources.getDrawable(R.drawable.closed_eye)
                binding.inputPasswordLayout.editText?.inputType = 129
            }
        }

        binding.inputPasswordRepeatLayout.setEndIconOnClickListener {
            if (binding.inputPasswordRepeatLayout.editText?.inputType == 129) {
                binding.inputPasswordRepeatLayout.endIconDrawable =
                    resources.getDrawable(R.drawable.open_eye)
                binding.inputPasswordRepeatLayout.editText?.inputType = 145
            } else {
                binding.inputPasswordRepeatLayout.endIconDrawable =
                    resources.getDrawable(R.drawable.closed_eye)
                binding.inputPasswordRepeatLayout.editText?.inputType = 129
            }
        }

        setEditText(binding.inputNameLayout, binding.inputName)
        setEditText(binding.inputLoginLayout, binding.inputLogin)
        setEditText(binding.inputPasswordLayout, binding.inputPassword)
        setEditText(binding.inputPasswordRepeatLayout, binding.inputPasswordRepeat)


        viewModel.registrationState.observe(viewLifecycleOwner) {
            when (it) {
                RegistrationState.SUCCESS -> {
                    // TODO: Сохранить авторизацию в SharedPreferences
                    // TODO: Перейти на главный экран
                }

                RegistrationState.ERROR -> {
                    binding.inputLoginLayout.error =
                        "User with this login already exists"
                }
            }
        }


        binding.signupButton.setOnClickListener {
            var name = binding.inputName.text.toString()
            var login = binding.inputLogin.text.toString()
            var password = binding.inputPassword.text.toString()
            var passwordConfirm = binding.inputPasswordRepeat.text.toString()

            if (name.isEmpty()) {
                binding.inputNameLayout.error = "Name is required"
            }

            if (login.isEmpty()) {
                binding.inputLoginLayout.error = "Login is required"
            }

            if (password.isEmpty()) {
                binding.inputPasswordLayout.error = "Password is required"
            }

            if (passwordConfirm.isEmpty()) {
                binding.inputPasswordRepeatLayout.error = "Password confirmation is required"
            }

            if (name.isNotEmpty() && login.isNotEmpty() && password.isNotEmpty() && passwordConfirm.isNotEmpty()) {
                if (password != passwordConfirm) {
                    binding.inputPasswordRepeatLayout.error = "Passwords do not match"
                } else {
                    viewModel.onSignUpClick(name, login, password)
                }
            }

        }

//        binding.inputLogin.addTextChangedListener(viewModel.loginTextWatcher)
        return binding.root
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
}