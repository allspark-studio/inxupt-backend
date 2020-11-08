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

    /**
     * 根据当前版本号来判断是否需要更新
     * code: 0-更新 1-不更新
     * @param form 当前版本号和平台
     */
    @Override
    public VersionVO checkVersion(CheckVersionForm form) {
        String[] versionNum = form.getVersion().split("\\.");
        Version latestVersion = versionMapper.selectLatestVersion();
        String[] latestVersionNum = latestVersion.getVersion().split("\\.");
        int code = generateCode(versionNum, latestVersionNum);
        int force = latestVersion.getForce();
        VersionVO versionVO = new VersionVO();
        if (code == 0) {
            versionVO.setCode(code);
            versionVO.setMsg("ok");
            versionVO.setVersion(latestVersion.getVersion());
            versionVO.setUrl(latestVersion.getUrl());
            versionVO.setLog(latestVersion.getDescription().replaceAll("\\\\n", "\n"));
            versionVO.setForce(force);
        }
        if (code == 1) {
            versionVO.setCode(code);
            versionVO.setMsg("no");
        }
        return versionVO;
    }

    private int generateCode(String[] versionNum, String[] latestVersionNum) {
        int code = 1;
        if (Integer.parseInt(versionNum[0]) < Integer.parseInt(latestVersionNum[0])) {
            code = 0;
            return code;
        }else if (Integer.parseInt(versionNum[0]) > Integer.parseInt(latestVersionNum[0])) {
            return code;
        }
        if (Integer.parseInt(versionNum[1]) < Integer.parseInt(latestVersionNum[1])) {
            code = 0;
            return code;
        }else if (Integer.parseInt(versionNum[1]) > Integer.parseInt(latestVersionNum[1])) {
            return code;
        }
        if (Integer.parseInt(versionNum[2]) < Integer.parseInt(latestVersionNum[2])) {
            code = 0;
            return code;
        }else if (Integer.parseInt(versionNum[2]) > Integer.parseInt(latestVersionNum[2])) {
            return code;
        }
        return code;
    }
}
