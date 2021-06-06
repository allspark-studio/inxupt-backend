package com.allsparkstudio.zaixiyou.util;

import com.allsparkstudio.zaixiyou.pojo.form.CetInquireGradeForm;
import com.allsparkstudio.zaixiyou.pojo.form.CetInquireTicketForm;
import com.allsparkstudio.zaixiyou.pojo.vo.CetGradeVO;
import com.allsparkstudio.zaixiyou.pojo.vo.CetTicketVO;
import com.allsparkstudio.zaixiyou.pojo.vo.CetVerificationCodeVO;
import com.google.gson.Gson;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.codehaus.jettison.json.JSONObject;
import springfox.documentation.spring.web.json.Json;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: Marble
 * @Date: 2021/6/2 17:08
 * @Description:
 * 爬取cet成绩的相关工具
 * 目前爬取成绩不需要验证码，但不确定以后是否会引入
 */
public class CetUtils {
    public static final String C="CET";
    /**
     * 获取验证码的地址
     */
    public static final String IMG_URL_TICKET = "http://cet-bm.neea.cn/Home/VerifyCodeImg";
    /**
     * 四级成绩的请求地址
     */
    public static final String REFERER_TICKET = "http://cet-bm.neea.cn/Home/QueryTestTicket";
    /**
     * 准考证号的请求地址
     */
    public static final String REFERER_GRADE = "http://cet.neea.edu.cn/cet";
    /**
     * 查询准考证号的地址
     */
    public static final String QUERY_URL_TICKET = "http://cet-bm.neea.cn/Home/ToQueryTestTicket";
    /**
     * 查询成绩的地址
     */
    public static final String QUERY_URL_GRADE = "http://cache.neea.edu.cn/cet/query";
    /**
     * 解析准考证号时需要进行判断的魔法值，每年可能都不一样
     */
    public static final String JUDGEMENT =  "1";

    /**
     * 通过这个方法获取一组新的验证码以及cookie
     * @return 返回图片的信息以及cookie
     */
    public static CetVerificationCodeVO getVerification(){
        CetVerificationCodeVO cetVerificationCodeVO = new CetVerificationCodeVO();
        //初始化三个基本的信息， client post response
        HttpPost httpPost = new HttpPost(IMG_URL_TICKET);
        CloseableHttpClient httpClient =  HttpClients.createDefault();
        CloseableHttpResponse response = null;
        //向post中添加信息
        httpPost.addHeader("Origin","http://cet-bm.neea.cn");
        httpPost.addHeader("Connection","keep-alive");
        //httpPost.addHeader("User-Agent","ApiPOST Runtime +https://www.apipost.cn");
        httpPost.addHeader("content-type","multipart/form-data;");
        try {
            //发送请求 获取响应
            response = httpClient.execute(httpPost);
            if(response.getStatusLine().getStatusCode()==200){
                byte[] image = EntityUtils.toByteArray(response.getEntity());
                //放在本地自己看验证码
                /*FileOutputStream fos = new FileOutputStream("D:/TestResource/code.jpg");
                fos.write(image);
                fos.close();*/


                cetVerificationCodeVO.setImage(ImageUtils.byteImageToBase64(image));
                cetVerificationCodeVO.setCookie(getCookie(response.getAllHeaders()));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return cetVerificationCodeVO;
    }


    /**
     *
     * @param inquireTicketForm  查询所必要的信息表单
     * @return 返回查询结果
     */
    public static CetTicketVO findBackTicket(CetInquireTicketForm inquireTicketForm){
        CetTicketVO cetTicketVO = new CetTicketVO();
        //创建三个基本的信息
        CloseableHttpClient httpClient  = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(QUERY_URL_TICKET);
        CloseableHttpResponse response = null;
        //添加请求头参数
        httpPost.addHeader("Cookie",inquireTicketForm.getCookie());
        httpPost.addHeader("Referer",REFERER_TICKET);
        httpPost.addHeader("X-Requested-With","XMLHttpRequest");
        httpPost.addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.864.37");
        httpPost.addHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
        httpPost.addHeader("Connection","keep-alive");
        //设置请求体 这里容易出错
        List<NameValuePair> list = new LinkedList<>();
        BasicNameValuePair pair = new BasicNameValuePair("provinceCode",inquireTicketForm.getProvinceCode());
        BasicNameValuePair pair1 = new BasicNameValuePair("IDTypeCode",inquireTicketForm.getIdTypeCode());
        BasicNameValuePair pair2 = new BasicNameValuePair("IDNumber",inquireTicketForm.getIdNumber());
        BasicNameValuePair pair3 = new BasicNameValuePair("Name",inquireTicketForm.getName());
        BasicNameValuePair pair4 = new BasicNameValuePair("verificationCode",inquireTicketForm.getVerificationCode());
        list.add(pair);
        list.add(pair2);
        list.add(pair3);
        list.add(pair4);
        list.add(pair1);
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(list,"UTF-8"));
            response = httpClient.execute(httpPost);
            if(response.getStatusLine().getStatusCode()==200){
                Map map = changeJsonToMap(EntityUtils.toString(response.getEntity()));
                if((Double)map.get("ExceuteResultType")==1.0){
                    List<Map<String,String>> message = (List<Map<String,String>>) map.get("Message");
                    cetTicketVO.setTestTicket(message.get(0).get("TestTicket"));
                    cetTicketVO.setSubjectName(message.get(0).get("SubjectName"));
                }else {
                    return null;
                }
                //System.out.println(EntityUtils.toString(response.getEntity()));
            }else {
                return null;
                //System.out.println(EntityUtils.toString(response.getEntity()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cetTicketVO;
    }

    /**
     * 获取Cet成绩
     * @param ticket 准考证号
     * @param name   姓名
     * @return 返回CetGradeVO
     */
    public static CetGradeVO inquireGrade(String ticket,String name){
        CetGradeVO cetGradeVO = null;
        //创建httpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //idCode每年都可能不一样，需要更改方法getIdCode
        String idCode= getIdCode(ticket);
        String data = idCode+","+ticket+","+name;
        String url = QUERY_URL_GRADE+"?data="+data;
        //获取get请求模板
        HttpGet httpGet = new HttpGet(url);
        //获取响应模板
        CloseableHttpResponse response = null;
        //向请求中添加请求头
        httpGet.addHeader("Referer",REFERER_GRADE);
        httpGet.addHeader("Cookie","language=1");
        try{
            response=httpClient.execute(httpGet);
            if(response.getStatusLine().getStatusCode()==200){
                String resultString = EntityUtils.toString(response.getEntity(),"UTF-8");
                //下面三行模式串匹配 将resultString转成json格式
                String skh = "(?<=\\()[^\\)]+";
                Pattern pattern = Pattern.compile(skh);
                Matcher matcher = pattern.matcher(resultString);
                Gson gson = new Gson();
                boolean is = matcher.find();
                if (is) {
                    Map<String,Double> map = gson.fromJson(matcher.group(),Map.class);
                    // 把Json数据转为map 看一下结果集的键
                    System.out.println(map.toString());
                    cetGradeVO = new CetGradeVO();
                    cetGradeVO.setTotalGrade(String.valueOf(map.get("s")));
                    cetGradeVO.setListeningGrade(String.valueOf(map.get("l")));
                    cetGradeVO.setWritingGrade(String.valueOf(map.get("w")));
                    cetGradeVO.setReadingGrade(String.valueOf(map.get("r")));
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return cetGradeVO;
    }
    /**
     * 从headers中获取Cookie的信息
     * @param headers
     * @return
     */
    private static String getCookie(Header[] headers){
        List<String> lists = new ArrayList<>();
        for (Header header : headers) {
            if("Set-Cookie".equals(header.getName())){
                lists.add(header.getValue());
            }
        }
        Gson gson = new Gson();
        String s = gson.toJson(lists);
        return lists.get(0);
    }

    /**
     *  修改阴间json
     * @param json
     * @return 返回map
     */
    public static Map changeJsonToMap(String json){
        if(json==null){
            return null;
        }
        //这是一个实例，格式严重错误
        // {"ExceuteResultType":1,"Message":"[{\"SubjectName\":\"英语四级笔试\",\"TestTicket\":\"610151202106807\"}]","Result":null}
        char[] chars = json.toCharArray();
        int i=0;
        for (int j=0;j<chars.length;j++) {
            if(chars[j]=='"'){
                i++;
                if(i==5||i==14){
                    chars[j]=' ';
                }
            }
        }
        String s = String.valueOf(chars);
        String replace = s.replace(" ", "").replace("\"","'").replace("\\","");
        Gson gson = new Gson();
        Map map = (Map<String,Object>)gson.fromJson(replace, Map.class);
        return map;
    }
    /**
     *  提取准考证号中关于年份科目的信息
     * @param ticketNumber 准考证号
     * @return CET_202012_DANGCI 这个每年的都好像不一样，所以可能要每年改
     */
    private static String getIdCode(String ticketNumber){
        String subString = ticketNumber.substring(9,10);
        if(JUDGEMENT.equals(subString)){
            //这里需要改，没有别人的准考证号做参考,还不清楚怎么改
            return "CET_202012_DANGCI";
        }else {
            return "CET_202012_DANGCI";
        }
    }

}
