package org.student.dto;

import java.util.Objects;
import java.util.UUID;

public record ExternalMetaInfoDto(UUID externalId, String artifactName, long artifactSize) {
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ExternalMetaInfoDto that = (ExternalMetaInfoDto) object;
        return artifactSize == that.artifactSize && Objects.equals(externalId, that.externalId) && Objects.equals(artifactName, that.artifactName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(externalId, artifactName, artifactSize);
    }
}
