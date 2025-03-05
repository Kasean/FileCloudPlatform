package archiver;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.student.archiver.ArchiverService;
import org.student.archiver.ArchiverServiceImpl;
import org.student.configs.properties.KeyStoreProperties;
import org.student.models.Artifact;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class ArchiverServiceTest {

    private static final String RSA_ALIAS = "RSAAlias";
    private static final String ROOT_PASSWORD = "P@55w0rd";
    private static final String PATH_TO_KEY_STORE = "src/test/resources";
    private ArchiverService archiverService;

    @BeforeEach
    void setUp() {
        archiverService = new ArchiverServiceImpl(ROOT_PASSWORD.toCharArray(),PATH_TO_KEY_STORE,RSA_ALIAS);
    }

    @AfterEach
    void tearDown() {
        Path path = Paths.get(PATH_TO_KEY_STORE + "/keystore.jks");
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testEncrypt() {
        byte[] rawArtifactMessage = "Test message".getBytes();
        Artifact artifact = archiverService.encrypt(rawArtifactMessage);

        assertNotNull(artifact, "Artifact should not be null");
        assertNotEquals(rawArtifactMessage[0], artifact.getArtifactData()[0]);
    }

    @Test
    void testDecrypt() {
        var testStr = "Test message";
        byte[] rawArtifactMessage = testStr.getBytes();
        Artifact encryptedArtifact = archiverService.encrypt(rawArtifactMessage);
        Artifact decryptedArtifact = archiverService.decrypt(encryptedArtifact);

        var actualStr = new String(decryptedArtifact.getArtifactData());

        assertNotNull(decryptedArtifact, "Decrypted artifact should not be null");
        assertEquals(actualStr, testStr);

    }

}
