package dev.rene.neuroiqtest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dev.rene.neuroiqtest.databinding.MockTestLayoutBinding

class TotalScore : AppCompatActivity() {

    lateinit var binding: MockTestLayoutBinding
    private var topics:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=MockTestLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val score = intent.getIntExtra("Score", 0)
       binding.userScore.text = score.toString()



        val average = intent.getDoubleExtra("userPercentage", 0.0)
        val roundedAverage = String.format("%.2f", average)

        binding.averageScore.text = roundedAverage


//        val subject = intent.getStringExtra("subject")


        binding.buttonCheckAnswer.setOnClickListener{
            val intent = Intent(this@TotalScore,MockUserAnswer::class.java)
           // intent.putExtra("subject","$subject")
            startActivity(intent)

        }

        binding.buttonNext.setOnClickListener{
            val intent = Intent(this@TotalScore, NeuroSets::class.java)
            startActivity(intent)
        }


    }
}