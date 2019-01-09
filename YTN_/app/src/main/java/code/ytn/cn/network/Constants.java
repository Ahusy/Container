package code.ytn.cn.network;

/**
 * Created by dell on 18/3/17.
 * 接口地址
 */
public class Constants {

    public static class Urls {
        // 测试环境域名
//        public static String URL_BASE_DOMAIN = "http://39.104.82.242:8081";

        // 正式环境域名
        public static String URL_BASE_DOMAIN = "https://api.yitunnel.com";
        // 正式环境域名
//        public static String URL_BASE_DOMAIN = "https://testapi.yitunnel.com";
        // 海航
//        public static String URL_BASE_DOMAIN = "https://vending-dev.yitunnel.com";
        // 陈飞
//        public static String URL_BASE_DOMAIN = "http://192.168.16.101:8080";
//        public static String URL_BASE_DOMAIN = "http://39.104.82.242:8081";

//        public static String URL_BASE_DOMAIN = "https://wxapp.antke.cn";

//        public static String URL_BASE_DOMAIN = "http://192.168.2.7:8081";

        // 姜涛
//        public static String URL_BASE_DOMAIN = "http://192.168.31.18:8080";
        // 泰华
//        public static String URL_BASE_DOMAIN = "http://192.168.31.137:8080";
//        public static String URL_BASE_DOMAIN = "http://117.107.169.45:8080";

        // FT
//        public static String URL_BASE_DOMAIN = "http://192.168.16.109:8080";
//        public static String URL_BASE_DOMAIN = "http://192.168.31.159:8081";
//        public static String URL_BASE_DOMAIN = "http://39.104.109.220:8088";

        // YZY
//        public static String URL_BASE_DOMAIN = "http://192.168.31.44:8080";


        // 关门
        public static String URL_POST_CLOSE = URL_BASE_DOMAIN + "/AppCloseTheDoor";
        // 开门
        public static String URL_POST_OPEN = URL_BASE_DOMAIN + "/AppOpenTheDoor";
        // 清除信息
        public static String URL_POST_CLEAR_USER_INFO = URL_BASE_DOMAIN + "/AppClearUserInfo";

        // 版本更新
        public static String URL_POST_CHECK_UPDATE = URL_BASE_DOMAIN + "/versionUpdate";

        // 登录
        public static String URL_POST_LOGIN = URL_BASE_DOMAIN + "/login";
        // 验证码
        public static String URL_POST_GET_IDENTIFY_CODE = URL_BASE_DOMAIN + "/getIdentifyCode";
        // 忘记密码
        public static String URL_POST_RETRIEVE_PASSWORD = URL_BASE_DOMAIN + "/retrievePassword";
        // 货柜管理
        public static String URL_POST_CABINET_MANAGE = URL_BASE_DOMAIN + "/cabinetManage";
        // 入库单列表查询
        public static String URL_POST_IN_ORDERLIST = URL_BASE_DOMAIN + "/inOrderList";
        // 入库单详情查询
        public static String URL_POST_IN_ORDERLIST_DETAIL = URL_BASE_DOMAIN + "/inOrderDetail";
        // 入库
        public static String URL_POST_GET_IN_STORAGE = URL_BASE_DOMAIN + "/inStorage";
        // 出库列表查询
        public static String URL_POST_OUT_STORAGELIST = URL_BASE_DOMAIN + "/outStorageList";
        // 出库详情查询
        public static String URL_POST_OUT_ORDER_DETAIL = URL_BASE_DOMAIN + "/outOrderDetail";
        // 出库
        public static String URL_POST_GET_OUT_STORAGE = URL_BASE_DOMAIN + "/outStorage";
        // 货柜列表查询
//        public static String URL_POST_GET_CABINET_LIST = URL_BASE_DOMAIN + "/cabinetList";
        // 选择调出货柜商品
        public static String URL_POST_CABINET_GOODSLIST = URL_BASE_DOMAIN + "/cabinetGoodsList";
        // 提交调拨单
        public static String URL_POST_GET_COMMIT_REQUISITION = URL_BASE_DOMAIN + "/transfers";
        // 盘点单
        public static String URL_POST_GET_CHECK_ORDERLIST = URL_BASE_DOMAIN + "/checkOrderList";
        // 盘点详情
        public static String URL_POST_GET_CHECK_ORDERLIST_DETAIL = URL_BASE_DOMAIN + "/checkOrderDetails";
        // 提交盘点单
        public static String URL_POST_GET_REFER_CHECKORDER = URL_BASE_DOMAIN + "/addCheckOrder";
        // 订单查询
        public static String URL_POST_GET_GET_ORDERLIST = URL_BASE_DOMAIN + "/getOrderList";
        // 订单详情
        public static String URL_POST_ORDER_DETAILS = URL_BASE_DOMAIN + "/orderDetails";
        // 设备报修
        public static String URL_POST_REPAIR = URL_BASE_DOMAIN + "/repair";
        // 锁定冰柜
        public static String URL_POST_GET_LOCK_CABINET = URL_BASE_DOMAIN + "/lockCabinet";
        //地区
        public static String URL_POST_AREA = URL_BASE_DOMAIN + "/selectSiteInfo";
        // 查询货柜状态
        public static String URL_POST_GET_CABINET_STATUS = URL_BASE_DOMAIN + "/getCabinetStatus";
        // 申请补货
        public static String URL_POST_ADD_GOODS = URL_BASE_DOMAIN + "/addGoods";
        // 补货列表
        public static String URL_POST_ORDER_ADD_LIST = URL_BASE_DOMAIN + "/OrderAddList";
        // 补货详情
        public static String URL_POST_ORDER_GOODS_LIST = URL_BASE_DOMAIN + "/OrderGoodsList";
        // 新建入库单
        public static String URL_POST_NODEPOT_IN_STORAGE = URL_BASE_DOMAIN + "/NodepotInStorage";
        // 新建出库单
        public static String URL_POST_NODEPOT_OUT_STORAGE = URL_BASE_DOMAIN + "/NodepotOutStorage";
        // 查询消息列表
        public static String URL_POST_QUERY_MESSAGE_LIST = URL_BASE_DOMAIN + "/queryMessageList";
        // 消息详情
        public static String URL_POST_MESSAGE_DETAIL = URL_BASE_DOMAIN + "/messageDetail";
        // 删除消息
        public static String URL_POST_DELETE_MESSAGE = URL_BASE_DOMAIN + "/deleteMessage";

        /**
         * 吉野家
         */

        // 获取货柜渠道
        public static String URL_POST_GET_CABINET_CHANNEL = URL_BASE_DOMAIN + "/getCabinetChannel";
        // 签入签出
        public static String URL_POST_STORE_SIGN = URL_BASE_DOMAIN + "/storeSign";
        // 店长首页
        public static String URL_POST_GET_STORE_LIST = URL_BASE_DOMAIN + "/getStoreList";
        // 店长首页（详情）
        public static String URL_POST_GET_STORE_DETAIL = URL_BASE_DOMAIN + "/getStoreDetail";
        // 订单消单
        public static String URL_POST_CANCLE_ORDER = URL_BASE_DOMAIN + "/cancleOrder";
        // 店铺状态
        public static String URL_POST_CHANGE_STAUTS = URL_BASE_DOMAIN + "/changeStatus";
        // 订单状态
        public static String URL_POST_STORE_ORDER_LIST = URL_BASE_DOMAIN + "/getStoreOrderList";
        // 提交异常订单信息
        public static String URL_POST_SUBMIT_ABNORMAL = URL_BASE_DOMAIN + "/submitAbnormal";
        // 代客下单
        public static String URL_POST_REPLACE_PLACE_ORDER = URL_BASE_DOMAIN + "/replaceOrder";
        // 商品调价
        public static String URL_POST_MODIFY_PRICE = URL_BASE_DOMAIN + "/modifyPrice";
        // 条形码商品
        public static String URL_POST_GET_GOODS_DETAIL = URL_BASE_DOMAIN + "/getgoodsDetail";
        // 提交库存预警设置
        public static String URL_POST_UPDATE_STOCK_WARNING = URL_BASE_DOMAIN + "/updateStockWarning";
        // 查看缺货商品
        public static String URL_POST_GOODS_WARNING = URL_BASE_DOMAIN + "/cabinetGoodsWarning";
        // 查看设备状态
        public static String URL_POST_CHECK_CABINET_STATUS = URL_BASE_DOMAIN + "/checkCabinetStatus";
        // 查询消单
        public static String URL_POST_GET_CANCEL_ORDER_LIST = URL_BASE_DOMAIN + "/getCancelOrderList";
        // 消单详情
        public static String URL_POST_GET_CANCEL_ORDER_DETAIL = URL_BASE_DOMAIN + "/getCancelOrderDetails";
        // 售卖排行
        public static String URL_POST_GET_SALES_STATIS_LIST = URL_BASE_DOMAIN + "/getSalesStatisticsList";
        // 商品金额
        public static String URL_POST_GET_TOTAL_MONEY = URL_BASE_DOMAIN + "/getTotalMoney";
        // 查询优惠
        public static String URL_POST_GET_ITEM_RESULT = URL_BASE_DOMAIN + "/getItemsResult";

    }
}
