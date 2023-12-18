FROM jboss/keycloak

USER root

RUN dnf update -y && dnf install -y jq

USER jboss

COPY create_users_and_roles.sh /opt/jboss/

ENTRYPOINT [ "/bin/bash", "/opt/jboss/create_users_and_roles.sh" ]
