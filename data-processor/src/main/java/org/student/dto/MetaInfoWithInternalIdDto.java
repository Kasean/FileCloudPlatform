package org.student.dto;

import java.util.Objects;
import java.util.UUID;

public record MetaInfoWithInternalIdDto(UUID internalId, String artifactName, long artifactSize) {
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        MetaInfoWithInternalIdDto that = (MetaInfoWithInternalIdDto) object;
        return artifactSize == that.artifactSize && Objects.equals(internalId, that.internalId) && Objects.equals(artifactName, that.artifactName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(internalId, artifactName, artifactSize);
    }
}
