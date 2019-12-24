package cgland.job.sk_android.mvp.view.fragment.message

import android.graphics.Color
import android.os.Bundle
import android.view.*
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import android.content.Context
import android.widget.TextView
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import app.findbest.vip.R

class MessageChatRecordSelectMenuFragment : Fragment() {

    var toolbar1: Toolbar?=null
    private var mContext: Context? = null

    private lateinit var selecter: MenuSelect

    lateinit var textView1:TextView
    lateinit var textView2:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(parentFragment==null){
            mContext = activity
        }else{
            mContext = parentFragment!!.context
        }
    }
    companion object {
        fun newInstance(): MessageChatRecordSelectMenuFragment {
            return MessageChatRecordSelectMenuFragment()
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val fragmentView=createView()

        if(parentFragment==null){
            selecter =  activity as MenuSelect
        }else{
            selecter =  parentFragment as MenuSelect
        }


        return fragmentView
    }
    private fun createView(): View {
        return UI {
            linearLayout {
                linearLayout() {

                    relativeLayout {

                        textView1=textView {
                            backgroundResource= R.drawable.bottom_border_yellow_3dp
                        }.lparams {
                            height=matchParent
                            width=dip(20)
                            centerInParent()
                        }


                        textView {
                            backgroundColor=Color.TRANSPARENT
                            text="チャット履歴"
                            textSize=16f
                            gravity=Gravity.CENTER
                            textColorResource=R.color.normalTextColor
                            setOnClickListener(object:View.OnClickListener{
                                override fun onClick(v: View?) {
                                    textView2.visibility=View.INVISIBLE
                                    textView1.visibility=View.VISIBLE
                                    selecter.getMenuSelect(0)

                                }

                            })
                        }.lparams {
                            height=matchParent
                            width=matchParent
                        }



                    }.lparams {
                        width=0
                        weight=1f
                        height= matchParent
                    }


                    relativeLayout {


                        textView2=textView {
                            backgroundResource=R.drawable.bottom_border_yellow_3dp
                            visibility=View.INVISIBLE
                        }.lparams {
                            height=matchParent
                            width=dip(20)
                            centerInParent()
                        }




                        textView {
                            backgroundColor=Color.TRANSPARENT
                            text="連絡先"
                            textSize=16f
                            gravity=Gravity.CENTER
                            textColorResource=R.color.gray7f
                            setOnClickListener(object:View.OnClickListener{
                                override fun onClick(v: View?) {
                                    textView1.visibility=View.INVISIBLE
                                    textView2.visibility=View.VISIBLE
                                    selecter.getMenuSelect(1)
                                }

                            })
                        }.lparams {
                            height=matchParent
                            width=matchParent
                        }

                    }.lparams {
                        width=0
                        weight=1f
                        height= matchParent
                    }

                }.lparams() {
                    width = matchParent
                    height = dip(62)
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


    interface  MenuSelect{

        fun  getMenuSelect(index:Int)
    }

}




