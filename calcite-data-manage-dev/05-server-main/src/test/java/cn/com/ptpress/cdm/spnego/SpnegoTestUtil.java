/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.com.ptpress.cdm.spnego;

import org.apache.kerby.kerberos.kerb.KrbException;
import org.apache.kerby.kerberos.kerb.server.SimpleKdcServer;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;

/**
 * Utility class for setting up SPNEGO
 */
public class SpnegoTestUtil {

    public static final String REALM = "EXAMPLE.COM";
    public static final String KDC_HOST = "localhost";
    public static final String CLIENT_PRINCIPAL = "client@" + REALM;
    public static final String SERVER_PRINCIPAL = "HTTP/" + KDC_HOST + "@" + REALM;

    private static final String TARGET_DIR_NAME = System.getProperty("target.dir", "target");
    public static final File TARGET_DIR = new File(System.getProperty("user.dir"), TARGET_DIR_NAME);

    private SpnegoTestUtil() {
    }

    public static int getFreePort() throws IOException {
        try (ServerSocket s = new ServerSocket(0)) {
            s.setReuseAddress(true);
            return s.getLocalPort();
        }
    }

    public static void setupUser(SimpleKdcServer kdc, File keytab, String principal)
            throws KrbException {
        kdc.createPrincipal(principal);
        kdc.exportPrincipal(principal, keytab);
    }

    /**
     * Recursively deletes a {@link File}.
     */
    public static void deleteRecursively(File d) {
        if (d.isDirectory()) {
            for (String name : d.list()) {
                File child = new File(d, name);
                if (child.isFile()) {
                    child.delete();
                } else {
                    deleteRecursively(d);
                }
            }
        }
        d.delete();
    }
}

// End SpnegoTestUtil.java
