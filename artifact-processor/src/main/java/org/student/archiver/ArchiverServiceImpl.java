package org.student.archiver;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.student.configs.KeyStoreConfig;
import org.student.models.Artifact;
import org.student.models.ArtifactMetaInfo;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.student.archiver.KeyStoreManager.getInstance;

public class ArchiverServiceImpl implements ArchiverService {

    private static final String SKIP_ENCRYPTION = " Skip encryption.";
    private static final String RSA = "RSA";
    private static final String ALIAS_PATTERN = "%s%s";
    private static final String SKIP_DECRYPTION = " Skip decryption.";
    private static final String KEYSTORE_JKS_ROOT_PATH = "/keystore.jks";

    private final KeyStore keyStore;
    private final char[] rootPassword;
    private final String pathToKeyStore;
    private final ArchiverRepository archiverRepository = new ArchiverIMStorage(); // TODO: add choice in config: real db or in memory storage
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

        Cipher cipher = createCipher(Cipher.ENCRYPT_MODE, publicKey);
        if (cipher == null) return artifact;

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

        long validity = 100 * 365 * 24 * 60 * 60; // 100 years
        String sigAlgName = "SHA256WithRSA";

        Certificate certificate = null;
        try {
            certificate = generateSelfSignedCertificate(keyPair, "CN=Test", validity, sigAlgName);
        } catch (Exception e) {
            System.err.println("Error creating certificate: " + e.getMessage() + e + SKIP_ENCRYPTION);
            return false;
        }

        KeyStore.PrivateKeyEntry privateKeyEntry = new KeyStore.PrivateKeyEntry(keyPair.getPrivate(), new Certificate[]{certificate});
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

    private Certificate generateSelfSignedCertificate(KeyPair keyPair, String dn, long validity, String sigAlgName) throws Exception {
        X500Name issuerName = new X500Name(dn);
        BigInteger serial = BigInteger.valueOf(new SecureRandom().nextInt());

        Date from = new Date();
        Date to = new Date(from.getTime() + validity * 1000);

        SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo.getInstance(keyPair.getPublic().getEncoded());

        X509v3CertificateBuilder certBuilder = new X509v3CertificateBuilder(issuerName, serial, from, to, issuerName, publicKeyInfo);

        ContentSigner signer = new JcaContentSignerBuilder(sigAlgName).build(keyPair.getPrivate());

        X509CertificateHolder certHolder = certBuilder.build(signer);

        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");

        return certFactory.generateCertificate(new ByteArrayInputStream(certHolder.getEncoded()));
    }

    private String generateAlias() {
        return String.format(ALIAS_PATTERN, defaultRSAAlias, UUID.randomUUID());
    }

    private boolean saveKeyStoreToDisk(KeyStore keyStore) {
        try {
            Path path = Paths.get(pathToKeyStore + KEYSTORE_JKS_ROOT_PATH);
            Files.createDirectories(path.getParent());
            try (OutputStream fos = Files.newOutputStream(path)) {
                keyStore.store(fos, rootPassword);
            }
            return true;
        } catch (CertificateException | KeyStoreException | IOException | NoSuchAlgorithmException e) {
            System.err.println("Error in saving KeyStore to ROM:" + e.getMessage() + e + SKIP_ENCRYPTION);
            return false;
        }
    }

    private Cipher createCipher(int mode, Key key) {
        try {
            var instance =  Cipher.getInstance(RSA);
            instance.init(mode, key);
            return instance;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            System.err.println("Error in instancing Cipher:" + e.getMessage() + e + SKIP_ENCRYPTION);
            return null;
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

        PrivateKey privateKey = getPrivateKeyFromKeyStore(keyStore, archiverRepository.getArtifactAlias(encryptedArtifact.getMetaInfo()));
        if (privateKey == null) return encryptedArtifact;

        Cipher cipher = createCipher(Cipher.DECRYPT_MODE, privateKey);
        if (cipher == null) return encryptedArtifact;

        var decrypted = decryptByteArray(cipher, encryptedArtifact);
        if (decrypted.length == 0) return encryptedArtifact;

        encryptedArtifact.setArtifactData(decrypted);

        return encryptedArtifact;
    }

    @Override
    public void shutdown() {
        saveKeyStoreToDisk(keyStore);
    }

    private PrivateKey getPrivateKeyFromKeyStore(KeyStore keyStore, Optional<String> artifactAliasOpt) {

        if (artifactAliasOpt.isEmpty()){
            System.err.println("Null alias!!!" + SKIP_DECRYPTION);
            return null;
        }

        var artifactAlias = artifactAliasOpt.get();

        try {

            Key key = keyStore.getKey(artifactAlias, rootPassword);
            if (key instanceof PrivateKey) {
                PrivateKey privateKey = (PrivateKey) key;
                return privateKey;
            } else {
                System.err.println("Error in getting private key from KeyStore: not founded key in store." + SKIP_ENCRYPTION);
                return null;
            }
        } catch (NoSuchAlgorithmException | UnrecoverableEntryException | KeyStoreException e) {
            System.err.println("Error in getting private key from KeyStore: " + e.getMessage() + e + SKIP_DECRYPTION);
            return null;
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
