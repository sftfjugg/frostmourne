package com.autohome.frostmourne.monitor.dao.mybatis.frostmourne.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.autohome.frostmourne.monitor.dao.mybatis.frostmourne.domain.generate.AlarmLog;
import com.autohome.frostmourne.monitor.model.enums.ExecuteStatus;
import com.autohome.frostmourne.monitor.model.enums.VerifyResult;

public interface IAlarmLogRepository {

    int deleteByPrimaryKey(Long id);

    int insert(AlarmLog record);

    int insertSelective(AlarmLog record);

    Optional<AlarmLog> selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AlarmLog record);

    int updateByPrimaryKey(AlarmLog record);

    List<AlarmLog> find(Date startTime, Date endTime, Long alarmId, VerifyResult verifyResult, ExecuteStatus executeResult, Boolean alert);

    Optional<AlarmLog> selectLatest(Long alarmId, VerifyResult verifyResult);

    void clearBefore(Date reserveLine);

    long count(Date startTime, Date endTime, VerifyResult verifyResult);

    List<AlarmLog> selectRecently(int i);
}
