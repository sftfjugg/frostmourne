package com.autohome.frostmourne.monitor.dao.mybatis.frostmourne.repository;

import java.util.List;
import java.util.Optional;

import com.autohome.frostmourne.monitor.dao.mybatis.frostmourne.domain.generate.ServiceInfo;
import com.autohome.frostmourne.monitor.model.contract.ServiceInfoQueryForm;
import com.github.pagehelper.PageInfo;

public interface IServiceInfoRepository {

    void insertSelective(ServiceInfo record);

    void updateByPrimaryKeySelective(ServiceInfo record);

    Optional<ServiceInfo> getById(Long id);

    List<ServiceInfo> listByIds(List<Long> ids);

    PageInfo<ServiceInfo> find(ServiceInfoQueryForm form);

}
