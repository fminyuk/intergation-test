package org.nnc.research.it.requests;

public class SessionFactoryImpl implements SessionFactory {
    @Override
    public Session createSession() {
        return new SessionImpl();
    }
}
