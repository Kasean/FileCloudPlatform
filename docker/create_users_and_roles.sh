#!/bin/bash

# Start Keycloak
/opt/jboss/tools/docker-entrypoint.sh -b 0.0.0.0 &

# Wait for Keycloak to start
sleep 20

# Get admin access token
TOKEN=$(curl -s -X POST -H "Content-Type: application/x-www-form-urlencoded" -d 'username=admin' -d 'password=admin' -d 'grant_type=password' -d 'client_id=admin-cli' "http://localhost:9990/auth/realms/master/protocol/openid-connect/token" | jq -r .access_token)

# Create roles
curl -s -X POST -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" -d '{"name":"SUPERADMIN"}' "http://localhost:9990/auth/admin/realms/master/roles"
curl -s -X POST -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" -d '{"name":"ADMIN"}' "http://localhost:9990/auth/admin/realms/master/roles"

# Create client
curl -s -X POST -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" -d '{"clientId":"CoreApplication", "secret":"SecretCoreApplication", "enabled":true, "publicClient":false, "redirectUris":["/*"]}' "http://localhost:9990/auth/admin/realms/master/clients"

# Create users
curl -s -X POST -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" -d '{"username":"SUPERADMIN","enabled":true,"credentials":[{"type":"password","value":"superadminpassword","temporary":false}]}' "http://localhost:9990/auth/admin/realms/master/users"
curl -s -X POST -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" -d '{"username":"ADMIN","enabled":true,"credentials":[{"type":"password","value":"adminpassword","temporary":false}]}' "http://localhost:9990/auth/admin/realms/master/users"

# Assign roles to users
SUPERADMIN_ID=$(curl -s -X GET -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" "http://localhost:9990/auth/admin/realms/master/users?username=SUPERADMIN" | jq -r .[0].id)
ADMIN_ID=$(curl -s -X GET -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" "http://localhost:9990/auth/admin/realms/master/users?username=ADMIN" | jq -r .[0].id)

curl -s -X POST -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" -d '[{"id":"","name":"SUPERADMIN"}]' "http://localhost:9990/auth/admin/realms/master/users/$SUPERADMIN_ID/role-mappings/realm"
curl -s -X POST -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" -d '[{"id":"","name":"ADMIN"}]' "http://localhost:9990/auth/admin/realms/master/users/$ADMIN_ID/role-mappings/realm"