package dev.rene.neuroiqtest.classes

class UserQuestionData
    (

    var answerOptions: List<String>,
    var correctAnswer: Int,
    var question: String,
    var solution:String,
    var userAnswer:Int
)
{

    constructor() : this(listOf(),-1,"","",-1)
}