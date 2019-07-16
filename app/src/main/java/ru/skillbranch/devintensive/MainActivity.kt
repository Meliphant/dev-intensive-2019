package ru.skillbranch.devintensive

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.skillbranch.devintensive.extensions.hideKeyboard
import ru.skillbranch.devintensive.models.Bender

class MainActivity : AppCompatActivity() {

    private lateinit var benderImage: ImageView
    private lateinit var textTxt: TextView
    private lateinit var messageEt: EditText
    private lateinit var sendBtn: ImageView

    private lateinit var bender: Bender

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        benderImage = iv_bender
        textTxt = tv_text
        messageEt = et_message
        sendBtn = iv_send
        val status = savedInstanceState?.getString("STATUS") ?: Bender.Status.NORMAL.name
        val question = savedInstanceState?.getString("QUESTION") ?: Bender.Question.NAME.name
        bender = Bender(Bender.Status.valueOf(status), Bender.Question.valueOf(question))

        Log.d("M_MainActivity", "onCreate $status $question")

        val (r, g, b) = bender.status.color
        benderImage.setColorFilter(Color.rgb(r, g, b), PorterDuff.Mode.MULTIPLY)

        textTxt.text = bender.askQuestion()

        sendBtn.setOnClickListener { sendAnswer() }

        messageEt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                sendAnswer()
                hideKeyboard()
                true
            } else {
                false
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putString("STATUS", bender.status.name)
        outState?.putString("QUESTION", bender.question.name)
        super.onSaveInstanceState(outState)
        Log.d("M_MainActivity", "onSaveInstanceState ${bender.status.name} ${bender.question.name}")
    }

    private fun sendAnswer() {
        bender.listenAnswer(messageEt.text.toString()).apply {
            benderImage.setColorFilter(Color.rgb(second.first, second.second, second.third), PorterDuff.Mode.MULTIPLY)
            textTxt.text = first
        }
        messageEt.setText("")
    }

}
