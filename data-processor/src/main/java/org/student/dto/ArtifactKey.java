package org.student.dto;

import java.util.UUID;

public record ArtifactKey(UUID userId, UUID externalId) {
}
