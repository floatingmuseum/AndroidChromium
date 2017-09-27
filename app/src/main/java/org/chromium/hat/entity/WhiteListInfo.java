package org.chromium.hat.entity;

import java.util.List;
import java.util.Map;

/**
 * Created by Floatingmuseum on 2017/9/20.
 */

public class WhiteListInfo {

    private int isUse;
    private List<String> swl;
    private List<String> awl;
    private List<String> abl;
    private List<String> sbl;
    private Map<String, NetTime[]> netTime;
    private Boolean if_can_download_file;

    public int getIsUse() {
        return isUse;
    }

    public void setIsUse(int isUse) {
        this.isUse = isUse;
    }

    public List<String> getSwl() {
        return swl;
    }

    public void setSwl(List<String> swl) {
        this.swl = swl;
    }

    public List<String> getAwl() {
        return awl;
    }

    public void setAwl(List<String> awl) {
        this.awl = awl;
    }

    public List<String> getAbl() {
        return abl;
    }

    public void setAbl(List<String> abl) {
        this.abl = abl;
    }

    public List<String> getSbl() {
        return sbl;
    }

    public void setSbl(List<String> sbl) {
        this.sbl = sbl;
    }

    public Map<String, NetTime[]> getNetTime() {
        return netTime;
    }

    public void setNetTime(Map<String, NetTime[]> netTime) {
        this.netTime = netTime;
    }

    public Boolean getIf_can_download_file() {
        return if_can_download_file;
    }

    public void setIf_can_download_file(Boolean if_can_download_file) {
        this.if_can_download_file = if_can_download_file;
    }

    public static class NetTime{
        public String sTime;
        public String eTime;
    }

    @Override
    public String toString() {
        return "WhiteListInfo{" +
                "isUse=" + isUse +
                ", swl=" + swl +
                ", awl=" + awl +
                ", abl=" + abl +
                ", sbl=" + sbl +
                ", netTime=" + netTime +
                ", if_can_download_file=" + if_can_download_file +
                '}';
    }
}
