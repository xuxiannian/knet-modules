package com.baidubce.services.aipage;

import com.baidubce.AbstractBceClient;
import com.baidubce.BceClientConfiguration;
import com.baidubce.BceClientException;
import com.baidubce.auth.DefaultBceCredentials;
import com.baidubce.auth.SignOptions;
import com.baidubce.http.Headers;
import com.baidubce.http.HttpMethodName;
import com.baidubce.http.handler.BceErrorResponseHandler;
import com.baidubce.http.handler.BceJsonResponseHandler;
import com.baidubce.http.handler.BceMetadataResponseHandler;
import com.baidubce.http.handler.HttpResponseHandler;
import com.baidubce.internal.InternalRequest;
import com.baidubce.internal.RestartableInputStream;
import com.baidubce.model.AbstractBceRequest;
import com.baidubce.services.aipage.model.*;
import com.baidubce.util.HttpUtils;
import com.baidubce.util.JsonUtils;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.*;

public class AiPageClient extends AbstractBceClient {


    private static final String BASE_URL = "site";

    private static final String VERSION = "/v1";

    private static final String[] HEADERS_TO_SIGN = {"host", "x-bce-date"};


    private static final HttpResponseHandler[] handlers = new HttpResponseHandler[]{
            new BceMetadataResponseHandler(),
            new BceErrorResponseHandler(),
            new BceJsonResponseHandler()
    };
    private static final String CLIENT_TOKEN = "clientToken";

    private boolean internal;

    private String accessKey;

    private String secretKey;

    protected AiPageClient(String accessKey, String secretKey, BceClientConfiguration bceClientConfiguration,
                           boolean internal) {
        super(bceClientConfiguration, handlers);
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.internal = internal;
    }

    public static AiPageClient createAiPageClient(String accessKey, String secretKey, String endpoint) {
        BceClientConfiguration bceClientConfiguration = new BceClientConfiguration().withEndpoint(endpoint)
                .withCredentials(new DefaultBceCredentials(accessKey, secretKey));
        return new AiPageClient(accessKey, secretKey, bceClientConfiguration, false);
    }


    public AiPageListResponse createAiPage(AiPageCreateRequest request) {
        InternalRequest internalRequest = this.createRequest(request, HttpMethodName.POST, BASE_URL, "new");
        internalRequest.addParameter(CLIENT_TOKEN, this.generateClientToken());
        attachRequestToBody(request, internalRequest);
        return this.invokeHttpClient(internalRequest, AiPageListResponse.class);

    }

    public AiPageListResponse renewAiPage(AiPageRenewRequest request) {
        InternalRequest internalRequest = this.createRequest(request, HttpMethodName.POST, BASE_URL, "renew");
        internalRequest.addParameter(CLIENT_TOKEN, this.generateClientToken());
        attachRequestToBody(request, internalRequest);
        return this.invokeHttpClient(internalRequest, AiPageListResponse.class);

    }
    public AiPageListResponse listAiPage(AiPageListRequest request) {
        InternalRequest internalRequest = this.createRequest(request, HttpMethodName.GET, BASE_URL, "list");
        attachRequestToBody(request, internalRequest);
        return this.invokeHttpClient(internalRequest, AiPageListResponse.class);

    }

    public AiPageResponse infoAiPage(AiPageListRequest request) {
        InternalRequest internalRequest = this.createRequest(request, HttpMethodName.GET, BASE_URL, "16778c56-9a47-431c1-b1d7-19a1acdd94a7");
        attachRequestToBody(request, internalRequest);
        return this.invokeHttpClient(internalRequest, AiPageResponse.class);

    }


    private InternalRequest createRequest(
            AbstractBceRequest bceRequest, HttpMethodName httpMethod, String... pathVariables) {
        List<String> path = new ArrayList<String>();
        path.add(VERSION);

        if (pathVariables != null) {
            for (String pathVariable : pathVariables) {
                path.add(pathVariable);
            }
        }

        URI uri = HttpUtils.appendUri(this.getEndpoint(), path.toArray(new String[path.size()]));
        InternalRequest request = new InternalRequest(httpMethod, uri);
        SignOptions signOptions = new SignOptions();
        signOptions.setHeadersToSign(new HashSet<String>(Arrays.asList(HEADERS_TO_SIGN)));
        request.setSignOptions(signOptions);
        request.setCredentials(bceRequest.getRequestCredentials());

        return request;
    }

    private void attachRequestToBody(AbstractBceRequest request, InternalRequest httpRequest) {
        String strJson = JsonUtils.toJsonString(request);
        byte[] requestJson = null;
        try {
            requestJson = strJson.getBytes(DEFAULT_ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw new BceClientException("Unsupported encode.", e);
        }
        System.out.println(strJson);
        httpRequest.addHeader(Headers.BCE_DATE, "2020-10-20");
        httpRequest.addHeader(Headers.CONTENT_TYPE, "application/json; charset=utf-8");
        httpRequest.setContent(RestartableInputStream.wrap(requestJson));
    }

    private String generateClientToken() {
        return UUID.randomUUID().toString();
    }
}
