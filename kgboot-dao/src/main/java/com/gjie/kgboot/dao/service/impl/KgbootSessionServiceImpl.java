package com.gjie.kgboot.dao.service.impl;

//import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gjie.kgboot.dao.domain.KgbootSession;
import com.gjie.kgboot.dao.service.KgbootSessionService;
import com.gjie.kgboot.dao.mapper.boot.KgbootSessionMapper;
import org.springframework.stereotype.Service;

/**
* @author gongjie
* @description 针对表【kgboot_session(session保存)】的数据库操作Service实现
* @createDate 2023-06-26 21:18:26
*/
@Service
//@DS("kgboot")
public class KgbootSessionServiceImpl extends ServiceImpl<KgbootSessionMapper, KgbootSession>
    implements KgbootSessionService{

}




