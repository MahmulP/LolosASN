package com.lolos.asn.data.viewmodel.model

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lolos.asn.data.response.DrillingFinishResponse
import com.lolos.asn.data.response.DrillingRequest
import com.lolos.asn.data.response.DrillingStartResponse
import com.lolos.asn.data.response.LatContentItemItem
import com.lolos.asn.data.retrofit.ApiConfig
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DrillingStartViewModel: ViewModel() {
    private val _drillingData = MutableLiveData<DrillingStartResponse?>()
    val drillingData: LiveData<DrillingStartResponse?> = _drillingData

    private val _examDrilling = MutableLiveData<List<List<LatContentItemItem>>>()
    val examDrilling: LiveData<List<List<LatContentItemItem>>> = _examDrilling

    private val _currentQuestionIndex = MutableLiveData<Int>()
    val currentQuestionIndex: LiveData<Int> get() = _currentQuestionIndex

    private val _questionHandicapIndex = MutableLiveData<Int>()
    val questionHandicapIndex: LiveData<Int> = _questionHandicapIndex

    private val _remainingTime = MutableLiveData<String>()
    val remainingTime: LiveData<String> get() = _remainingTime

    private val _remainingWaktu = MutableLiveData<Int>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    private val _isFinish = MutableLiveData<Boolean>()
    val isFinish: LiveData<Boolean> = _isFinish

    private val _isCorrect = MutableLiveData<Boolean>()
    val isCorrect: LiveData<Boolean> = _isCorrect

    private val _selectedAnswers = MutableLiveData<MutableMap<Int, Int>>()
    val selectedAnswers: LiveData<MutableMap<Int, Int>> = _selectedAnswers

    private var countdownJob: Job? = null

    private val _drillingRequest = MutableLiveData<DrillingRequest>()
    val drillingRequest: LiveData<DrillingRequest> = _drillingRequest

    private var totalMudah: Int = 0
    private var totalSedang: Int = 0
    private var totalSusah: Int = 0
    private var totalSalah: Int = 0

    private val previousAnswers = mutableMapOf<Int, Int>()

    init {
        _currentQuestionIndex.value = 0
        _questionHandicapIndex.value = 0
        _selectedAnswers.value = mutableMapOf()
        initializeDrillingRequest()
    }

    private fun initializeDrillingRequest() {
        val initialDrillingRequest = DrillingRequest(
            totalMudah = 0,
            totalSedang = 0,
            totalSusah = 0,
            totalKosong = 10,
            totalSalah = 0,
            totalBenar = 0,
            totalPengerjaan = 0
        )

        _drillingRequest.value = initialDrillingRequest
    }

    fun startDrilling(latsolId: String?, token: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().startDrilling(latsolId, token)
        client.enqueue(object : Callback<DrillingStartResponse> {
            override fun onResponse(
                call: Call<DrillingStartResponse>,
                response: Response<DrillingStartResponse>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    val responseBody = response.body()
                    _drillingData.value = responseBody
                    _examDrilling.value = responseBody?.data?.latContent
                }
            }

            override fun onFailure(call: Call<DrillingStartResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
                _isLoading.value = false
            }
        })
    }

    fun finishDrilling(userId: String?, latsolId: String?, drillingRequest: DrillingRequest, token: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().finishDrilling(userId = userId, latsolId = latsolId, request = drillingRequest, token = token)
        client.enqueue(object: Callback<DrillingFinishResponse> {
            override fun onResponse(call: Call<DrillingFinishResponse>, response: Response<DrillingFinishResponse>) {
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

            override fun onFailure(call: Call<DrillingFinishResponse>, t: Throwable) {
                _isLoading.value = false
                _isFinish.value = false
                _message.value = t.message
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun nextQuestion() {
        val index = _questionHandicapIndex.value ?: return
        if (index < (_currentQuestionIndex.value?.let { _examDrilling.value?.getOrNull(it)?.size }
                ?: 0) - 1) {
            _questionHandicapIndex.value = index + 1
        }
    }

    fun addQuestionIndex() {
        val index = _currentQuestionIndex.value ?: return
        if (index < 2) {
            _currentQuestionIndex.value = index + 1
        }
    }

    fun decreaseQuestionIndex() {
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

    fun calculateScores(questionIndex: Int, handicapIndex: Int, optionIndex: Int): DrillingRequest {
        val contentItem = _examDrilling.value?.get(questionIndex)?.get(handicapIndex) ?: return DrillingRequest()
        val drillingItem = _drillingData.value
        val waktu: Int = drillingItem?.data?.waktu ?: 0
        val remainingWaktu: Int = _remainingWaktu.value ?: 0

        // Get the previous answer for this question
        val previousAnswer = previousAnswers[handicapIndex]

        // Only update the score if the new answer is different from the previous answer
        if (previousAnswer != optionIndex) {
            if (previousAnswer != null) {
                if (previousAnswer == contentItem.jawaban) {
                    when (contentItem.handicap) {
                        1 -> {
                            totalMudah -= 1
                        }
                        2 -> {
                            totalSedang -= 1
                        }
                        else -> {
                            totalSusah -= 1
                        }
                    }
                } else {
                    totalSalah -= 1
                }
            }
            if (contentItem.jawaban == optionIndex) {
                when (contentItem.handicap) {
                    1 -> {
                        totalMudah += 1
                    }
                    2 -> {
                        totalSedang += 1
                    }
                    else -> {
                        totalSusah += 1
                    }
                }

                _isCorrect.value = true
            } else {
                totalSalah += 1

                _isCorrect.value = false
            }

            previousAnswers[handicapIndex] = optionIndex
        }

        val drillingRequestData = DrillingRequest(
            totalMudah = totalMudah,
            totalSedang = totalSedang,
            totalSusah = totalSusah,
            totalKosong = (_examDrilling.value?.get(questionIndex)?.size ?: 0) - totalMudah - totalSalah - totalSusah - totalSedang,
            totalSalah = totalSalah,
            totalBenar = totalMudah + totalSedang + totalSusah,
            totalPengerjaan = waktu - remainingWaktu
        )

        Log.d(TAG, "calculateScores - DrillingRequest: $drillingRequestData")
        Log.d(TAG, "calculateScores - Total Mudah: $totalMudah, Total Sedang: $totalSedang, TotalSusah: $totalSusah")
        Log.d(TAG, "iscorrect: ${_isCorrect.value}")
        Log.d(TAG, "index: ${_currentQuestionIndex.value}")

        _drillingRequest.value = drillingRequestData

        return drillingRequestData
    }


    fun isAnswerFilled(questionIndex: Int): Boolean {
        return selectedAnswers.value?.containsKey(questionIndex) == true
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
                _remainingWaktu.value = time
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
        private const val TAG = "DrillingStartViewModel"
    }
}