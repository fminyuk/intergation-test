package org.nnc.research.requests;

public class SessionFactoryImpl implements SessionFactory {
    @Override
    public Session createSession() {
        return new SessionImpl();
    }
}
