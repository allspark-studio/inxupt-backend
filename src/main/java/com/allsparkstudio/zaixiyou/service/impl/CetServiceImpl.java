package com.allsparkstudio.zaixiyou.service.impl;

import com.allsparkstudio.zaixiyou.pojo.form.CetInquireGradeForm;
import com.allsparkstudio.zaixiyou.pojo.form.CetInquireTicketForm;
import com.allsparkstudio.zaixiyou.pojo.vo.CetGradeVO;
import com.allsparkstudio.zaixiyou.pojo.vo.CetTicketVO;
import com.allsparkstudio.zaixiyou.pojo.vo.CetVerificationCodeVO;
import com.allsparkstudio.zaixiyou.service.CetService;
import com.allsparkstudio.zaixiyou.util.CetUtils;

/**
 * @Author: Marble
 * @Date: 2021/6/2 20:54
 * @Description: Cet服务层
 */
public class CetServiceImpl implements CetService {

    @Override
    public CetGradeVO getCetGrade(CetInquireGradeForm cetInquireGradeForm) {
        CetGradeVO cetGradeVO = CetUtils.inquireGrade(cetInquireGradeForm.getTicket(), cetInquireGradeForm.getName());
        return cetGradeVO;
    }

    @Override
    public CetTicketVO findBackCetTicket(CetInquireTicketForm cetInquireTicketForm) {
        CetTicketVO backTicket = CetUtils.findBackTicket(cetInquireTicketForm);
        return backTicket;
    }

    @Override
    public CetVerificationCodeVO getVerificationCode() {
        return CetUtils.getVerification();
    }

}
