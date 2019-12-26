package app.findbest.vip.individual.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import app.findbest.vip.R
import app.findbest.vip.commonfrgmant.FragmentParent
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI

class FbMain:FragmentParent() {
    private var mContext: Context? = null
    lateinit var myEdit:EditText
    lateinit var myText:TextView

    companion object {
        fun newInstance(): FbMain {
            return FbMain()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(parentFragment==null){
            mContext = activity
        }else{
            mContext = parentFragment?.context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createView()
    }

    private fun createView():View {
        val view = View.inflate(mContext, R.layout.edit_number, null)
        myEdit = view.findViewById(R.id.et_word)
        myText = view.findViewById(R.id.tv_word_count)
        return UI {
            verticalLayout {
                linearLayout {
                    addView(view)
                    myEdit.addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

                        }

                        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                            myText.text = s.length.toString()
                        }

                        override fun afterTextChanged(s: Editable) {

                        }
                    })
                }.lparams(width = matchParent,height = wrapContent){
                    topMargin = dip(15)
                    bottomMargin = dip(30)
                }

                button {
                    backgroundResource = R.drawable.button_gradient
                    text = "提交"
                    textColor = Color.WHITE
                    textSize = 15f
                }.lparams(width = matchParent,height = dip(47)){
                    leftMargin = dip(15)
                    rightMargin = dip(15)
                }

            }
        }.view
    }


}