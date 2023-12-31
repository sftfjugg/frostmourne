package com.autohome.frostmourne.monitor.dao.mybatis.frostmourne.repository.impl;

import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import com.autohome.frostmourne.monitor.dao.mybatis.frostmourne.domain.generate.Recipient;
import com.autohome.frostmourne.monitor.dao.mybatis.frostmourne.mapper.dynamic.RecipientDynamicMapper;
import com.autohome.frostmourne.monitor.dao.mybatis.frostmourne.mapper.dynamic.RecipientDynamicSqlSupport;
import com.autohome.frostmourne.monitor.dao.mybatis.frostmourne.repository.IRecipientRepository;
import com.autohome.frostmourne.monitor.model.enums.RecipientType;
import com.autohome.frostmourne.monitor.tool.MybatisTool;
import org.springframework.stereotype.Repository;

@Repository
public class RecipientRepository implements IRecipientRepository {

    @Resource
    private RecipientDynamicMapper recipientDynamicMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return recipientDynamicMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Recipient record) {
        return recipientDynamicMapper.insert(record);
    }

    @Override
    public int insertSelective(Recipient record) {
        return recipientDynamicMapper.insertSelective(record);
    }

    @Override
    public Optional<Recipient> selectByPrimaryKey(Long id) {
        return recipientDynamicMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(Recipient record) {
        return recipientDynamicMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Recipient record) {
        return recipientDynamicMapper.updateByPrimaryKey(record);
    }

    @Override
    public int deleteByAlarm(Long alarmId) {
        return recipientDynamicMapper.delete(query -> query.where().and(RecipientDynamicSqlSupport.alarmId, isEqualTo(alarmId)));
    }

    @Override
    public int deleteByAlarmAndType(Long alarmId, RecipientType type) {
        return recipientDynamicMapper.delete(query -> query.where().and(RecipientDynamicSqlSupport.alarmId, isEqualTo(alarmId))
            .and(RecipientDynamicSqlSupport.type, isEqualTo(type).when(MybatisTool::notNull)));
    }

    @Override
    public List<Recipient> findByAlarmAndType(Long alarmId, RecipientType type) {
        return recipientDynamicMapper.select(query -> query.where().and(RecipientDynamicSqlSupport.alarmId, isEqualTo(alarmId))
            .and(RecipientDynamicSqlSupport.type, isEqualTo(type).when(MybatisTool::notNull)));
    }
}
