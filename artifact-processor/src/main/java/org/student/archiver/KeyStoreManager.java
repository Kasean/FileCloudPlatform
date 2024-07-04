package org.student.archiver;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class KeyStoreManager {

    private static KeyStore instance = null;

    public static KeyStore getInstance(char[] rootPassword, String pathToKeyStore){
        if (instance == null){
            createKeyStore(rootPassword, pathToKeyStore);
            return instance;
        } else
            return instance;
    }

    private static void createKeyStore(char[] rootPassword, String pathToKeyStore) {
        try {
            loadKSFromDisk(rootPassword, pathToKeyStore);
        } catch (KeyStoreException e) {
            System.err.println("Error in creating KeyStore:" + e.getMessage() + e + " Skip encryption.");
            instance =  null;
        }
    }

    private static void loadKSFromDisk(char[] rootPassword, String pathToKeyStore) throws KeyStoreException {
        try {
            Path path = Paths.get(pathToKeyStore + "/keystore.jks");
            try (InputStream fis = Files.newInputStream(path)) {
                instance.load(fis, rootPassword);
            }
        } catch (IOException | NoSuchAlgorithmException | CertificateException e) {
            System.err.println("Error in loading KeyStore from ROM:" + e.getMessage() + e + " Create new instance.");

            instance = KeyStore.getInstance("JKS");
        }
    }

}
