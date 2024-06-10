package com.lolos.asn.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.lolos.asn.R
import com.lolos.asn.adapter.DialogNumberDiscussionAdapter
import com.lolos.asn.data.response.TryoutContentItem
import com.lolos.asn.data.viewmodel.model.ExaminationViewModel
import com.lolos.asn.databinding.FragmentNumberDiscussionDialogBinding

class NumberDiscussionDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentNumberDiscussionDialogBinding
    private val examinationViewModel: ExaminationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNumberDiscussionDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = binding.toolbar
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            dismiss()
        }

        examinationViewModel.examTryout.observe(viewLifecycleOwner) { questions ->
            val twkCategory = questions.filter { it.category == 2 }
            val tiuCategory = questions.filter { it.category == 1 }
            val tkpCategory = questions.filter { it.category == 3 }

            if (twkCategory.isNotEmpty()) {
                setupTwkRecycleView(twkCategory)
            } else {
                binding.tvTwk.visibility = View.GONE
            }

            if (tiuCategory.isNotEmpty()) {
                setupTiuRecycleView(tiuCategory)
            } else {
                binding.tvTiu.visibility = View.GONE
            }

            if (tkpCategory.isNotEmpty()) {
                setupTkpRecycleView(tkpCategory)
            } else {
                binding.tvTkp.visibility = View.GONE
            }
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

    private fun setupTwkRecycleView(tryoutContentItems: List<TryoutContentItem>) {
        binding.rvNumberTwk.layoutManager = GridLayoutManager(requireContext(), 5)
        val adapter = DialogNumberDiscussionAdapter(examinationViewModel, this)
        binding.rvNumberTwk.adapter = adapter

        adapter.submitList(tryoutContentItems)
    }

    private fun setupTiuRecycleView(tryoutContentItems: List<TryoutContentItem>) {
        binding.rvNumberTiu.layoutManager = GridLayoutManager(requireContext(), 5)
        val adapter = DialogNumberDiscussionAdapter(examinationViewModel, this)
        binding.rvNumberTiu.adapter = adapter

        adapter.submitList(tryoutContentItems)
    }

    private fun setupTkpRecycleView(tryoutContentItems: List<TryoutContentItem>) {
        binding.rvNumberTkp.layoutManager = GridLayoutManager(requireContext(), 5)
        val adapter = DialogNumberDiscussionAdapter(examinationViewModel, this)
        binding.rvNumberTkp.adapter = adapter

        adapter.submitList(tryoutContentItems)
    }
}

