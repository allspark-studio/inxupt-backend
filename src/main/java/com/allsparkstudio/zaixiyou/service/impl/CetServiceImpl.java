package com.allsparkstudio.zaixiyou.service.impl;

import com.allsparkstudio.zaixiyou.enums.ResponseEnum;
import com.allsparkstudio.zaixiyou.pojo.form.CetInquireGradeForm;
import com.allsparkstudio.zaixiyou.pojo.form.CetInquireTicketForm;
import com.allsparkstudio.zaixiyou.pojo.vo.CetGradeVO;
import com.allsparkstudio.zaixiyou.pojo.vo.CetTicketVO;
import com.allsparkstudio.zaixiyou.pojo.vo.CetVerificationCodeVO;
import com.allsparkstudio.zaixiyou.pojo.vo.ResponseVO;
import com.allsparkstudio.zaixiyou.service.CetService;
import com.google.gson.Gson;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author : Marble
 * @date : 2021/6/2 20:54
 * @Description : Cet服务层
 */
@Service
public class CetServiceImpl implements CetService {

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
    @Override
    public ResponseVO<CetVerificationCodeVO> getVerificationCode(){
        //存储需要返回的数据
        CetVerificationCodeVO cetVerificationCodeVO = new CetVerificationCodeVO();

        //初始化三个基本的信息， client post response
        HttpPost httpPost = new HttpPost(IMG_URL_TICKET);
        CloseableHttpClient httpClient =  HttpClients.createDefault();
        CloseableHttpResponse response = null;

        //向post中添加信息
        httpPost.addHeader("Origin","http://cet-bm.neea.cn");
        httpPost.addHeader("Connection","keep-alive");
        httpPost.addHeader("content-type","multipart/form-data;");

        try {
            //发送请求 获取响应
            response = httpClient.execute(httpPost);
            if(response.getStatusLine().getStatusCode()==200){
                byte[] image = EntityUtils.toByteArray(response.getEntity());
                //放在本地自己看验证码
                /*
                FileOutputStream fos = new FileOutputStream("D:/TestResource/code.jpg");
                fos.write(image);
                fos.close();
                */
                cetVerificationCodeVO.setImage(byteImageToBase64(image));
                cetVerificationCodeVO.setCookie(getCookie(response.getAllHeaders()));
                return ResponseVO.success(ResponseEnum.SUCCESS.getCode(),ResponseEnum.SUCCESS.getMsg(),cetVerificationCodeVO);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseVO.success(ResponseEnum.ERROR.getCode(),ResponseEnum.ERROR.getMsg(),cetVerificationCodeVO);
    }


    /**
     * @param inquireTicketForm  查询所必要的信息表单
     * @return 返回查询结果
     */
    @Override
    public ResponseVO<CetTicketVO> findBackCetTicket(CetInquireTicketForm inquireTicketForm){
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
        BasicNameValuePair provinceCode = new BasicNameValuePair("provinceCode",inquireTicketForm.getProvinceCode());
        BasicNameValuePair IDTypeCode = new BasicNameValuePair("IDTypeCode",inquireTicketForm.getIdTypeCode());
        BasicNameValuePair IDNumber = new BasicNameValuePair("IDNumber",inquireTicketForm.getIdNumber());
        BasicNameValuePair Name = new BasicNameValuePair("Name",inquireTicketForm.getName());
        BasicNameValuePair verificationCode = new BasicNameValuePair("verificationCode",inquireTicketForm.getVerificationCode());
        list.add(provinceCode);
        list.add(IDTypeCode);
        list.add(IDNumber);
        list.add(Name);
        list.add(verificationCode);
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
                    return ResponseVO.error(ResponseEnum.PARAM_ERROR);
                }
                //System.out.println(EntityUtils.toString(response.getEntity()));
            }else {
                return ResponseVO.error(ResponseEnum.ERROR);
                //System.out.println(EntityUtils.toString(response.getEntity()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseVO.success(ResponseEnum.SUCCESS.getCode(),ResponseEnum.SUCCESS.getMsg(),cetTicketVO);
    }

    /**
     *  获取cet成绩
     * @param cetInquireGradeForm cet提交信息的表单
     * @return
     */
    @Override
    public ResponseVO<CetGradeVO> getCetGrade(CetInquireGradeForm cetInquireGradeForm){

        String ticket=cetInquireGradeForm.getTicket();
        String name=cetInquireGradeForm.getName();
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
            //状态码为200
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
        }catch (Exception e){
            return ResponseVO.error(ResponseEnum.PARAM_ERROR);
        }
        return ResponseVO.success(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMsg(),cetGradeVO);
    }

    /**
     * 从headers中获取Cookie的信息
     * @param headers 请求头信息
     * @return 返回第一个就是sessionId
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
     * 修改阴间json
     * @param json 需要修改的json
     * @return 返回map
     */
    public static Map changeJsonToMap(String json){
        if(json==null){
            return null;
        }
        //这是一个实例，格式严重错误  将这个转为正常json
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
        if(ticketNumber==null){
            return null;
        }
        String subString = ticketNumber.substring(9,10);
        if(JUDGEMENT.equals(subString)){
            //这里需要改，没有别人的准考证号做参考,还不清楚怎么改
            return "CET_202012_DANGCI";
        }else {
            return "CET_202012_DANGCI";
        }
    }

    /**
     * 字节图片转base64
     * @param image 字节图片
     * @return BASE64编码格式图片
     */
    public static String byteImageToBase64(byte[] image){
        /*BASE64Encoder encoder = new BASE64Encoder();
        String encode = encoder.encode(image);
        */
        String s = Base64.encodeBase64String(image);
        return s;
    }

    /**
     * 网络图片转BASE64 暂时用不到，但是以后查询成绩可能会增加验证码 这个就先放这
     */
    public static String netImageToBase64(String netImagePath) {
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        try {
            // 创建URL
            URL url = new URL(netImagePath);
            byte[] by = new byte[1024];
            // 创建链接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            InputStream is = conn.getInputStream();
            // 将内容读取内存中
            int len = -1;
            while ((len = is.read(by)) != -1) {
                data.write(by, 0, len);
            }
            // 关闭流
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data.toByteArray());
    }

}
