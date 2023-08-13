package goje.contactsapp

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import goje.contactsapp.databinding.ActivityMainBinding
import goje.contactsapp.utils.Constants


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val c: Configuration = resources.configuration
        val scale: Float = c.fontScale
        Constants.fontSize = scale
        if (scale > 1.6) {
            Constants.USER_ENABLED_BIG_FONT_SIZE = true
        }

    }



}