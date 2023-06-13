package com.gjie.kgboot.dao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gjie.kgboot.dao.domain.OperateLog;
import com.gjie.kgboot.dao.service.OperateLogService;
import com.gjie.kgboot.dao.mapper.OperateLogMapper;
import org.springframework.stereotype.Service;

/**
* @author gongjie
* @description 针对表【operate_log】的数据库操作Service实现
* @createDate 2023-06-12 22:16:11
*/
@Service
public class OperateLogServiceImpl extends ServiceImpl<OperateLogMapper, OperateLog>
    implements OperateLogService{
        public String test(){
            if(1==1){
                throw new RuntimeException("就是异常了");
            }
            return "";
        }
}




