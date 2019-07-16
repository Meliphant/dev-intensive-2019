package ru.skillbranch.devintensive

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.skillbranch.devintensive.extensions.hideKeyboard
import ru.skillbranch.devintensive.models.Bender

class MainActivity : AppCompatActivity(), TextView.OnEditorActionListener {

    lateinit var benderImage: ImageView
    lateinit var textTxt: TextView
    lateinit var messageEt: EditText
    lateinit var sendBtn: ImageView
    lateinit var benderObj: Bender

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        benderImage = iv_bender
        textTxt = tv_text
        messageEt = et_message
        sendBtn = iv_send
        val status = savedInstanceState?.getString("STATUS") ?: Bender.Status.NORMAL.name
        val question = savedInstanceState?.getString("QUESTION") ?: Bender.Question.NAME.name
        val wrongAnswer = savedInstanceState?.getInt("WRONG_ANSWER") ?: 0
        benderObj = Bender(Bender.Status.valueOf(status), Bender.Question.valueOf(question), wrongAnswer)

        Log.d("M_MainActivity", "onCreate $status $question")

        val (r, g, b) = benderObj.status.color
        benderImage.setColorFilter(Color.rgb(r, g, b), PorterDuff.Mode.MULTIPLY)
        textTxt.text = benderObj.askQuestion()
        sendBtn.setOnClickListener { listenAnswer() }
        messageEt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                listenAnswer()
                hideKeyboard()
                true
            } else {
                false
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putString("STATUS", benderObj.status.name)
        outState?.putString("QUESTION", benderObj.question.name)
        outState?.putInt("MISSES", benderObj.wrongAnswer)

        Log.d("M_MainActivity", "onSaveInstanceState ${benderObj.status.name} ${benderObj.question.name}")
    }

    override fun onEditorAction(textView: TextView?, actionId: Int, event: KeyEvent?): Boolean =
            if (textView == messageEt && actionId == EditorInfo.IME_ACTION_DONE) {
                listenAnswer()
                hideKeyboard()
                true
            } else false

    private fun listenAnswer() {
        val (phrase, color) = benderObj.listenAnswer(messageEt.text.toString())
        val (r, g, b) = color

        messageEt.setText("")
        benderImage.setColorFilter(Color.rgb(r, g, b), PorterDuff.Mode.MULTIPLY)
        textTxt.text = phrase
    }

}
