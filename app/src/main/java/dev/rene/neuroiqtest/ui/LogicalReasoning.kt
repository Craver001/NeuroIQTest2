package dev.rene.neuroiqtest.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dev.rene.neuroiqtest.LogicalReasoningData.LogicData
import dev.rene.neuroiqtest.R
import dev.rene.neuroiqtest.classes.LogicQuestionClass
import dev.rene.neuroiqtest.databinding.HomepageBinding
import dev.rene.neuroiqtest.databinding.LogicalReasoningTestBinding

class LogicalReasoning : AppCompatActivity()

{

    private lateinit var binding: LogicalReasoningTestBinding
    private var currentQuestionIndex = 0
    private var score = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= LogicalReasoningTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        // Load the first question
        loadQuestion(LogicData.questions[currentQuestionIndex])
    }

    fun loadQuestion(question: LogicQuestionClass) {
        binding.mainImage.setImageResource(question.imageLogic)

        binding.image1.setImageResource(question.options[0])
        binding.image2.setImageResource(question.options[1])
        binding.image3.setImageResource(question.options[2])
        binding.image4.setImageResource(question.options[3])

        // handle click
        binding.image1.setOnClickListener { checkAnswer(0, question) }
        binding.image2.setOnClickListener { checkAnswer(1, question) }
        binding.image3.setOnClickListener { checkAnswer(2, question) }
        binding.image4.setOnClickListener { checkAnswer(3, question) }
    }

    fun checkAnswer(selectedIndex: Int, question: LogicQuestionClass) {
        if (selectedIndex == question.correctAnswerIndex) {
            // Correct ✅

            score += 1
            // Example: move to next question
            if (currentQuestionIndex < LogicData.questions.size - 1) {
                currentQuestionIndex++
                loadQuestion(LogicData.questions[currentQuestionIndex])
            } else {
                // No more questions proceed to scoreboard
                val intent = Intent(this@LogicalReasoning, TotalScore::class.java)
                intent.putExtra("Score", score) // passing score to other activity
                intent.putExtra("test","logical")
                startActivity(intent) // navigate to other screen
            }


        } else {
            // Example: move to next question
            if (currentQuestionIndex < LogicData.questions.size - 1) {
                currentQuestionIndex++
                loadQuestion(LogicData.questions[currentQuestionIndex])
            } else {
                // No more questions proceed to scoreboard
                val intent = Intent(this@LogicalReasoning, TotalScore::class.java)
                intent.putExtra("Score", score) // passing score to other activity
                intent.putExtra("test","logical")
                startActivity(intent) // navigate to other screen
            }

        }
    }





}