package com.company.nill.myTool.utill;

import com.bind.front.exception.http.BadRequestException;
import com.google.gson.Gson;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @param <T> the type parameter
 */
public class LegacyCall<T> {

    private Logger L = LoggerFactory.getLogger(this.getClass());

    private String url = "";
    private HttpMethod method = HttpMethod.POST;
    private Map<String,String> header = new HashMap<String,String>();
    private boolean multiValue = false;
    private boolean isJson = true;
    private int connectTimeout = 5000;
    private int readTimeout = 60*1000;

    public LegacyCall(String url){
        this.url = url;
    }
    public LegacyCall(boolean multiValue, String url){
        this.url = url;
        this.multiValue = multiValue;
    }

    public LegacyCall(String url, Map<String,String> header){
        this.url = url;
        this.header = header;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public void setJson(boolean json) {
        isJson = json;
    }

    /**
     * Sets url.
     *
     * @param url the url
     */
    public void setUrl(String url) { this.url = url; }

    /**
     * Sets method.
     *
     * @param method the method
     */
    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    /**
     * Sets header.
     *
     * @param header the header
     */
    public void setHeader(Map<String, String> header) { this.header.putAll(header); }

    public void addHeader(String key,String value) { this.header.put(key,value); }

    /**
     * Sets multi value.
     *
     * @param multiValue the multi value
     */
    public void setMultiValue(boolean multiValue) { this.multiValue = multiValue; }

    private boolean isHeader(){
        return !header.isEmpty();
    }

    private HttpHeaders jsonHeader(Map<String,String> header) {

        HttpHeaders requestHeaders = new HttpHeaders();
        for(String key : header.keySet()){
            requestHeaders.add(key, header.get(key));
        }
        if(isJson && !this.multiValue){
            requestHeaders.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
//            requestHeaders.add("Content-Type", "application/json");
        }
        return requestHeaders;

    }


    private T exechangeForJson(HttpEntity<?> requestEntity, Class<T> tClass) throws BadRequestException {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                L.warn("By pass to response entity.");
            }
        });

        if (this.method == HttpMethod.PATCH) {
            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
            requestFactory.setConnectTimeout(connectTimeout);
            requestFactory.setReadTimeout(readTimeout);
            restTemplate.setRequestFactory(requestFactory);
        } else {
            ((org.springframework.http.client.SimpleClientHttpRequestFactory)
                    restTemplate.getRequestFactory()).setReadTimeout(readTimeout);

            ((org.springframework.http.client.SimpleClientHttpRequestFactory)
                    restTemplate.getRequestFactory()).setConnectTimeout(connectTimeout);
        }

        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, this.method, requestEntity, String.class);
        System.out.println(responseEntity+"<<<\n\n\n");
        if(responseEntity.getStatusCode() != HttpStatus.OK && responseEntity.getStatusCode() != HttpStatus.NO_CONTENT) {
            L.error("서버 연결오류["+this.url+"] :: " + responseEntity.getBody());
            throw new BadRequestException(responseEntity.getStatusCode().name());
        }

        // 데이터 없을 경우에도 파싱된 객체 얻을 수 있도록 수정
        String json = responseEntity.getBody();
        if(responseEntity.getStatusCode() == HttpStatus.NO_CONTENT) {
            json = tClass.isArray() ? "[]":"{}";
        }
        /*GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        Gson gson = gsonBuilder.create();*/

        return new Gson().fromJson(json, tClass);
        //gson.fromJson(json,tClass);
    }

    public String exechangeForForm(HttpEntity<?> requestEntity) throws BadRequestException {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                L.warn("By pass to response entity.");
            }
        });

        if (this.method == HttpMethod.PATCH) {
            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
            requestFactory.setConnectTimeout(connectTimeout);
            requestFactory.setReadTimeout(readTimeout);
            restTemplate.setRequestFactory(requestFactory);
        } else {
            ((org.springframework.http.client.SimpleClientHttpRequestFactory)
                    restTemplate.getRequestFactory()).setReadTimeout(readTimeout);

            ((org.springframework.http.client.SimpleClientHttpRequestFactory)
                    restTemplate.getRequestFactory()).setConnectTimeout(connectTimeout);
        }


        ResponseEntity<String> responseEntity = restTemplate.postForEntity( url, requestEntity , String.class );

        if(responseEntity.getStatusCode() != HttpStatus.OK && responseEntity.getStatusCode() != HttpStatus.NO_CONTENT) {
            L.error("서버 연결오류["+this.url+"] :: " + responseEntity.getBody());
            throw new BadRequestException(responseEntity.getStatusCode().name());
        }

        String json = responseEntity.getBody();
        if(responseEntity.getStatusCode() == HttpStatus.NO_CONTENT) {
            json = "";
        }
        L.debug("api call response {}",json);
        return json;
    }


    private ResponseEntity<byte[]> exechangeForByte(HttpEntity<?> requestEntity) throws BadRequestException {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                L.warn("By pass to response entity.");
            }
        });

        if (this.method == HttpMethod.PATCH) {
            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
            requestFactory.setConnectTimeout(connectTimeout);
            requestFactory.setReadTimeout(readTimeout);
            restTemplate.setRequestFactory(requestFactory);
        } else {
            ((org.springframework.http.client.SimpleClientHttpRequestFactory)
                    restTemplate.getRequestFactory()).setReadTimeout(readTimeout);

            ((org.springframework.http.client.SimpleClientHttpRequestFactory)
                    restTemplate.getRequestFactory()).setConnectTimeout(connectTimeout);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(MediaType.IMAGE_JPEG_VALUE));

        ResponseEntity<byte[]> responseEntity = restTemplate.exchange( url, HttpMethod.POST, requestEntity, byte[].class);

        if(responseEntity.getStatusCode() != HttpStatus.OK && responseEntity.getStatusCode() != HttpStatus.NO_CONTENT) {
            L.error("서버 연결오류["+this.url+"] :: " + responseEntity.getBody());
            throw new BadRequestException(responseEntity.getStatusCode().name());
        }

        return responseEntity;
    }


    public Map<String,Object> exechangeForMap(HttpEntity<?> requestEntity) throws BadRequestException, BadRequestException {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                L.warn("By pass to response entity.");
            }
        });

        if (this.method == HttpMethod.PATCH) {
            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
            requestFactory.setConnectTimeout(connectTimeout);
            requestFactory.setReadTimeout(readTimeout);
            restTemplate.setRequestFactory(requestFactory);
        } else {
            ((org.springframework.http.client.SimpleClientHttpRequestFactory)
                    restTemplate.getRequestFactory()).setReadTimeout(readTimeout);

            ((org.springframework.http.client.SimpleClientHttpRequestFactory)
                    restTemplate.getRequestFactory()).setConnectTimeout(connectTimeout);
        }


        ResponseEntity<String> responseEntity = restTemplate.exchange(url, this.method, requestEntity, String.class);
        if(responseEntity.getStatusCode() != HttpStatus.OK && responseEntity.getStatusCode() != HttpStatus.NO_CONTENT) {
            L.error("서버 연결오류["+this.url+"] :: " + responseEntity.getBody());
            throw new BadRequestException(responseEntity.getStatusCode().name());
        }

        // 데이터 없을 경우에도 파싱된 객체 얻을 수 있도록 수정
        String json = responseEntity.getBody();
        if(responseEntity.getStatusCode() == HttpStatus.NO_CONTENT) {
            json = "";
        }

        Map<String,Object> map = new HashMap<String,Object>();
        if(responseEntity.getHeaders().get("totalCount") != null)
            map.put("totalCount", responseEntity.getHeaders().get("totalCount").get(0));
        map.put("body", json);

        return map;
    }

    private HttpEntity<?> setRequestEntiry(Object obj){
        return new HttpEntity<>(obj,this.jsonHeader(header));
    }

    /**
     * MultiValueMap으로 변경한다
     * @param reqVo
     * @return
     */
    private MultiValueMap<String,String> setParam(Object reqVo){
        if(reqVo instanceof Map){
            MultiValueMap<String,String> param = new LinkedMultiValueMap<String,String>();
            Map<String,String> paramMap = (Map)reqVo;
            for(String key : paramMap.keySet()){
                param.add(key,paramMap.get(key));
            }
            return param;
        }else {
            MultiValueMap<String,String> param = new LinkedMultiValueMap<String,String>();
            return setParam(reqVo,param);
        }
    }

    /**
     * Object를 MultiValueMap으로 변경한다
     * @param reqVo
     * @param params
     * @return
     */
    private MultiValueMap<String,String> setParam(Object reqVo, MultiValueMap<String,String> params){
        if(reqVo == null) return params;
        try {
            return setParam(BeanUtils.describe(reqVo));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }


    private String getSourceClass(){
        try{
            StackTraceElement stack = new Throwable().fillInStackTrace().getStackTrace()[2];
            String result = stack.getClassName()+"."+stack.getMethodName();
            return result;
        }catch (Exception e){
            return "";
        }
    }

    private String getJsonForLogging(Object obj){
        if(obj==null) return "";
        try{
            return new Gson().toJson(obj);
        }catch(Exception e){
            return "";
        }
    }

    public T asyncSend(final Object paramObj,final Class<T> tClass){
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    send(paramObj,tClass);
                } catch (BadRequestException e) {
                    L.error(e.getMessage());
                }
            }
        };
        Thread t = new Thread(r);
        t.start();
        return null;
    }

    /**
     * Send t.
     *
     * @param paramObj the param obj
     * @param tClass   the t class
     * @return the t
     */
    public T send(Object paramObj,Class<T> tClass) throws BadRequestException {
        if(paramObj==null) paramObj = new HashMap<>();
        //로그 출력
        String json = getJsonForLogging(paramObj);
        L.debug("Caller is {} => Call to {}  parameter info : {} "
                ,getSourceClass(),
                this.url
                ,json
        );

        if(multiValue){
            MultiValueMap<String,String> param = setParam(paramObj);
            HttpEntity<?> requestEntity = setRequestEntiry(param);
            return this.exechangeForJson(requestEntity,tClass);
        }else{
            HttpEntity<?> requestEntity = setRequestEntiry(json);
            return this.exechangeForJson(requestEntity,tClass);
        }
    }

    public String sendXml(Object paramObj) throws BadRequestException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        if(paramObj==null) paramObj = new HashMap<>();
        //로그 출력
        String json = getJsonForLogging(paramObj);
        L.warn("Caller is {} => Call to {}  parameter info : {} "
                ,getSourceClass(),
                this.url
                ,json
        );

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(setParam(paramObj), headers);
        return  this.exechangeForForm(request);
    }


    public ResponseEntity<byte[]> sendForByte(Object paramObj) throws BadRequestException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        if(paramObj==null) paramObj = new HashMap<>();
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(setParam(paramObj), headers);

        return  this.exechangeForByte(request);
    }
    
    
    /**
     * Send t.
     *
     * @param paramObj the param obj
     * @param tClass   the t class
     * @return the t
     */
    

}
