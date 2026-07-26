package com.example.playlistmaker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.settings.presentation.SettingsViewModel
import com.example.playlistmaker.utils.BindingFragment
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class SettingsFragment : BindingFragment<FragmentSettingsBinding>() {
    private val viewModel: SettingsViewModel by activityViewModel()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSettingsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            viewModel.observeIsDark().observe(viewLifecycleOwner) {
                themeSwitcher.isChecked = it
            }
            themeSwitcher.setOnCheckedChangeListener { _, checked ->
                viewModel.setTheme(checked)
            }

            shareButton.setOnClickListener {
                viewModel.shareApp()
            }

            supportButton.setOnClickListener {
                viewModel.sendSupportTicket()
            }

            agreementButton.setOnClickListener {
                viewModel.openUserAgreement()
            }
        }
    }
}
