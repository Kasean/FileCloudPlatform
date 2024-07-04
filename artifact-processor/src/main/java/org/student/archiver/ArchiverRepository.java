package org.student.archiver;

import org.student.models.ArtifactMetaInfo;

import java.util.Optional;

public interface ArchiverRepository {

    void saveArtifactAlias(ArtifactMetaInfo metaInfo, String alias);

    Optional<String> getArtifactAlias(ArtifactMetaInfo metaInfo);
}
