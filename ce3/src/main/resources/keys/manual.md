### RSA key pair PKCS#8 PEM

#### generate private

```shell
openssl genpkey \
  -algorithm RSA \
  -pkeyopt rsa_keygen_bits:3072 \
  -out rsa-private-a.pem
```

#### extract public

```shell
openssl pkey \
  -in rsa-private-a.pem \
  -pubout \
  -out rsa-public-a.pem
```

---

### RSA key pair PKCS#1 PEM

#### generate private

```shell
openssl genrsa -out rsa-pkcs1-b.pem 2048
```

#### extract public

```shell
openssl rsa \
  -in rsa-pkcs1-b.pem \
  -pubout \
  -out rsa-public-b.pem
```

---

### Ed25519 key pair PKCS#8 PEM

```shell
openssl genpkey \
  -algorithm ED25519 \
  -out ed25519-private-b.pem
```

```shell
openssl pkey \
  -in ed25519-private-b.pem \
  -pubout \
  -out ed25519-public-b.pem
```

---

### install openssl with Ed25519 support

```shell
brew install openssl@3
openssl -algo

/opt/homebrew/opt/openssl@3/bin/openssl genpkey \
  -algorithm ED25519 \
  -out ed25519-private-c.pem

/opt/homebrew/opt/openssl@3/bin/openssl pkey \
  -in ed25519-private-c.pem \
  -pubout \
  -out ed25519-public-c.pem
```