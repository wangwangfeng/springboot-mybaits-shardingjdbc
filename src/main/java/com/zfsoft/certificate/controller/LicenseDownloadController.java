package com.zfsoft.certificate.controller;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zfsoft.certificate.pojo.base.Constant;
import com.zfsoft.certificate.util.Base64Tools;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.Map;

/**
 * 证照下载控制台-测试接口
 *
 * @author 86131
 */
@Api(tags = "证照下载后台")
@ApiIgnore
@Slf4j
@Controller
public class LicenseDownloadController {

    @Autowired
    private Constant constant;

    @ApiOperation(value = "证照下载", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "id", value = "主键", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "format", value = "下载文件格式", required = true, dataType = "String")
    })
    @RequestMapping(value = "/download/{id}/{format}", method = RequestMethod.GET)
    @ResponseBody
    public String download(@PathVariable(name = "id") String id,
                           @PathVariable(name = "format") String format) {

        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("format", format);
        String result = HttpUtil.post(constant.getZzDownload(), JSON.toJSONString(map));
        JSONObject object = JSONObject.parseObject(result);
        if ("200".equals(object.get("flag"))) {
            JSONArray jsonArray = (JSONArray) object.get("data");
            String base = (String) jsonArray.getJSONObject(0).get("dataStr");
            System.out.println(base);
            Base64Tools.base64StringToPdf(base, "D://aaa." + format);
        }
        return null;

    }

}
