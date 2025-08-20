package dev.rene.neuroiqtest.LogicalReasoningData

import dev.rene.neuroiqtest.R
import dev.rene.neuroiqtest.classes.LogicQuestionClass


object LogicData {
    val questions = listOf(
        LogicQuestionClass(
            id = 1,
            imageLogic = R.drawable.raster1,
            options = listOf(
                R.drawable.raster1_optie1,
                R.drawable.raster1_optie2,
                R.drawable.raster1_optie3,
                R.drawable.raster1_optie4
            ),
            correctAnswerIndex = 3 // means option_1c is correct
        ),
        LogicQuestionClass(
            id = 2,
            imageLogic = R.drawable.raster2,
            options = listOf(
                R.drawable.raster2_optie1,
                R.drawable.raster2_optie2,
                R.drawable.raster2_optie3,
                R.drawable.raster2_optie4
            ),
            correctAnswerIndex = 1 // means option_1c is correct
        // ➝ You can add more questions here...
    )
    )
}