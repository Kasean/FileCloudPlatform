package org.student.configs;

public class KeyStoreConfig {

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
