package org.nnc.research.it;

import org.mockserver.integration.ClientAndServer;
import org.nnc.research.it.requests.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Test
@ContextConfiguration(locations = {"/SomeIT-context.xml"})
public class SomeIT extends AbstractTestNGSpringContextTests {
    @Value("${mockserver.port}")
    private int mockServerPort;

    @Autowired
    private Listener listener;

    @Autowired
    private SessionFactory sessionFactory;

    private ClientAndServer mockServer;

    @BeforeMethod
    public void startServer() {
        mockServer = ClientAndServer.startClientAndServer(mockServerPort);
    }

    @AfterMethod
    public void stopServer() {
        mockServer.stop();
    }

    @Test
    public void test() {
        System.out.println(listener != null);
    }
}
