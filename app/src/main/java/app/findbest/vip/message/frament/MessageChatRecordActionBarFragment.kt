package cgland.job.sk_android.mvp.view.fragment.message

import android.graphics.Color
import android.os.Bundle
import android.view.*
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import android.content.Context
import android.graphics.Typeface
import android.widget.ImageView
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import app.findbest.vip.R

class MessageChatRecordActionBarFragment : Fragment() {

    var toolbar1: Toolbar?=null
    private var mContext: Context? = null

    private lateinit var actionBarSelecter: ActionBarSearch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(parentFragment==null){
            mContext = activity
        }else{
            mContext = parentFragment!!.context
        }


    }
    companion object {
        fun newInstance(): MessageChatRecordActionBarFragment {
            return MessageChatRecordActionBarFragment()
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val fragmentView=createView()

        if(parentFragment==null){
            actionBarSelecter =  activity as ActionBarSearch
        }else{
            actionBarSelecter =  parentFragment as ActionBarSearch
        }

        return fragmentView
    }
    private fun createView(): View {
        return UI {
            linearLayout { relativeLayout() {
                textView() {
                    backgroundResource = R.drawable.actionbar_bottom_border
                }.lparams() {
                    width = matchParent
                    height = dip(65)

                }
                relativeLayout() {

                    toolbar1 = toolbar {
                        isEnabled = true
                        title = ""
                        //navigationIconResource = R.mipmap.icon_back


                    }.lparams() {
                        width = matchParent
                        height = dip(65)
                        alignParentBottom()

                    }


                    textView {
                        text = "メッセージ"
                        backgroundColor = Color.TRANSPARENT
                        gravity = Gravity.CENTER
                        textColorResource = R.color.toolBarTextColor
                        textSize = 16f
                        setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))

                    }.lparams() {
                        width = matchParent
                        height = wrapContent
                        height = dip(65 - getStatusBarHeight(this@MessageChatRecordActionBarFragment.context!!))
                        alignParentBottom()
                    }


                    linearLayout() {


                        gravity=Gravity.CENTER_VERTICAL  or  Gravity.RIGHT



                        imageView {

                            scaleType = ImageView.ScaleType.CENTER_CROP
                            setImageResource(R.mipmap.icon_search_nor)
                            setOnClickListener(object:View.OnClickListener{
                                override fun onClick(v: View?) {
                                    actionBarSelecter.searchGotClick()
                                }
                            })

                        }.lparams() {
                            width = wrapContent
                            height =wrapContent
                            rightMargin=dip(15)

                        }


                    }.lparams() {
                        width = wrapContent
                        height = dip(65 - getStatusBarHeight(this@MessageChatRecordActionBarFragment.context!!))
                        alignParentRight()
                        alignParentBottom()
                    }
                }.lparams() {
                    width = matchParent
                    height = dip(65)
                }
            }.lparams() {
                width = matchParent
                height = dip(65)
            }
            }
        }.view

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


    interface  ActionBarSearch{

        fun  searchGotClick()
    }

}




