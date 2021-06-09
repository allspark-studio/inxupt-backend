package com.allsparkstudio.zaixiyou.service;

import com.allsparkstudio.zaixiyou.pojo.form.CetInquireGradeForm;
import com.allsparkstudio.zaixiyou.pojo.form.CetInquireTicketForm;
import com.allsparkstudio.zaixiyou.pojo.vo.CetGradeVO;
import com.allsparkstudio.zaixiyou.pojo.vo.CetTicketVO;
import com.allsparkstudio.zaixiyou.pojo.vo.CetVerificationCodeVO;
import com.allsparkstudio.zaixiyou.pojo.vo.ResponseVO;

import javax.xml.ws.Response;

/**
 * @Author: Marble
 * @Date: 2021/6/2 20:53
 * @Description:
 */
public interface CetService {
    /**
     * 获取cet成绩
     * @param cetInquireGradeForm cet提交信息的表单
     * @return 返回cet成绩
     */
    ResponseVO<CetGradeVO> getCetGrade(CetInquireGradeForm cetInquireGradeForm);

    /**
     * 获找回cet账号
     * @param cetInquireTicketForm 找回cet准考号需要的信息
     * @return 返回cet信息
     */
    ResponseVO<CetTicketVO> findBackCetTicket(CetInquireTicketForm cetInquireTicketForm);

    /**
     *  获取验证码以及cookie
     * @return 返回cookie(sessionId)以及验证码
     */
    ResponseVO<CetVerificationCodeVO> getVerificationCode();

}
