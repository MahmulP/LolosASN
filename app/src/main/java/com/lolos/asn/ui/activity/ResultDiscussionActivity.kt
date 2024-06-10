package com.lolos.asn.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.lolos.asn.R
import com.lolos.asn.adapter.DiscussionAnswerAdapter
import com.lolos.asn.adapter.DiscussionNumberAdapter
import com.lolos.asn.data.data.UserData
import com.lolos.asn.data.preference.UserPreferences
import com.lolos.asn.data.preference.userPreferencesDataStore
import com.lolos.asn.data.response.ExaminationResponse
import com.lolos.asn.data.response.TryoutContentItem
import com.lolos.asn.data.response.TryoutRequest
import com.lolos.asn.data.viewmodel.factory.AuthViewModelFactory
import com.lolos.asn.data.viewmodel.model.AuthViewModel
import com.lolos.asn.data.viewmodel.model.ExaminationViewModel
import com.lolos.asn.databinding.ActivityResultDiscussionBinding
import com.lolos.asn.ui.dialog.NumberDiscussionDialogFragment

class ResultDiscussionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultDiscussionBinding
    private val examinationViewModel by viewModels<ExaminationViewModel>()
    private val authViewModel: AuthViewModel by viewModels {
        val pref = UserPreferences.getInstance(this.userPreferencesDataStore)
        AuthViewModelFactory(pref)
    }

    private var currentRequest: TryoutRequest? = null
    private var currentTryoutData: ExaminationResponse? = null
    private var currentUserData: UserData? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultDiscussionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val tryoutId = intent.getStringExtra("tryout_id")

        examinationViewModel.startTryout(tryoutId)
        examinationViewModel.examTryout.observe(this) { questions ->
            if (questions.isNotEmpty()) {
                val index = examinationViewModel.currentQuestionIndex.value ?: 0
                updateQuestion(examinationViewModel.currentQuestionIndex.value ?: 0)
                setupNumberRecycleView(questions)
                setupAnswerRecycleView(questions, index)

                binding.cvPembahasan.visibility = View.VISIBLE
            }
        }

        examinationViewModel.tryoutRequest.observe(this) { request ->
            currentRequest = request
        }

        examinationViewModel.tryoutData.observe(this) { tryoutData ->
            currentTryoutData = tryoutData
        }

        authViewModel.getAuthUser().observe(this) { userData ->
            currentUserData = userData
        }

        examinationViewModel.currentQuestionIndex.observe(this) { index ->
            updateQuestion(index)
        }

        binding.btnNext.setOnClickListener {
            val questionsList = examinationViewModel.examTryout.value ?: return@setOnClickListener
            if ((examinationViewModel.currentQuestionIndex.value ?: 0) < questionsList.size - 1) {
                examinationViewModel.nextQuestion()
                val index = examinationViewModel.currentQuestionIndex.value ?: 0
                examinationViewModel.examTryout.observe(this) { questions ->
                    setupAnswerRecycleView(questions, index)
                    setupNumberRecycleView(questions)
                }
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
            val dialog = NumberDiscussionDialogFragment()
            dialog.show(supportFragmentManager, "NumberDialogFragment")
            examinationViewModel.examTryout.observe(this) { questions ->
                setupNumberRecycleView(questions)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupNumberRecycleView(tryoutContentItems: List<TryoutContentItem>) {
        binding.rvNumber.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val adapter = DiscussionNumberAdapter(this, examinationViewModel)
        binding.rvNumber.adapter = adapter

        adapter.submitList(tryoutContentItems)
    }

    private fun setupAnswerRecycleView(tryoutContentItems: List<TryoutContentItem>, currentQuestionIndex: Int) {
        val adapter = DiscussionAnswerAdapter()

        // Set up RecyclerView once
        binding.rvOpsi.layoutManager = LinearLayoutManager(this)
        binding.rvOpsi.adapter = adapter

        tryoutContentItems.let {
            val options = it.getOrNull(currentQuestionIndex)?.option ?: emptyList()
            val selectedAnswer = it.getOrNull(currentQuestionIndex)?.jawaban ?: -1
            adapter.setOptions(options, selectedAnswer)
        }
    }


    @SuppressLint("SetTextI18n")
    fun updateQuestion(index: Int) {
        val question = examinationViewModel.examTryout.value?.get(index)
        val questions = examinationViewModel.examTryout.value

        if (questions != null) {
            if (index == questions.size - 1) {
                binding.btnNext.visibility = View.INVISIBLE
            } else {
                binding.btnNext.visibility = View.VISIBLE
            }
        }

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

            val rightAnswer = when (question.jawaban) {
                0 -> "A"
                1 -> "B"
                2 -> "C"
                3 -> "D"
                else -> "E"
            }

            if (question.category != 3) {
                binding.tvRightAnswer.text = getString(R.string.right_answer, rightAnswer)
                binding.tvRightAnswer.visibility = View.VISIBLE
            } else {
                binding.tvRightAnswer.visibility = View.GONE
            }

            binding.tvRightAnswerDescription.text = question.pembahasan

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
}