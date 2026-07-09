package com.example.playlistmaker.settings.ui

import android.os.Bundle
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.settings.presentation.SettingsViewModel
import com.example.playlistmaker.utils.BindingActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : BindingActivity<ActivitySettingsBinding>() {
    override fun createBinding() = ActivitySettingsBinding.inflate(layoutInflater)
    override fun getBackButton() = binding.backButton

    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            viewModel.observeIsDark().observe(this@SettingsActivity) {
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
