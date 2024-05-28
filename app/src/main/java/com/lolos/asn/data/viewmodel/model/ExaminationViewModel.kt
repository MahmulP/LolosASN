package com.lolos.asn.data.viewmodel.model

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lolos.asn.data.response.ExaminationResponse
import com.lolos.asn.data.response.TryoutContentItem
import com.lolos.asn.data.retrofit.ApiConfig
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExaminationViewModel: ViewModel() {
    private val _tryoutData = MutableLiveData<ExaminationResponse>()
    val tryoutData: LiveData<ExaminationResponse> = _tryoutData

    private val _examTryout = MutableLiveData<List<TryoutContentItem>>()
    val examTryout: LiveData<List<TryoutContentItem>> get() = _examTryout

    private val _currentQuestionIndex = MutableLiveData<Int>()
    val currentQuestionIndex: LiveData<Int> get() = _currentQuestionIndex

    private val _remainingTime = MutableLiveData<String>()
    val remainingTime: LiveData<String> get() = _remainingTime

    private val _score = MutableLiveData<List<String>>()
    val score: LiveData<List<String>> = _score

    private val _selectedAnswers = MutableLiveData<MutableMap<Int, Int>>()
    val selectedAnswers: LiveData<MutableMap<Int, Int>> = _selectedAnswers

    private var countdownJob: Job? = null

    init {
        _currentQuestionIndex.value = 0
        _selectedAnswers.value = mutableMapOf()
    }

    fun startTryout(tryoutId: String?) {
        val client = ApiConfig.getApiService().startTryout(tryoutId)
        client.enqueue(object : Callback<ExaminationResponse> {
            override fun onResponse(
                call: Call<ExaminationResponse>,
                response: Response<ExaminationResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    _tryoutData.value = responseBody?.let {
                        it
                    }
                    responseBody?.data?.tryoutContent?.let { content ->
                        _examTryout.value = content.filterNotNull()
                    }
                }
            }

            override fun onFailure(call: Call<ExaminationResponse>, t: Throwable) {
                Log.e("TryoutViewModel", "onFailure: ${t.message}")
            }
        })
    }

    fun nextQuestion() {
        val index = _currentQuestionIndex.value ?: return
        if (index < (_examTryout.value?.size ?: 0) - 1) {
            _currentQuestionIndex.value = index + 1
        }
    }

    fun previousQuestion() {
        val index = _currentQuestionIndex.value ?: return
        if (index > 0) {
            _currentQuestionIndex.value = index - 1
        }
    }

    fun selectAnswer(questionIndex: Int, answerIndex: Int) {
        selectedAnswers.value?.put(questionIndex, answerIndex)
        Log.d("SelectedAnswer", "selectAnswer: ${selectedAnswers.value}")
    }

    fun getSelectedAnswer(questionIndex: Int): Int? {
        return selectedAnswers.value?.get(questionIndex)
    }

    fun isAnswerFilled(questionIndex: Int): Boolean {
        return selectedAnswers.value?.containsKey(questionIndex) == true
    }

    fun updatePosition(questionIndex: Int) {
        _currentQuestionIndex.value = questionIndex
    }

    @SuppressLint("DefaultLocale")
    fun startCountdown(totalSeconds: Int) {
        countdownJob?.cancel()
        countdownJob = viewModelScope.launch {
            for (time in totalSeconds downTo 0) {
                val hours = time / 3600
                val minutes = (time % 3600) / 60
                val seconds = time % 60
                _remainingTime.value = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                delay(1000L)
            }
        }
    }

    fun startCountdownIfNeeded(totalSeconds: Int) {
        if (countdownJob == null) {
            startCountdown(totalSeconds)
        }
    }

    override fun onCleared() {
        super.onCleared()
        countdownJob?.cancel()
    }

}