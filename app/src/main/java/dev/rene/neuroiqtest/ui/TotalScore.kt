package dev.rene.neuroiqtest.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dev.rene.neuroiqtest.databinding.UserScoreBinding

class TotalScore : AppCompatActivity() {

    lateinit var binding: UserScoreBinding
    private var topics:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= UserScoreBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val score = intent.getIntExtra("Score", 0)
        binding.userScore.text = score.toString()



        val average = intent.getDoubleExtra("userPercentage", 0.0)
        val roundedAverage = String.format("%.2f", average)

        binding.averageScore.text = roundedAverage


        val subject = intent.getStringExtra("test")


        binding.buttonCheckAnswer.setOnClickListener{

            if (subject =="logical"){

                val intent = Intent(this@TotalScore, LogicalReasoningReview::class.java)
                startActivity(intent)
            }
            else {
                val intent = Intent(this@TotalScore,MultipleChoiceUserAnswer::class.java)
                // intent.putExtra("subject","$subject")
                startActivity(intent)

            }



        }

        binding.buttonNext.setOnClickListener{
            val intent = Intent(this@TotalScore, HomePage::class.java)
            startActivity(intent)
        }


    }
}