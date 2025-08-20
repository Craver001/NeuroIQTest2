package dev.rene.neuroiqtest.classes

data class QuestionClass(
    val answerOptions: List<String>,
    val correctAnswer: Int,
    val question: String,
    val Solution:String

) {

    constructor() : this(listOf(),-1,"","")
}