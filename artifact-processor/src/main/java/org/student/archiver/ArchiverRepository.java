package org.student.archiver;

import org.student.models.ArtifactMetaInfo;

public interface ArchiverRepository {

    void saveArtifactAlias(ArtifactMetaInfo metaInfo, String alias);
}
