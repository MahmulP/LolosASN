package com.lolos.asn.ui.dialog

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.lolos.asn.R
import com.lolos.asn.databinding.FragmentNumberDialogBinding
import com.lolos.asn.databinding.FragmentPaymentStatusDialogBinding
import com.lolos.asn.ui.activity.HistoryActivity

class PaymentStatusDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentPaymentStatusDialogBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPaymentStatusDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNext.setOnClickListener {
            startActivity(Intent(requireActivity(), HistoryActivity::class.java))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        return dialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.FullScreenDialogTheme)
    }
}

