#!/bin/bash

/opt/jboss/keycloak/bin/kcadm.sh config credentials --server http://localhost:9990/auth --realm master --user admin --password admin

/opt/jboss/keycloak/bin/kcadm.sh create users -r master -s username=SUPERADMIN -s enabled=true
/opt/jboss/keycloak/bin/kcadm.sh add-roles --uusername SUPERADMIN --rolename SUPERADMIN

/opt/jboss/keycloak/bin/kcadm.sh create users -r master -s username=ADMIN -s enabled=true
/opt/jboss/keycloak/bin/kcadm.sh add-roles --uusername ADMIN --rolename ADMIN

exec /opt/jboss/tools/docker-entrypoint.sh -b 0.0.0.0
