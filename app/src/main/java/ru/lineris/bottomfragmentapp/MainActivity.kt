package ru.lineris.bottomfragmentapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.lineris.bottomfragment.BottomQuestionFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        BottomQuestionFragment.newInstance(
            "Hello World!",
        "You can press button",
            "Positive",
            "Negative")
            .show(supportFragmentManager, "test")
    }
}