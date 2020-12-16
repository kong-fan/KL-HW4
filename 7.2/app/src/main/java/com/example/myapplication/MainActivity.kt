package com.example.myapplication

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private lateinit var ed_height: EditText
    private lateinit var ed_weight:EditText
    private lateinit var btn_boy: RadioButton
    private lateinit var tv_weight: TextView
    private  lateinit var tv_bmi:TextView
    private  lateinit var tv_progress:TextView
    private lateinit var  ll_progress: LinearLayout
    private lateinit var  progressBar2: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ed_height = findViewById(R.id.ed_height)
        ed_weight = findViewById(R.id.ed_weight)
        btn_boy = findViewById(R.id.btn_boy)
        tv_weight = findViewById(R.id.tv_weight)
        tv_bmi = findViewById(R.id.tv_bmi)
        tv_progress = findViewById(R.id.tv_progress)
        ll_progress = findViewById(R.id.ll_progress)
        progressBar2 = findViewById(R.id.progressBar2)
            findViewById<View>(R.id.btn_calculate).setOnClickListener {
                if (ed_height.length() < 1) {
                    Toast.makeText(this@MainActivity, "請輸入身高", Toast.LENGTH_SHORT).show()
                } else if (ed_weight.length() < 1) {
                    Toast.makeText(this@MainActivity, "請輸入體重", Toast.LENGTH_SHORT).show()
                } else {
                    GlobalScope.launch { runAsyncTask() }
                }
            }
        }
            private suspend fun runAsyncTask() {
                 try{
                    withContext(Dispatchers.Main) {
                        tv_weight.text = "標準體重\n無"
                        tv_bmi.text = "體脂肪\n無"
                        progressBar2.progress = 0
                        tv_progress.text = "0%"
                        ll_progress.visibility = View.VISIBLE
                    }

                    for(i in 1..10) {
                        var progress = 0
                        while (progress <= 100) {
                            try {
                                delay(50)
                                withContext(Dispatchers.Main) {
                                    progressBar2.progress = progress
                                    tv_progress.text = progress.toString() + "%"
                                }
                                progress++
                            } catch (e: InterruptedException) {
                                e.printStackTrace()
                            }
                        }
                        withContext(Dispatchers.Main) {
                            ll_progress.visibility = View.GONE
                            val h = Integer.valueOf(ed_height.text.toString())
                            val w = Integer.valueOf(ed_weight.text.toString())
                            val standWeight: Double
                            val bodyFat: Double
                            if (btn_boy.isChecked) {
                                standWeight = (h - 80) * 0.7
                                bodyFat = (w - 0.88 * standWeight) / w * 100
                            } else {
                                standWeight = (h - 70) * 0.6
                                bodyFat = (w - 0.82 * standWeight) / w * 100
                            }
                            tv_weight.text = String.format("標準體重\n%.2f", standWeight)
                            tv_bmi.text = String.format("體脂肪\n%.2f", bodyFat)
                        }
                    }
                }catch (e: Exception){
                     Log.e(localClassName,"Cancelled",e)
                 }
    }
}