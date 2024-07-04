package org.student.archiver;

import org.student.models.Artifact;

public interface ArchiverService {

    Artifact encrypt(byte[] rawArtifactMessage);

}
