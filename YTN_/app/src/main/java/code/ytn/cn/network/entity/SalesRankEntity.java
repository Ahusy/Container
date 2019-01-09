package code.ytn.cn.network.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dell on 2018/7/11
 */

public class SalesRankEntity extends Entity {

    @SerializedName("goods_list")
    private List<SalesRankListEntity> listEntities;

    public List<SalesRankListEntity> getListEntities() {
        return listEntities;
    }

    public void setListEntities(List<SalesRankListEntity> listEntities) {
        this.listEntities = listEntities;
    }

    public class SalesRankListEntity{
        @SerializedName("sequence")
        private String num;

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        @SerializedName("fname")
        private String fname;
        @SerializedName("specs")
        private String specs;
        @SerializedName("nowCount")
        private String nowCount;
        @SerializedName("ringRatio")
        private String ringRatio;
        @SerializedName("trend")
        private String trend;

        public String getFname() {
            return fname;
        }

        public void setFname(String fname) {
            this.fname = fname;
        }

        public String getSpecs() {
            return specs;
        }

        public void setSpecs(String specs) {
            this.specs = specs;
        }

        public String getNowCount() {
            return nowCount;
        }

        public void setNowCount(String nowCount) {
            this.nowCount = nowCount;
        }

        public String getRingRatio() {
            return ringRatio;
        }

        public void setRingRatio(String ringRatio) {
            this.ringRatio = ringRatio;
        }

        public String getTrend() {
            return trend;
        }

        public void setTrend(String trend) {
            this.trend = trend;
        }
    }
}
