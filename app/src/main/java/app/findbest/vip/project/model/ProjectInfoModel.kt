package app.findbest.vip.project.model

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import java.io.Serializable

data class ProjectInfoModel(
    var id: String, //项目主键
    var name: String, //项目名称
    var styles: JsonArray, //风格数组
    var color: Int, //颜色模式  RGB 模式 = 3,CMYK 模式 = 6,不限 = 12
    var size: String, //规格尺寸
    var format: Int, //稿件格式 PSD=3,JPEG=6,PNG=9,其他格式=12
    var pubPerm: Int, //发布权限 不允许公开=3,按约定公开=6,可公开=9
    var conception: String, //稿件构思
    var testing: Int, //试稿要求无需试稿=3,有偿试稿=6,无偿试稿=9
    var supplement: String, //补充说明
    var samples: JsonArray, //参考图例数组
    var commitAt: Long, //预计交付时间
    var payAt: Long, //预计结算时间
    var payCurrency: JsonObject, //支付币种
    var entityCount: Int, //原画数量
    var userBounty: String, //个人单张稿酬
    var orgBounty: String, //企业单张稿酬
    var publicity: Int, //允许应征方, 0 表示不公开, 1 表示只允许个人, 2 表示只允许公司, 3 表示允许个人和公司
    var guaranteeType: Int, //担保类型 平台担保 = 6  不使用平台担保 = 12
    var margin: String, //保证金
    var tax: Int, //税金
    var allowedCountries: ArrayList<String>, //允许应征的国家
    var bonuses: JsonArray, //项目评价,奖金
    var stages: JsonArray, //项目阶段有几个阶段
    var status: Int, //项目状态, 包含项目审核状态和制作阶段
    var category: String, //分类
    var createAt: Long, //记录创建时间
    var pubAt: Long, //发布时间
    var consumer: JsonObject //发包方

): Serializable