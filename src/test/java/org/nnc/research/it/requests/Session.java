package org.nnc.research.it.requests;

import java.util.Map;

public interface Session extends AutoCloseable {
    Map<String, String> getHeaders();

    Response get(final String url, final Map<String, String> params) throws Exception;

    Response post(final String url, final Map<String, String> params, final String json, final Map<String, String> data) throws Exception;
}
