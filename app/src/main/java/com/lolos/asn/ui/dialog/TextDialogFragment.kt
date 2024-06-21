package com.lolos.asn.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.lolos.asn.R
import com.lolos.asn.databinding.FragmentTextDialogBinding

class TextDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentTextDialogBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTextDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tryoutStatus = arguments?.getString(TRYOUT_STATUS)

        if (tryoutStatus == "Failed") {
            binding.tvTitle.text = getString(R.string.keep_spirit)
            binding.tvText.text = getString(R.string.failed_text_spirit)
        } else {
            binding.tvTitle.text = getString(R.string.good_result)
            binding.tvText.text = getString(R.string.good_result_text)
        }

        binding.btnOk.setOnClickListener {
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    companion object {
        private const val TRYOUT_STATUS = "arg_data"

        fun newInstance(tryoutStatus: String?): TextDialogFragment {
            val fragment = TextDialogFragment()
            val args = Bundle()
            args.putString(TRYOUT_STATUS, tryoutStatus)
            fragment.arguments = args
            return fragment
        }
    }
}