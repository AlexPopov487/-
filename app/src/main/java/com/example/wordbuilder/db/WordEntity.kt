package com.example.wordbuilder.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "words_rus")
data class WordEntity(
    @PrimaryKey(autoGenerate = true) @NotNull @ColumnInfo(name = "id") val id: Int = 0,
    @NotNull @ColumnInfo(name = "word") val word: String = "",

    )
