package com.example.countryquizz.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.example.countryquizz.databinding.RegisterDialogBinding
import com.example.countryquizz.ui.viewmodels.RegistrationViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterDialog : DialogFragment() {

    private lateinit var binding: RegisterDialogBinding
    private val viewModel by viewModel<RegistrationViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = RegisterDialogBinding.inflate(layoutInflater).also { it ->
            val email = it.etEmailRegister
            val password = it.etPasswordRegister

            it.btnRegisterUser.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.register(email.text.toString(), password.text.toString())
                }
            }

            viewModel.isRegistered.observe(this, {
                if(it) {
                    dismiss()
                }
                else {
                    Toast.makeText(requireContext(), "Registration failed", Toast.LENGTH_SHORT).show()
                }
            })
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels)
        val height = (resources.displayMetrics.heightPixels * 0.5).toInt()

        dialog!!.window?.setLayout(width, height)
    }

}