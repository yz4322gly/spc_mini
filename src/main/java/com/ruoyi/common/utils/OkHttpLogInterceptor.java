package com.ruoyi.common.utils;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author guolinyuan
 */
@Component
public class OkHttpLogInterceptor implements Interceptor
{
    @Value("${ruoyi.config.okhttp.log.isLog: false}")
    private boolean isLog;

    @Value("${ruoyi.config.okhttp.log.headInfo: false}")
    private boolean headInfo;

    @Value("${ruoyi.config.okhttp.log.bodyInfo: false}")
    private boolean bodyInfo ;

    private static final String CONTENT_TYPE = "Content-Type";

    private Logger logger = LoggerFactory.getLogger(OkHttpLogInterceptor.class);

    @Override
    public Response intercept(Chain chain) throws IOException
    {
        if (isLog)
        {
            Request request = chain.request();
            logger.debug("---------------------服务器OKHTTP发起请求-------------------");

            // 记录下请求内容
            logger.debug("URL : {}", request.url().toString());
            logger.debug("http method : {}", request.method());


            //如果配置了打印详细请求头，打印所有请求头，否则只打印CONTENT_TYPE
            if (headInfo)
            {
                logger.debug("-----request header start-----");
                Headers headers = request.headers();

                headers.forEach(pair -> logger.debug("{} : {}", pair.getFirst(), pair.getSecond()));

                logger.debug("-----request header end-----");
            }
            else
            {
                logger.debug("{} : {}", CONTENT_TYPE, request.header(CONTENT_TYPE));
            }


            //如果是get，则不打印参数，因为在url上面了，是其他看配置打印实体详情
            if (!"GET".equalsIgnoreCase(request.method()))
            {
                if (bodyInfo)
                {
                    logger.debug("-----request body start----");
                    if (request.body() != null)
                    {
                        Buffer buffer = new Buffer();
                        request.body().writeTo(buffer);
                        logger.debug("{}",buffer.readString(Charset.defaultCharset()));
                    }
                    else
                    {
                        logger.debug("");
                    }
                    logger.debug("-----request body end-----");
                }
            }
            logger.debug("-----------------------------------------------------------");
            Response response = chain.proceed(chain.request());

            //如果配置了打印详细请求头，打印所有请求头，否则只打印CONTENT_TYPE
            if (headInfo)
            {
                logger.debug("-----response header start-----");
                Headers headers = response.headers();

                headers.forEach(pair -> logger.debug("{} : {}", pair.getFirst(), pair.getSecond()));

                logger.debug("-----response header end-----");
            }
            else
            {
                logger.debug("{} : {}", CONTENT_TYPE, request.header(CONTENT_TYPE));
            }


            logger.debug("-----response body start-----");
            logger.debug("{}", response.peekBody(1024 * 1024).string());
            logger.debug("-----response body end-----");

            logger.debug("---------------------服务器OKHTTP响应结束-------------------");

            return response;
        }
        else
        {
            return chain.proceed(chain.request());
        }
    }
}
