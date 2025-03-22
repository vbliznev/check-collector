package org.example;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.example.pojo.HelperPojoJson;
import org.example.pojo.PojoJson;
import org.json.JSONObject;

import javax.net.ssl.SSLContext;

import static org.example.Config.PROVERKACHEKA_URL;
import static org.example.Config.TOKEN_PROVERKACHEKA;

public class ProverkaChekaApi {
    private final HelperPojoJson helperPojoJson = new HelperPojoJson();

    public PojoJson.Root getInformationAboutCheckAndReturnPOJO(String qrCodeText) throws Exception {
        CloseableHttpClient httpclient = createHttpClient();
        PojoJson.Root root;
        try {
            JSONObject body = new JSONObject();
            body.put("token", TOKEN_PROVERKACHEKA);
            body.put("qrraw", qrCodeText);

            HttpPost httpPost = new HttpPost(PROVERKACHEKA_URL);
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setHeader("Cookie", "ENGID=1.1");

            StringEntity entity = new StringEntity(body.toString());
            httpPost.setEntity(entity);
            WorkWithTG.logger.info("Запрос сгенерирован успешно");

            CloseableHttpResponse response = httpclient.execute(httpPost);
            try {
                if (response.getStatusLine().getStatusCode() == 200) {
                    HttpEntity responseEntity = response.getEntity();
                    String result = EntityUtils.toString(responseEntity);
                    root = helperPojoJson.parseJson(result);
                    //todo обработать варианты ответов
                    WorkWithTG.logger.info("Ответ получен успешно");

                } else {
                    throw new RuntimeException("Ошибка при получении информации о чеке ");
                }
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
        return root;
    }
    private static CloseableHttpClient createHttpClient() throws Exception {
        SSLContext sslcontext = SSLContext.getDefault();
        return HttpClients.custom()
                .setSSLContext(sslcontext)
                .build();
    }

}