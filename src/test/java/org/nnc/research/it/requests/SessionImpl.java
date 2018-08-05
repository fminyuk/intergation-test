package org.nnc.research.it.requests;

import com.google.common.primitives.Bytes;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SessionImpl implements Session {
    private final static Logger LOG = LoggerFactory.getLogger(SessionImpl.class);

    private final Map<String, String> headers = new HashMap<>();
    private final CloseableHttpClient client = HttpClients.custom().build();

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Response get(final String url, final Map<String, String> params) throws Exception {
        LOG.info(String.format("GET %s", url));
        final URI uri = buildURI(url, params);
        final HttpGet get = new HttpGet(uri);
        final Response response = getResponse(get);
        LOG.info(String.format("Response [%d]:\n%s", response.getStatusCode(), response.getText()));
        return response;
    }

    public Response post(final String url, final Map<String, String> params, final String json, final Map<String, String> data) throws Exception {
        LOG.info(String.format("POST %s", url));
        final URI uri = buildURI(url, params);
        final HttpPost post = new HttpPost(uri);
        if (json != null) {
            post.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
        }

        if(data != null) {
            final List<NameValuePair> parameters = new ArrayList<>();
            for (final Map.Entry<String, String> entry: data.entrySet()) {
                parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }

            post.setEntity(new UrlEncodedFormEntity(parameters));
        }

        final Response response = getResponse(post);
        LOG.info(String.format("Response [%d]:\n%s", response.getStatusCode(), response.getText()));
        return response;
    }

    private Response getResponse(final HttpUriRequest request) throws IOException {
        for (final Map.Entry<String, String> header : this.headers.entrySet()) {
            request.addHeader(header.getKey(), header.getValue());
        }

        try (final CloseableHttpResponse response = this.client.execute(request)) {
            final int statusCode = response.getStatusLine().getStatusCode();
            final ContentType contentType = ContentType.getOrDefault(response.getEntity());
            try (final InputStream is = response.getEntity().getContent()) {
                final List<Integer> data = new ArrayList<>();
                int value;
                while ((value = is.read()) != -1) {
                    data.add(value);
                }

                return new Response(statusCode, contentType.getCharset(), Bytes.toArray(data));
            }
        }
    }

    @Override
    public void close() throws IOException {
        client.close();
    }

    private static URI buildURI(final String url, final Map<String, String> params) throws URISyntaxException {
        final URIBuilder builder = new URIBuilder(url);
        if (params != null) {
            final List<NameValuePair> nvps = new ArrayList<>();
            for (final Map.Entry<String, String> param : params.entrySet()) {
                nvps.add(new BasicNameValuePair(param.getKey(), param.getValue()));
            }

            builder.addParameters(nvps);
        }

        return builder.build();
    }
}
