package ru.lineris.bottomfragmentapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import ru.lineris.bottomfragment.BottomQuestionFragment
import ru.lineris.bottomfragmentapp.databinding.ActivityMainBinding

private const val TAG = "BottomFragment"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        test()

        binding.showAgain.setOnClickListener {
            test()
        }
    }

    private fun test() {
        BottomQuestionFragment.newInstance(
            "Hello!",
            "Click the button below",
            "Positive",
            "Negative",
            "https://github.com/kuleshovdv/BottomFragment")
            .setOnShowAction {
                Log.d(TAG, "onShowAction")
                binding.showAgain.visibility = View.GONE
            }
            .setDismissAction {
                Log.d(TAG, "onDismissAction")
                binding.showAgain.visibility = View.VISIBLE
            }
            .setPositiveAction {
                Log.d(TAG, "Positive Action")
                binding.lastAction.text = "Last action: POSITIVE"
            }
            .setNegativeAction {
                Log.d(TAG, "Negative Action")
                binding.lastAction.text = "Last action: NEGATIVE"
            }
            .show(supportFragmentManager, "test")
    }
}