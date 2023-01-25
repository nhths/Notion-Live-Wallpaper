package io.github.nhths.notionlivewallpaper

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import io.github.nhths.notionlivewallpaper.databinding.ActivitySettingsBinding
import io.github.nhths.notionlivewallpaper.ui.model.SettingsActivityViewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val viewModel by viewModels<SettingsActivityViewModel>(factoryProducer = {SettingsActivityViewModel.Factory(application)})

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        viewModel.parseIntent(intent)
    }
}