#!/usr/bin/env bash
set -e -x

DIR=src/test/resources
DOMAIN=localhost

ca_key=${DIR}/testCA.key
ca_cert=${DIR}/testCA.crt

server_key=${DIR}/server.key
server_cert=${DIR}/server.crt

ca_subject="/C=US/ST=California/L=Sunnyvale/O=Test/CN=testCA"
server_subject="/C=US/ST=California/L=Sunnyvale/O=Test/CN=${DOMAIN}"

tmp_dir=$(mktemp -d -t cert-XXXXXXXXXX) || exit 1
trap 'rm -rf "$tmp_dir"' EXIT

# Create CA key and certificate
openssl genrsa -out ${ca_key} 2048
openssl req -x509 -new -nodes -key ${ca_key} -sha256 -days 3650 -out ${ca_cert} -subj "${ca_subject}"

# Create Server key and certificate
openssl req -new -newkey rsa:2048 -sha256 -nodes -keyout ${server_key} -subj "${server_subject}" -out ${tmp_dir}/server.crt
cat <<EOF >${tmp_dir}/v3.ext
authorityKeyIdentifier=keyid,issuer
basicConstraints=CA:FALSE
keyUsage=digitalSignature,nonRepudiation,keyEncipherment,dataEncipherment,keyAgreement
extendedKeyUsage=serverAuth
subjectAltName=@alt_names

[alt_names]
DNS.1=${DOMAIN}
EOF

openssl x509 -req -in ${tmp_dir}/server.crt -CA ${ca_cert} -CAkey ${ca_key} -CAcreateserial -out ${server_cert} -sha256 -extfile ${tmp_dir}/v3.ext
