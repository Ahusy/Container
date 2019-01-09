package code.ytn.cn.network;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import code.ytn.cn.network.entity.AreaEntity;
import code.ytn.cn.network.entity.BlockEntity;
import code.ytn.cn.network.entity.ChannelEntity;
import code.ytn.cn.network.entity.ChooseGoodsEntity;
import code.ytn.cn.network.entity.ChooseGoodsListEntity;
import code.ytn.cn.network.entity.CounterEntity;
import code.ytn.cn.network.entity.Entity;
import code.ytn.cn.network.entity.FacilityStatusEntity;
import code.ytn.cn.network.entity.InOrderListEntity;
import code.ytn.cn.network.entity.InputGoodsDetailEntity;
import code.ytn.cn.network.entity.InputGoodsEntity;
import code.ytn.cn.network.entity.ItemResultEntity;
import code.ytn.cn.network.entity.MessageEntity;
import code.ytn.cn.network.entity.OpenCounterEntity;
import code.ytn.cn.network.entity.OrderMissDetailEntity;
import code.ytn.cn.network.entity.OrderMissEntity;
import code.ytn.cn.network.entity.OrderQueryDetailEntity;
import code.ytn.cn.network.entity.OrderQueryEntity;
import code.ytn.cn.network.entity.OutGoodsDetailEntity;
import code.ytn.cn.network.entity.OutGoodsEntity;
import code.ytn.cn.network.entity.ReperCheckDetailEntity;
import code.ytn.cn.network.entity.RepertoryCheckEntity;
import code.ytn.cn.network.entity.ReplmentDetailEntity;
import code.ytn.cn.network.entity.ReplmentEntity;
import code.ytn.cn.network.entity.SalesRankEntity;
import code.ytn.cn.network.entity.ShopHomeDetailEntity;
import code.ytn.cn.network.entity.ShopHomeListEntity;
import code.ytn.cn.network.entity.StatusEntity;
import code.ytn.cn.network.entity.UpdateEntity;
import code.ytn.cn.network.entity.UserEntity;
import code.ytn.cn.network.json.GsonObjectDeserializer;


/**
 * json解析
 */
public class Parsers {

    private static Gson gson = GsonObjectDeserializer.produceGson();

    /**
     * @return 所有请求的公共部分，业务层的返回码和返回提示
     */
    public static Entity getResult(String data) {
        Entity result = gson.fromJson(data, new TypeToken<Entity>() {
        }.getType());
//        LogUtil.d("======data======",""+data);
        if (result == null) {
            result = new Entity();
        }
        return result;
    }

    /**
     * @return 版本更新
     */
    public static UpdateEntity getUpdate(String data) {
        return gson.fromJson(data, new TypeToken<UpdateEntity>() {
        }.getType());
    }


    /**
     * @return 获取地区列表
     */
    public static List<AreaEntity> getAreaList(String data) {
        List<AreaEntity> areaEntities = null;
        try {
            JSONObject jsonObject = new JSONObject(data);
            areaEntities = gson.fromJson(jsonObject.optString("site_info"), new TypeToken<List<AreaEntity>>() {
            }.getType());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return areaEntities;
    }

    /**
     * 获取token
     **/
    public static UserEntity getUserInfo(String data) {
        return gson.fromJson(data, new TypeToken<UserEntity>() {
        }.getType());
    }

    /**
     * 获取货柜列表
     **/
    public static CounterEntity getCounter(String data) {
        return gson.fromJson(data, new TypeToken<CounterEntity>() {
        }.getType());
    }

    /**
     * 入库单查询
     **/
    public static InputGoodsEntity getInputGoods(String data) {
        return gson.fromJson(data, new TypeToken<InputGoodsEntity>() {
        }.getType());
    }

    /**
     * 入库单详情
     **/
    public static InputGoodsDetailEntity getInputGoodsDetail(String data) {
        return gson.fromJson(data, new TypeToken<InputGoodsDetailEntity>() {
        }.getType());
    }

    /**
     * 执行入库
     **/
    public static InOrderListEntity getInOrderList(String data) {
        return gson.fromJson(data, new TypeToken<InOrderListEntity>() {
        }.getType());
    }

    /**
     * 出库单查询
     **/
    public static OutGoodsEntity getOutGoods(String data) {
        return gson.fromJson(data, new TypeToken<OutGoodsEntity>() {
        }.getType());
    }

    /**
     * 出库单详情
     **/
    public static OutGoodsDetailEntity getOutGoodsDetailEntity(String data) {
        return gson.fromJson(data, new TypeToken<OutGoodsDetailEntity>() {
        }.getType());
    }

    /**
     * 选择调出货柜商品
     **/
    public static ChooseGoodsEntity getChooseGoodsEntity(String data) {
        return gson.fromJson(data, new TypeToken<ChooseGoodsEntity>() {
        }.getType());
    }


    /**
     * 盘点单查询
     **/
    public static RepertoryCheckEntity getReperCheck(String data) {
        return gson.fromJson(data, new TypeToken<RepertoryCheckEntity>() {
        }.getType());
    }


    /**
     * 盘点单详情查询
     **/
    public static ReperCheckDetailEntity getReperCheckDeatil(String data) {
        return gson.fromJson(data, new TypeToken<ReperCheckDetailEntity>() {
        }.getType());
    }


    /**
     * 订单查询
     **/
    public static OrderQueryEntity getOrderQueryEntity(String data) {
        return gson.fromJson(data, new TypeToken<OrderQueryEntity>() {
        }.getType());
    }

    /**
     * 订单详情
     **/
    public static OrderQueryDetailEntity getOrderQueryDetailEntity(String data) {
        return gson.fromJson(data, new TypeToken<OrderQueryDetailEntity>() {
        }.getType());
    }

    public static OpenCounterEntity getOpenCounterEntity(String data) {
        return gson.fromJson(data, new TypeToken<OpenCounterEntity>() {
        }.getType());
    }

    // 锁定冰柜
    public static BlockEntity getBlockEntity(String data) {
        return gson.fromJson(data, new TypeToken<BlockEntity>() {
        }.getType());
    }

    /**
     * 获取补货单列表
     **/
    public static ReplmentEntity getReplmentEntity(String data) {
        return gson.fromJson(data, new TypeToken<ReplmentEntity>() {
        }.getType());
    }

    /**
     * 补货单详情
     **/
    public static ReplmentDetailEntity getReplmentDetailEntity(String data) {
        return gson.fromJson(data, new TypeToken<ReplmentDetailEntity>() {
        }.getType());
    }


    public static StatusEntity getStatusEntity(String data) {
        return gson.fromJson(data, new TypeToken<StatusEntity>() {
        }.getType());
    }

    /**
     * 店长首页列表
     **/
    public static ShopHomeListEntity getShopHomeListEntity(String data) {
        return gson.fromJson(data, new TypeToken<ShopHomeListEntity>() {
        }.getType());
    }

    /**
     * 店长首页详情列表
     **/
    public static ShopHomeDetailEntity getShopHomeDetailEntity(String data) {
        return gson.fromJson(data, new TypeToken<ShopHomeDetailEntity>() {
        }.getType());
    }

    /**
     * 店长首页详情列表
     **/
    public static ChooseGoodsListEntity getChooseGoodsListEntity(String data) {
        return gson.fromJson(data, new TypeToken<ChooseGoodsListEntity>() {
        }.getType());
    }

    /**
     * 设备状态
     **/
    public static FacilityStatusEntity getFacilityStatusEntity(String data) {
        return gson.fromJson(data, new TypeToken<FacilityStatusEntity>() {
        }.getType());
    }

    /**
     * 消单列表
     **/
    public static OrderMissEntity getOrderMissEntity(String data) {
        return gson.fromJson(data, new TypeToken<OrderMissEntity>() {
        }.getType());
    }

    /**
     * 消单列表详情
     **/
    public static OrderMissDetailEntity getOrderMissDetailEntity(String data) {
        return gson.fromJson(data, new TypeToken<OrderMissDetailEntity>() {
        }.getType());
    }

    /**
     * 商品排行
     **/
    public static SalesRankEntity getSalesRankEntity(String data) {
        return gson.fromJson(data, new TypeToken<SalesRankEntity>() {
        }.getType());
    }

    /**
     * 消息列表
     **/
    public static MessageEntity getMessageEntity(String data) {
        return gson.fromJson(data, new TypeToken<MessageEntity>() {
        }.getType());
    }

    /**
     * 消息详情
     **/
    public static MessageEntity.MessageListEntity getMessageDetailEntity(String data) {
        return gson.fromJson(data, new TypeToken<MessageEntity.MessageListEntity>() {
        }.getType());
    }

    /**
     * 渠道号
     **/
    public static ChannelEntity getChannelEntity(String data) {
        return gson.fromJson(data, new TypeToken<ChannelEntity>() {
        }.getType());
    }

    /**
     * 获取优惠后的商品
     **/
    public static ItemResultEntity getItemsResult(String data) {
        return gson.fromJson(data, new TypeToken<ItemResultEntity>() {
        }.getType());
    }

}