package com.ruoyi.common.utils;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.spring.SpringUtils;
import okhttp3.*;
import org.springframework.core.io.ClassPathResource;

import javax.net.ssl.*;
import java.net.URLDecoder;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


/**
 * @author guolinyuan
 */
public class HttpUtils
{
    private static OkHttpClient wxTLSokHttpClient;

    private static OkHttpClient defaultOkHttpClient;

    private static final OkHttpLogInterceptor okHttpLogInterceptor = SpringUtils.getBean(OkHttpLogInterceptor.class);
    

    public static OkHttpClient getDefaultOkHttpClient()
    {
        //初始化默认的客户端
        if (defaultOkHttpClient == null)
        {
            try
            {
                defaultOkHttpClient = new OkHttpClient.Builder()
                        .retryOnConnectionFailure(false)
                        .connectTimeout(3, TimeUnit.SECONDS)
                        .readTimeout(3, TimeUnit.SECONDS)
                        .writeTimeout(3, TimeUnit.SECONDS)
                        .addInterceptor(okHttpLogInterceptor)
                        .build();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                throw new ServiceException("初始化默认的客户端失败！");
            }
        }

        return defaultOkHttpClient;
    }

    public static String get(String url, Map<String, String> param)
    {
        return get(url,param,getDefaultOkHttpClient());
    }

    @SuppressWarnings("all")
    public static String get(String url, Map<String, String> param,OkHttpClient okHttpClient)
    {

        try
        {
            HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(url)).newBuilder();

            if (param != null)
            {
                param.forEach(urlBuilder::addQueryParameter);
            }

            Response response = okHttpClient.newCall(new Request.Builder()
                    .get()
                    .addHeader("Connection","close")
                    .url(urlBuilder.build())
                    .build()
            ).execute();
            ResponseBody body = response.body();
            if (body == null)
            {
                return "";
            }
            return body.string();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new ServiceException("请求第三方出错");
        }
    }


    public static String post(String url, RequestBody requestBody)
    {
        return post(url, requestBody,getDefaultOkHttpClient());
    }

    public static String post(String url, RequestBody requestBody,OkHttpClient okHttpClient)
    {
        try
        {
            Response response = okHttpClient.newCall(new Request.Builder()
                    .post(requestBody)
                    .addHeader("Connection","close")
                    .url(url)
                    .build()
            ).execute();
            ResponseBody body = response.body();
            if (body == null)
            {
                return "";
            }
            return body.string();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new ServiceException("请求第三方出错");
        }
    }

    public static Map<String, String> stringToMap(String formBody)
    {
        String[] params = formBody.split("&");
        Map<String, String> paramsMap = new HashMap<>(params.length);
        for (String param : params)
        {
            try
            {
                String[] kv = param.split("=");
                if (kv.length == 2)
                {
                    paramsMap.put(URLDecoder.decode(kv[0], "UTF-8"), URLDecoder.decode(kv[1], "UTF-8"));
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return paramsMap;
    }

    public static ResponseBody postO(String url, RequestBody requestBody)
    {
        try
        {
            Response response = getDefaultOkHttpClient().newCall(new Request.Builder()
                    .post(requestBody)
                    .addHeader("Connection","close")
                    .url(url)
                    .build()
            ).execute();
            return response.body();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new ServiceException("请求第三方出错");
        }
    }
}
