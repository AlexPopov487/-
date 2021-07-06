package com.example.wordbuilder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.wordbuilder.adapter.WordAdapter
import com.example.wordbuilder.databinding.FragmentMainScreenBinding
import com.example.wordbuilder.utils.hideKeyboard
import com.example.wordbuilder.viewModel.WordViewModel
import com.google.android.material.snackbar.Snackbar

class MainScreenFragment: Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMainScreenBinding.inflate(inflater, container, false)

        val viewModel: WordViewModel by viewModels()

        val adapter = WordAdapter()
        binding.rVResponse.adapter = adapter

        binding.bTSearch.setOnClickListener {
            val wordToQuery = binding.eTWord.text.toString().trim()
            it.hideKeyboard()
            if (wordToQuery.length >= 7) {
                Snackbar.make(binding.root, getString(R.string.snackBar_word_too_long_to_calculate), Snackbar.LENGTH_LONG).show()
                viewModel.clearData()
                return@setOnClickListener
            }
            viewModel.getMatchingWords(wordToQuery)
        }

        viewModel.wordList.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }

        viewModel.dataState.observe(viewLifecycleOwner){ state ->
            binding.progressBar.isVisible = state.isLoading
            binding.tVEmptyResponse.isVisible = state.isEmptyResponse
        }

        return binding.root
    }
}