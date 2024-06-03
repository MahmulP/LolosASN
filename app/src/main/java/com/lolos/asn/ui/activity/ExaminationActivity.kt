package com.lolos.asn.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.lolos.asn.R
import com.lolos.asn.adapter.ExaminationAnswerAdapter
import com.lolos.asn.adapter.ExaminationNumberAdapter
import com.lolos.asn.data.response.TryoutContentItem
import com.lolos.asn.data.viewmodel.model.ExaminationViewModel
import com.lolos.asn.databinding.ActivityExaminationBinding
import com.lolos.asn.ui.dialog.NumberDialogFragment
import com.lolos.asn.ui.dialog.ValidationDialogFragment

class ExaminationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExaminationBinding
    val examinationViewModel by viewModels<ExaminationViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExaminationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tryoutId = intent.getStringExtra("tryout_id")
        examinationViewModel.startTryout(tryoutId)
        examinationViewModel.examTryout.observe(this) { questions ->
            if (questions.isNotEmpty()) {
                val index = examinationViewModel.currentQuestionIndex.value ?: 0
                updateQuestion(examinationViewModel.currentQuestionIndex.value ?: 0)
                setupNumberRecycleView(questions)
                setupAnswerRecycleView(questions, index)

                examinationViewModel.tryoutData.observe(this) {
                    val time = it.data.tryoutDuration
                    examinationViewModel.startCountdownIfNeeded(time)
                }
            }
        }

        examinationViewModel.remainingTime.observe(this) { remainingTime ->
            if (remainingTime == "00:00:00") {
                startActivity(Intent(this, ResultActivity::class.java))
                finish()
            } else {
                binding.tvTime.text = remainingTime
            }
        }

        examinationViewModel.currentQuestionIndex.observe(this) { index ->
            updateQuestion(index)
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val dialog = ValidationDialogFragment()
                dialog.show(supportFragmentManager, "ValidationDialogFragment")
            }
        })

        binding.btnNext.setOnClickListener {
            val questionsList = examinationViewModel.examTryout.value ?: return@setOnClickListener
            if ((examinationViewModel.currentQuestionIndex.value ?: 0) < questionsList.size - 1) {
                examinationViewModel.nextQuestion()
                val index = examinationViewModel.currentQuestionIndex.value ?: 0
                examinationViewModel.examTryout.observe(this) { questions ->
                    setupAnswerRecycleView(questions, index)
                    setupNumberRecycleView(questions)
                }
            } else {
                finishQuiz()
            }
        }

        binding.btnPrevious.setOnClickListener {
            examinationViewModel.previousQuestion()
            val index = examinationViewModel.currentQuestionIndex.value ?: 0
            examinationViewModel.examTryout.observe(this) { questions ->
                setupAnswerRecycleView(questions, index)
                setupNumberRecycleView(questions)
            }
        }


        binding.btnCheckAll.setOnClickListener {
            val dialog = NumberDialogFragment()
            dialog.show(supportFragmentManager, "NumberDialogFragment")
            examinationViewModel.examTryout.observe(this) { questions ->
                setupNumberRecycleView(questions)
            }
        }
    }

    private fun setupNumberRecycleView(tryoutContentItems: List<TryoutContentItem>) {
        binding.rvNumber.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val adapter = ExaminationNumberAdapter(this, examinationViewModel)
        binding.rvNumber.adapter = adapter

        adapter.submitList(tryoutContentItems)
    }

    fun setupAnswerRecycleView(tryoutContentItems: List<TryoutContentItem>, currentQuestionIndex: Int) {
        val selectedAnswer = examinationViewModel.getSelectedAnswer(currentQuestionIndex)
        val adapter = ExaminationAnswerAdapter(this, examinationViewModel)

        // Set up RecyclerView once
        binding.rvOpsi.layoutManager = LinearLayoutManager(this)
        binding.rvOpsi.adapter = adapter

        tryoutContentItems.let {
            val options = it.getOrNull(currentQuestionIndex)?.option ?: emptyList()
            adapter.setOptions(options, selectedAnswer)
        }
    }


    @SuppressLint("SetTextI18n")
    fun updateQuestion(index: Int) {
        val question = examinationViewModel.examTryout.value?.get(index)
        if (question != null) {
            if (index == 0) {
                binding.btnPrevious.visibility = View.INVISIBLE
            } else {
                binding.btnPrevious.visibility = View.VISIBLE
            }

            binding.tvNomorSoal.text = "Soal ${index + 1}"
            binding.btnPrevious.text = "Soal ${index}"
            binding.tvSoal.text = question.question
            binding.toolbar.title = if (question.category == 1) {
                getString(R.string.twk)
            } else if (question.category == 2) {
                getString(R.string.tiu)
            } else {
                getString(R.string.tkp)
            }

            val questionsList = examinationViewModel.examTryout.value ?: return
            setupAnswerRecycleView(questionsList, index)

            if ((examinationViewModel.currentQuestionIndex.value ?: 0) < (questionsList.size - 1)) {
                binding.btnNext.text = "Soal ${index + 2}"
            } else {
                binding.btnNext.text = getString(R.string.done_text)
            }

            // Load question image if available
            if (!question.questionImage.isNullOrEmpty()) {
                binding.ivSoal.visibility = View.VISIBLE

                val constraintLayout = binding.root
                val constraintSet = ConstraintSet()
                constraintSet.clone(constraintLayout)
                constraintSet.connect(binding.tvSoal.id, ConstraintSet.TOP, binding.ivSoal.id, ConstraintSet.BOTTOM)
                constraintSet.applyTo(constraintLayout)

                Glide.with(this)
                    .load(question.questionImage)
                    .error(R.drawable.no_image)
                    .into(binding.ivSoal)
            } else {
                binding.ivSoal.setImageDrawable(null)
                binding.ivSoal.visibility = View.GONE

                val constraintLayout = binding.root
                val constraintSet = ConstraintSet()
                constraintSet.clone(constraintLayout)
                constraintSet.connect(binding.tvSoal.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
                constraintSet.applyTo(constraintLayout)
            }
        }
    }

    private fun finishQuiz() {
        val dialog = ValidationDialogFragment()
        dialog.show(supportFragmentManager, "ValidationDialogFragment")
    }
}