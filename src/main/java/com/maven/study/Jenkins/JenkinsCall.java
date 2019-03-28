package com.maven.study.Jenkins;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Properties;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


public class JenkinsCall {
    //private static Logger logger = LoggerFactory.getLogger("");

    public static void main(String[] args) throws IOException {
        // 프로퍼티 파일 위치
        String propFile = "C:\\Users\\pjy1551\\Desktop\\기타\\JenkinsLoginInfo.properties";
        // 프로퍼티 객체 생성
        Properties props = new Properties();
        // 프로퍼티 파일 스트림에 담기
        FileInputStream fis = new FileInputStream(propFile);
        // 프로퍼티 파일 로딩
        props.load(new java.io.BufferedInputStream(fis));
        // 프로퍼티 항목 읽기
        String url = props.getProperty("url"); // url정보
        String user = props.getProperty("user"); // 사용자정보
        String password = props.getProperty("password");; //api token정보
        
        
        String response = scrape(url, user, password);
        System.out.println(response);

    }

    // 젠킨스 배포 정보 가져오기
    public static String scrape(String urlString, String username, String password) throws IOException {
        URI uri = URI.create(urlString);
        HttpHost host = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(new AuthScope(uri.getHost(), uri.getPort()), new UsernamePasswordCredentials(username, password));
        
        AuthCache authCache = new BasicAuthCache();
        
        BasicScheme basicAuth = new BasicScheme();
        authCache.put(host, basicAuth);
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
        
        //HttpPost httpPost = new HttpPost(uri); // post 방식 -> 배포
        HttpGet httpGet = new HttpGet(uri); // get 방식 -> 정보조회
       
        HttpClientContext localContext = HttpClientContext.create();
        localContext.setAuthCache(authCache);
 
        HttpResponse response = httpClient.execute(host, httpGet, localContext); // httpGet, httpPost 중 선택
 
        return EntityUtils.toString(response.getEntity());
    }

    
}
