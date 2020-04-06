package com.example.kotlinasync

import android.graphics.Color
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity() {
    var myVariable = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnDoAsync.setOnClickListener {
            val task = TestAsyncTask(this)
            val editText = findViewById(R.id.editText) as EditText

            task.execute(Integer.parseInt(editText.text.toString()))
        }
    }

    companion object {
        class TestAsyncTask internal constructor(context: MainActivity) : AsyncTask<Int, String, String?>() {
            private var response: String? = null
            private val activityReference: WeakReference<MainActivity> = WeakReference(context)

            override fun onPreExecute() {
                val activity = activityReference.get()

                if (activity == null || activity.isFinishing)
                    return

                activity.progressBar.visibility = View.VISIBLE
            }

            override fun doInBackground(vararg params: Int?): String? {
                try {
                    val time = params[0]?.times(1000)

                    time?.toLong()?.let {
                        Thread.sleep(it)
                    }

                    response = "Completed!"
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                    response = e.message
                } catch (e: Exception) {
                    e.printStackTrace()
                    response = e.message
                }

                return response
            }


            override fun onPostExecute(result: String?) {
                val activity = activityReference.get()

                if (activity == null || activity.isFinishing)
                    return

                activity.progressBar.visibility = View.GONE
                activity.textView.setTextColor(Color.parseColor("#2ecc71"))
                activity.textView.text = result.let { it }

            }
        }
    }
}
