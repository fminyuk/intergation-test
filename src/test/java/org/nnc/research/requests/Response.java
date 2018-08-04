package org.nnc.research.requests;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class Response {
    private final int statusCode;
    private final Charset charset;
    private final byte[] data;

    public Response(int statusCode, Charset charset, byte[] data) {
        this.statusCode = statusCode;
        this.charset = charset;
        this.data = data;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public Charset getCharset() {
        return this.charset;
    }

    public byte[] getData() {
        return this.data;
    }

    public String getText() {
        return this.charset == null ? new String(this.data) : new String(this.data, this.charset);
    }

    public JsonNode getJson() throws IOException {
        final ObjectMapper jsonMapper = new ObjectMapper();
        return jsonMapper.readTree(this.getText());
    }

    public Document getXml() throws ParserConfigurationException, IOException, SAXException {
        final DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder builder = builderFactory.newDocumentBuilder();
        try (final InputStream is = new ByteArrayInputStream(this.data)) {
            return builder.parse(is);
        }
    }
}
