package org.student.archiver;

import org.student.configs.KeyStoreConfig;
import org.student.models.Artifact;
import org.student.models.ArtifactMetaInfo;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.UUID;

import static org.student.archiver.KeyStoreManager.getInstance;

public class ArchiverServiceImpl implements ArchiverService {

    private static final String SKIP_ENCRYPTION = " Skip encryption.";
    private static final String RSA = "RSA";
    private static final String ALIAS_PATTERN = "%s%s";
    private static final String SKIP_DECRYPTION = " Skip decryption.";

    private final KeyStore keyStore;
    private final char[] rootPassword;
    private final String pathToKeyStore;
    private final ArchiverRepository archiverRepository = new IMStorage(); // TODO: add choice in config: real db or in memory storage
    private final String defaultRSAAlias;

    public ArchiverServiceImpl(KeyStoreConfig config) {
        this.rootPassword = config.getRootPassword().toCharArray();
        this.pathToKeyStore = config.getPathToKeyStore();
        this.defaultRSAAlias = config.getDefaultRSAAlias();

        this.keyStore = getInstance(rootPassword, pathToKeyStore);
    }

    @Override
    public Artifact encrypt(byte[] rawArtifactMessage) {

        Artifact artifact = new Artifact(rawArtifactMessage);

        KeyPair keyPair = generateKeyPair();
        if (keyPair == null) return artifact;

        if (keyStore == null) return artifact;

        if (!loadKeyStore(keyStore)) return artifact;

        if (!addPrivateKeyToKeyStore(keyStore, keyPair, artifact.getMetaInfo())) return artifact;

        if (!saveKeyStoreToDisk(keyStore)) return artifact;

        PublicKey publicKey = keyPair.getPublic();

        Cipher cipher = createCipher();
        if (cipher == null) return artifact;

        if (!initCipher(cipher, publicKey)) return artifact;

        return encryptByteArray(cipher, artifact);
    }

    private KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA);
            keyPairGenerator.initialize(2048);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error in creating KeyPairGenerator:" + e.getMessage() + e + SKIP_ENCRYPTION);
            return null;
        }
    }

    private boolean loadKeyStore(KeyStore keyStore) {
        try {
            keyStore.load(null, rootPassword);
            return true;
        } catch (IOException | NoSuchAlgorithmException | CertificateException e) {
            System.err.println("Error in loading KeyStore:" + e.getMessage() + e + SKIP_ENCRYPTION);
            return false;
        }
    }

    private boolean addPrivateKeyToKeyStore(KeyStore keyStore, KeyPair keyPair, ArtifactMetaInfo metaInfo) {
        KeyStore.PrivateKeyEntry privateKeyEntry = new KeyStore.PrivateKeyEntry(keyPair.getPrivate(), new Certificate[]{});
        try {
            var alias = generateAlias();
            keyStore.setEntry(alias, privateKeyEntry, new KeyStore.PasswordProtection(rootPassword));
            archiverRepository.saveArtifactAlias(metaInfo, alias);
            return true;
        } catch (KeyStoreException e) {
            System.err.println("Error in adding private key to KeyStore:" + e.getMessage() + e + SKIP_ENCRYPTION);
            return false;
        }
    }

    private String generateAlias() {
        return String.format(ALIAS_PATTERN, defaultRSAAlias, UUID.randomUUID());
    }

    private boolean saveKeyStoreToDisk(KeyStore keyStore) {
        try {
            Path path = Paths.get(pathToKeyStore + "/keystore.jks");
            try (OutputStream fos = Files.newOutputStream(path)) {
                keyStore.store(fos, rootPassword);
            }
            return true;
        } catch (CertificateException | KeyStoreException | IOException | NoSuchAlgorithmException e) {
            System.err.println("Error in saving KeyStore to ROM:" + e.getMessage() + e + SKIP_ENCRYPTION);
            return false;
        }
    }

    private Cipher createCipher() {
        try {
            return Cipher.getInstance(RSA);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            System.err.println("Error in instancing Cipher:" + e.getMessage() + e + SKIP_ENCRYPTION);
            return null;
        }
    }

    private boolean initCipher(Cipher cipher, PublicKey publicKey) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return true;
        } catch (InvalidKeyException e) {
            System.err.println("Error in initing Cipher:" + e.getMessage() + e + SKIP_ENCRYPTION);
            return false;
        }
    }

    private Artifact encryptByteArray(Cipher cipher, Artifact artifact) {
        try {
            var finalRes = cipher.doFinal(artifact.getArtifactData());
            artifact.setArtifactData(finalRes);
            return artifact;
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            System.err.println("Error in encrypting bytes array:" + e.getMessage() + e + SKIP_ENCRYPTION);
            return artifact;
        }
    }

    @Override
    public Artifact decrypt(Artifact encryptedArtifact) {

        if (keyStore == null) return encryptedArtifact;

        if (!loadKeyStore(keyStore)) return encryptedArtifact;

        PrivateKey privateKey = getPrivateKeyFromKeyStore(keyStore, archiverRepository.getArtifactAlias(encryptedArtifact.getMetaInfo()));
        if (privateKey == null) return encryptedArtifact;

        Cipher cipher = createCipher();
        if (cipher == null) return encryptedArtifact;

        if (!initCipherForDecryption(cipher, privateKey)) return encryptedArtifact;

        var decrypted = decryptByteArray(cipher, encryptedArtifact);
        if (decrypted.length == 0) return encryptedArtifact;

        encryptedArtifact.setArtifactData(decrypted);

        return encryptedArtifact;
    }

    private PrivateKey getPrivateKeyFromKeyStore(KeyStore keyStore, String artifactAlias) {

        if (artifactAlias == null){
            System.err.println("Null alias!!!" + SKIP_DECRYPTION);
            return null;
        }

        try {
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(artifactAlias, new KeyStore.PasswordProtection(rootPassword));
            return privateKeyEntry.getPrivateKey();
        } catch (NoSuchAlgorithmException | UnrecoverableEntryException | KeyStoreException e) {
            System.err.println("Error in getting private key from KeyStore:" + e.getMessage() + e + SKIP_DECRYPTION);
            return null;
        }
    }

    private boolean initCipherForDecryption(Cipher cipher, PrivateKey privateKey) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return true;
        } catch (InvalidKeyException e) {
            System.err.println("Error in initing Cipher for decryption:" + e.getMessage() + e + SKIP_DECRYPTION);
            return false;
        }
    }

    private byte[] decryptByteArray(Cipher cipher, Artifact encryptedMessage) {
        try {
            return cipher.doFinal(encryptedMessage.getArtifactData());
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            System.err.println("Error in decrypting bytes array:" + e.getMessage() + e + SKIP_DECRYPTION);
            return new byte[0];
        }
    }
}
