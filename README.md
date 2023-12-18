# Pet project

![Java CI](https://github.com/Kasean/FileGarbage/actions/workflows/java_ci.yml/badge.svg)

### Ports:

1. core application - 8080
2. keycloak - 9990

errors 

docker-keycloak-1: 14:37:24,942 INFO  [org.jboss.vfs] (MSC service thread 1-8) VFS000002: Failed to clean existing content for temp file provider of type temp. Enable DEBUG level log to find what caused this
docker-keycloak-1: WARNING: An illegal reflective access operation has occurred
docker-keycloak-1: WARNING: Illegal reflective access by org.wildfly.extension.elytron.SSLDefinitions (jar:file:/opt/jboss/keycloak/modules/system/layers/base/org/wildfly/extension/elytron/main/wildfly-elytron-integration-18.0.4.Final.jar!/) to method com.sun.net.ssl.internal.ssl.Provider.isFIPS()
docker-keycloak-1: WARNING: Please consider reporting this to the maintainers ofdocker-keycloak-1:  org.wildfly.extension.elytron.SSLDefinitions
docker-keycloak-1: WARNING: Use --illegal-access=warn to enable warnings of further illegal reflective access operations
docker-keycloak-1: WARNING: All illegal access operations will be denied in a future release
docker-keycloak-1: 14:37:26,265 INFO  [org.wildfly.security] (ServerService Thread Pool -- 21) ELY00001: WildFly Elytron version 1.18.3.Final
