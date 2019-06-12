package tt.common.service;

import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import tt.common.service.httpclient.HttpResult;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ApiService implements BeanFactoryAware {


    @Autowired(required = false)
    private RequestConfig requestConfig;

    private BeanFactory beanFactory;

    @Override   //该方法在Spring容器初始化时自动调用
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    private CloseableHttpClient getHttpClient() {
        return beanFactory.getBean(CloseableHttpClient.class);
    }

    /***
     * GET请求，如果响应200，返回请求内容，如果相应404或500，返回null
     * @param url
     * @return
     * @throws IOException
     * @throws URISyntaxException
     * @throws URISyntaxException
     */
    public String doGet(String url) throws IOException, URISyntaxException, URISyntaxException {
        // 创建http GET请求
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(this.requestConfig);
        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = this.getHttpClient().execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
               return EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return null;
    }

    /***
     * 带有参数的GET请求
     * @param url
     * @param parameters
     * @return
     * @throws URISyntaxException
     * @throws IOException
     */
    public String doGet(String url, Map<String, String> parameters) throws URISyntaxException, IOException {
        // 创建Httpclient对象

        // 定义请求的参数
        URIBuilder uriBuilder = new URIBuilder(url);
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            uriBuilder.setParameter(entry.getKey(), entry.getValue());
        }
        URI uri = uriBuilder.build();
        return this.doGet(uri.toString());
    }

    /***
     * 带参数的POST请求
     * @param url
     * @param parameters
     * @return
     * @throws IOException
     */
    public HttpResult doPost(String url, Map<String, String> parameters) throws IOException {
        // 创建http POST请求
        HttpPost httpPost = new HttpPost(url);

        //构建POST请求的参数
        if (null != parameters) {
            List<NameValuePair> parametersList = new ArrayList<NameValuePair>(0);
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                parametersList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            // 构造一个form表单式的实体
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parametersList);
            // 将请求实体设置到httpPost对象中
            httpPost.setEntity(formEntity);
        }

        httpPost.setConfig(this.requestConfig);
        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = this.getHttpClient().execute(httpPost);
            return new HttpResult(
                    response.getStatusLine().getStatusCode(),
                    EntityUtils.toString(response.getEntity(), "UTF-8")
            );
            // 判断返回状态是否为200
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    /***
     * 没有参数的POST请求
     * @param url
     * @return
     * @throws IOException
     */
    public HttpResult doPost(String url) throws IOException {
        return this.doPost(url, (Map<String, String>) null);
    }

    /***
     * json格式参数POST请求
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    public HttpResult doPost(String url, String json) throws IOException {
        // 创建http POST请求
        HttpPost httpPost = new HttpPost(url);

        //构建POST请求的参数
        if (null != json) {
            StringEntity formEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
            // 将请求实体设置到httpPost对象中
            httpPost.setEntity(formEntity);
        }

        httpPost.setConfig(this.requestConfig);
        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = this.getHttpClient().execute(httpPost);
            return new HttpResult(
                    response.getStatusLine().getStatusCode(),
                    EntityUtils.toString(response.getEntity(), "UTF-8")
            );
            // 判断返回状态是否为200
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

}
