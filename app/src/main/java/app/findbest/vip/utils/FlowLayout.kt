package app.findbest.vip.utils

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

import java.util.ArrayList


class FlowLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    ViewGroup(context, attrs, defStyleAttr) {

    private val mLineViews = ArrayList<List<View>>()
    private val mLineHeight = ArrayList<Int>()

    /**
     * 测量所有子View大小,确定ViewGroup的宽高
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        //由于onMeasure会执行多次,避免重复的计算控件个数和高度,这里需要进行清空操作
        mLineViews.clear()
        mLineHeight.clear()

        //获取测量的模式和尺寸大小
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec) - paddingLeft - paddingRight
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec) + paddingTop + paddingBottom


        //记录ViewGroup真实的测量宽高
        var viewGroupWidth = 0 - paddingLeft - paddingRight
        var viewGroupHeight = paddingTop + paddingBottom

        if (widthMode == View.MeasureSpec.EXACTLY && heightMode == View.MeasureSpec.EXACTLY) {
            viewGroupWidth = widthSize
            viewGroupHeight = heightSize
        } else {
            //当前所占的宽高
            var currentLineWidth = 0
            var currentLineHeight = 0

            //用来存储每一行上的子View
            var lineView: MutableList<View> = ArrayList()
            val childViewsCount = childCount
            for (i in 0 until childViewsCount) {
                val childView = getChildAt(i)
                //对子View进行测量
                measureChild(childView, widthMeasureSpec, heightMeasureSpec)
//                val marginLayoutParams = childView.layoutParams as ViewGroup.MarginLayoutParams
//                val leftMargin=marginLayoutParams.leftMargin
//                val topMargin=marginLayoutParams.topMargin
//                val rightMargin=marginLayoutParams.rightMargin
//                val bottomMargin=marginLayoutParams.bottomMargin

                val leftMargin=0
                val topMargin=0
                val rightMargin=0
                val bottomMargin=0
                val childViewWidth =
                    childView.measuredWidth +leftMargin +rightMargin
                val childViewHeight =
                    childView.measuredHeight + topMargin +bottomMargin

                if (currentLineWidth + childViewWidth > widthSize) {
                    //当前行宽+子View+左右外边距>ViewGroup的宽度,换行
                    viewGroupWidth = Math.max(currentLineWidth, widthSize)
                    viewGroupHeight += currentLineHeight
                    //添加行高
                    mLineHeight.add(currentLineHeight)
                    //添加行对象
                    mLineViews.add(lineView)

                    //new新的一行
                    lineView = ArrayList()
                    //添加行对象里的子View
                    lineView.add(childView)
                    currentLineWidth = childViewWidth

                } else {
                    //当前行宽+子View+左右外边距<=ViewGroup的宽度,不换行
                    currentLineWidth += childViewWidth
                    currentLineHeight = Math.max(currentLineHeight, childViewHeight)
                    //添加行对象里的子View
                    lineView.add(childView)
                }


                if (i == childViewsCount - 1) {
                    //最后一个子View的时候
                    //添加行对象
                    mLineViews.add(lineView)
                    viewGroupWidth = Math.max(currentLineWidth, widthSize)
                    viewGroupHeight += childViewHeight
                    //添加行高
                    mLineHeight.add(currentLineHeight)

                }


            }

        }


        setMeasuredDimension(viewGroupWidth, viewGroupHeight)

    }

    /**
     * 设置ViewGroup里子View的具体位置
     *
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

        var left = paddingLeft
        var top = paddingTop
        //一共有几行
        val lines = mLineViews.size
        for (i in 0 until lines) {
            //每行行高
            val lineHeight = mLineHeight[i]
            //行内有几个子View
            val viewList = mLineViews[i]
            val views = viewList.size

            for (j in 0 until views) {
                val view = viewList[j]
//                val marginLayoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
//                val leftMargin=marginLayoutParams.leftMargin
//                val topMargin=marginLayoutParams.topMargin
                val leftMargin=0
                val topMargin=0
                val vl = left + leftMargin
                val vt = top + topMargin
                val vr = vl + view.measuredWidth
                val vb = vt + view.measuredHeight
                view.layout(vl, vt, vr, vb)
                left += view.measuredWidth + leftMargin + topMargin
            }
            left = paddingLeft
            top += lineHeight

        }


    }

    /**
     * 指定ViewGroup的LayoutParams
     *
     * @param attrs
     * @return
     */
    override fun generateLayoutParams(attrs: AttributeSet): ViewGroup.LayoutParams {
        return ViewGroup.MarginLayoutParams(context, attrs)
    }

}
