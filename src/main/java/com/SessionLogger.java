package com;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Loguje moment wylogowania.
 *
 * @author Ryszard Poklewski-Koziełł
 */
public class SessionLogger implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent se) {
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        System.out.println("User logged out");
    }
}
