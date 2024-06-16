package com.lolos.asn.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.lolos.asn.R
import com.lolos.asn.adapter.DrillingExamAnswerAdapter
import com.lolos.asn.adapter.DrillingExamNumberAdapter
import com.lolos.asn.data.data.UserData
import com.lolos.asn.data.preference.UserPreferences
import com.lolos.asn.data.preference.userPreferencesDataStore
import com.lolos.asn.data.response.DrillingRequest
import com.lolos.asn.data.response.DrillingStartResponse
import com.lolos.asn.data.response.LatContentItemItem
import com.lolos.asn.data.viewmodel.factory.AuthViewModelFactory
import com.lolos.asn.data.viewmodel.model.AuthViewModel
import com.lolos.asn.data.viewmodel.model.DrillingStartViewModel
import com.lolos.asn.data.viewmodel.model.DrillingViewModel
import com.lolos.asn.databinding.ActivityDrillingExamBinding
import com.lolos.asn.ui.dialog.InfoDialogFragment
import com.lolos.asn.ui.dialog.ValidationDrillingFinishFragment

class DrillingExamActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDrillingExamBinding

    private val drillingStartViewModel by viewModels<DrillingStartViewModel>()
    private val drillingViewModel by viewModels<DrillingViewModel>()
    private val authViewModel: AuthViewModel by viewModels {
        val pref = UserPreferences.getInstance(this.userPreferencesDataStore)
        AuthViewModelFactory(pref)
    }

    private var currentRequest: DrillingRequest? = null
    private var currentDrillingData: DrillingStartResponse? = null
    private var currentUserData: UserData? = null
    private var authToken: String = "token"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDrillingExamBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val latsolId = intent.getStringExtra("latsol_id")

        drillingStartViewModel.examDrilling.observe(this) { questions ->
            if (questions.isNotEmpty()) {
                val index = drillingStartViewModel.currentQuestionIndex.value ?: 0
                val handicapIndex = drillingStartViewModel.questionHandicapIndex.value ?: 0
                updateQuestion(index, handicapIndex)
                setupNumberRecycleView(questions, index)
                setupAnswerRecycleView(questions, index, handicapIndex)

                drillingStartViewModel.drillingData.observe(this) {
                    val time = it?.data?.waktu
                    if (time != null) {
                        drillingStartViewModel.startCountdownIfNeeded(time)
                    }
                }
            }
        }

        drillingStartViewModel.drillingRequest.observe(this) { request ->
            if (request != null) {
                currentRequest = request
            }
        }

        drillingStartViewModel.drillingData.observe(this) { drillingData ->
            currentDrillingData = drillingData
        }

        drillingStartViewModel.isFinish.observe(this) { result ->
            if (result) {
                var historyLatId: String?
                drillingViewModel.getHistoryDrilling(latsolId = latsolId, userId = currentUserData?.userId, token = "Bearer ${currentUserData?.token}")

                drillingViewModel.drillingHistory.observe(this) { response ->
                    val lastHistoryLatId = response?.data?.filterNotNull()?.lastOrNull()?.historyLatId
                    historyLatId = lastHistoryLatId

                    val intent = Intent(this, DrillingResultActivity::class.java).apply {
                        putExtra("latHistory_id", historyLatId)
                        putExtra("navigate_from", "exam")
                    }

                    startActivity(intent)
                }
            }
        }

        authViewModel.getAuthUser().observe(this) { userData ->
            currentUserData = userData
            if (userData.token != null) {
                authToken = "Bearer ${userData.token}"
                drillingStartViewModel.startDrilling(latsolId, authToken)
            }
        }

        drillingStartViewModel.remainingTime.observe(this) { remainingTime ->
            val request = currentRequest
            val userData = currentUserData
            val token = authToken

            if (remainingTime == "00:00:01") {
                if (request != null && latsolId != null && userData != null) {
                    drillingStartViewModel.finishDrilling(userId = userData.userId, latsolId = latsolId, drillingRequest = request, token = token)
                }
            } else {
                binding.tvTime.text = remainingTime
            }
        }

        binding.ibInfo.setOnClickListener {
            val dialog = InfoDialogFragment()
            dialog.show(supportFragmentManager, "ValidationDialogFragment")
        }

        drillingStartViewModel.currentQuestionIndex.observe(this) { index ->
            drillingStartViewModel.questionHandicapIndex.observe(this) { handicapIndex ->
                updateQuestion(index, handicapIndex)
            }
        }

        binding.tvScore.text = "+3"

        binding.btnNext.setOnClickListener {
            val secondArray = drillingStartViewModel.examDrilling.value?.getOrNull(1) ?: return@setOnClickListener
            val secondArraySize = secondArray.size

            val observer = Observer<Boolean> { isCorrect ->
                if (isCorrect) {
                    drillingStartViewModel.addQuestionIndex()
                } else {
                    drillingStartViewModel.decreaseQuestionIndex()
                }
            }

            drillingStartViewModel.isCorrect.observe(this, observer)
            drillingStartViewModel.isCorrect.removeObserver(observer)

            if ((drillingStartViewModel.questionHandicapIndex.value ?: 0) < secondArraySize - 1) {
                drillingStartViewModel.nextQuestion()
                val index = drillingStartViewModel.currentQuestionIndex.value ?: 0
                val handicapIndex = drillingStartViewModel.questionHandicapIndex.value ?: 0
                updateScore(index, handicapIndex)
                drillingStartViewModel.examDrilling.observe(this) { questions ->
                    setupAnswerRecycleView(questions, index, handicapIndex)
                    setupNumberRecycleView(questions, index)

                    binding.rvNumber.scrollToPosition(handicapIndex)
                }
            } else {
                val dialog = ValidationDrillingFinishFragment.newInstance(latsolId)
                dialog.show(supportFragmentManager, "ValidationDialogFragment")
            }

            binding.scrollviewLayout.scrollTo(0, 0)
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val dialog = ValidationDrillingFinishFragment.newInstance(latsolId)
                dialog.show(supportFragmentManager, "ValidationDialogFragment")
            }
        })
    }

    @SuppressLint("SetTextI18n")
    fun updateQuestion(index: Int, handicapIndex: Int) {
        val question = drillingStartViewModel.examDrilling.value?.getOrNull(index)?.getOrNull(handicapIndex)

        if (question != null) {
            binding.tvNomorSoal.text = "Soal ${handicapIndex + 1}"
            binding.tvSoal.text = question.question
            binding.toolbar.title = if (question.category == 1) {
                getString(R.string.tiu)
            } else if (question.category == 2) {
                getString(R.string.twk)
            } else {
                getString(R.string.tkp)
            }

            val questionsList = drillingStartViewModel.examDrilling.value?.getOrNull(1) ?: return
//            setupAnswerRecycleView(questionsList, index)

            if ((drillingStartViewModel.questionHandicapIndex.value ?: 0) < (questionsList.size - 1)) {
                binding.btnNext.text = "Soal ${handicapIndex + 2}"
            } else {
                binding.btnNext.text = getString(R.string.done_text)
            }

            if (!question.question.isNullOrEmpty()) {
                binding.tvSoal.visibility = View.VISIBLE
            } else {
                binding.tvSoal.visibility = View.GONE
            }

            // Load question image if available
            if (question.questionImage != null) {
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

    private fun updateScore(index: Int, handicapIndex: Int) {
        val question = drillingStartViewModel.examDrilling.value?.getOrNull(index)?.getOrNull(handicapIndex)
        if (question != null) {
            if (question.handicap == 1) {
                binding.tvScore.text = getString(R.string.score_easy)
            } else if (question.handicap == 2) {
                binding.tvScore.text = getString(R.string.score_medium)
            } else {
                binding.tvScore.text = getString(R.string.score_hard)
            }
        }
    }

    private fun setupAnswerRecycleView(tryoutContentItems: List<List<LatContentItemItem>>, currentQuestionIndex: Int, handicapIndex: Int) {
        val selectedAnswer = drillingStartViewModel.getSelectedAnswer(handicapIndex)
        val adapter = DrillingExamAnswerAdapter(this, drillingStartViewModel)

        // Set up RecyclerView once
        binding.rvOpsi.layoutManager = LinearLayoutManager(this)
        binding.rvOpsi.adapter = adapter

        tryoutContentItems.let {
            val options = it.getOrNull(currentQuestionIndex)?.getOrNull(handicapIndex)?.option ?: emptyList()
            adapter.setOptions(options, selectedAnswer)
        }
    }

    private fun setupNumberRecycleView(contentItems: List<List<LatContentItemItem>>, index: Int) {
        binding.rvNumber.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val adapter = DrillingExamNumberAdapter(this, drillingStartViewModel)
        binding.rvNumber.adapter = adapter

        contentItems.getOrNull(index).let {
            adapter.submitList(it)
        }
    }
}