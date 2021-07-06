package com.example.wordbuilder.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query


@Dao
interface WordDao {
    @Query("SELECT * FROM words_rus")
    fun getAll(): LiveData<List<WordEntity>>

    @Query("SELECT * FROM words_rus WHERE id= :id")
    fun getById(id: Int): WordEntity

    @Query("SELECT word FROM words_rus WHERE word IN (:words) ")
    suspend fun getMatches(words: List<String>): List<String>
}