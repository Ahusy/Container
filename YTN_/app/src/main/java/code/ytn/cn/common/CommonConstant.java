package code.ytn.cn.common;

/**
 * 公共常量
 */
public interface CommonConstant {

	String EXTRA_FROM = "extra_from";
	String EXTRA_TYPE = "extra_type";

	String NUM = "num";
	String LACK_NUM = "lack_num";
	String TIME = "time";
	String ID = "id";
	String OUT_ID = "out_id";
	String CHECK_ID = "check_id";
	String STORE_ID = "store_id";
	String CHOOSE_ID = "choseId";
	String CABINET_ID = "cabinet_id";
	String USER_ID = "user_id";
	String RELATION_ID = "relation_id";

	String STORE_CABINET = "store_cabinet";
	String YTO_ORDER = "yto_order";
	String YTO_GOODS = "yto_goods";
	String YTO_MONEY = "yto_money";
	String TO_ORDER = "to_order";
	String YXD_COUNT = "yxd_count";
	String TXD_COUNT = "txd_count";
	String T_COUNT = "t_count";

	String NAME = "name";
	String STORE_NAME = "store_name";
	String CABINET_NAME = "cabinet_name";
	String CHOOSE_NAME = "choseName";
	String DEPOT_NAME = "depot_name";
	String USER_NAME = "user_name";
	String PRO_NAME = "pro_name";
	String OP_NAME = "op_name";
	String RELATION_NAME = "relation_name";
	String CODE = "code";
	String RELATION_CODE = "relation_code";

	String TYPE = "type";
	String PAY_TYPE = "pay_type";
	String ERROR_STATUS = "error_status";
	String ADDRESS = "address";
	String DISTANCE = "distance";
	String OPEN_DEPOT = "open_depot";
	String STATUS = "status";
	String OPERATOR = "operator";
	String MONEY = "money";
	String REQUEST_NET_SUCCESS = "0";//网络请求成功
	String SEARCH_PRE_KEY_HISTORY = "search_pre_key_history";//搜索历史记录
	String LIST = "list";

	String STATUS_ZERO = "0";
	String STATUS_ONE = "1";// 状态
	String STATUS_TWO = "2";// 状态
	String STATUS_THREE = "3";// 状态
	String STATUS_FOUR = "4";

	// 经纬度
	String LONG_ITUDE = "longitude";
	String LATI_TUDE = "latitude";

	// 订单状态
	String ORDER_ZERO = "0";
	String ORDER_ONE = "1";
	String ORDER_TWO = "2";
	String ORDER_THREE = "3";
	String ORDER_FOUR = "4";
	String ORDER_FIVE = "5";
	String ORDER_SIX = "6";
	String ORDER_SEVEN = "7";
	String ORDER_EIGHT = "8";
	String ORDER_NINE = "9";


	int ALLOT = 1;
	int REPLMENT = 2;
	int NEWINPUT = 3;
	int NEW_OUT = 4;

	String PAGENUM = "current_page";
	String PAGESIZE = "page_size";
	String PAGE_SIZE_10 = "10";
	String PAGE_SIZE_20 = "20";

	// 签入签出
	String CLOCKIN = "clockIn";

	//语言
	String LANGUAGE_SIMPLIFIED_CHINESE = "zh_CN";
	String LANGUAGE_TRADITIONAL_CHINESE = "zh_HK";
	String LANGUAGE_KOREAN = "ko";
	String LANGUAGE_JAPANESE = "ja";
	String LANGUAGE_RUSSIAN = "ru";
	String LANGUAGE_MALAY_MALAYSIA = "ms_MY";
	String LANGUAGE_ENGLISH = "en";

	//商品列表排序
	String ORDER_FILED_VOLUME = "sale_num";
	String ORDER_FILED_PRICE = "selling_price";
	String ORDER_TYPE_DESC = "desc";
	String ORDER_TYPE_ASC = "asc";

	//请求网络，请求码，必须要情况下可以追加自定义名称
	int REQUEST_NET_ONE = 1;
	int REQUEST_NET_TWO = 2;
	int REQUEST_NET_THREE = 3;
	int REQUEST_NET_FOUR = 4;
	int REQUEST_NET_FIVE = 5;
	int REQUEST_NET_SIX = 6;
	int REQUEST_NET_SEVEN = 7;
	int REQUEST_NET_EIGHT = 8;
	int REQUEST_NET_NINE = 9;
	int REQUEST_NET_TEN = 10;

	//使用startActivityForResult时的请求码
	int REQUEST_ACT_ONE = 1;
	int REQUEST_ACT_TWO = 2;
	int REQUEST_ACT_THREE = 3;
	int REQUEST_ACT_FOUR = 4;
	int REQUEST_ACT_FIVE = 5;


	//权限配置时
	int REQUEST_CAMERA_PERMISSION_CODE = 1;//相机
	int REQUEST_STORAGE_PERMISSION_CODE = 2;//内存

	//from
	int FROM_ACT_ONE = 1;
	int FROM_ACT_TWO = 2;
	int FROM_ACT_THREE = 3;
	int FROM_ACT_FOUR = 4;
	int FROM_ACT_FIVE = 5;


	//选择规格
	int FROM_PRODUCT_DETAIL = 1;
	int FROM_SHOP_CAR_LIST = 2;
	int FROM_DRAW_DETAIL = 3;

	//上传图片地址
	String FRONT_PATH = "/sdcard/front.jpg";
	String BACK_PATH = "/sdcard/back.jpg";
	String AVATAR_PATH = "/sdcard/back.jpg";

	//编辑地址类型
	int TYPE_ADD = 1;//新增
	int TYPE_EDIT = 2;//编辑

}
