package app.findbest.vip.individual.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.findbest.vip.R
import com.google.gson.JsonObject
import org.jetbrains.anko.*
import java.text.SimpleDateFormat
import java.util.*


class PersonAccountAdapter(
    context: Context,
    private val mDataSet: MutableList<JsonObject> = mutableListOf()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private var mContext: Context = context
    private lateinit var balance: TextView
    private lateinit var freezeBalance: TextView
    private lateinit var country: TextView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = with(parent.context) {
                verticalLayout {
                    backgroundResource = R.drawable.ffe3e3e3_bottom_line
                    relativeLayout {
                        country = textView {
                            textSize = 13f
                            textColor = Color.parseColor("#666666")
                        }.lparams(wrapContent, wrapContent) {
                            alignParentLeft()
                            centerVertically()
                        }

                        balance = textView {
                            textSize = 20f
                            textColor = Color.parseColor("#333333")
                        }.lparams(wrapContent, wrapContent){
                            alignParentRight()
                            centerVertically()
                        }
                    }.lparams(matchParent, dip(45))

                    relativeLayout {
                        textView {
                            textResource = R.string.tl_frozen_amount
                            textSize = 13f
                            textColor = Color.parseColor("#FF666666")
                        }.lparams(wrapContent, wrapContent) {
                            alignParentLeft()
                            centerVertically()
                        }

                        freezeBalance = textView {
                            textSize = 20f
                            textColor = Color.parseColor("#FF333333")
                        }.lparams(wrapContent, wrapContent) {
                            alignParentRight()
                            centerVertically()
                        }
                    }.lparams(matchParent, dip(45))
                }
        }
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(h: RecyclerView.ViewHolder, position: Int) {

        val aaaa = mDataSet[position]["currency"].asJsonObject
        when(aaaa["name"].asString){
            "CNY" -> country.textResource = R.string.tl_rmb
            "USD" -> country.textResource = R.string.tl_usd
            "JPY" -> country.textResource = R.string.tl_jpy
            "KRO" -> country.textResource = R.string.tl_won
        }

        balance.text = mDataSet[position]["balance"].asString
        freezeBalance.text = mDataSet[position]["freezeBalance"].asString

    }

    override fun getItemCount(): Int {
        return mDataSet.size
    }

    private inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    fun resetItems(items: MutableList<JsonObject>) {
        mDataSet.addAll(items)
        notifyDataSetChanged()
    }
}