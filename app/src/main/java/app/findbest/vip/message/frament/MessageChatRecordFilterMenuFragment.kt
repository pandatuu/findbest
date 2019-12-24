package cgland.job.sk_android.mvp.view.fragment.message

import android.graphics.Color
import android.os.Bundle
import android.view.*
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import android.content.Context
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import app.findbest.vip.R
import cgland.job.sk_android.mvp.model.message.ChatRecordModel

class MessageChatRecordFilterMenuFragment : Fragment() {

    var toolbar1: Toolbar? = null
    private var mContext: Context? = null

    lateinit var textView1: TextView
    lateinit var textView2: TextView
    lateinit var textView3: TextView
    lateinit var textView4: TextView
    lateinit var linear: LinearLayout
    lateinit var dataMap: MutableMap<String, Int>

    private lateinit var filterMenu: FilterMenu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(parentFragment==null){
            mContext = activity
        }else{
            mContext = parentFragment!!.context
        }
    }

    companion object {
        fun newInstance(map: MutableMap<String, Int>): MessageChatRecordFilterMenuFragment {
            val f = MessageChatRecordFilterMenuFragment()
            f.dataMap = map
            return f
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val fragmentView = createView()
        if(parentFragment==null){
            filterMenu = activity as FilterMenu
        }else{
            filterMenu = parentFragment as FilterMenu
        }
        return fragmentView
    }

    private fun createView(): View {
        var view = UI {
            linearLayout {

                linear = linearLayout {
                    backgroundResource = R.drawable.radius_border_searcher_theme_border
                    gravity = Gravity.CENTER_VERTICAL
                    textView1 = textView {
                        text = ""
                        gravity = Gravity.CENTER
                        textSize = 14f
                        textColorResource = R.color.white
                        backgroundResource = R.drawable.radius_left_theme
                        setOnClickListener(object : View.OnClickListener {
                            override fun onClick(v: View?) {

                            }

                        })
                    }.lparams {
                        height = matchParent
                        width = 0
                        weight = 1f
                    }


                    textView {
                        backgroundResource = R.drawable.left_border_theme
                    }.lparams {
                        height = matchParent
                        width = dip(1)
                    }


                    textView2 = textView {
                        text = ""
                        gravity = Gravity.CENTER
                        textSize = 14f
                        textColorResource = R.color.black33
                        backgroundResource = R.color.transparent
                        setOnClickListener(object : View.OnClickListener {
                            override fun onClick(v: View?) {

                            }

                        })
                    }.lparams {
                        height = matchParent
                        width = 0
                        weight = 1f
                    }



                    textView {
                        backgroundResource = R.drawable.left_border_theme
                    }.lparams {
                        height = matchParent
                        width = dip(1)
                    }


                    textView3 = textView {
                        text = ""
                        gravity = Gravity.CENTER
                        textSize = 14f
                        textColorResource = R.color.black33
                        backgroundResource = R.color.transparent
                        setOnClickListener(object : View.OnClickListener {
                            override fun onClick(v: View?) {

                            }

                        })

                    }.lparams {
                        height = matchParent
                        width = 0
                        weight = 1f

                    }

                    textView {
                        backgroundResource = R.drawable.left_border_theme
                    }.lparams {
                        height = matchParent
                        width = dip(1)
                    }


                    textView4 = textView {
                        text = ""
                        gravity = Gravity.CENTER
                        textSize = 14f
                        textColorResource = R.color.black33
                        backgroundResource = R.color.transparent
                        setOnClickListener(object : View.OnClickListener {
                            override fun onClick(v: View?) {

                            }

                        })
                        leftPadding = dip(5)
                        rightPadding = dip(5)
                    }.lparams {
                        height = matchParent
                        width = wrapContent
                    }


                }.lparams() {
                    width = matchParent
                    height = dip(32)
                    bottomMargin = dip(18)
                    topMargin = dip(18)
                    leftMargin = dip(15)
                    rightMargin = dip(15)
                }
            }
        }.view
        setTitleName(dataMap)
        return view

    }

    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId)
            var scale = context.getResources().getDisplayMetrics().density;
            result = ((result / scale + 0.5f).toInt());
        }
        return result
    }

    fun setTitleName(map: MutableMap<String, Int>) {

        var set = map.keys
        var iter = set.iterator()
        for (i in 1..map.size) {
            if (linear.getChildAt((i - 1) * 2) != null) {
                var textView = (linear.getChildAt((i - 1) * 2) as TextView)
                var t = iter.next()
                textView.text = t
                textView.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        resetBackground()
                        textView.textColor = Color.WHITE
                        if (i == 1) {
                            textView.backgroundResource = R.drawable.radius_left_theme
                        } else if (i == map.size) {
                            textView.backgroundResource = R.drawable.radius_right_theme
                        } else {
                            textView.backgroundColorResource = R.color.themeColor

                        }
                        var index = map.get(t);
                        filterMenu.getFilterMenuselect(index!!)
                    }
                })
            }
        }
    }


    fun resetBackground() {
        textView1.backgroundResource = R.color.transparent
        textView2.backgroundResource = R.color.transparent
        textView3.backgroundResource = R.color.transparent
        textView4.backgroundResource = R.color.transparent
        textView1.textColorResource = R.color.black33
        textView2.textColorResource = R.color.black33
        textView3.textColorResource = R.color.black33
        textView4.textColorResource = R.color.black33
    }


    interface FilterMenu {
        fun getFilterMenuselect(index: Int)
    }

}




