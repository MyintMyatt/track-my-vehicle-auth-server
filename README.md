### Auth Server

#### User Types
1. Admin (Web Portal)
2. Employee (Mobile)
3. Driver (Mobile)

- Admin login using email and password
- Employee login using email and password
- Driver login using phone and password

## Key Generation
- Generate **Private Key** Using OpenSSL
```zsh
openssl genpkey -algorithm RSA -out private-key.pem -pkeyopt rsa_keygen_bits:2048
```
- Extract **Public Key**
```zsh
openssl rsa -pubout -in private-key.pem -out public-key.pem
```