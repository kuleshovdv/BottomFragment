# BottomFragment

Simple and convenient extention of the **BottomSheetDialogFragment **class for dialogs.
Use newinstance pattern you can build dialogs with:
1. Title
2. TextView body
3. QR code ImageView
4. Button for positive user reaction
5. Button for negative user reaction
6. Lamda functions for show/dismiss dialog, positice/negative action.

Example:
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
