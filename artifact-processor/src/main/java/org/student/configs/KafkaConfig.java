package org.student.configs;

import java.util.Set;

public class KafkaConfig {
    private String bootstrapServers;
    private String groupId;
    private Set<String> topicsForCreate;
    private Set<String> topicsForRead;
    private Set<String> topicsForUpdate;
    private Set<String> topicsForDelete;

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Set<String> getTopicsForCreate() {
        return topicsForCreate;
    }

    public void setTopicsForCreate(Set<String> topicsForCreate) {
        this.topicsForCreate = topicsForCreate;
    }

    public Set<String> getTopicsForRead() {
        return topicsForRead;
    }

    public void setTopicsForRead(Set<String> topicsForRead) {
        this.topicsForRead = topicsForRead;
    }

    public Set<String> getTopicsForUpdate() {
        return topicsForUpdate;
    }

    public void setTopicsForUpdate(Set<String> topicsForUpdate) {
        this.topicsForUpdate = topicsForUpdate;
    }

    public Set<String> getTopicsForDelete() {
        return topicsForDelete;
    }

    public void setTopicsForDelete(Set<String> topicsForDelete) {
        this.topicsForDelete = topicsForDelete;
    }
}
