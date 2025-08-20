package dev.rene.neuroiqtest.classes

// Universal model for logic test
data class LogicQuestionClass(
    val id: Int, // unique id for each question
    val imageLogic: Int, // main logic image (drawable resource)
    val options: List<Int>, // list of option images (Image1-4)
    val correctAnswerIndex: Int // index of the correct option (0 to 3)
)
