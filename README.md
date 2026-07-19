### Auth Server

An enterprise-grade real-time ferry car tracking system that enables driver, vehicles, employee and ways management through admin portal.Employees receive push notification or mail alerts when ferry arrives at the designated pickup point.Administrators can monitor the live location of every ferry, while employees can notify driver of theirs attendance.

### Library
- [track-my-vehicle-commons](https://github.com/MyintMyatt/track-my-vehicle-commons)
- [track-my-vehicle-core-domain](https://github.com/MyintMyatt/track-my-vehicle-core-domain)

### Services
1. [Auth Service](https://github.com/MyintMyatt/track-my-vehicle-auth-server) - port _9000_
2. [Admin Service](https://github.com/MyintMyatt/track-my-vehicle-admin-service) - port _8081_

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