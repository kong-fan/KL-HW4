package com.example.myapplication

import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.widget.Button
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {
    private var rabprogress = 0
    private var turprogress = 0
    val scope = CoroutineScope(Dispatchers.Default)
    private lateinit var seekBar   : SeekBar
    private lateinit var seekBar2  : SeekBar
    private lateinit var btn_start : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        seekBar = findViewById(R.id.seekBar)
        seekBar2 = findViewById(R.id.seekBar2)
        btn_start = findViewById(R.id.btn_start)

        btn_start.setOnClickListener {
            btn_start.isEnabled = false
            rabprogress = 0
            turprogress = 0
            seekBar.progress = 0
            seekBar2.progress = 0
        scope.launch {
            runScope()
            runCoroutines()
             }
        }
    }
    private fun runScope() {
        GlobalScope.launch {
            while (rabprogress <= 100 && turprogress <= 100) {
                try {
                    delay(100)
                    rabprogress += (Math.random() * 3).toInt()
                    val msg = Message()
                    msg.what = 1
                    mHandler.sendMessage(msg)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
    }
    private val mHandler = Handler(Handler.Callback { msg ->
        when (msg.what) {
            1 -> seekBar.progress = rabprogress
        }
        if (rabprogress >= 100 && turprogress < 100) {
            Toast.makeText(this@MainActivity, "兔子勝利", Toast.LENGTH_SHORT).show()
            btn_start.isEnabled = true
        }
        false
    })
    override fun onPause() {
        super.onPause()
        scope.coroutineContext.cancel()
    }
    private suspend fun runCoroutines() {
        try {
            // 相當於doInBackground
            for (i in 1..10){
                while (turprogress <= 100 && rabprogress < 100) {
                    try {
                            delay(100)
                            turprogress += (Math.random() * 3).toInt()
                            withContext(Dispatchers.Main) {
                                seekBar2.setProgress(turprogress)
                            }
                    } catch (e: InterruptedException) {
                            e.printStackTrace()
                    }
                }
            }
            // 相當於onPostExecute
            withContext(Dispatchers.Main) {
                if (turprogress >= 100 && rabprogress < 100) {
                    Toast.makeText(this@MainActivity, "烏龜勝利", Toast.LENGTH_SHORT).show()
                    btn_start.isEnabled = true
                }
            }
        } catch (e: Exception) {
            Log.e(localClassName, "Cancelled", e)
        }
    }

}
