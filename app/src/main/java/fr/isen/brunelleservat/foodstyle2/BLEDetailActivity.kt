package fr.isen.brunelleservat.foodstyle2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import fr.isen.brunelleservat.foodstyle2.databinding.ActivityBLEDetailBinding

class BLEDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBLEDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBLEDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}