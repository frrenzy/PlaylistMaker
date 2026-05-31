package com.example.playlistmaker.settings.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.settings.presentation.SettingsViewModel
import com.example.playlistmaker.utils.connectBackButton

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        connectBackButton(binding.backButton)

        viewModel = ViewModelProvider(
            this,
            SettingsViewModel.getFactory(this)
        ).get(
            SettingsViewModel::class.java
        )

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