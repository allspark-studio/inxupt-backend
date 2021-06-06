package com.allsparkstudio.zaixiyou;

import com.allsparkstudio.zaixiyou.pojo.form.CetInquireTicketForm;
import com.allsparkstudio.zaixiyou.pojo.vo.CetGradeVO;
import com.allsparkstudio.zaixiyou.pojo.vo.CetTicketVO;
import com.allsparkstudio.zaixiyou.pojo.vo.CetVerificationCodeVO;
import com.allsparkstudio.zaixiyou.util.CetUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import jdk.jfr.internal.Utils;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * @Author: Marble
 * @Date: 2021/6/3 21:30
 * @Description: 测试CetUtils
 */
public class testCetUtils {
    public static void main(String[] args) {
        /*CetGradeVO cetGradeVO = CetUtils.inquireGrade("610151202106807", "马世鹏");
        System.out.println(cetGradeVO);*/
        CetVerificationCodeVO verification = CetUtils.getVerification();

        Scanner scanner = new Scanner(System.in);

        String next = scanner.next();

        CetInquireTicketForm cetInquireTicketForm = new CetInquireTicketForm();
        cetInquireTicketForm.setIdNumber("610115200105237275");
        cetInquireTicketForm.setIdTypeCode("1");
        cetInquireTicketForm.setProvinceCode("61");
        cetInquireTicketForm.setName("马世鹏");
        //cetInquireTicketForm.setCookie("ASP.NET_SessionId=zpidhqq5252ge4w4hkuebfnz;");
        cetInquireTicketForm.setCookie(verification.getCookie());
        cetInquireTicketForm.setVerificationCode(next);
        CetTicketVO backTicket = CetUtils.findBackTicket(cetInquireTicketForm);
        System.out.println(backTicket);
        /*String test = "{\"ExceuteResultType\":1,\"Message\":\"[{\"SubjectName\":\"英语四级笔试\",\"TestTicket\":\"610151202106807\"}]\",\"Result\":null}";
        char[] chars = test.toCharArray();
        System.out.println("======");
        for (char aChar : chars) {
            System.out.print(aChar+"-");
        }
        int i=0;
        for (int j=0;j<chars.length;j++) {
            if(chars[j]=='"'){
                i++;
                if(i==5||i==14){
                    chars[j]=' ';
                }
            }
        }
        System.out.println();
        String s = String.valueOf(chars);
        System.out.println(s);
        System.out.println("========");
        String replace = s.replace(" ", "").replaceAll("\"","'");
        System.out.println(replace);
        System.out.println("========");

        Gson gson = new Gson();
        //String s1 = gson.toJson(replace);
       *//* CetTicketMessage cetTicketMessage = gson.fromJson(replace, CetTicketMessage.class);
        System.out.println(cetTicketMessage);*//*
        Map<String,Object> map = gson.fromJson(replace, Map.class);
        System.out.println(map.toString());
        System.out.println("=====");
        List list = CetUtils.changeJsonToMap(test);
        System.out.println(list.toString());
    }*/

    }
}
