package dev.rene.neuroiqtest

data class QuestionClass(
    val answerOptions: List<String>,
    val correctAnswer: Int,
    val question: String,
    val Solution:String

) {

    constructor() : this(listOf(),-1,"","")
}