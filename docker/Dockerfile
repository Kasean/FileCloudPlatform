FROM jboss/keycloak

USER root

RUN curl -L https://github.com/stedolan/jq/releases/download/jq-1.6/jq-linux64 -o /usr/bin/jq && \
    chmod +x /usr/bin/jq

USER jboss

COPY create_users_and_roles.sh /opt/jboss/

ENTRYPOINT [ "/bin/bash", "/opt/jboss/create_users_and_roles.sh" ]
