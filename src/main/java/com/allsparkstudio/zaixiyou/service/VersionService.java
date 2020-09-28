package com.allsparkstudio.zaixiyou.service;

import com.allsparkstudio.zaixiyou.pojo.form.CheckVersionForm;
import com.allsparkstudio.zaixiyou.pojo.vo.VersionVO;

public interface VersionService {
    VersionVO checkVersion(CheckVersionForm form);
}
