package dev.rene.neuroiqtest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dev.rene.neuroiqtest.databinding.ActivityPreTestBinding

class NeuroSets : AppCompatActivity() {
    private lateinit var binding: ActivityPreTestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityPreTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val subject =intent.getStringExtra("subject")


        binding.setA.setOnClickListener {
            val intent = Intent(this@NeuroSets, MockExamTest::class.java)
            intent.putExtra("set", "Set A") // add an extra with the selected set
            intent.putExtra("topics","$subject")
            startActivity(intent)
        }

        binding.setB.setOnClickListener {
            val intent = Intent(this@NeuroSets, MockExamTest::class.java)
            intent.putExtra("set", "Set B") // add an extra with the selected set
            intent.putExtra("topics","$subject")
            startActivity(intent)
        }

        binding.setC.setOnClickListener {
            val intent = Intent(this@NeuroSets, MockExamTest::class.java)
            intent.putExtra("set", "Set C") // add an extra with the selected set
            intent.putExtra("topics","$subject")
            startActivity(intent)
        }


    }
}