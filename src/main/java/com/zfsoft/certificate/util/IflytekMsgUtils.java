package com.zfsoft.certificate.util;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.iflytek.fsp.shield.java.sdk.constant.HttpConstant;
import com.iflytek.fsp.shield.java.sdk.constant.SdkConstant;
import com.iflytek.fsp.shield.java.sdk.enums.Method;
import com.iflytek.fsp.shield.java.sdk.http.ApiClient;
import com.iflytek.fsp.shield.java.sdk.http.BaseApp;
import com.iflytek.fsp.shield.java.sdk.model.ApiRequest;
import com.iflytek.fsp.shield.java.sdk.model.ApiResponse;
import com.iflytek.fsp.shield.java.sdk.model.ApiSignStrategy;
import com.iflytek.fsp.shield.java.sdk.model.ResultInfo;


public class IflytekMsgUtils extends BaseApp {

    /**
     * modify by xuhao ,切换最新调用方式和调用  appId 此调用有短信
     */
    public IflytekMsgUtils() {
        this.apiClient = new ApiClient();
        this.apiClient.init();
        this.appId = "7350c3589b104835a17983c83bc0aee3";
        this.appSecret = "186B4A8D04B3D07274A6D0A06C228F6F";
        this.host = ConstantUtils.constant.getIflytekHost();
        this.httpPort = Integer.parseInt(ConstantUtils.constant.getIflytekPort());
        this.httpsPort = 443;
        this.stage = "RELEASE";
        this.publicKey = "305C300D06092A864886F70D0101010500034B003048024100858D7C9BF9BE435ECDD25F070D33088879D042F89F36632A7C8D81CD8A2AD329F48E2CCE1C1BF9B6312E912BDBCCB344174705BD6CEBA2184ECDA241BF937C890203010001";
        this.equipmentNo = "XXX";
        this.signStrategyUrl = "/getSignStrategy";
        this.tokenUrl = "/getTokenUrl";
        this.publicKey = "305C300D06092A864886F70D0101010500034B003048024100858D7C9BF9BE435ECDD25F070D33088879D042F89F36632A7C8D81CD8A2AD329F48E2CCE1C1BF9B6312E912BDBCCB344174705BD6CEBA2184ECDA241BF937C890203010001";
        //关闭云锁验证
        this.icloudlockEnabled = false;
    }

    /**
     * 初始化，获取服务签名策略
     */
    /**
     * 初始化，获取服务签名策略
     */
    private void initSignStrategy(ApiRequest apiRequest) {
        //获取服务安全策略信息
        ApiSignStrategy signStrategy = super.getSignStrategy(apiRequest.getPath());
        //判断是否需要token校验
        if (null != signStrategy && "token".equals(signStrategy.getSignType())) {
            //从本地缓存获取token信息,如果本地缓存存在token信息，验证本地缓存token的有效次数，
            //1.如果验证通过，token次数-1，回写到本地缓存；
            //2.如果验证不通过，从新获取token信息，并写到本地缓存。

            //从token服务获取token信息
            ResultInfo resultInfo = super.getTokenInfo(signStrategy);
            if (null != resultInfo && SdkConstant.SUCCESS.equals(resultInfo.getCode())) {
                apiRequest.setTokenValue(resultInfo.getData().getTokenValue());
                //apiRequest.getHeaders().appendOne(SdkConstant.AUTH_EQUIPMENTNO,equipmentNo);
            } else {
                System.err.println("获取token信息失败");
            }
        }
    }


    /**
     * Version:201907090939522777
     */
    public ApiResponse key_WEIHU(byte[] body) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/api-0.8.20/zzwk-service/criterion/indexMaintain", SdkConstant.AUTH_TYPE_ENCRYPT, "1");
        // initSignStrategy(apiRequest);
        apiRequest.setBody(body);

        return syncInvoke(apiRequest);
    }

    /**
     * Version:201907090939525794
     */
    public ApiResponse cl_key_WEIHU(byte[] body) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/api-0.8.20/zzwk-service/materialService/materialIndex", SdkConstant.AUTH_TYPE_ENCRYPT, "1");
        // initSignStrategy(apiRequest);
        apiRequest.setBody(body);

        return syncInvoke(apiRequest);
    }

    /**
     * 认证token-api-0.8.20
     * Version:201907041447168693
     */
    public ApiResponse authToken(byte[] body) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/api-0.8.20/zzwk-service/criterion/authToken", SdkConstant.AUTH_TYPE_ENCRYPT, "1");
        // initSignStrategy(apiRequest);
        apiRequest.setBody(body);

        return syncInvoke(apiRequest);
    }


    /**
     * 材料信息查询-api-0.8.20
     * Version:201907151001131493
     */
    public ApiResponse queryMaterial(byte[] body) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/api-0.8.20/zzwk-service/materialService/queryMaterial", SdkConstant.AUTH_TYPE_ENCRYPT, "1");
        // initSignStrategy(apiRequest);
        apiRequest.setBody(body);

        return syncInvoke(apiRequest);
    }

    /**
     * 材料下载-api-0.8.20
     * Version:201907151001136145
     */
    public ApiResponse materialDownload(byte[] body) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/api-0.8.20/zzwk-service/materialService/materialDownload", SdkConstant.AUTH_TYPE_ENCRYPT, "1");
        // initSignStrategy(apiRequest);
        apiRequest.setBody(body);

        return syncInvoke(apiRequest);
    }


    /**
     * 获取材料类型列表-api-0.8.20
     * Version:201907151001133859
     */
    public ApiResponse getMaterialStyle(byte[] body) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/api-0.8.20/zzwk-service/materialService/getMaterialStyle", SdkConstant.AUTH_TYPE_ENCRYPT, "1");
        // initSignStrategy(apiRequest);
        apiRequest.setBody(body);

        return syncInvoke(apiRequest);
    }

    /**
     * 获取待认证Token-api-0.8.20
     * Version:201907041447164166
     */
    public ApiResponse getUnAuthToken(byte[] body) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/api-0.8.20/zzwk-service/criterion/getUnAuthToken", SdkConstant.AUTH_TYPE_ENCRYPT, "1");
        // initSignStrategy(apiRequest);
        apiRequest.setBody(body);

        return syncInvoke(apiRequest);
    }


    /**
     * 材料简要信息查询-api-0.8.20
     * Version:201907151001134408
     */
    public ApiResponse materialExamine(byte[] body) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/api-0.8.20/zzwk-service/materialService/materialExamine", SdkConstant.AUTH_TYPE_ENCRYPT, "1");
        // initSignStrategy(apiRequest);
        apiRequest.setBody(body);

        return syncInvoke(apiRequest);
    }


    /**
     * 获取token简要信息
     * Version:201907041447164527
     */
    public ApiResponse getTokenInfo(byte[] body) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/api-0.8.20/zzwk-service/criterion/getTokenInfo", SdkConstant.AUTH_TYPE_ENCRYPT, "1");
        // initSignStrategy(apiRequest);
        apiRequest.setBody(body);

        return syncInvoke(apiRequest);
    }

    /**
     * 获取证照材料信息
     * Version:201907151042037244
     */
    public ApiResponse queryzZMaterial(byte[] body) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/api-0.8.20/zzwk-service/currency/queryMaterial", SdkConstant.AUTH_TYPE_ENCRYPT, "1");
        // initSignStrategy(apiRequest);
        apiRequest.setBody(body);

        return syncInvoke(apiRequest);
    }

    /**
     * 下载证照材料数据
     * Version:201907151042038282
     */
    public ApiResponse zZmaterialDownload(byte[] body) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/api-0.8.20/zzwk-service/currency/materialDownload", SdkConstant.AUTH_TYPE_ENCRYPT, "1");
        // initSignStrategy(apiRequest);
        apiRequest.setBody(body);

        return syncInvoke(apiRequest);
    }


    /**
     * 证照材料简要信息
     * Version:201907151042036280
     */
    public ApiResponse querySummary(byte[] body) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/api-0.8.20/zzwk-service/currency/querySummary", SdkConstant.AUTH_TYPE_ENCRYPT, "1");
        // initSignStrategy(apiRequest);
        apiRequest.setBody(body);

        return syncInvoke(apiRequest);
    }


    /**
     * 发送短信
     *
     * @param body
     */
    public ApiResponse sendMsg(byte[] body) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/api-0.8.20/zzwk-service/criterion/sendVerificationCode", SdkConstant.AUTH_TYPE_ENCRYPT, "1");
        apiRequest.setBody(body);
        return syncInvoke(apiRequest);
    }

    /**
     * 验证验证码
     *
     * @param body
     */
    public ApiResponse verificationCode(byte[] body) {
        ApiRequest apiRequest = new ApiRequest(HttpConstant.SCHEME_HTTP, Method.POST, "/api-0.8.20/zzwk-service/criterion/authVerificationCode", SdkConstant.AUTH_TYPE_ENCRYPT, "1");
        apiRequest.setBody(body);
        return syncInvoke(apiRequest);
    }


    /**
     * wuxf 190905 根据请求信息获取 证照材料简要信息，主要用于判断证照是否存在
     * （请求信息：typeCode材料类型，ownerId持有者证件号码，ownerName持有者名称，useForm使用形式 1优先电子证照，无证照查询材料，2仅使用电子证照，3仅使用电子材料）
     *
     * @param jsonReq 传入json参数
     * @return
     */
    public JSONObject querySummaryResp(JSONObject jsonReq) throws Exception {
        //	jsonReq.put("typeCode", "00001401");
        //	jsonReq.put("ownerId", "340827199311256314");
        //	jsonReq.put("ownerName", "111");
        //	jsonReq.put("useForm", 2);
        String resp = new String(this.querySummary(jsonReq.toString().getBytes()).getBody());
        if (StrUtil.isEmpty(resp)) {
            return null;
        }
        JSONObject respJson = JSONObject.parseObject(resp);
        //请求成功，返回请求结果
        if (respJson.getString("flag").equals("200")) {
            return respJson.getJSONArray("data").getJSONObject(0);
        }
        return null;
    }

    /**
     * wuxf 190905 根据请求信息获取 待认证token
     * （请求信息：applyId申请人证件号码，applyName申请人名称，applyPhone申请人联系方式，isApp是否由皖事通出示二维码，false否 ，true是）
     *
     * @param jsonReq 传入json参数
     * @return
     */
    public String getUnAuthTokenResp(JSONObject jsonReq) {
        //	jsonReq.put("applyId", "340121198911304374");
        //	jsonReq.put("applyName", "吴险峰");
        //	jsonReq.put("applyPhone", "13866163420");
        //	jsonReq.put("isApp", true);
        String resp = new String(this.getUnAuthToken(jsonReq.toString().getBytes()).getBody());
        if (StrUtil.isEmpty(resp)) {
            return null;
        }
        JSONObject jsonResp = JSONObject.parseObject(resp);
        if ("200".equals(jsonResp.getString("flag"))) {
            return jsonResp.getJSONArray("data").getJSONObject(0).toString();
        }
        return null;

    }

}
