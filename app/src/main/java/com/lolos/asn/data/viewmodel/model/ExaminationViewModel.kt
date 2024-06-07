package com.lolos.asn.data.viewmodel.model

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lolos.asn.data.response.ExaminationResponse
import com.lolos.asn.data.response.FinishTryoutResponse
import com.lolos.asn.data.response.ListCategoryScoreItem
import com.lolos.asn.data.response.TryoutContentItem
import com.lolos.asn.data.response.TryoutRequest
import com.lolos.asn.data.retrofit.ApiConfig
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
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

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    private val _isFinish = MutableLiveData<Boolean>()
    val isFinish: LiveData<Boolean> = _isFinish

    private val _selectedAnswers = MutableLiveData<MutableMap<Int, Int>>()
    val selectedAnswers: LiveData<MutableMap<Int, Int>> = _selectedAnswers

    private var countdownJob: Job? = null

    private val _subCategoryScores = MutableLiveData<List<ListCategoryScoreItem>>()

    private val _tryoutRequest = MutableLiveData<TryoutRequest>()
    val tryoutRequest: LiveData<TryoutRequest> = _tryoutRequest

    private var tiuScore: Int = 0
    private var tiuWrong: Int = 0
    private var twkScore: Int = 0
    private var twkWrong: Int = 0
    private var tkpScore: Int = 0

    private val previousAnswers = mutableMapOf<Int, Int>()

    init {
        _currentQuestionIndex.value = 0
        _selectedAnswers.value = mutableMapOf()
        _subCategoryScores.value = mutableListOf()
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
                        _examTryout.value = content
                    }

                }
            }

            override fun onFailure(call: Call<ExaminationResponse>, t: Throwable) {
                Log.e("TryoutViewModel", "onFailure: ${t.message}")
            }
        })
    }

    fun finishTryout(userId: String?, tryoutId: String?, tryoutRequest: TryoutRequest) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().clearTryout(userId = userId, tryoutId = tryoutId, request = tryoutRequest)
        client.enqueue(object: Callback<FinishTryoutResponse> {
            override fun onResponse(call: Call<FinishTryoutResponse>, response: Response<FinishTryoutResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val message = responseBody.message
                        _message.value = message
                        _isFinish.value = true
                        Log.d(TAG, "onResponse: $message")
                    }
                    onCleared()
                } else {
                    val error = response.errorBody()?.string() ?: "Unknown error"
                    val jsonObject = JSONObject(error)
                    val message = jsonObject.getString("message")
                    _message.value = message
                    _isFinish.value = false
                    Log.e(TAG, "onFailure: $message")
                }
            }

            override fun onFailure(call: Call<FinishTryoutResponse>, t: Throwable) {
                _isLoading.value = false
                _isFinish.value = false
                _message.value = t.message
                Log.e(TAG, "onFailure: ${t.message}")
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

    fun calculateScores(questionIndex: Int, optionIndex: Int): TryoutRequest {
        val contentItem = _examTryout.value?.get(questionIndex) ?: return TryoutRequest()
        val subCategoryId = contentItem.subCategoryId.toInt()

        // Get the previous answer for this question
        val previousAnswer = previousAnswers[questionIndex]

        // Only update the score if the new answer is different from the previous answer
        if (previousAnswer != optionIndex) {
            when (contentItem.category) {
                1 -> { // TWK
                    if (previousAnswer != null) {
                        if (previousAnswer == contentItem.jawaban) {
                            twkScore -= 5
                        } else {
                            twkWrong -= 1
                        }
                    }
                    if (contentItem.jawaban == optionIndex) {
                        twkScore += 5
                    } else {
                        twkWrong += 1
                    }
                }
                2 -> { // TIU
                    if (previousAnswer != null) {
                        if (previousAnswer == contentItem.jawaban) {
                            tiuScore -= 5
                        } else {
                            tiuWrong -= 1
                        }
                    }
                    if (contentItem.jawaban == optionIndex) {
                        tiuScore += 5
                    } else {
                        tiuWrong += 1
                    }
                }
                3 -> { // TKP
                    if (previousAnswer != null) {
                        tkpScore -= contentItem.jawabanTkp.getOrNull(previousAnswer) ?: 0
                    }
                    val tkpValue = contentItem.jawabanTkp.getOrNull(optionIndex) ?: 0
                    tkpScore += tkpValue
                }
            }

            // Update the subcategory score
            val updatedSubCategoryScores = _subCategoryScores.value?.toMutableList() ?: mutableListOf()
            val existingItemIndex = updatedSubCategoryScores.indexOfFirst { it.subCategoryId == subCategoryId }
            if (existingItemIndex != -1) {
                val currentScore = updatedSubCategoryScores[existingItemIndex].subCategoryScore
                val scoreChange = if (previousAnswer == contentItem.jawaban) {
                    -5
                } else if (contentItem.jawaban == optionIndex) {
                    5
                } else {
                    0
                }
                updatedSubCategoryScores[existingItemIndex] = ListCategoryScoreItem(
                    subCategoryId = subCategoryId,
                    subCategoryScore = currentScore + scoreChange
                )
            } else {
                val scoreChange = if (contentItem.jawaban == optionIndex) 5 else 0
                updatedSubCategoryScores.add(
                    ListCategoryScoreItem(
                        subCategoryId = subCategoryId,
                        subCategoryScore = scoreChange
                    )
                )
            }
            _subCategoryScores.value = updatedSubCategoryScores

            // Update the previous answer for this question
            previousAnswers[questionIndex] = optionIndex
        }

        val tryoutRequestData = TryoutRequest(
            twkWrong = twkWrong,
            twkScore = twkScore,
            tiuScore = tiuScore,
            tkpScore = tkpScore,
            listCategoryScore = _subCategoryScores.value ?: emptyList(),
            tiuWrong = tiuWrong,
            tryoutScore = tiuScore + tkpScore + twkScore
        )

        Log.d("TryoutViewModel", "calculateScores - TryoutRequest: $tryoutRequestData")
        Log.d("TryoutViewModel", "calculateScores - TIU Score: $tiuScore, TWK Score: $twkScore, TKP Score: $tkpScore")
        Log.d("TryoutViewModel", "calculateScores - ListCategoryScore: ${_subCategoryScores.value}")

        _tryoutRequest.value = tryoutRequestData

        return tryoutRequestData
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

    companion object {
        private const val TAG = "ExaminationViewModel"
    }
}