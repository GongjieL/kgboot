package com.gjie.kgboot.api.client.redis;

public class CacheExecuteResult {
    private Boolean executeSuccess;

    public CacheExecuteResult(Boolean executeSuccess) {
        this.executeSuccess = executeSuccess;
    }

    public Boolean getExecuteSuccess() {
        return executeSuccess;
    }

    public void setExecuteSuccess(Boolean executeSuccess) {
        this.executeSuccess = executeSuccess;
    }
}
