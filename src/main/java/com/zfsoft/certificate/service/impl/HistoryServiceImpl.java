package com.zfsoft.certificate.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zfsoft.certificate.mapper.db1.HistoryMapper;
import com.zfsoft.certificate.pojo.History;
import com.zfsoft.certificate.service.HistoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class HistoryServiceImpl extends ServiceImpl<HistoryMapper, History> implements HistoryService {

    @Resource
    private HistoryMapper historyMapper;

}
