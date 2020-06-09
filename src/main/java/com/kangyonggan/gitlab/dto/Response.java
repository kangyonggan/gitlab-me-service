package com.kangyonggan.gitlab.dto;

import com.alibaba.fastjson.JSONObject;
import com.kangyonggan.gitlab.constants.Resp;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 通用响应
 *
 * @author kyg
 */
@Data
public class Response implements Serializable {

    /**
     * 响应码
     */
    private String respCo;

    /**
     * 响应消息
     */
    private String respMsg;

    /**
     * 响应数据
     */
    private Map<String, Object> data = new HashMap<>(16);

    /**
     * 置为失败
     *
     * @return
     */
    public Response failure() {
        return failure(Resp.FAILURE.getRespCo(), Resp.FAILURE.getRespMsg());
    }

    /**
     * 置为失败
     *
     * @param respMsg
     * @return
     */
    public Response failure(String respMsg) {
        return failure(Resp.FAILURE.getRespCo(), respMsg);
    }

    /**
     * 置为失败
     *
     * @param respCo
     * @param respMsg
     * @return
     */
    public Response failure(String respCo, String respMsg) {
        this.respCo = respCo;
        this.respMsg = respMsg;
        this.data.clear();
        return this;
    }

    /**
     * put
     *
     * @param key
     * @param value
     * @return
     */
    public Response put(String key, Object value) {
        if (data == null) {
            data = new HashMap<>(16);
        }

        data.put(key, value);
        return this;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
