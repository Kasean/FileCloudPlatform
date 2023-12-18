FROM jboss/keycloak

USER root

RUN apt-get update && apt-get install -y jq

USER jboss

COPY create_users_and_roles.sh /opt/jboss/

ENTRYPOINT [ "/bin/bash", "/opt/jboss/create_users_and_roles.sh" ]
