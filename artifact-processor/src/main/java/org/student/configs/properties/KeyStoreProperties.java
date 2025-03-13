package org.student.configs.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "key-store")
public class KeyStoreProperties {

    private String rootPassword;

    private String pathToKeyStore;

    private String defaultRSAAlias;

    public String getRootPassword() {
        return rootPassword;
    }

    public void setRootPassword(String rootPassword) {
        this.rootPassword = rootPassword;
    }

    public String getPathToKeyStore() {
        return pathToKeyStore;
    }

    public void setPathToKeyStore(String pathToKeyStore) {
        this.pathToKeyStore = pathToKeyStore;
    }

    public String getDefaultRSAAlias() {
        return defaultRSAAlias;
    }

    public void setDefaultRSAAlias(String defaultRSAAlias) {
        this.defaultRSAAlias = defaultRSAAlias;
    }
}
