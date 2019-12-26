package app.findbest.vip.message.fragment

import android.os.Bundle
import android.view.*
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.findbest.vip.R
import app.findbest.vip.message.adapter.MessageChatRecordListAdapter
import app.findbest.vip.message.model.ChatRecordModel
import app.findbest.vip.utils.recyclerView


import com.facebook.react.bridge.UiThreadUtil.runOnUiThread
import imui.jiguang.cn.imuisample.messages.MessageListActivity
import org.json.JSONArray


class MessageChatRecordListFragment : Fragment(){


    private var mContext: Context? = null
    lateinit var recycler : RecyclerView
    lateinit var adapter: MessageChatRecordListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(parentFragment==null){
            mContext = activity
        }else{
            mContext = parentFragment?.context
        }
    }

    companion object {


        fun newInstance(): MessageChatRecordListFragment {
            val fragment = MessageChatRecordListFragment()
            return fragment
        }
    }


    override fun onStart() {
        super.onStart()
        println("联系人列表页面加载成功")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var fragmentView=createView()

        return fragmentView
    }

    fun createView(): View {
        val view = UI {
            linearLayout {
                backgroundResource= R.color.white
                linearLayout {

                    recycler= recyclerView{
                        overScrollMode = View.OVER_SCROLL_NEVER
                        var manager= LinearLayoutManager(this.getContext())
                        setLayoutManager(manager)
                        //manager.setStackFromEnd(true);

                    }.lparams {
                        leftMargin=dip(14)
                        rightMargin=dip(14)
                    }
                }.lparams {
                    width= matchParent
                    height=matchParent
                }
            }
        }.view

        adapter = MessageChatRecordListAdapter(recycler, mutableListOf()) { item ->
            var intent =Intent(mContext, MessageListActivity::class.java)
            intent.putExtra("hisId",item.uid)
            intent.putExtra("companyName",item.companyName)
            intent.putExtra("hisName",item.userName)



            intent.putExtra("hislogo",item.avatar)




            if(item.lastPositionId==null || "".equals(item.lastPositionId)){

                println("跳转到聊天界面之前,数据异常:缺少lastPositionId")
            }else{
                println("跳转到聊天界面之前position_id"+item.lastPositionId)
            }

            intent.putExtra("position_id",item.lastPositionId)

  

            startActivity(intent)
            activity!!.overridePendingTransition(R.anim.right_in, R.anim.left_out)
        }
        recycler.setAdapter(adapter)

        setRecyclerAdapter(
            MessageChatRecordFragment.chatRecordList
        )




        return view
    }


    fun setRecyclerAdapter(chatRecordList: MutableList<ChatRecordModel>){
        runOnUiThread(Runnable {
            adapter.setChatRecords(chatRecordList)
        })
    }


}

