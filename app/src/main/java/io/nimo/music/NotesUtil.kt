package io.nimo.music

import kotlin.random.Random

class NotesUtil {

    val notes = listOf(
        Note("E2", 23, listOf(Pair(6,0))),
        Note("F2", 22, listOf(Pair(6,1))),
        Note("G2", 21, listOf(Pair(6,3))),
        Note("A2", 20, listOf(Pair(6,5), Pair(5,0))),
        Note("B2", 19, listOf(Pair(6,7), Pair(5,2))),
        Note("C3", 18, listOf(Pair(6,8), Pair(5,3))),
        Note("D3", 17, listOf(Pair(6,10), Pair(5,5), Pair(4,0))),
        Note("E3", 16, listOf(Pair(6,12), Pair(5,7))),
        Note("F3", 15, listOf(Pair(5,8), Pair(4,3))),
        Note("G3", 14, listOf(Pair(5,10), Pair(4,5), Pair(3,0))),
        Note("A3", 13, listOf(Pair(5,12), Pair(4,7))),
        Note("B3", 12, listOf(Pair(4,9), Pair(3,4), Pair(2,0))),
        Note("C4", 11, listOf(Pair(4,10), Pair(3,5))),
        Note("D4", 10, listOf(Pair(4,12), Pair(3,7))),
        Note("E4", 9, listOf(Pair(3,9), Pair(2,5), Pair(1,0))),
        Note("F4", 8, listOf(Pair(3,10), Pair(2,6), Pair(1,1))),
        Note("G4", 7, listOf(Pair(3,12), Pair(2,8), Pair(1,3))),
        Note("A4", 6, listOf(Pair(2,10), Pair(1,5))),
        Note("B4", 5, listOf(Pair(2,12), Pair(1,7))),
        Note("C5", 4, listOf(Pair(1,8))),
        Note("D5", 3, listOf(Pair(1,10))),
        Note("E5", 2, listOf(Pair(1,12))),
    )

    fun random(currentNote: Int): Pair<Int,Note> {
        var randomIndex: Int
        do {
            randomIndex = Random.nextInt(notes.size)
        } while (randomIndex == currentNote)
        return Pair(randomIndex, notes[randomIndex])
    }

}