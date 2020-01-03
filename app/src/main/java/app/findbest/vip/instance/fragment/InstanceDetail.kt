package app.findbest.vip.instance.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.support.v4.UI
import android.graphics.Color
import android.view.*
import org.jetbrains.anko.*
import android.graphics.Typeface
import app.findbest.vip.R
import app.findbest.vip.commonfrgmant.FragmentParent

import android.widget.*
import app.findbest.vip.instance.view.InvitationActivity
import app.findbest.vip.utils.*
import click
import com.bumptech.glide.Glide

import withTrigger


class InstanceDetail : FragmentParent() {
    var toolbar1: Toolbar? = null
    private var mContext: Context? = null
    lateinit var activityInstance: Context


    var screenWidth = 0
    var picWidth = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (parentFragment != null) {
            mContext = parentFragment?.context
        } else {
            mContext = activity
        }
    }

    companion object {
        fun newInstance(context: Context): InstanceDetail {
            val f = InstanceDetail()
            f.activityInstance = context
            return f
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createView()
    }


    private fun createView(): View {

        screenWidth = px2dp(getDisplay(mContext!!)!!.width.toFloat())
        picWidth = (screenWidth - 18) / 2
        if (picWidth < 180) {
        } else {
            picWidth = 180
        }


        lateinit var image: ImageView

        var view = UI {
            verticalLayout {

                relativeLayout() {

                    textView() {
                        backgroundColor = Color.parseColor("#FFE3E3E3")
                    }.lparams() {
                        width = matchParent
                        height = dip(1)
                        alignParentBottom()

                    }
                    relativeLayout() {


                        toolbar1 = toolbar {
                            backgroundResource = R.color.transparent
                            isEnabled = true
                            navigationIconResource = R.mipmap.nav_ico_return

                            title = ""
                            this.withTrigger().click {

                                activity!!.finish()
                                activity!!.overridePendingTransition(
                                    R.anim.left_in,
                                    R.anim.right_out
                                )

                            }
                        }.lparams() {
                            width = matchParent
                            height = dip(65)
                            alignParentBottom()
                            height = dip(65 - getStatusBarHeight(this@InstanceDetail.context!!))
                        }



                        textView {
                            text = ""
                            backgroundColor = Color.TRANSPARENT
                            gravity = Gravity.CENTER
                            textColor = Color.parseColor("#FF222222")
                            textSize = 16f
                            setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))

                        }.lparams() {
                            width = matchParent
                            height = wrapContent
                            height = dip(65 - getStatusBarHeight(this@InstanceDetail.context!!))
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

                //////////////////////////////////////////////////////////////////
                /////////////////////////////////////////////////////////////////

                linearLayout {

                    gravity = Gravity.CENTER_VERTICAL
                    roundImageView {

                        Glide.with(this@InstanceDetail)
                            .load(activity!!.intent.getStringExtra("logo"))
                            .skipMemoryCache(false)
                            .dontAnimate()
                            .centerCrop()
                            .placeholder(R.mipmap.no_pic_show)
                            .into(this)


                    }.lparams() {
                        width = dip(45)
                        height = dip(45)
                    }


                    textView {

                        text = activity!!.intent.getStringExtra("name")
                        textSize = 17f
                        textColor = Color.parseColor("#FF444444")

                    }.lparams() {

                        leftMargin = dip(8)
                    }

                }.lparams() {
                    width = matchParent
                    height = dip(80)
                    leftMargin = dip(15)
                    rightMargin = dip(15)
                }

                textView() {
                    backgroundColor = Color.parseColor("#FFE3E3E3")
                }.lparams() {
                    width = matchParent
                    height = dip(1)

                }

                //////////////////////////////////////////////////////////////////////
                //////////////////////////////////////////////////////////////////////


                linearLayout {
                    gravity=Gravity.CENTER


                    image= imageView {
                        //setImageResource(R.drawable.pic2)
                      //  setTheWidth(screenWidth)

                    }.lparams() {
                        width = matchParent
                        height=matchParent
                        margin = dip(5)
                        rightMargin=dip(10)
                        leftMargin=dip(10)
                    }




                }.lparams() {
                    height = dip(0)
                    weight = 1f
                    width = matchParent
                }



                textView {

                    gravity=Gravity.CENTER
                    text=activity!!.getString(R.string.invite_painter)
                    textSize=16f
                    textColor=Color.WHITE
                    backgroundResource=R.drawable.enable_rectangle_button



                    this.withTrigger().click {



                        lateinit var intent: Intent
                        //跳转详情
                        intent = Intent(activityInstance, InvitationActivity::class.java)
                        //画师/团队的id
                        intent.putExtra("id", activity!!.intent.getStringExtra("id"))

                        startActivity(intent)
                        activity!!.overridePendingTransition(R.anim.right_in, R.anim.left_out)


                    }


                }.lparams() {
                    height = dip(50)
                    width = matchParent
                }


            }
        }.view

        Glide.with(image.context)
            .load(activity!!.intent.getStringExtra("url"))
            .centerCrop()
            .placeholder(R.mipmap.no_pic_show)
            .into(image)




        return view

    }


}