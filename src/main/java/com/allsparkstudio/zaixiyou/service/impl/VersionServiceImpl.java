package com.allsparkstudio.zaixiyou.service.impl;

import com.allsparkstudio.zaixiyou.dao.VersionMapper;
import com.allsparkstudio.zaixiyou.pojo.form.CheckVersionForm;
import com.allsparkstudio.zaixiyou.pojo.po.Version;
import com.allsparkstudio.zaixiyou.pojo.vo.VersionVO;
import com.allsparkstudio.zaixiyou.service.VersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VersionServiceImpl implements VersionService {

    @Autowired
    VersionMapper versionMapper;

    @Override
    public VersionVO checkVersion(CheckVersionForm form) {
        String[] versionNum = form.getVersion().split("\\.");
        Version latestVersion = versionMapper.selectLatestVersion();
        String[] latestVersionNum = latestVersion.getVersion().split("\\.");
        int code = 1;
        int force = 0;
        if (Integer.parseInt(versionNum[2]) < Integer.parseInt(latestVersionNum[2]) ||
                Integer.parseInt(versionNum[1]) < Integer.parseInt(latestVersionNum[1]) ||
                Integer.parseInt(versionNum[0]) < Integer.parseInt(latestVersionNum[0])) {
            code = 0;
            if (Integer.parseInt(versionNum[2]) < Integer.parseInt(latestVersionNum[2]) ||
                    Integer.parseInt(versionNum[1]) < Integer.parseInt(latestVersionNum[1])) {
                force = 1;
            }
        }
        VersionVO versionVO = new VersionVO();
        if (code == 0) {
            versionVO.setCode(code);
            versionVO.setMsg("ok");
            versionVO.setVersion(latestVersion.getVersion());
            versionVO.setUrl(latestVersion.getUrl());
            versionVO.setLog(latestVersion.getDescription());
            versionVO.setForce(force);
        }
        if (code == 1) {
            versionVO.setCode(code);
            versionVO.setMsg("no");
        }
        return versionVO;
    }
}
