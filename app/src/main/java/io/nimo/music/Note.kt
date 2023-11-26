package io.nimo.music

data class Note(
    val name: String, // A, B etc
    val position: Int, // on the staff
    val fretPositions: List<Pair<Int,Int>> = emptyList(),
)