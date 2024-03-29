package ru.lineris.bottomfragment

import android.app.Dialog
import android.content.DialogInterface
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

const val ARG_HEADER = "messageHeader"
const val ARG_MESSAGE = "messageText"
const val ARG_QR = "qrCode"
const val ARG_POSITIVE_BUTTON = "buttonYesText"
const val ARG_NEGATIVE_BUTTON = "buttonNoText"
const val ARG_TECH_INFO = "techInfo"


class BottomQuestionFragment : BottomSheetDialogFragment() {
    private lateinit var positiveAction: () -> Unit
    private lateinit var negativeAction: () -> Unit
    private lateinit var dismissAction: () -> Unit
    private lateinit var onShowAction: () -> Unit
    private var alert: Boolean = false
    private var alertResourceId = R.raw.alert_sound

    fun setPositiveAction(action: () -> Unit): BottomQuestionFragment {
        positiveAction = action
        return this
    }

    fun setNegativeAction(action: () -> Unit): BottomQuestionFragment {
        negativeAction = action
        return this
    }

    fun setDismissAction(action: () -> Unit): BottomQuestionFragment {
        dismissAction = action
        return this
    }

    fun setOnShowAction(action: () -> Unit): BottomQuestionFragment {
        onShowAction = action
        return this
    }

    fun setAlert(resourceId: Int = R.raw.alert_sound): BottomQuestionFragment {
        alert = true
        alertResourceId = resourceId
        return this
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_question, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialog ->
            playAlert()
            if (::onShowAction.isInitialized)
                onShowAction()
            val d = dialog as BottomSheetDialog
            val bottomSheet: View? =
                d.findViewById(com.google.android.material.R.id.design_bottom_sheet)
            if (bottomSheet != null)
                BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
        }
        return dialog
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val textHeader: TextView = view.findViewById(R.id.textHeader)
        val textView: TextView = view.findViewById(R.id.textView)
        val buttonYes: AppCompatButton = view.findViewById(R.id.buttonYes)
        val buttonNo: AppCompatButton = view.findViewById(R.id.buttonNo)
        val qrCodeView: AppCompatImageView = view.findViewById(R.id.qrImageView)
        val techInfoView: TextView = view.findViewById(R.id.techInfo)

        val head = arguments?.getString(ARG_HEADER)
        if (head.isNullOrEmpty()) {
            textHeader.visibility = View.GONE
        } else {
            textHeader.text = head
            textHeader.visibility = View.VISIBLE
        }

        val textYes = arguments?.getString(ARG_POSITIVE_BUTTON)
        val textNo = arguments?.getString(ARG_NEGATIVE_BUTTON)

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

        val qr = arguments?.getString(ARG_QR)
        if (qr.isNullOrEmpty()) {
            qrCodeView.visibility = View.GONE
        } else {
            qrCodeView.setImageBitmap(QRHelper.buildQRcode(qr))
            qrCodeView.visibility = View.VISIBLE
        }

        val techInfo = arguments?.getString(ARG_TECH_INFO)
        if (techInfo.isNullOrEmpty()) {
            techInfoView.visibility = View.GONE
        } else {
            techInfoView.text = techInfo
            techInfoView.visibility = View.VISIBLE
        }

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

    override fun onDismiss(dialog: DialogInterface) {
        if (::dismissAction.isInitialized)
            dismissAction()
        super.onDismiss(dialog)
    }

    private fun playAlert() {
        val minBufSize = AudioTrack.getMinBufferSize(44100, AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT);
        val audioTrack = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setSampleRate(44100)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build()
            )
            .setBufferSizeInBytes(minBufSize)
            .build()

        GlobalScope.launch {
            val inputStream = resources.openRawResource(alertResourceId)
            val byteData = ByteArray(1024)
            audioTrack.play()
            while (true) {
                val chunkSize = inputStream.read(byteData)
                if (chunkSize > 0) {
                    audioTrack.write(byteData, 0, chunkSize)
                } else
                    break
            }
            audioTrack.release()
        }
    }

    companion object {
        fun newInstance(messageHeader: String,
                        messageText: String,
                        buttonPositiveText: String,
                        buttonNegativeText: String,
                        qrCode: String? = null,
                        techInfo: String? = null): BottomQuestionFragment =
            BottomQuestionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_HEADER, messageHeader)
                    putString(ARG_MESSAGE, messageText)
                    putString(ARG_POSITIVE_BUTTON, buttonPositiveText)
                    putString(ARG_NEGATIVE_BUTTON, buttonNegativeText)
                    if (!qrCode.isNullOrEmpty())
                        putString(ARG_QR, qrCode)
                    if (!techInfo.isNullOrEmpty())
                        putString(ARG_TECH_INFO, techInfo)
                }
            }

    }
}
