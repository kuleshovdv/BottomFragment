package ru.lineris.bottomfragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

const val ARG_HEADER = "messageHeader"
const val ARG_MESSAGE = "messageText"
const val ARG_BUTTON = "buttonText"
const val ARG_POSITIVE_BUTTON = "buttonYesText"
const val ARG_NEGATIVE_BUTTON = "buttonNoText"
const val ARG_ICON_RES = "iconRes"


class BottomQuestionFragment : BottomSheetDialogFragment() {
    private lateinit var positiveAction: () -> Unit
    private lateinit var negativeAction: () -> Unit

    fun setPositiveAction(action: () -> Unit): BottomQuestionFragment {
        positiveAction = action
        return this
    }

    fun setNegativeAction(action: () -> Unit): BottomQuestionFragment {
        negativeAction = action
        return this
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_question, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val textHeader: TextView = view.findViewById(R.id.textHeader)
        val textView: TextView = view.findViewById(R.id.textView)
        val buttonYes: AppCompatButton = view.findViewById(R.id.buttonYes)
        val buttonNo: AppCompatButton = view.findViewById(R.id.buttonNo)
        val icon: ImageView = view.findViewById(R.id.icon)

        val head = arguments?.getString(ARG_HEADER)
        if (head.isNullOrEmpty()) {
            textHeader.visibility = View.GONE
        } else {
            textHeader.text = head
            textHeader.visibility = View.VISIBLE
        }

        val textYes = arguments?.getString(ARG_POSITIVE_BUTTON)
        val textNo = arguments?.getString(ARG_NEGATIVE_BUTTON)
        val iconRes = arguments?.getInt(ARG_ICON_RES)

        textView.text = arguments?.getString(ARG_MESSAGE)
        if (textYes?.isNotEmpty() == true) {
            buttonYes.text = textYes
            buttonYes.visibility = View.VISIBLE
        } else
            buttonYes.visibility = View.GONE
        if (textNo?.isNotEmpty() == true) {
            buttonNo.text = textNo
            buttonNo.visibility = View.VISIBLE
        } else
            buttonNo.visibility = View.GONE
        if (iconRes != null) {
            icon.setImageResource(iconRes)
            icon.visibility = View.VISIBLE
        } else
            icon.visibility = View.GONE


        buttonYes.setOnClickListener {
            //parentFragment?.childFragmentManager?.beginTransaction()?.remove(this)?.commit()
            dismiss()
            if (::positiveAction.isInitialized)
                positiveAction()
        }

        buttonNo.setOnClickListener {
//            parentFragment?.childFragmentManager?.beginTransaction()?.remove(this)?.commit()
            dismiss()
            if (::negativeAction.isInitialized)
                negativeAction()
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }



    companion object {
        fun newInstance(messageHeader: String,
                        messageText: String,
                        buttonPositiveText: String,
                        buttonNegativeText: String,
                        icon: Int? = null): BottomQuestionFragment =
            BottomQuestionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_HEADER, messageHeader)
                    putString(ARG_MESSAGE, messageText)
                    putString(ARG_POSITIVE_BUTTON, buttonPositiveText)
                    putString(ARG_NEGATIVE_BUTTON, buttonNegativeText)
                    if (icon != null)
                        putInt(ARG_ICON_RES, icon)
                }
            }

    }
}
