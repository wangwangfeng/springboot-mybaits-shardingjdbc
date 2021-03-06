package com.zfsoft.certificate.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iflytek.fsp.shield.java.sdk.model.ApiResponse;
import com.zfsoft.certificate.mapper.db1.ZzWorkCertificateMapper;
import com.zfsoft.certificate.pojo.*;
import com.zfsoft.certificate.service.*;
import com.zfsoft.certificate.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 86131
 */
@Service
public class ZzWorkCertificateServiceImpl extends ServiceImpl<ZzWorkCertificateMapper, ZzWorkCertificate> implements ZzWorkCertificateService {

    @Resource
    private ZzWorkCertificateMapper zzWorkCertificateMapper;

    @Autowired
    private ZzWorkCertificateService zzWorkCertificateService;

    @Autowired
    private ZzWorkContentService zzWorkContentService;

    @Autowired
    private ZzWorkBusinessMetadataService zzWorkBusinessMetadataService;

    @Autowired
    private ZzWorkModelService zzWorkModelService;

    @Autowired
    private ZzWorkStyleService zzWorkStyleService;

    @Autowired
    private SysAttaService sysAttaService;

    @Autowired
    private ZzWorkCertificateVersionRecordService zzWorkCertificateVersionRecordService;

    @Autowired
    private ZzWorkCertificateLogsService zzWorkCertificateLogsService;

    @Override
    public List<ZzWorkCertificate> getZzWorkCertificateByContentCode(String contentCode) {
        return zzWorkCertificateMapper.getZzWorkCertificateByContentCode(contentCode);
    }

    @Override
    public void updateCerStatusById(String id, String cerStatus) {
        zzWorkCertificateMapper.updateCerStatusById(id, cerStatus);
    }

    @Override
    public List<ZzWorkCertificate> getZzWorkCertificateMaintenanceByContentCode(String contentCode) {
        return zzWorkCertificateMapper.getZzWorkCertificateMaintenanceByContentCode(contentCode);
    }

    @Override
    public List<ZzWorkCertificate> findByContentCodeAndInfo(String contentCode, String infoCode, String ownerId) throws Exception {
        return zzWorkCertificateMapper.findByContentCodeAndInfo(contentCode, infoCode, ownerId);
    }

    /**
     * @Description: TODO
     * @Date: 2021/6/10 16:27
     * @author: wwf
     * @param: jsonObject
     * @return: java.util.Map<java.lang.String, java.lang.String>
     **/
    @Override
    public Map<String, String> zzMake(JSONObject jsonObject) throws Exception {

        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("flag", "false");
        if (jsonObject == null) {
            resultMap.put("result", "?????????????????????");
            return resultMap;
        }
        if (("null".equals(jsonObject.getString("contentCode")))
                || (StringUtils.isEmpty(jsonObject.getString("contentCode")))) {
            resultMap.put("result", "??????:???????????????????????????");
            return resultMap;
        }
        if (("null".equals(jsonObject.getString("ownerId")))
                || (StringUtils.isEmpty(jsonObject.getString("ownerId")))) {
            resultMap.put("result", "??????:?????????ID???????????????");
            return resultMap;
        }
        if (("null".equals(jsonObject.getString("ownerName")))
                || (StringUtils.isEmpty(jsonObject.getString("ownerName")))) {
            resultMap.put("result", "??????:??????????????????????????????");
            return resultMap;
        }
        if (("null".equals(jsonObject.getString("infoCode")))
                || (StringUtils.isEmpty(jsonObject.getString("infoCode")))) {
            resultMap.put("result", "??????:?????????????????????????????????");
            return resultMap;
        }
        if (("null".equals(jsonObject.getString("infoValidityBegin")))
                || (StringUtils.isEmpty(jsonObject.getString("infoValidityBegin")))) {
            resultMap.put("result", "??????:?????????????????????????????????");
            return resultMap;
        }
        if (("null".equals(jsonObject.getString("infoValidityEnd")))
                || (StringUtils.isEmpty(jsonObject.getString("infoValidityEnd")))) {
            resultMap.put("result", "??????:?????????????????????????????????");
            return resultMap;
        }
        if (!DateUtils.isValidDate(jsonObject.getString("infoValidityBegin"))) {
            resultMap.put("result", "??????:?????????????????????????????????");
            return resultMap;
        }
        if (!DateUtils.isValidDate(jsonObject.getString("infoValidityEnd"))) {
            resultMap.put("result", "??????:?????????????????????????????????");
            return resultMap;
        }

        String ownerId = jsonObject.getString("ownerId");
        String infoCode = jsonObject.getString("infoCode");
        String contentCode = jsonObject.getString("contentCode");

        ZzWorkContent content = zzWorkContentService.getZzWorkContentByCode(contentCode);
        if (content == null) {
            resultMap.put("result", "??????:????????????????????????");
            return resultMap;
        }

        /**
         * ???????????????????????????????????????????????????????????????????????????
         */
        List<ZzWorkCertificate> zzWorkCertificates = zzWorkCertificateMapper
                .findByContentCodeAndInfo(contentCode, infoCode, ownerId);
        if (CollUtil.isNotEmpty(zzWorkCertificates)) {
            for (ZzWorkCertificate certificate : zzWorkCertificates) {
                /**
                 * ?????????????????????(?????????????????????:20250808)????????????????????????
                 * ???????????????????????????????????????
                 * ????????????: ????????????????????? < ????????????????????? ?????? ????????????????????? < ?????????????????????
                 * ???????????????????????????????????????
                 */
                String infoValidityBegin = jsonObject.getString("infoValidityBegin");
                String infoValidityEnd = jsonObject.getString("infoValidityEnd");
                int end = certificate.getInfoValidityEnd().compareTo(infoValidityEnd);
                int begin = certificate.getInfoValidityBegin().compareTo(infoValidityBegin);
                if (!(end < 0 || begin < 0)) {
                    resultMap.put("result", "??????:???????????????????????????????????????????????????");
                    return resultMap;
                } else {
                    // ????????????????????????????????????
                    System.out.println("++++++???????????????????????????????????????????????????????????????++++++");
                    JSONObject json = new JSONObject();
                    json.put("code", certificate.getCertificateCode());
                    json.put("type", "7");
                    json.put("reason", "????????????");
                    JSONObject object = new JSONObject();
                    object.put("userName", "????????????????????????");
                    object.put("userCode", "340800");
                    object.put("idcard", "340800");
                    object.put("orgCode", "340800");
                    object.put("orgName", "????????????????????????");
                    object.put("bizCode", "zzwk");
                    object.put("bizName", "????????????");
                    json.put("reqStr", object);
                    Map<String, String> map = maintenanceCertificateOnline(json);
                    System.out.println(map.get("result"));
                    if (StringUtils.isEmpty(map.get("result"))) {
                        System.out.println("++++++????????????????????????++++++");
                    } else {
                        // ??????????????????????????????????????????
                        ZzWorkCertificateLogs zzWorkCertificateLogs = new ZzWorkCertificateLogs();
                        zzWorkCertificateLogs.setAttaOid(certificate.getAttaOid());
                        zzWorkCertificateLogs.setCerStatus(certificate.getCerStatus());
                        zzWorkCertificateLogs.setCertificateCode(certificate.getCertificateCode());
                        zzWorkCertificateLogs.setContentCode(certificate.getContentCode());
                        zzWorkCertificateLogs.setContentName(certificate.getContentName());
                        zzWorkCertificateLogs.setInfoCode(certificate.getInfoCode());
                        zzWorkCertificateLogs.setInfoValidityBegin(certificate.getInfoValidityBegin());
                        zzWorkCertificateLogs.setInfoValidityEnd(certificate.getInfoValidityEnd());
                        zzWorkCertificateLogs.setOwnerId(certificate.getOwnerId());
                        zzWorkCertificateLogs.setOwnerName(certificate.getOwnerName());
                        zzWorkCertificateLogs.setResult("?????????????????????");
                        zzWorkCertificateLogs.setType("?????????????????????");
                        zzWorkCertificateLogs.setModifyDate(new Date());
                        zzWorkCertificateLogsService.saveOrUpdate(zzWorkCertificateLogs);
                    }
                }
            }
        }

        List<ZzWorkBusinessMetadata> metaList = zzWorkBusinessMetadataService
                .getZzWorkBusinessMetadataByStyleId(content.getStyleId());
        JSONObject dataObj = new JSONObject((Map<String, Object>) jsonObject.get("data"));
        JSONObject dataJson = new JSONObject();
        if (CollUtil.isNotEmpty(metaList)
                && !dataObj.isEmpty()) {
            for (ZzWorkBusinessMetadata meta : metaList) {
                if ((ConstantUtils.constant.getOne().equals(meta.getControlType()))
                        || (ConstantUtils.constant.getThree().equals(meta.getControlType()))) {
                    dataJson.put(meta.getName(), dataObj.get(meta.getName()));
                } else if ("Y".equals(meta.getIsRequire())) {
                    if (("null".equals(dataObj.get(meta.getName())))
                            || (StringUtils.isEmpty(dataObj.getString(meta.getName())))) {
                        System.out.println("--?????????????????????--");
                    }
                }
                if (("Y".equals(meta.getIsRequire())) &&
                        ((StringUtils.isEmpty(dataObj.getString(meta.getName())))
                                || ("null".equals(dataObj.getString(meta.getName()))))) {
                    resultMap.put("result", "??????:" + meta.getMetadata() + "???????????????");
                    return resultMap;
                }
            }
        } else {
            resultMap.put("result", "??????:??????????????????????????????");
            return resultMap;
        }

        ZzWorkModel model = zzWorkModelService.getZzWorkModelByContentCode(contentCode);
        if (model == null) {
            resultMap.put("result", "??????:?????????????????????????????????");
            return resultMap;
        }

        String certificateCode = CodeUtil.produceCode(content.getIssueCodeStr() + "11", "0001");
        String attaZipPath = DownloadUtil.getSysAttaAbsFilePathByAttaOid(model.getAttaOid());
        System.out.println("????????????ZIP??????:" + attaZipPath);
        String fileDirectoryPath = DownloadUtil.getSysAttaAbsFileParentPathByAttaOid(model.getAttaOid())
                + model.getModelCode();
        System.out.println("????????????????????????:" + fileDirectoryPath);
        File directory = new File(fileDirectoryPath);
        if ((!directory.isDirectory()) || (!directory.exists())) {
            // ?????????ZIP????????????
            ZipUtils.decompress(attaZipPath, fileDirectoryPath);
        }
        List<String> fileList = new ArrayList();
        File[] files = new File(fileDirectoryPath).listFiles();
        for (File f : files) {
            if (FileUtils.isOFD(f)) {
                fileList.add(f.getPath());
            }
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        String ymd = sdf.format(new Date());
        String createTime = sdf1.format(new Date());
        String ofdXml = HTTPAgentUtil.metaDataJSONOfdXml(metaList, dataObj, certificateCode);
        SysAtta atta = null;
        long start = DateUtil.currentSeconds();
        if (!fileList.isEmpty()) {
            String realSavePath = "elms" + "/" + "ofd" + "/"
                    + contentCode + "/" + ymd + "/";
            String ofdName = UUID.randomUUID() + ".ofd";
            System.out.println("===================????????????================");
            if (fileList.size() == 1) {
                atta = HTTPAgentUtil.singleConvert(fileList.get(0), ofdXml, realSavePath, ofdName);
            }
            if (fileList.size() > 1) {
                String[] ofdPaths = new String[fileList.size()];
                String[] xmlPaths = new String[fileList.size()];
                for (int i = 0; i < fileList.size(); i++) {
                    ofdPaths[i] = fileList.get(i);
                    xmlPaths[i] = ofdXml;
                }
                atta = HTTPAgentUtil.multiPageConvert(ofdPaths, xmlPaths, realSavePath, ofdName,
                        null, null, null);
            }
            System.out.println("===================????????????================");
            System.out.println("????????????????????????" + (DateUtil.currentSeconds() - start));
        }
        if (atta != null) {
            ZzWorkCertificate cer = new ZzWorkCertificate(jsonObject);
            cer.setData(dataJson.toJSONString());
            LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) jsonObject.get("reqStr");
            if (CollUtil.isEmpty(map)) {
                resultMap.put("result", "??????:??????????????????????????????");
                return resultMap;
            }
            if (("null".equals(String.valueOf(map.get("userCode"))))
                    || (StringUtils.isEmpty(String.valueOf(map.get("userCode"))))) {
                resultMap.put("result", "??????:??????????????????????????????");
                return resultMap;
            }
            if (("null".equals(String.valueOf(map.get("orgCode"))))
                    || (StringUtils.isEmpty(String.valueOf(map.get("orgCode"))))) {
                resultMap.put("result", "??????:????????????????????????????????????");
                return resultMap;
            }
            if (("null".equals(String.valueOf(map.get("bizCode"))))
                    || (StringUtils.isEmpty(String.valueOf(map.get("bizCode"))))) {
                resultMap.put("result", "??????:???????????????????????????");
                return resultMap;
            }
            if (("null".equals(String.valueOf(map.get("bizName"))))
                    || (StringUtils.isEmpty(String.valueOf(map.get("bizName"))))) {
                resultMap.put("result", "??????:???????????????????????????");
                return resultMap;
            }
            cer.setUserName("null".equals(String.valueOf(map.get("userName"))) ?
                    null : String.valueOf(map.get("userName")));
            cer.setUserCode("null".equals(String.valueOf(map.get("userCode"))) ?
                    null : String.valueOf(map.get("userCode")));
            cer.setIdCard("null".equals(String.valueOf(map.get("idcard"))) ?
                    null : String.valueOf(map.get("idcard")));
            cer.setOrgCode("null".equals(String.valueOf(map.get("orgCode"))) ?
                    null : String.valueOf(map.get("orgCode")));
            cer.setOrgName("null".equals(String.valueOf(map.get("orgName"))) ?
                    null : String.valueOf(map.get("orgName")));
            cer.setBizCode("null".equals(String.valueOf(map.get("bizCode"))) ?
                    null : String.valueOf(map.get("bizCode")));
            cer.setBizName("null".equals(String.valueOf(map.get("bizName"))) ?
                    null : String.valueOf(map.get("bizName")));
            cer.setAttaOid(atta.getOid());
            cer.setDeleteState(ConstantUtils.constant.getZero());
            cer.setCerStatus(ConstantUtils.constant.getZero());
            cer.setModelId(model.getId());
            cer.setOrgTrustCode(content.getIssueCodeStr());
            cer.setVersion("0001");
            cer.setCreateDate(new Date());
            cer.setStyleId(content.getStyleId());
            cer.setPostFlag(ConstantUtils.constant.getOne());
            cer.setContentName(content.getName());
            cer.setCertificateCode(certificateCode);
            this.saveOrUpdate(cer);
            /**
             * ????????????
             */
            long startTwo = DateUtil.currentSeconds();
            String sealFlag = zzWorkCertificateSeal(cer);
            System.out.println("????????????????????????" + (DateUtil.currentSeconds() - startTwo));
            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("ownerId", cer.getOwnerId());
            jsonMap.put("contentName", cer.getContentName());
            jsonMap.put("infoCode", cer.getInfoCode());
            jsonMap.put("code", cer.getCertificateCode());
            jsonMap.put("ownerName", cer.getOwnerName());
            jsonMap.put("areaCode", "340800");
            jsonMap.put("version", cer.getVersion());
            jsonMap.put("contentCode", cer.getContentCode());
            jsonMap.put("operateType", "I");
            jsonMap.put("systemCode", "aqzzk");
            jsonMap.put("bizId", cer.getId());
            jsonMap.put("validBegin", cer.getInfoValidityBegin().substring(0, 4)
                    + "-" + cer.getInfoValidityBegin().substring(4, 6)
                    + "-" + cer.getInfoValidityBegin().substring(6, 8));
            jsonMap.put("validEnd", cer.getInfoValidityEnd().substring(0, 4)
                    + "-" + cer.getInfoValidityEnd().substring(4, 6)
                    + "-" + cer.getInfoValidityEnd().substring(6, 8));
            jsonMap.put("dataSource", ConstantUtils.constant.getTwo());
            jsonMap.put("makeTime", sdf1.format(cer.getCreateDate()));
            jsonMap.put("createTime", createTime);
            jsonMap.put("manyToOne", "");
            jsonMap.put("ownerIds", "");
            jsonMap.put("ownerNames", "");
            jsonMap.put("contentMd5", StringUtils.getMD5String(dataJson.toJSONString()));
            // 2020-11-28?????? ????????????(????????????infoTypeCode??????????????????18??????111???999??????????????????18??????011???099???signCount???????????????)
            jsonMap.put("infoTypeCode", StringUtils.infoTypeCode(cer.getOwnerId()));
            jsonMap.put("signCount", "");
            String jsonStr = JSON.toJSONString(jsonMap);
            resultMap.put("code", cer.getCertificateCode());
            resultMap.put("id", cer.getId());
            resultMap.put("attaOid", cer.getAttaOid());
            resultMap.put("result", "????????????");
            resultMap.put("flag", "true");
            // ????????????or??????
            resultMap.put("sealFlag", sealFlag);
/*            try {
                // ??????????????????
                IflytekUtils iflytekUtils = new IflytekUtils();
                ApiResponse apu = iflytekUtils.key_WEIHU(jsonStr.getBytes(StandardCharsets.UTF_8.name()));
                String res = new String(apu.getBody(), StandardCharsets.UTF_8.name());
                System.out.println("????????????????????????:" + res);
                JSONObject resObject = JSONObject.parseObject(res);
                if (!resObject.isEmpty()) {
                    if ("200".equals(resObject.getString("flag"))) {
                        resultMap.put("apiResult", "true");
                    }
                }
            } catch (Exception e) {
                System.out.println("+++++????????????????????????++++++");
            }*/
        } else {
            resultMap.put("result", "??????:???????????????????????????");
            return resultMap;
        }
        return resultMap;

    }

    /**
     * ????????????????????????
     *
     * @param jsonObject
     * @return
     * @throws Exception
     */
    //@Override
    public Map<String, String> maintenanceCertificateOnline(JSONObject jsonObject) throws Exception {

        Map<String, String> resultMap = new HashMap<>();
        String result = "";
        String statusType = "";
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        // ?????????????????????????????????????????????????????? -start-
        if (jsonObject.isEmpty()) {
            result += "??????:?????????????????????";
            resultMap.put("result", result);
            return resultMap;
        }
        String type = jsonObject.getString("type");
        String code = jsonObject.getString("code");
        if ("null".equals(code) || StringUtils.isEmpty(code)) {
            result += "??????:??????????????????????????????";
            resultMap.put("result", result);
            return resultMap;
        }
        if ("null".equals(type) || StringUtils.isEmpty(type)) {
            result += "??????:???????????????????????????";
            resultMap.put("result", result);
            return resultMap;
        }
        // DOTO ????????????
/*        if (!ElmsBaseStaticParameter.MAINTENANCE_TYPE_SUPPORTED.contains(type)) {
            result += "??????:????????????????????????";
            resultMap.put("result", result);
            return resultMap;
        }
        if (ElmsBaseStaticParameter.MAINTENANCE_TYPE_NEED_REASON.contains(type)
                && StringUtils.isEmpty(jsonObject.getString("reason"))) {
            result += "??????:??????????????????????????????????????????";
            resultMap.put("result", result);
            return resultMap;
        }*/
        JSONObject object = (JSONObject) jsonObject.get("reqStr");
        if (object.isEmpty()) {
            result = "??????:??????????????????????????????";
            resultMap.put("result", result);
            return resultMap;
        }
        if ("null".equals(object.getString("userCode"))
                || StringUtils.isEmpty(String.valueOf(object.get("userCode")))) {
            result = "??????:??????????????????????????????";
            resultMap.put("result", result);
            return resultMap;
        }
        if ("null".equals(object.getString("orgCode"))
                || StringUtils.isEmpty(object.getString("orgCode"))) {
            result = "??????:????????????????????????????????????";
            resultMap.put("result", result);
            return resultMap;
        }
        if ("null".equals(object.getString("bizCode"))
                || StringUtils.isEmpty(object.getString("bizCode"))) {
            result = "??????:???????????????????????????";
            resultMap.put("result", result);
            return resultMap;
        }
        if ("null".equals(object.getString("bizName"))
                || StringUtils.isEmpty(object.getString("bizName"))) {
            result = "??????:???????????????????????????";
            resultMap.put("result", result);
            return resultMap;
        }
        ZzWorkCertificate certificate = new LambdaQueryChainWrapper<>(zzWorkCertificateMapper)
                .eq(ZzWorkCertificate::getCertificateCode, code)
                .eq(ZzWorkCertificate::getDeleteState, ConstantUtils.constant.getZero())
                .orderByDesc(ZzWorkCertificate::getCreateDate)
                .one();
        if (certificate == null) {
            result += "??????:????????????????????????";
            resultMap.put("result", result);
            return resultMap;
        }
        switch (type) {
            case "5": // ??????
                certificate.setCerStatus(ConstantUtils.constant.getTwo());
                certificate.setDeleteState(ConstantUtils.constant.getOne());
                statusType = "D";
                break;
            case "6": // ??????
                certificate.setCerStatus(ConstantUtils.constant.getThree());
                certificate.setDeleteState(ConstantUtils.constant.getOne());
                statusType = "D";
                break;
            case "7": // ??????
                certificate.setCerStatus(ConstantUtils.constant.getFour());
                certificate.setDeleteState(ConstantUtils.constant.getOne());
                statusType = "D";
                break;
            case "3": // ??????
                certificate.setCerStatus(ConstantUtils.constant.getOne());
                statusType = "U";
                break;
            case "4": // ??????
                certificate.setCerStatus(ConstantUtils.constant.getZero());
                statusType = "U";
                break;
            case "9": // ??????
                certificate.setCerStatus(ConstantUtils.constant.getFive());
                statusType = "U";
                break;
            case "10": // ??????
                certificate.setCerStatus(ConstantUtils.constant.getZero());
                statusType = "U";
                break;
            default:
                statusType = "U";
                break;
        }

        DecimalFormat countFormat = new DecimalFormat("0000");
        String version = countFormat.format(Integer.parseInt(certificate.getVersion()) + 1);
        ZzWorkCertificateVersionRecord record = new ZzWorkCertificateVersionRecord(certificate);
        record.setMaintenanceUserName("null".equals(object.getString("userName")) ?
                null : object.getString("userName"));
        record.setMaintenanceUserCode("null".equals(object.getString("userCode")) ?
                null : object.getString("userCode"));
        record.setMaintenanceIdcard("null".equals(object.getString("idcard")) ?
                null : object.getString("idcard"));
        record.setMaintenanceOrgCode("null".equals(object.getString("orgCode")) ?
                null : object.getString("orgCode"));
        record.setMaintenanceOrgName("null".equals(object.getString("orgName")) ?
                null : object.getString("orgName"));
        record.setMaintenanceOrgCode("null".equals(object.getString("bizCode")) ?
                null : object.getString("bizCode"));
        record.setMaintenanceOrgName("null".equals(object.getString("bizName")) ?
                null : object.getString("bizName"));
        record.setMaintenanceType(type);
        record.setMaintenanceReason(jsonObject.getString("reason"));
        record.setCreateDate(new Date());
        zzWorkCertificateVersionRecordService.save(record);
        certificate.setVersion(version);
        zzWorkCertificateService.saveOrUpdate(certificate);
        IflytekUtils iflytekUtils = new IflytekUtils();
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("ownerId", certificate.getOwnerId());
        jsonMap.put("contentName", certificate.getContentName());
        jsonMap.put("infoCode", certificate.getInfoCode());
        jsonMap.put("code", certificate.getCertificateCode());
        jsonMap.put("ownerName", certificate.getOwnerName());
        jsonMap.put("areaCode", "340800");
        jsonMap.put("version", certificate.getVersion());
        jsonMap.put("contentCode", certificate.getContentCode());
        //????????????(??????:I;??????:U;??????:D)
        jsonMap.put("operateType", statusType);
        jsonMap.put("systemCode", "aqzzk");
        jsonMap.put("bizId", certificate.getId());
        //????????????(1:????????????;2:????????????;3:job??????)
        jsonMap.put("dataSource", ConstantUtils.constant.getTwo());
        jsonMap.put("makeTime", sdf1.format(certificate.getCreateDate()));
        jsonMap.put("createTime", sdf1.format(new Date()));
        jsonMap.put("validBegin", certificate.getInfoValidityBegin().substring(0, 4) + "-"
                + certificate.getInfoValidityBegin().substring(4, 6) + "-"
                + certificate.getInfoValidityBegin().substring(6, 8));
        jsonMap.put("validEnd", certificate.getInfoValidityEnd().substring(0, 4) + "-"
                + certificate.getInfoValidityEnd().substring(4, 6) + "-"
                + certificate.getInfoValidityEnd().substring(6, 8));
        jsonMap.put("contentMd5", StringUtils.getMD5String(certificate.getData()));
        // 2020-11-28?????? ????????????(????????????infoTypeCode??????????????????18??????111???999??????????????????18??????011???099???signCount???????????????)
        jsonMap.put("infoTypeCode", StringUtils.infoTypeCode(certificate.getOwnerId()));
        jsonMap.put("signCount", "");
        String jsonStr = JSON.toJSONString(jsonMap);
        ApiResponse apu = iflytekUtils.key_WEIHU(jsonStr.getBytes(StandardCharsets.UTF_8.name()));
        String res = new String(apu.getBody(), StandardCharsets.UTF_8.name());
        System.out.println("??????????????????????????????:" + res);
        resultMap.put("result", result);
        resultMap.put("id", certificate.getId());
        return resultMap;
    }

    /**
     * ????????????
     * ????????????"true",????????????"false"
     *
     * @param zzWorkCertificate ?????????????????????????????????
     * @return
     */
    public String zzWorkCertificateSeal(ZzWorkCertificate zzWorkCertificate) {
        String resultFlag = "true";
        try {
            System.out.println("????????????????????????===============-=-");
            String contentCode = zzWorkCertificate.getContentCode();
            if (StringUtils.isNotEmpty(contentCode)) {
                ZzWorkContent content = zzWorkContentService.getZzWorkContentByCode(contentCode);
                if (content != null) {
                    System.out.println("??????????????????===================");
                    String sealCodeFirst = content.getSealCodeFirst();
                    String sealCodeSecond = content.getSealCodeSecond();
                    String sealCodeThird = content.getSealCodeThird();
                    if ((StringUtils.isNotEmpty(sealCodeFirst))
                            || (StringUtils.isNotEmpty(sealCodeSecond))) {
                        ZzWorkStyle zzWorkStyle = zzWorkStyleService.getZzWorkStyleById(content.getStyleId());
                        if (zzWorkStyle != null) {
                            String ruleInfoFirst = zzWorkStyle.getRuleInfoFirst();
                            String ruleInfoSecond = zzWorkStyle.getRuleInfoSecond();
                            String ruleInfoThird = zzWorkStyle.getRuleInfoThird();
                            if ((StringUtils.isNotEmpty(ruleInfoFirst))
                                    || (StringUtils.isNotEmpty(ruleInfoSecond))) {
                                String attaOId = zzWorkCertificate.getAttaOid();
                                if (StringUtils.isNotEmpty(attaOId)) {
                                    SysAtta atta = sysAttaService.getSysAttaByOid(attaOId);
                                    if (atta != null) {
                                        // ?????????????????????????????????
                                        String resultPath = ConstantUtils.constant.getMakeDiskZ() + atta.getFilePath() + atta.getName();
                                        System.out.println("???????????????????????????:" + resultPath);
                                        File zzCateFile = new File(resultPath);
                                        FileInputStream inputFile = new FileInputStream(zzCateFile);
                                        byte[] buffer = new byte[(int) zzCateFile.length()];
                                        inputFile.read(buffer);
                                        inputFile.close();
                                        String modelName = Base64Tools.encodeBase64String(buffer);
                                        Map<String, String> xmlStr = new HashMap<>();
                                        System.out.println("xml??????==========================");
                                        xmlStr.put("xmlStr", DownloadUtil.sealAutoPdfZF(modelName, atta.getName(),
                                                sealCodeFirst, sealCodeSecond, sealCodeThird,
                                                ruleInfoFirst, ruleInfoSecond, ruleInfoThird));
                                        System.out.println("????????????============================");
                                        String result = HttpUtils.post(ConstantUtils.constant.getMakeSealUrl(), xmlStr);
                                        System.out.println("??????????????????========" + result);
                                        boolean success = DownloadUtil.getXmlAttribute(result, resultPath);
                                        if (success) {
                                            zzWorkCertificateService.updateCerStatusById(zzWorkCertificate.getId(), "6");
                                        } else {
                                            zzWorkCertificateService.updateCerStatusById(zzWorkCertificate.getId(), "9");
                                            resultFlag = "false";
                                        }
                                    } else {
                                        resultFlag = "false";
                                    }
                                } else {
                                    resultFlag = "false";
                                }
                            } else {
                                resultFlag = "false";
                            }
                        } else {
                            resultFlag = "false";
                        }
                    } else {
                        resultFlag = "false";
                    }
                } else {
                    resultFlag = "false";
                }
            }
        } catch (Exception e) {
            resultFlag = "false";
            System.out.println("?????????????????????" + e.getMessage());
        }
        return resultFlag;
    }

}
