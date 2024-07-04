# SaaS project - cloud file storage

![Java CI](https://github.com/Kasean/FileGarbage/actions/workflows/java_ci.yml/badge.svg)

## Stack: java 17, spring boot 3.x

## Documentation:

### Application ports:

1. core application - 8080
2. keycloak - 9990

### Base structure

![Structure](docs/pictures/FileCloudStorageBaseStruct.png)

### Modules docs:

#### 1. Artifact Processor

##### Base structure:

![ArtifactProcessorStructure](docs/pictures/ArtifactProcessorBaseStructure.png)

Core data storage and Archiver data storage modules - in alfa version this is simple in memory storage (Map for example). 
In first beta version - ![Riak KV](https://riak.com/products/integrations/) or Apache cassandra

