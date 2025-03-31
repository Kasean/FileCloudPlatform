# SaaS project - cloud artifacts storage

![Java CI](https://github.com/Kasean/FileGarbage/actions/workflows/java_ci.yml/badge.svg)

## Stack: java 17, spring boot 3.x

## Documentation:

### Application ports:

1. core application - 8080
2. data application - 8083
3. keycloak - 9990
4. kafka - 9092

### Base structure

![Structure](docs/pictures/FileCloudStorageBaseStruct.png)

---

## 1. Artifact Processor

### Base Structure

![Artifact Processor Structure](docs/pictures/ArtifactProcessorBaseStructure.png)

The Artifact Processor includes core data storage and Archiver data storage modules. In the alpha version, this is a simple in-memory storage (e.g., using a Map).

In the first beta version, we will implement either [Riak KV](https://riak.com/products/integrations/) or Apache Cassandra for more robust storage solutions.

---

## 2. Data Processor

### Base Structure

![Data Processor Structure](docs/pictures/DataProcessorBaseStruct.png)

The Data Processor module is responsible for handling artifact meta-information.

---

## 3. Core Backend

### Base Structure

![Core Backend Structure](docs/pictures/CoreBackendStruct.png)

Core Backend is the module that the user interacts with to add artifacts. It sends requests via Kafka to other modules and, after receiving the results, executes them.

- **Actuator is available at:** `/actuator`
- **API description:**
    - **JSON:**
        - Path: `/api/v1/docs`
    - **Swagger UI:**
        - Path: `/api/v1/docs-ui.html`
