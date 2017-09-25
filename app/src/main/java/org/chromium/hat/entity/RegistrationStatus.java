package org.chromium.hat.entity;

/**
 * Created by Floatingmuseum on 2017/9/25.
 */

public class RegistrationStatus {
    private int result;
    private String region_server_host;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getRegion_server_host() {
        return region_server_host;
    }

    public void setRegion_server_host(String region_server_host) {
        this.region_server_host = region_server_host;
    }

    @Override
    public String toString() {
        return "RegistrationStatus{" +
                "result=" + result +
                ", region_server_host='" + region_server_host + '\'' +
                '}';
    }
}
