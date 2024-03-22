package com.jm.client.sql.gateway;

import com.jm.client.sql.gateway.api.DefaultApi;
import com.jm.client.sql.gateway.invoker.ApiClient;
import com.jm.client.sql.gateway.invoker.ApiException;
import com.jm.client.sql.gateway.model.OpenSessionRequestBody;
import com.jm.client.sql.gateway.model.OpenSessionResponseBody;

import java.util.HashMap;

public class FlinkSqlGateway {

    private FlinkSqlGateway() {}

    public static DefaultApi sqlGatewayApi(String basePath) {
        ApiClient client = new ApiClient();
        client.setBasePath(basePath);
        return new DefaultApi(client);
    }

    public static DefaultApi sqlGatewayApi() {
        ApiClient client = new ApiClient();
        return new DefaultApi(client);
    }

    public static void main(String[] args) throws ApiException {
        DefaultApi api = new DefaultApi(new ApiClient().setBasePath("http://192.168.201.75:8083"));
        OpenSessionResponseBody openSessionResponseBody = api.openSession(
                new OpenSessionRequestBody().sessionName("example").properties(new HashMap<String, String>() {{
                    put("wzw", "yy");
                }}));
        System.out.println(openSessionResponseBody.toJson());
        System.out.println(openSessionResponseBody.getSessionHandle());
    }
}
