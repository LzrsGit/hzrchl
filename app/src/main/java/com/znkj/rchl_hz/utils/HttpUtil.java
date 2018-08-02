package com.znkj.rchl_hz.utils;

import com.znkj.rchl_hz.HttpCallbackListener;
import com.znkj.rchl_hz.model.HcInfo;
import com.znkj.rchl_hz.model.Xsxx;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lenovo-pc on 2018-01-29.
 */

public class HttpUtil {
    //��װ�ķ���������
    public static void sendHttpRequest(final String address, final HttpCallbackListener listener) {
        if (!HttpUtil.isNetworkAvailable()){
            //����д��Ӧ���������ô���
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try{
                    URL url = new URL(address);
                    //ʹ��HttpURLConnection
                    connection = (HttpURLConnection) url.openConnection();
                    //���÷����Ͳ���
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    //��ȡ���ؽ��
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null){
                        response.append(line);
                    }
                    //�ɹ���ص�onFinish
                    if (listener != null){
                        listener.onFinish(response.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //�����쳣��ص�onError
                    if (listener != null){
                        listener.onError(e);
                    }
                }finally {
                    if (connection != null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    public static void sendXsRequest(final String address, final Xsxx xsxx, final HttpCallbackListener listener) {
        if (!HttpUtil.isNetworkAvailable()){
            //����д��Ӧ���������ô���
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try{

                    JSONObject xsJSON = new JSONObject();
                    xsJSON.put("sfzh",xsxx.getSfzh());
                    xsJSON.put("xm",xsxx.getXm());
                    xsJSON.put("sjh",xsxx.getSjh());
                    xsJSON.put("xsnr",xsxx.getXsnr());

                    String content = String.valueOf(xsJSON);

                    URL url = new URL(address);
                    //ʹ��HttpURLConnection
                    connection = (HttpURLConnection) url.openConnection();
                    //���÷����Ͳ���
                    connection.setConnectTimeout(5000);
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);
                    connection.setRequestProperty("User-Agent", "Fiddler");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("Charset", "UTF-8");
                    OutputStream os = connection.getOutputStream();
                    os.write(content.getBytes());
                    os.close();
                    //��ȡ���ؽ��
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null){
                        response.append(line);
                    }
                    //�ɹ���ص�onFinish
                    if (listener != null){
                        listener.onFinish(response.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //�����쳣��ص�onError
                    if (listener != null){
                        listener.onError(e);
                    }
                }finally {
                    if (connection != null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    public static void sendHcRequest(final String address, final HcInfo hcInfo, final HttpCallbackListener listener) {
        if (!HttpUtil.isNetworkAvailable()){
            //����д��Ӧ���������ô���
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try{
                    JSONObject hcJSON = new JSONObject();
                    if(hcInfo.getSjlx().equalsIgnoreCase("hc")){
                        hcJSON.put("sjlx",hcInfo.getSjlx());
                        hcJSON.put("hclx",hcInfo.getHclx());
                        hcJSON.put("hcbb",hcInfo.getHcbb());
                        hcJSON.put("hcr_xm",hcInfo.getHcr_xm());
                        hcJSON.put("hcr_sfzh",hcInfo.getHcr_sfzh());
                        hcJSON.put("hcr_sjh",hcInfo.getHcr_sjh());
                        hcJSON.put("hcr_hcdz",hcInfo.getHcdz());
                        if (hcInfo.getHclx().trim().equalsIgnoreCase("jq")){
                            hcJSON.put("bhcr_sjh",hcInfo.getBhcr_sjh());
                            hcJSON.put("bhcr_sfzh",hcInfo.getBhcr_sfzh());
                            hcJSON.put("bhcr_zp",hcInfo.getBhcr_zp());
                            hcJSON.put("bhcr_zp2",hcInfo.getBhcr_zp2());
                            hcJSON.put("bhcr_jnjw",hcInfo.getBhcr_jnjw());
                        }else if (hcInfo.getHclx().trim().equalsIgnoreCase("mh")){
                            hcJSON.put("bhcr_sjh",hcInfo.getBhcr_sjh());
                            hcJSON.put("bhcr_xm",hcInfo.getBhcr_xm());
                            hcJSON.put("bhcr_hjszd",hcInfo.getBhcr_hjszd());
                            hcJSON.put("bhcr_mz",hcInfo.getBhcr_mz());
                            hcJSON.put("bhcr_csrqks",hcInfo.getBhcr_csrqks());
                            hcJSON.put("bhcr_csrqjs",hcInfo.getBhcr_csrqjs());
                            hcJSON.put("bhcr_zp",hcInfo.getBhcr_zp());
                            hcJSON.put("bhcr_zp2",hcInfo.getBhcr_zp2());
                            hcJSON.put("bhcr_jnjw",hcInfo.getBhcr_jnjw());
                        }else if(hcInfo.getHclx().trim().equalsIgnoreCase("cl")){
                            hcJSON.put("cl_cllx",hcInfo.getCl_cllx());
                            hcJSON.put("cl_clhm",hcInfo.getCl_clhm());
                            hcJSON.put("cl_scwps",hcInfo.getCl_scwps());
                            hcJSON.put("cl_wjwp",hcInfo.getCl_wjwp());
                            hcJSON.put("ywwjwp",hcInfo.getYwwjwp());
                        }else if(hcInfo.getHclx().trim().equalsIgnoreCase("zh")){
                            hcJSON.put("cl_cllx",hcInfo.getCl_cllx());
                            hcJSON.put("cl_clhm",hcInfo.getCl_clhm());
                            hcJSON.put("cl_scwps",hcInfo.getCl_scwps());
                            hcJSON.put("zh_ryxx",hcInfo.getZh_ryxx());
                            hcJSON.put("cl_wjwp",hcInfo.getCl_wjwp());
                            hcJSON.put("ywwjwp",hcInfo.getYwwjwp());
                        }else if(hcInfo.getHclx().trim().equalsIgnoreCase("jw")){
                            hcJSON.put("bhcr_zjlx",hcInfo.getBhcr_zjlx());
                            hcJSON.put("bhcr_sfzh",hcInfo.getBhcr_sfzh());
                            hcJSON.put("bhcr_gjdq",hcInfo.getBhcr_hjszd());
                            hcJSON.put("bhcr_zp",hcInfo.getBhcr_zp());
                            hcJSON.put("bhcr_zp2",hcInfo.getBhcr_zp2());
                            hcJSON.put("bhcr_jnjw",hcInfo.getBhcr_jnjw());
                        }
                    }else if(hcInfo.getSjlx().equalsIgnoreCase("hjd")){
                        hcJSON.put("sjlx",hcInfo.getSjlx());
                    }else if(hcInfo.getSjlx().equalsIgnoreCase("gjdq")){
                        hcJSON.put("sjlx",hcInfo.getSjlx());
                    }else if(hcInfo.getSjlx().equalsIgnoreCase("zjzl")){
                        hcJSON.put("sjlx",hcInfo.getSjlx());
                    }else if(hcInfo.getSjlx().equalsIgnoreCase("abqx")){
                        hcJSON.put("sjlx",hcInfo.getSjlx());
                        hcJSON.put("hcr_sfzh",hcInfo.getHcr_sfzh());
                    }
                    String content = String.valueOf(hcJSON);

                    URL url = new URL(address);
                    //ʹ��HttpURLConnection
                    connection = (HttpURLConnection) url.openConnection();
                    //���÷����Ͳ���
                    connection.setConnectTimeout(5000);
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);
                    connection.setRequestProperty("User-Agent", "Fiddler");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("Charset", "UTF-8");
                    OutputStream os = connection.getOutputStream();
                    os.write(content.getBytes());
                    os.close();
                    //��ȡ���ؽ��
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null){
                        response.append(line);
                    }
                    //�ɹ���ص�onFinish
                    if (listener != null){
                        listener.onFinish(response.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //�����쳣��ص�onError
                    if (listener != null){
                        listener.onError(e);
                    }
                }finally {
                    if (connection != null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }


    //��װ��������������URL
    public static String getURLWithParams(String address, HashMap<String,String> params) throws UnsupportedEncodingException {
        //���ñ���
        final String encode = "UTF-8";
        StringBuilder url = new StringBuilder(address);
        url.append("?");
        //��map�е�key��value�������URL��
        for(Map.Entry<String, String> entry:params.entrySet())
        {
            url.append(entry.getKey()).append("=");
            url.append(URLEncoder.encode(entry.getValue(), encode));
            url.append("&");
        }
        //ɾ�����һ��&
        url.deleteCharAt(url.length() - 1);
        return url.toString();
    }
    //�жϵ�ǰ�����Ƿ����
    public static boolean isNetworkAvailable(){
        return true;
    }

}
