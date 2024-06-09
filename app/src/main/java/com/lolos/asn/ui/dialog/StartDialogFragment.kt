package com.lolos.asn.ui.dialog

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.lolos.asn.databinding.FragmentStartDialogBinding
import com.lolos.asn.ui.activity.ExaminationActivity

class StartDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentStartDialogBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStartDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tryoutId = arguments?.getString(TRYOUT_ID)
        val tryoutName = arguments?.getString(TRYOUT_NAME)

        binding.tvTitle.text = tryoutName

        binding.btnStart.setOnClickListener {
            val intent = Intent(requireContext(), ExaminationActivity::class.java)
            intent.putExtra("tryout_id", tryoutId)
            startActivity(intent)
            dismiss()
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }

    companion object {
        private const val TRYOUT_ID = "arg_data"
        private const val TRYOUT_NAME = "tryout_name"

        fun newInstance(tryoutId: String?, tryoutName: String?): StartDialogFragment {
            val fragment = StartDialogFragment()
            val args = Bundle()
            args.putString(TRYOUT_ID, tryoutId)
            args.putString(TRYOUT_NAME, tryoutName)
            fragment.arguments = args
            return fragment
        }
    }
}