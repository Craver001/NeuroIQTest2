package dev.rene.neuroiqtest

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.CountDownTimer
import java.util.concurrent.TimeUnit
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.*
import dev.rene.neuroiqtest.databinding.MockTest2Binding

import android.provider.Settings;

@Suppress("DEPRECATION")
class
MockExamTest : AppCompatActivity() {
    lateinit var binding: MockTest2Binding
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseRef: DatabaseReference
    private lateinit var quizQuestions: List<QuestionClass>
    private var currentQuestionIndex = 0
    private var selectedAnswer: TextView? = null

    private var userScore = 0
    private var scorePercentage = 0
    //timer function
    private var isRunning: Boolean = false
    private lateinit var timer: CountDownTimer
    private var elapsedTime: Long = 0
    ///
    private var userSet: String? = null
    private var getSubject:String?=null

    private var progressStatus = 0
    private var item= 1
    private var clickCount: Int = 0
    private var numberItem = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= MockTest2Binding.inflate(layoutInflater)
        setContentView(binding.root)


        // Initialize the Firebase database
        database = FirebaseDatabase.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference


        // Retrieve the selected set from the Intent extra
        userSet = intent.getStringExtra("set")


        binding.progressBar2.progress = progressStatus
        binding.topicTitle.text="$getSubject"

        binding.numberQuestion.text ="Question #$item"

        blurOperation()

        val userSet = userSet ?: ""
        val ref = database.reference.child("Neuro")

// Fetch all available sets and select a random one
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val availableSets = dataSnapshot.children.map { it.key }.filterNotNull()

                    if (availableSets.isNotEmpty()) {
                        val randomSet = availableSets.random()  // Pick a random set
                        val selectedRef = ref.child(randomSet)  // Reference to the random set

                        selectedRef.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(setSnapshot: DataSnapshot) {
                                if (setSnapshot.exists()) {
                                    quizQuestions = setSnapshot.children.map { questionSnapshot ->
                                        val questionText =
                                            questionSnapshot.child("question").getValue(String::class.java) ?: ""
                                        val answerOptions = questionSnapshot.child("options")
                                            .getValue(object : GenericTypeIndicator<List<String>>() {})
                                            ?: emptyList()
                                        val correctAnswerIndex =
                                            questionSnapshot.child("correctAnswer").getValue(Long::class.java)
                                                ?.toInt() ?: -1
                                        val solutionIndex =
                                            questionSnapshot.child("solution").getValue(String::class.java) ?: ""
                                        val paragraphs = solutionIndex.split("//")
                                        val formattedSolution = paragraphs.joinToString("\n\n")

                                        QuestionClass(
                                            answerOptions,
                                            correctAnswerIndex,
                                            questionText,
                                            formattedSolution
                                        )
                                    }

                                    // Display first question from the selected set
                                    showQuestionAtIndex(0)
                                    binding.progressBar2.visibility = View.GONE
                                    startTimer()
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Log.e(ContentValues.TAG, "Error loading quiz questions from set", error.toException())
                            }
                        })
                    } else {
                        Log.e(ContentValues.TAG, "No sets available in the database")
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(ContentValues.TAG, "Error loading sets from Firebase", error.toException())
            }
        })


        clickListener()

        binding.showSolution.isEnabled = false


    }

    private fun showQuestionAtIndex(index: Int) {
        val question = quizQuestions.getOrNull(index)
        if (question != null) {

            binding.questionTextView.text= question.question
            binding.optionA.text = question.answerOptions.getOrNull(0)
            binding.optionB.text = question.answerOptions.getOrNull(1)
            binding.optionC.text = question.answerOptions.getOrNull(2)
            binding.optionD.text = question.answerOptions.getOrNull(3)
            binding.solution.text = question.Solution
        }


    }

    private fun showNextQuestion() {

        if (currentQuestionIndex < quizQuestions.size - 1) {
            currentQuestionIndex++
            showQuestionAtIndex(currentQuestionIndex)

            binding.showSolution.setText(R.string.lock)
            binding.showSolution.visibility = View.VISIBLE
//             blurOperation()
            startTimer()


        } else {
            val totalQuestions = quizQuestions.size
            // Calculate the score percentage
            val scorePercentage = (userScore.toDouble() / totalQuestions.toDouble()) * 100

            val userId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

            val questionRef = databaseRef.child("UserAverage").child("$userId").child("$getSubject")
            questionRef.setValue(scorePercentage)


            val intent = Intent(this@MockExamTest, TotalScore::class.java)
           // resetTimer()
            intent.putExtra("Score", userScore)
          //  intent.putExtra("subject",getSubject)
            intent.putExtra("userPercentage", scorePercentage)
            startActivity(intent)

        }
    }

    private fun selectAnswer(answer: TextView) {
        selectedAnswer?.setBackgroundResource(R.drawable.shape4)
        answer.setBackgroundColor(Color.GREEN)
        selectedAnswer = answer

        val answerNumber = getAnswerNumber(answer)

        // save user's answer to database
        val userId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        val currentQuestion = quizQuestions.getOrNull(currentQuestionIndex)

        if (userId != null && currentQuestion != null) {
            val userQuestionData = UserQuestionData(
                question = currentQuestion.question,
                answerOptions = currentQuestion.answerOptions,
                correctAnswer = currentQuestion.correctAnswer,
                userAnswer = answerNumber,
                solution = currentQuestion.Solution
            )
            val questionRef = databaseRef.child("UserAnswer").child(userId).child("question$currentQuestionIndex")
            questionRef.setValue(userQuestionData)
        }
    }


    private fun getAnswerNumber(answerView: TextView): Int {
        return when (answerView) {
            binding.optionA -> 0
            binding.optionB -> 1
            binding.optionC -> 2
            binding.optionD -> 3
            else -> -1
        }
    }

    private fun getSelectedAnswerIndex(): Int {
        return when (selectedAnswer) {
            binding.optionA -> 0
            binding.optionB -> 1
            binding.optionC -> 2
            binding.optionD -> 3
            else -> -1
        }
    }


    private fun clickListener() {
        binding.optionA.setOnClickListener {
            selectAnswer(binding.optionA)

        }
        binding.optionB.setOnClickListener {
            selectAnswer(binding.optionB)

        }
        binding.optionC.setOnClickListener {
            selectAnswer(binding.optionC)

        }
        binding.optionD.setOnClickListener {
            selectAnswer(binding.optionD)
        }

        binding.next.setOnClickListener {

            if (selectedAnswer == null) {
                Toast.makeText(applicationContext, "Please select an answer", Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }

            // Check if the selected answer is correct
            val selectedAnswerIndex = getSelectedAnswerIndex()
            val currentQuestion = quizQuestions[currentQuestionIndex]
            val correctAnswerIndex = currentQuestion.correctAnswer


            if (selectedAnswerIndex == correctAnswerIndex) {
                // The answer is correct
                userScore += 1
            }

            // reset the selection
            selectedAnswer?.setBackgroundResource(R.drawable.shape4)
            selectedAnswer = null


            showNextQuestion()
            showNextQuestion()

            numberItem +=1

            binding.numberQuestion.setText("Question #$numberItem")



            isRunning = true

            item =+1

        }


        binding.exit.setOnClickListener{

            val intent = Intent(this@MockExamTest,NeuroSets::class.java)
            startActivity(intent)
        }


    }


    private fun blurOperation(){

        Handler(Looper.getMainLooper()).postDelayed ({
            // Create a TextView to display the text that you want to hide and blur
            val textView = binding.solution

            // Convert the TextView into a bitmap image
            val bitmap =
                Bitmap.createBitmap(textView.width, textView.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            textView.draw(canvas)

            // Apply a blur effect to the bitmap image
            val radius = 25
            val blurredBitmap = Bitmap.createBitmap(bitmap)
            RenderScript.create(this).apply {
                val input = Allocation.createFromBitmap(this, bitmap)
                val output = Allocation.createFromBitmap(this, blurredBitmap)
                ScriptIntrinsicBlur.create(this, Element.U8_4(this)).apply {
                    setInput(input)
                    setRadius(radius.toFloat())
                    forEach(output)
                }
                output.copyTo(blurredBitmap)
            }.destroy()

            // Create a Button in your layout to display the blurred image of the text
            val button = binding.showSolution

            // Set the blurred bitmap image as the background of the Button
            button.background = BitmapDrawable(resources, blurredBitmap)

            // Set the TextView's text color to transparent to hide the text
            textView.setTextColor(Color.TRANSPARENT)



        }, 100)}



    private fun startTimer() {
        if (!isRunning) {
            val durationInMillis = TimeUnit.MINUTES.toMillis(15)
            timer = object : CountDownTimer(durationInMillis, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val remainingTime = millisUntilFinished - elapsedTime
                    binding.timer.text = formatElapsedTime(remainingTime)
                    elapsedTime += 1000
                }

                override fun onFinish() {
                    dialog()
                }
            }
            timer.start()
            isRunning = true
        }
    }

    private fun formatElapsedTime(timeInMillis: Long): String {
        // convert time to hours, minutes, and seconds
        val hours = TimeUnit.MILLISECONDS.toHours(timeInMillis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(timeInMillis) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(timeInMillis) % 60

        // format the time as a string
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    private fun resetTimer() {
        if (isRunning) {
            timer.cancel()
            isRunning = false
        }
        elapsedTime = 0L
        binding.timer.text = formatElapsedTime(0L)
    }


    private fun dialog(){

        val dialogView = layoutInflater.inflate(R.layout.timeover, null)
        val ok = dialogView.findViewById<Button>(R.id.tryAgain)
        val totalQuestions = quizQuestions.size
        // Calculate the score percentage
        val scorePercentage = (userScore.toDouble() / totalQuestions.toDouble()) * 100


        val builder = AlertDialog.Builder(this)
            .setView(dialogView)

        val alertDialog = builder.create()

        ok.setOnClickListener {



            val intent = Intent(this@MockExamTest, TotalScore::class.java)
            intent.putExtra("Score", userScore)
            intent.putExtra("userPercentage", scorePercentage)
            startActivity(intent)
        }

        alertDialog.show()
    }


}
