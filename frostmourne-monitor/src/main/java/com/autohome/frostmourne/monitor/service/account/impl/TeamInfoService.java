package com.autohome.frostmourne.monitor.service.account.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import com.autohome.frostmourne.monitor.dao.mybatis.frostmourne.domain.generate.TeamInfo;
import com.autohome.frostmourne.monitor.dao.mybatis.frostmourne.repository.IAlarmRepository;
import org.springframework.stereotype.Service;

import com.autohome.frostmourne.common.contract.PagerContract;
import com.autohome.frostmourne.common.contract.ProtocolException;
import com.autohome.frostmourne.monitor.dao.mybatis.frostmourne.repository.ITeamInfoRepository;
import com.autohome.frostmourne.monitor.service.account.ITeamInfoService;
import com.autohome.frostmourne.monitor.service.account.IUserInfoService;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TeamInfoService implements ITeamInfoService {

    @Resource
    private ITeamInfoRepository teamInfoRepository;

    @Resource
    private IUserInfoService userInfoService;

    @Resource
    private IAlarmRepository alarmRepository;

    @Override
    public boolean insert(TeamInfo teamInfo, String account) {
        Optional<TeamInfo> optionalTeamInfo = teamInfoRepository.findByName(teamInfo.getTeamName());
        if (optionalTeamInfo.isPresent()) {
            throw new ProtocolException(567, "团队名已经存在");
        }
        teamInfo.setCreator(account);
        teamInfo.setModifier(account);
        Date now = new Date();
        teamInfo.setCreateAt(now);
        teamInfo.setModifyAt(now);
        return teamInfoRepository.insert(teamInfo);
    }

    @Override
    public boolean delete(Long teamId) {
        userInfoService.deleteByTeam(teamId);
        return teamInfoRepository.delete(teamId);
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    @Override
    public boolean update(TeamInfo teamInfo, String account) {
        teamInfo.setModifier(account);
        teamInfo.setModifyAt(new Date());
        if (teamInfo.getId() != null && teamInfo.getId() > 0) {
            Optional<TeamInfo> existTeam = teamInfoRepository.findById(teamInfo.getId());
            if (existTeam.isPresent() && !existTeam.get().getTeamName().equals(teamInfo.getTeamName())) {
                alarmRepository.updateTeamName(teamInfo.getTeamName(), existTeam.get().getTeamName());
            }
        }
        return teamInfoRepository.update(teamInfo);
    }

    @Override
    public PagerContract<TeamInfo> findPage(int pageIndex, int pageSize, Long id, String teamName) {
        return teamInfoRepository.findPage(pageIndex, pageSize, id, teamName);
    }

    @Override
    public List<TeamInfo> find(Long departmentId) {
        return teamInfoRepository.find(departmentId);
    }

    @Override
    public void deleteByDepartment(Long departmentId) {
        List<TeamInfo> teamList = find(departmentId);
        for (TeamInfo teamInfo : teamList) {
            delete(teamInfo.getId());
        }
    }

    @Override
    public Optional<TeamInfo> findByName(String teamName) {
        return teamInfoRepository.findByName(teamName);
    }

    @Override
    public Optional<TeamInfo> findById(Long teamId) {
        return teamInfoRepository.findById(teamId);
    }

    @Override
    public TeamInfo findFirstTeam() {
        return teamInfoRepository.findFirstTeam();
    }
}
