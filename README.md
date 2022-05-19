# BottomQuestionFragment

Simple and convenient extention of the BottomSheetDialogFragment class.
Use newinstance pattern for build dialog with:
1. Title
2. TextView body
3. QR code ImageView
4. Button for positive user reaction
5. Button for negative user reaction
6. Lamda functions for show/dismiss dialog, positive/negative action.

Like this

![BottomQuestionFragment](https://github.com/kuleshovdv/BottomFragment/raw/master/screen.png  "BottomQuestionFragment")

Add it in your root build.gradle at the end of repositories:
```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
Add the dependency
```
	dependencies {
		implementation 'com.github.kuleshovdv:BottomFragment:0.05'
	}
```

Example
```
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
```

If you need to start dialog from fragment
```
	.show(childFragmentManager, "test")
```	
