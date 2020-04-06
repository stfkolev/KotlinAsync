package com.example.kotlinasync

import android.graphics.Color
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.btnDoAsync
import kotlinx.android.synthetic.main.activity_main.progressBar
import kotlinx.android.synthetic.main.activity_main.textView
import kotlinx.android.synthetic.main.activity_second.*
import java.lang.ref.WeakReference
import kotlin.random.Random

class SecondActivity : AppCompatActivity() {
    val resultList: ArrayList<Boolean> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        btnDoAsync.setOnClickListener {
            TestAsyncTask(this).execute(Random.nextInt(2, 5))
            TestAsyncTask(this).execute(Random.nextInt(3, 5))
        }
    }

    companion object {
        internal class TestAsyncTask constructor(context: SecondActivity) : AsyncTask<Int, String, Boolean> () {
            private val activityReference: WeakReference<SecondActivity> = WeakReference(context)

            override fun onPreExecute() {
                val activity = activityReference.get()

                if (activity == null || activity.isFinishing)
                    return

                activity.progressBar.visibility = View.VISIBLE
            }

            override fun doInBackground(vararg params: Int?): Boolean {
                try {
                    params[0]?.times(1000)?.toLong()?.let {
                        Thread.sleep(it)
                    }
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                return Random.nextBoolean()
            }

            override fun onPostExecute(result:  Boolean) {
                val activity = activityReference.get()

                if (activity == null || activity.isFinishing)
                    return

                activity.resultList.add(result)

                if(activity.resultList.size > 1) {
                    if(activity.resultList[0] == activity.resultList[1]) {
                        activity.textView.setTextColor(Color.parseColor("#2ecc71"))
                        activity.textView.text = "Success!"
                    } else {
                        activity.textView.setTextColor(Color.parseColor("#e74c3c"))
                        activity.textView.text = "Failure"
                    }

                    activity.resultList.clear()
                    activity.progressBar.visibility = View.GONE
                }
            }
        }
    }
}
