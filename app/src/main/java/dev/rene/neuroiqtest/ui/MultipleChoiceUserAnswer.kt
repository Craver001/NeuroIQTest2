package dev.rene.neuroiqtest.ui

import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import com.google.firebase.database.*
import dev.rene.neuroiqtest.R
import dev.rene.neuroiqtest.classes.UserQuestionData
import dev.rene.neuroiqtest.databinding.MultipleChoiceTestReviewBinding

class MultipleChoiceUserAnswer : AppCompatActivity() {

    lateinit var binding: MultipleChoiceTestReviewBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var userQuestionData: List<UserQuestionData>
    private var currentQuestionIndex = 0
    private var numberItem = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MultipleChoiceTestReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Create a handler to delay the blur operation
        val userId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
       // val getTopics = intent.getStringExtra("subject")

        database = FirebaseDatabase.getInstance()
        val ref = database.reference.child("UserAnswer").child("$userId")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    userQuestionData = dataSnapshot.children.map { questionSnapshot ->
                        val questionText =
                            questionSnapshot.child("question").getValue(String::class.java) ?: ""
                        val answerOptions = questionSnapshot.child("answerOptions")
                            .getValue(object : GenericTypeIndicator<List<String>>() {})
                            ?: emptyList()
                        val correctAnswerIndex =
                            questionSnapshot.child("correctAnswer").getValue(Long::class.java)
                                ?.toInt() ?: -1
                        val solutionIndex =
                            questionSnapshot.child("solution").getValue(String::class.java) ?: ""
                        val paragraphs = solutionIndex.split("  ")
                        val formattedSolution = paragraphs.joinToString("\n\n")


                        val userAnswerIndex =
                            questionSnapshot.child("userAnswer").getValue(Long::class.java)?.toInt()
                                ?: -1

                        UserQuestionData(
                            answerOptions,
                            correctAnswerIndex,
                            questionText,
                            formattedSolution,
                            userAnswerIndex
                        )
                    }

                    // Use quizQuestions in your code after it has been initialized
                    showQuestionAtIndex(0)
                    binding.progressBar2.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle any errors loading quiz questions from Firebase Realtime Database
                Log.e(ContentValues.TAG, "Error loading quiz questions", error.toException())
            }
        })

        binding.next.setOnClickListener {

            numberItem +=1

            binding.numberQuestion.setText("Question #$numberItem")
            showNextQuestion()

        }

        binding.previous.setOnClickListener {
            showPreviousQuestion()
        }

    }


    private fun showNextQuestion() {
        if (::userQuestionData.isInitialized && currentQuestionIndex < userQuestionData.size - 1) {
            currentQuestionIndex++
            showQuestionAtIndex(currentQuestionIndex)
        } else {
            val intent = Intent(this@MultipleChoiceUserAnswer, HomePage::class.java)
            startActivity(intent)
        }
    }

    private fun showPreviousQuestion() {
        if (::userQuestionData.isInitialized && currentQuestionIndex > 0) {
            currentQuestionIndex--
            showQuestionAtIndex(currentQuestionIndex)
        }
    }

    private fun showQuestionAtIndex(index: Int) {
        val question = userQuestionData.getOrNull(index)
        if (question != null) {
            binding.questionTextView.text = question.question
            binding.optionA.apply {
                text = question.answerOptions.getOrNull(0)
                setBackgroundResource(R.drawable.shape) // Reset the background color
                if (question.userAnswer == 0 && question.userAnswer == question.correctAnswer) setBackgroundColor(
                    Color.GREEN
                )
                if (question.userAnswer == 0 && question.userAnswer != question.correctAnswer) setBackgroundColor(
                    Color.RED
                )

            }
            binding.optionB.apply {
                text = question.answerOptions.getOrNull(1)
                setBackgroundResource(R.drawable.shape) // Reset the background color
                if (question.userAnswer == 1 && question.userAnswer == question.correctAnswer) setBackgroundColor(
                    Color.GREEN
                )
                if (question.userAnswer == 1 && question.userAnswer != question.correctAnswer) setBackgroundColor(
                    Color.RED
                )
            }
            binding.optionC.apply {
                text = question.answerOptions.getOrNull(2)
                setBackgroundResource(R.drawable.shape) // Reset the background color
                if (question.userAnswer == 2 && question.userAnswer == question.correctAnswer) setBackgroundColor(
                    Color.GREEN
                )
                if (question.userAnswer == 2 && question.userAnswer != question.correctAnswer) setBackgroundColor(
                    Color.RED
                )
            }
            binding.optionD.apply {
                text = question.answerOptions.getOrNull(3)
                setBackgroundResource(R.drawable.shape) // Reset the background color
                if (question.userAnswer == 3 && question.userAnswer == question.correctAnswer) setBackgroundColor(
                    Color.GREEN
                )
                if (question.userAnswer == 3 && question.userAnswer != question.correctAnswer) setBackgroundColor(
                    Color.RED
                )
            }
            binding.solution.text = question.solution


        }
    }


}