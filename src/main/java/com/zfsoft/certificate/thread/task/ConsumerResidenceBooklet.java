package com.zfsoft.certificate.thread.task;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import com.zfsoft.certificate.pojo.ResidenceBooklet;
import com.zfsoft.certificate.pojo.base.CertificateBaseBO;
import com.zfsoft.certificate.pojo.base.CertificateReqStrBO;
import com.zfsoft.certificate.pojo.base.Constant;
import com.zfsoft.certificate.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 消费者，消费户口簿信息进行制证
 *
 * @author qwb
 */
@Slf4j
public class ConsumerResidenceBooklet implements Runnable {

    private ResidenceBooklet rb;

    private Constant constant;

    private String ownerName;

    public ConsumerResidenceBooklet(ResidenceBooklet rb, Constant constant, String ownerName) {
        this.rb = rb;
        this.constant = constant;
        this.ownerName = ownerName;
    }


    @Override
    public void run() {
        try {
            CertificateBaseBO certificateBaseBO = new CertificateBaseBO();
            CertificateReqStrBO certificateReqStrBO = new CertificateReqStrBO();
            //区划选择
            if (constant.getAqZoneCode().equals(rb.getQHDM().trim()) ||
                    constant.getYjqZoneCode().equals(rb.getQHDM().trim()) ||
                    constant.getDgqZoneCode().equals(rb.getQHDM().trim()) ||
                    constant.getYxqZoneCode().equals(rb.getQHDM().trim()) ||
                    constant.getJkqZoneCode().equals(rb.getQHDM().trim())) {
                //安庆市-迎江区-大观区-宜秀区-经开区
                certificateBaseBO.setContentCode("11340800003110803D0058");
                certificateReqStrBO.setOrgCode("11340800003110803D");
                certificateReqStrBO.setOrgName("安庆市公安局");
            } else if (constant.getHnxZoneCode().equals(rb.getQHDM().trim())) {
                //怀宁县
                certificateBaseBO.setContentCode("1134082200312387410058");
                certificateReqStrBO.setOrgCode("113408220031238741");
                certificateReqStrBO.setOrgName("怀宁县公安局");
            } else if (constant.getTcsZoneCode().equals(rb.getQHDM().trim())) {
                //桐城市
                certificateBaseBO.setContentCode("1134088100312122X70058");
                certificateReqStrBO.setOrgCode("1134088100312122X7");
                certificateReqStrBO.setOrgName("桐城市公安局");
            } else if (constant.getWjxZoneCode().equals(rb.getQHDM().trim())) {
                //望江县
                certificateBaseBO.setContentCode("11340827003134952M0058");
                certificateReqStrBO.setOrgCode("11340827003134952M");
                certificateReqStrBO.setOrgName("望江县公安局");
            } else if (constant.getThxZoneCode().equals(rb.getQHDM().trim())) {
                //太湖县
                certificateBaseBO.setContentCode("1134082500312990XK0058");
                certificateReqStrBO.setOrgCode("1134082500312990XK");
                certificateReqStrBO.setOrgName("太湖县公安局");
            } else if (constant.getYxxZoneCode().equals(rb.getQHDM().trim())) {
                //岳西县
                certificateBaseBO.setContentCode("11340828003136907W0058");
                certificateReqStrBO.setOrgCode("11340828003136907W");
                certificateReqStrBO.setOrgName("岳西县公安局");
            } else if (constant.getSsxZoneCode().equals(rb.getQHDM().trim())) {
                //宿松县
                certificateBaseBO.setContentCode("1134082600313207640058");
                certificateReqStrBO.setOrgCode("113408260031320764");
                certificateReqStrBO.setOrgName("宿松县公安局");
            } else if (constant.getQssZoneCode().equals(rb.getQHDM().trim()) ||
                    constant.getQssZoneCodeOne().equals(rb.getQHDM().trim())) {
                //潜山市
                certificateBaseBO.setContentCode("11340824003128309X0058");
                certificateReqStrBO.setOrgCode("11340824003128309X");
                certificateReqStrBO.setOrgName("潜山市公安局");
            } else {
                certificateBaseBO.setContentCode("");
                certificateReqStrBO.setOrgCode("");
                certificateReqStrBO.setOrgName("");
            }

            /**
             * 根据不同姓名制不同证
             */
            if (constant.getHkbOwnerName().equals(ownerName.trim())) {
                certificateBaseBO.setOwnerId(rb.getSHENFENZHENHAO());
                certificateBaseBO.setOwnerName(rb.getXINGMING());
            } else if (constant.getHkbOwnerName().concat("1").equals(ownerName.trim())) {
                certificateBaseBO.setOwnerId(rb.getSHENFENZHENHAO1());
                certificateBaseBO.setOwnerName(rb.getXINGMING1());
            } else if (constant.getHkbOwnerName().concat("2").equals(ownerName.trim())) {
                certificateBaseBO.setOwnerId(rb.getSHENFENZHENHAO2());
                certificateBaseBO.setOwnerName(rb.getXINGMING2());
            } else if (constant.getHkbOwnerName().concat("3").equals(ownerName.trim())) {
                certificateBaseBO.setOwnerId(rb.getSHENFENZHENHAO3());
                certificateBaseBO.setOwnerName(rb.getXINGMING3());
            } else if (constant.getHkbOwnerName().concat("4").equals(ownerName.trim())) {
                certificateBaseBO.setOwnerId(rb.getSHENFENZHENHAO4());
                certificateBaseBO.setOwnerName(rb.getXINGMING4());
            } else if (constant.getHkbOwnerName().concat("5").equals(ownerName.trim())) {
                certificateBaseBO.setOwnerId(rb.getSHENFENZHENHAO5());
                certificateBaseBO.setOwnerName(rb.getXINGMING5());
            } else if (constant.getHkbOwnerName().concat("6").equals(ownerName.trim())) {
                certificateBaseBO.setOwnerId(rb.getSHENFENZHENHAO6());
                certificateBaseBO.setOwnerName(rb.getXINGMING6());
            } else {
                certificateBaseBO.setOwnerId("");
                certificateBaseBO.setOwnerName("");
            }
            certificateBaseBO.setInfoCode(rb.getINFOCODE());
            certificateBaseBO.setInfoValidityBegin(rb.getINFOVALIDITYBEGIN());
            certificateBaseBO.setInfoValidityEnd(constant.getInfoValidityEnd());
            Map<String, String> dataMap = new HashMap<>();
            dataMap.put("huhao2", StringUtils.handleString(rb.getHUHAO2()));
            dataMap.put("hukouleibie2", StringUtils.handleString(rb.getHUKOULEIBIE2()));
            dataMap.put("paichusuomingcheng2", StringUtils.handleString(rb.getHUKOULEIBIE2()));
            dataMap.put("chengbanren2", StringUtils.handleString(rb.getCHENGBANREN2()));
            dataMap.put("huhao3", StringUtils.handleString(rb.getHUHAO3()));
            dataMap.put("hukouleibie3", StringUtils.handleString(rb.getHUKOULEIBIE3()));
            dataMap.put("paichusuomingcheng3", StringUtils.handleString(rb.getHUKOULEIBIE3()));
            dataMap.put("chengbanren3", StringUtils.handleString(rb.getCHENGBANREN3()));
            dataMap.put("huhao4", StringUtils.handleString(rb.getHUHAO4()));
            dataMap.put("hukouleibie4", StringUtils.handleString(rb.getHUKOULEIBIE4()));
            dataMap.put("paichusuomingcheng4", StringUtils.handleString(rb.getHUKOULEIBIE4()));
            dataMap.put("chengbanren4", StringUtils.handleString(rb.getCHENGBANREN4()));
            dataMap.put("huhao5", StringUtils.handleString(rb.getHUHAO5()));
            dataMap.put("hukouleibie5", StringUtils.handleString(rb.getHUKOULEIBIE5()));
            dataMap.put("paichusuomingcheng5", StringUtils.handleString(rb.getHUKOULEIBIE5()));
            dataMap.put("chengbanren5", StringUtils.handleString(rb.getCHENGBANREN5()));
            dataMap.put("huhao6", StringUtils.handleString(rb.getHUHAO6()));
            dataMap.put("hukouleibie6", StringUtils.handleString(rb.getHUKOULEIBIE6()));
            dataMap.put("paichusuomingcheng6", StringUtils.handleString(rb.getHUKOULEIBIE6()));
            dataMap.put("chengbanren6", StringUtils.handleString(rb.getCHENGBANREN6()));
            dataMap.put("hujizhang1", "");
            dataMap.put("hujizhang2", "");
            dataMap.put("hujizhang31", "");
            dataMap.put("hujizhang42", "");
            dataMap.put("hujizhang53", "");
            dataMap.put("hujizhang64", "");
            dataMap.put("hujizhang75", "");
            dataMap.put("hujizhang88", "");
            dataMap.put("hubie", StringUtils.handleString(rb.getHUBIE()));
            dataMap.put("huzhuxingming", StringUtils.handleString(rb.getHUZHUXINGMING()));
            dataMap.put("infoCode", StringUtils.handleString(rb.getINFOCODE()));
            dataMap.put("zhuzhi01", StringUtils.handleString(rb.getZHUZHI01()));
            dataMap.put("nian01", StringUtils.handleString(rb.getNIAN01()));
            dataMap.put("yue01", StringUtils.handleString(rb.getYUE01()));
            dataMap.put("ri01", StringUtils.handleString(rb.getRI01()));
            dataMap.put("xingming", StringUtils.handleString(rb.getXINGMING()));
            dataMap.put("shenfenzhenhao", StringUtils.handleString(rb.getSHENFENZHENHAO()));
            dataMap.put("guanxi", StringUtils.handleString(rb.getGUANXI()));
            dataMap.put("sex", StringUtils.handleString(rb.getSEX()));
            dataMap.put("chushengdi", StringUtils.handleString(rb.getCHUSHENGDI()));
            dataMap.put("minzu", StringUtils.handleString(rb.getMINZU()));
            dataMap.put("jiguan", StringUtils.handleString(rb.getJIGUAN()));
            dataMap.put("chushengriqi", StringUtils.handleString(rb.getCHUSHENGRIQI()));
            dataMap.put("nian", StringUtils.handleString(rb.getNIAN()));
            dataMap.put("yue", StringUtils.handleString(rb.getYUE()));
            dataMap.put("ri", StringUtils.handleString(rb.getRI()));
            dataMap.put("h_rename", StringUtils.handleString(rb.getH_RENAME()));
            dataMap.put("zhuzhi", StringUtils.handleString(rb.getZHUZHI()));
            dataMap.put("xinyang", StringUtils.handleString(rb.getXINYANG()));
            dataMap.put("shengao", StringUtils.handleString(rb.getSHENGAO()));
            dataMap.put("xuexin", StringUtils.handleString(rb.getXUEXIN()));
            dataMap.put("wenhuachengdu", StringUtils.handleString(rb.getWENHUACHENGDU()));
            dataMap.put("hunyin", StringUtils.handleString(rb.getHUNYIN()));
            dataMap.put("bingyi", StringUtils.handleString(rb.getBINGYI()));
            dataMap.put("fuwuchusuo", StringUtils.handleString(rb.getFUWUCHUSUO()));
            dataMap.put("zhiye", StringUtils.handleString(rb.getZHIYE()));
            dataMap.put("heshihedi", StringUtils.handleString(rb.getHESHIHEDI()));
            dataMap.put("heshihedi_bendi", StringUtils.handleString(rb.getHESHIHEDI_BENDI()));
            dataMap.put("xingming1", StringUtils.handleString(rb.getXINGMING1()));
            dataMap.put("shenfenzhenhao1", StringUtils.handleString(rb.getSHENFENZHENHAO1()));
            dataMap.put("guanxi1", StringUtils.handleString(rb.getGUANXI1()));
            dataMap.put("sex1", StringUtils.handleString(rb.getSEX1()));
            dataMap.put("chushengdi1", StringUtils.handleString(rb.getCHUSHENGDI1()));
            dataMap.put("minzu1", StringUtils.handleString(rb.getMINZU1()));
            dataMap.put("jiguan1", StringUtils.handleString(rb.getJIGUAN1()));
            dataMap.put("chushengriqi1", StringUtils.handleString(rb.getCHUSHENGRIQI1()));
            dataMap.put("nian1", StringUtils.handleString(rb.getNIAN1()));
            dataMap.put("yue1", StringUtils.handleString(rb.getYUE1()));
            dataMap.put("ri1", StringUtils.handleString(rb.getRI1()));
            dataMap.put("h_rename1", StringUtils.handleString(rb.getH_RENAME1()));
            dataMap.put("zhuzhi1", StringUtils.handleString(rb.getZHUZHI1()));
            dataMap.put("xinyang1", StringUtils.handleString(rb.getXINYANG1()));
            dataMap.put("shengao1", StringUtils.handleString(rb.getSHENGAO1()));
            dataMap.put("xuexin1", StringUtils.handleString(rb.getXUEXIN1()));
            dataMap.put("wenhuachengdu1", StringUtils.handleString(rb.getWENHUACHENGDU1()));
            dataMap.put("hunyin1", StringUtils.handleString(rb.getHUNYIN1()));
            dataMap.put("bingyi1", StringUtils.handleString(rb.getBINGYI1()));
            dataMap.put("fuwuchusuo1", StringUtils.handleString(rb.getFUWUCHUSUO1()));
            dataMap.put("zhiye1", StringUtils.handleString(rb.getZHIYE1()));
            dataMap.put("heshihedi1", StringUtils.handleString(rb.getHESHIHEDI1()));
            dataMap.put("heshihedi_bendi1", StringUtils.handleString(rb.getHESHIHEDI_BENDI1()));
            dataMap.put("xingming2", StringUtils.handleString(rb.getXINGMING2()));
            dataMap.put("shenfenzhenhao2", StringUtils.handleString(rb.getSHENFENZHENHAO2()));
            dataMap.put("guanxi2", StringUtils.handleString(rb.getGUANXI2()));
            dataMap.put("sex2", StringUtils.handleString(rb.getSEX2()));
            dataMap.put("chushengdi2", StringUtils.handleString(rb.getCHUSHENGDI2()));
            dataMap.put("minzu2", StringUtils.handleString(rb.getMINZU2()));
            dataMap.put("jiguan2", StringUtils.handleString(rb.getJIGUAN2()));
            dataMap.put("chushengriqi2", StringUtils.handleString(rb.getCHUSHENGRIQI2()));
            dataMap.put("nian2", StringUtils.handleString(rb.getNIAN2()));
            dataMap.put("yue2", StringUtils.handleString(rb.getYUE2()));
            dataMap.put("ri2", StringUtils.handleString(rb.getRI2()));
            dataMap.put("h_rename2", StringUtils.handleString(rb.getH_RENAME2()));
            dataMap.put("zhuzhi2", StringUtils.handleString(rb.getZHUZHI2()));
            dataMap.put("xinyang2", StringUtils.handleString(rb.getXINYANG2()));
            dataMap.put("shengao2", StringUtils.handleString(rb.getSHENGAO2()));
            dataMap.put("xuexin2", StringUtils.handleString(rb.getXUEXIN2()));
            dataMap.put("wenhuachengdu2", StringUtils.handleString(rb.getWENHUACHENGDU2()));
            dataMap.put("hunyin2", StringUtils.handleString(rb.getHUNYIN2()));
            dataMap.put("bingyi2", StringUtils.handleString(rb.getBINGYI2()));
            dataMap.put("fuwuchusuo2", StringUtils.handleString(rb.getFUWUCHUSUO2()));
            dataMap.put("zhiye2", StringUtils.handleString(rb.getZHIYE2()));
            dataMap.put("heshihedi2", StringUtils.handleString(rb.getHESHIHEDI2()));
            dataMap.put("heshihedi_bendi2", StringUtils.handleString(rb.getHESHIHEDI_BENDI2()));
            dataMap.put("xingming3", StringUtils.handleString(rb.getXINGMING3()));
            dataMap.put("shenfenzhenhao3", StringUtils.handleString(rb.getSHENFENZHENHAO3()));
            dataMap.put("guanxi3", StringUtils.handleString(rb.getGUANXI3()));
            dataMap.put("sex3", StringUtils.handleString(rb.getSEX3()));
            dataMap.put("chushengdi3", StringUtils.handleString(rb.getCHUSHENGDI3()));
            dataMap.put("minzu3", StringUtils.handleString(rb.getMINZU3()));
            dataMap.put("jiguan3", StringUtils.handleString(rb.getJIGUAN3()));
            dataMap.put("chushengriqi3", StringUtils.handleString(rb.getCHUSHENGRIQI3()));
            dataMap.put("nian3", StringUtils.handleString(rb.getNIAN3()));
            dataMap.put("yue3", StringUtils.handleString(rb.getYUE3()));
            dataMap.put("ri3", StringUtils.handleString(rb.getRI3()));
            dataMap.put("h_rename3", StringUtils.handleString(rb.getH_RENAME3()));
            dataMap.put("zhuzhi3", StringUtils.handleString(rb.getZHUZHI3()));
            dataMap.put("xinyang3", StringUtils.handleString(rb.getXINYANG3()));
            dataMap.put("shengao3", StringUtils.handleString(rb.getSHENGAO3()));
            dataMap.put("xuexin3", StringUtils.handleString(rb.getXUEXIN3()));
            dataMap.put("wenhuachengdu3", StringUtils.handleString(rb.getWENHUACHENGDU3()));
            dataMap.put("hunyin3", StringUtils.handleString(rb.getHUNYIN3()));
            dataMap.put("bingyi3", StringUtils.handleString(rb.getBINGYI3()));
            dataMap.put("fuwuchusuo3", StringUtils.handleString(rb.getFUWUCHUSUO3()));
            dataMap.put("zhiye3", StringUtils.handleString(rb.getZHIYE3()));
            dataMap.put("heshihedi3", StringUtils.handleString(rb.getHESHIHEDI3()));
            dataMap.put("heshihedi_bendi3", StringUtils.handleString(rb.getHESHIHEDI_BENDI3()));
            dataMap.put("xingming4", StringUtils.handleString(rb.getXINGMING4()));
            dataMap.put("shenfenzhenhao4", StringUtils.handleString(rb.getSHENFENZHENHAO4()));
            dataMap.put("guanxi4", StringUtils.handleString(rb.getGUANXI4()));
            dataMap.put("sex4", StringUtils.handleString(rb.getSEX4()));
            dataMap.put("chushengdi4", StringUtils.handleString(rb.getCHUSHENGDI4()));
            dataMap.put("minzu4", StringUtils.handleString(rb.getMINZU4()));
            dataMap.put("jiguan4", StringUtils.handleString(rb.getJIGUAN4()));
            dataMap.put("chushengriqi4", StringUtils.handleString(rb.getCHUSHENGRIQI4()));
            dataMap.put("nian4", StringUtils.handleString(rb.getNIAN4()));
            dataMap.put("yue4", StringUtils.handleString(rb.getYUE4()));
            dataMap.put("ri4", StringUtils.handleString(rb.getRI4()));
            dataMap.put("h_rename4", StringUtils.handleString(rb.getH_RENAME4()));
            dataMap.put("zhuzhi4", StringUtils.handleString(rb.getZHUZHI4()));
            dataMap.put("xinyang4", StringUtils.handleString(rb.getXINYANG4()));
            dataMap.put("shengao4", StringUtils.handleString(rb.getSHENGAO4()));
            dataMap.put("xuexin4", StringUtils.handleString(rb.getXUEXIN4()));
            dataMap.put("wenhuachengdu4", StringUtils.handleString(rb.getWENHUACHENGDU4()));
            dataMap.put("hunyin4", StringUtils.handleString(rb.getHUNYIN4()));
            dataMap.put("bingyi4", StringUtils.handleString(rb.getBINGYI4()));
            dataMap.put("fuwuchusuo4", StringUtils.handleString(rb.getFUWUCHUSUO4()));
            dataMap.put("zhiye4", StringUtils.handleString(rb.getZHIYE4()));
            dataMap.put("heshihedi4", StringUtils.handleString(rb.getHESHIHEDI4()));
            dataMap.put("heshihedi_bendi4", StringUtils.handleString(rb.getHESHIHEDI_BENDI4()));
            dataMap.put("xingming5", StringUtils.handleString(rb.getXINGMING5()));
            dataMap.put("shenfenzhenhao5", StringUtils.handleString(rb.getSHENFENZHENHAO5()));
            dataMap.put("guanxi5", StringUtils.handleString(rb.getGUANXI5()));
            dataMap.put("sex5", StringUtils.handleString(rb.getSEX5()));
            dataMap.put("chushengdi5", StringUtils.handleString(rb.getCHUSHENGDI5()));
            dataMap.put("minzu5", StringUtils.handleString(rb.getMINZU5()));
            dataMap.put("jiguan5", StringUtils.handleString(rb.getJIGUAN5()));
            dataMap.put("chushengriqi5", StringUtils.handleString(rb.getCHUSHENGRIQI5()));
            dataMap.put("nian5", StringUtils.handleString(rb.getNIAN5()));
            dataMap.put("yue5", StringUtils.handleString(rb.getYUE5()));
            dataMap.put("ri5", StringUtils.handleString(rb.getRI5()));
            dataMap.put("h_rename5", StringUtils.handleString(rb.getH_RENAME5()));
            dataMap.put("zhuzhi5", StringUtils.handleString(rb.getZHUZHI5()));
            dataMap.put("xinyang5", StringUtils.handleString(rb.getXINYANG5()));
            dataMap.put("shengao5", StringUtils.handleString(rb.getSHENGAO5()));
            dataMap.put("xuexin5", StringUtils.handleString(rb.getXUEXIN5()));
            dataMap.put("wenhuachengdu5", StringUtils.handleString(rb.getWENHUACHENGDU5()));
            dataMap.put("hunyin5", StringUtils.handleString(rb.getHUNYIN5()));
            dataMap.put("bingyi5", StringUtils.handleString(rb.getBINGYI5()));
            dataMap.put("fuwuchusuo5", StringUtils.handleString(rb.getFUWUCHUSUO5()));
            dataMap.put("zhiye5", StringUtils.handleString(rb.getZHIYE5()));
            dataMap.put("heshihedi5", StringUtils.handleString(rb.getHESHIHEDI5()));
            dataMap.put("heshihedi_bendi5", StringUtils.handleString(rb.getHESHIHEDI_BENDI5()));
            dataMap.put("xingming6", StringUtils.handleString(rb.getXINGMING6()));
            dataMap.put("shenfenzhenhao6", StringUtils.handleString(rb.getSHENFENZHENHAO6()));
            dataMap.put("guanxi6", StringUtils.handleString(rb.getGUANXI6()));
            dataMap.put("sex6", StringUtils.handleString(rb.getSEX6()));
            dataMap.put("chushengdi6", StringUtils.handleString(rb.getCHUSHENGDI6()));
            dataMap.put("minzu6", StringUtils.handleString(rb.getMINZU6()));
            dataMap.put("jiguan6", StringUtils.handleString(rb.getJIGUAN6()));
            dataMap.put("chushengriqi6", StringUtils.handleString(rb.getCHUSHENGRIQI6()));
            dataMap.put("nian6", StringUtils.handleString(rb.getNIAN6()));
            dataMap.put("yue6", StringUtils.handleString(rb.getYUE6()));
            dataMap.put("ri6", StringUtils.handleString(rb.getRI6()));
            dataMap.put("h_rename6", StringUtils.handleString(rb.getH_RENAME6()));
            dataMap.put("zhuzhi6", StringUtils.handleString(rb.getZHUZHI6()));
            dataMap.put("xinyang6", StringUtils.handleString(rb.getXINYANG6()));
            dataMap.put("shengao6", StringUtils.handleString(rb.getSHENGAO6()));
            dataMap.put("xuexin6", StringUtils.handleString(rb.getXUEXIN6()));
            dataMap.put("wenhuachengdu6", StringUtils.handleString(rb.getWENHUACHENGDU6()));
            dataMap.put("hunyin6", StringUtils.handleString(rb.getHUNYIN6()));
            dataMap.put("bingyi6", StringUtils.handleString(rb.getBINGYI6()));
            dataMap.put("fuwuchusuo6", StringUtils.handleString(rb.getFUWUCHUSUO6()));
            dataMap.put("zhiye6", StringUtils.handleString(rb.getZHIYE6()));
            dataMap.put("heshihedi6", StringUtils.handleString(rb.getHESHIHEDI6()));
            dataMap.put("heshihedi_bendi6", StringUtils.handleString(rb.getHESHIHEDI_BENDI6()));
            dataMap.put("shequ", StringUtils.handleString(rb.getSHEQU()));
            dataMap.put("paichusuomingcheng01", StringUtils.handleString(rb.getPAICHUSUOMINGCHENG01()));
            dataMap.put("chengbanren01", StringUtils.handleString(rb.getCHENGBANREN01()));
            dataMap.put("huhao", StringUtils.handleString(rb.getHUHAO()));
            dataMap.put("hukouleibie", StringUtils.handleString(rb.getHUKOULEIBIE()));
            dataMap.put("paichusuomingcheng", StringUtils.handleString(rb.getPAICHUSUOMINGCHENG()));
            dataMap.put("chengbanren", StringUtils.handleString(rb.getCHENGBANREN()));
            dataMap.put("huhao1", StringUtils.handleString(rb.getHUHAO1()));
            dataMap.put("hukouleibie1", StringUtils.handleString(rb.getHUKOULEIBIE1()));
            dataMap.put("paichusuomingcheng1", StringUtils.handleString(rb.getPAICHUSUOMINGCHENG1()));
            dataMap.put("chengbanren1", StringUtils.handleString(rb.getCHENGBANREN1()));
            certificateBaseBO.setData(dataMap);
            certificateReqStrBO.setUserName("安庆市数据资源局");
            certificateReqStrBO.setUserCode("340800");
            certificateReqStrBO.setIdcard("340800");
            certificateReqStrBO.setBizCode("zzwk");
            certificateReqStrBO.setBizName("证照文库");
            certificateBaseBO.setReqStr(certificateReqStrBO);
            JSONObject jsonObject = new JSONObject(certificateBaseBO);
            log.info("制证入参:{}", jsonObject.toString());
            String result = HttpUtil.post(constant.getMakeUrl(), jsonObject.toString());
            JSONObject object = new JSONObject(result);
            log.info("制证结果:{}", object.toString());
            if ("true".equals(object.get("flag"))) {

            }
        } catch (Exception e) {
            log.error("制证异常失败:{}" + e.getMessage());
        }
    }

}
