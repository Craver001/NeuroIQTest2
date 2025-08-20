package dev.rene.neuroiqtest.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dev.rene.neuroiqtest.databinding.HomepageBinding

class HomePage : AppCompatActivity() {
    private lateinit var binding: HomepageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= HomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)

       // val subject =intent.getStringExtra("subject")


        binding.multiple.setOnClickListener {
            val intent = Intent(this@HomePage, MultipleChoice::class.java)
            intent.putExtra("set", "Multiple Test") // add an extra with the selected set
          //  intent.putExtra("topics","$subject")
            startActivity(intent)
        }

        binding.logicalReasoning.setOnClickListener {
            val intent = Intent(this@HomePage, LogicalReasoning::class.java)
            intent.putExtra("set", "Logical Reasoning") // add an extra with the selected set
          //  intent.putExtra("topics","$subject")
            startActivity(intent)
        }

        binding.Decoding.setOnClickListener {
            val intent = Intent(this@HomePage, MultipleChoice::class.java)
            intent.putExtra("set", "Decoding") // add an extra with the selected set
         //   intent.putExtra("topics","$subject")
            startActivity(intent)
        }


    }
}