package com.example.wordbuilder.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.wordbuilder.db.AppDb
import com.example.wordbuilder.model.LoadingStateModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class WordViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDb.getInstance(application).dao()

    private val _wordList = MutableLiveData<List<String>>()
    val wordList: LiveData<List<String>>
        get() = _wordList

    private val _dataState = MutableLiveData<LoadingStateModel>()
    val dataState: LiveData<LoadingStateModel>
    get() = _dataState

    fun getMatchingWords(word: String) {
        viewModelScope.launch {
            // invalidate dataState data
            _dataState.value = LoadingStateModel()
            //start loading
            _dataState.value = LoadingStateModel(isLoading = true)

            val wordCombinations = calculateWordVariants(word)
            // chunkes of 999 - max value of item for db to process at once
            val chunkedCombinations = wordCombinations.chunked(999)
            //sort the final list alphabetically
            _wordList.value = checkDb(chunkedCombinations).sorted()
            _dataState.value = LoadingStateModel(isLoading = false)
            // if we get no possible combinations, tell about it to observer
            if (_wordList.value.isNullOrEmpty()) {
                _dataState.value = LoadingStateModel(isEmptyResponse = true)
            }
        }

    }

    private suspend fun checkDb(words: List<List<String>>): List<String> =
        withContext(Dispatchers.IO) {
            val matchingWords = emptyList<String>().toMutableList()

            for (list in words) {
                matchingWords += dao.getMatches(list)
            }
            matchingWords

        }

    private suspend fun calculateWordVariants(word: String): List<String> =
        withContext(Dispatchers.Main) {

            val allCombinations = MutableList<String>(0) { "" }

            val origArray = word.map { it.toString() }.toTypedArray()

            // basically tells how many times to run the algorithm for each letter
            // in the original word, i.e. dictates of how many letters the final
            // combinations will consist of
            val times = word.length

            var arrClone = origArray


            // Calculate all possible lengths
            for (z in 0 until times - 1) {

                // stores all sorting combinations
                val sortedList = Vector<String>()

                //get chars of original list one by one
                for (origItem in origArray) {
                    // and compare them with those of in clone arr
                    for (cloneItem in arrClone) {
                        // if they didn't match, concatenate
                        if (origItem != cloneItem) {
                            // again, all possible combinations are stored here
                            sortedList.add(cloneItem + origItem)
                        }
                    }
                }
                arrClone = sortedList.toArray(Array<String>(sortedList.size) { "" })
                allCombinations.addAll(arrClone)
            }
            Log.i("ViewModel", "TEST: finished sorting")
            allCombinations
        }

    fun clearData() {
        _wordList.value = listOf("")
    }
}