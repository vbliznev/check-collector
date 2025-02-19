package org.example;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import javax.net.ssl.SSLContext;

import static org.example.Config.BASE_URL;
import static org.example.Config.TOKEN;
import static org.example.WorkWithTG.processMessages;

public class ProverkaChekaApi {

    public String postMassageAndReturnJson(String qrCodeText) throws Exception {
        CloseableHttpClient httpclient = createHttpClient();
        String result = null;
        try {
            JSONObject body = new JSONObject();
            body.put("token", TOKEN);
            body.put("qrraw", qrCodeText);

            HttpPost httpPost = new HttpPost(BASE_URL);
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setHeader("Cookie", "ENGID=1.1");

            StringEntity entity = new StringEntity(body.toString());
            httpPost.setEntity(entity);
            processMessages.add("Запрос сгенерирован успешно");

            CloseableHttpResponse response = httpclient.execute(httpPost);
            try {
                if (response.getStatusLine().getStatusCode() == 200) {
                    HttpEntity responseEntity = response.getEntity();
                    result = EntityUtils.toString(responseEntity);
                    processMessages.add("Ответ получен успешно");

                } else {
                    throw new RuntimeException("Ошибка при получении информации о чеке ");
                }
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
        return result;
    }
    private static CloseableHttpClient createHttpClient() throws Exception {
        SSLContext sslcontext = SSLContext.getDefault();
        return HttpClients.custom()
                .setSSLContext(sslcontext)
                .build();
    }

}