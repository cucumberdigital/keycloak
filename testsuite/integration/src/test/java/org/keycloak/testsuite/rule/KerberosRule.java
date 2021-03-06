/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.keycloak.testsuite.rule;

import org.jboss.logging.Logger;
import org.keycloak.testsuite.federation.ldap.LDAPTestConfiguration;
import org.keycloak.util.ldap.KerberosEmbeddedServer;
import org.keycloak.util.ldap.LDAPEmbeddedServer;

import java.io.File;
import java.net.URL;
import java.util.Properties;

/**
 * @author <a href="mailto:mposolda@redhat.com">Marek Posolda</a>
 */
public class KerberosRule extends LDAPRule {

    private static final Logger log = Logger.getLogger(KerberosRule.class);

    private final String configLocation;

    public KerberosRule(String configLocation) {
        this(configLocation, null);
    }

    public KerberosRule(String configLocation, LDAPRuleCondition condition) {
        super(condition);
        this.configLocation = configLocation;

        // Global kerberos configuration
        URL krb5ConfURL = LDAPTestConfiguration.class.getResource("/kerberos/test-krb5.conf");
        String krb5ConfPath = new File(krb5ConfURL.getFile()).getAbsolutePath();
        log.info("Krb5.conf file location is: " + krb5ConfPath);
        System.setProperty("java.security.krb5.conf", krb5ConfPath);
    }

    @Override
    protected String getConnectionPropertiesLocation() {
        return configLocation;
    }

    @Override
    protected LDAPEmbeddedServer createServer() {
        Properties defaultProperties = new Properties();
        defaultProperties.setProperty(LDAPEmbeddedServer.PROPERTY_DSF, LDAPEmbeddedServer.DSF_INMEMORY);
        defaultProperties.setProperty(LDAPEmbeddedServer.PROPERTY_LDIF_FILE, "classpath:kerberos/users-kerberos.ldif");

        return new KerberosEmbeddedServer(defaultProperties);
    }
}
