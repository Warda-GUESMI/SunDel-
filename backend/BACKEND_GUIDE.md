# Backend Spring Boot - Guide de démarrage rapide
# ================================================
# Ce fichier décrit comment créer l'API Spring Boot
# qui accompagne l'application Android.

## Structure du projet Spring Boot recommandée

```
backend/
├── src/main/java/com/delivery/backend/
│   ├── config/
│   │   ├── SecurityConfig.java       ← JWT + CORS
│   │   └── JwtUtil.java
│   ├── controller/
│   │   ├── AuthController.java       ← POST /api/auth/login
│   │   ├── LivraisonController.java  ← GET/PUT /api/livraisons
│   │   ├── DashboardController.java  ← GET /api/dashboard
│   │   └── MessageController.java   ← GET/POST /api/messages
│   ├── model/
│   │   ├── Personnel.java
│   │   ├── LivraisonCom.java
│   │   └── Message.java
│   ├── repository/
│   │   └── (JPA Repositories)
│   └── service/
│       └── (Business logic)
└── src/main/resources/
    └── application.properties
```

## application.properties

```properties
server.port=8080
spring.datasource.url=jdbc:mysql://localhost:3306/BDG_LivraisonCom_25
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true
jwt.secret=your_jwt_secret_key_min_32_chars
jwt.expiration=86400000
```

## pom.xml dépendances clés

```xml
<dependencies>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
  </dependency>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
  </dependency>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
  </dependency>
  <dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
  </dependency>
  <dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
  </dependency>
</dependencies>
```

## Endpoints API attendus par l'app Android

| Méthode | URL                                     | Rôle         |
|---------|------------------------------------------|--------------|
| POST    | /api/auth/login                         | Tous         |
| GET     | /api/livraisons                         | Contrôleur   |
| GET     | /api/livraisons/today                   | Contrôleur   |
| GET     | /api/livraisons/livreur/{id}/today      | Livreur      |
| GET     | /api/livraisons/{nocde}                 | Tous         |
| PUT     | /api/livraisons/{nocde}                 | Livreur      |
| GET     | /api/dashboard                          | Contrôleur   |
| GET     | /api/messages                           | Tous         |
| POST    | /api/messages                           | Tous         |
| PUT     | /api/messages/{id}/lu                   | Tous         |
| GET     | /api/livreurs                           | Contrôleur   |

## Réponse login attendue

```json
{
  "idpers": 1,
  "nompers": "Ben Ali",
  "prenompers": "Sami",
  "login": "ctrl1",
  "telpers": "55001122",
  "codeposte": "controleur",
  "token": "eyJhbGci..."
}
```

## Réponse livraison attendue

```json
{
  "nocde": 1,
  "dateliv": "2025-04-23",
  "livreur_id": 2,
  "livreur_nom": "Rami Trabelsi",
  "modepay": "especes",
  "etatliv": "en_cours",
  "client_nom": "Chaabane",
  "client_prenom": "Ahmed",
  "client_tel": "22001100",
  "client_adresse": "3 rue Habib",
  "client_ville": "Tunis",
  "client_code_postal": "1001",
  "montant_total": 23.0,
  "nb_articles": 3,
  "ordre_livraison": 1,
  "remarques": null
}
```

## Notes importantes

1. L'IP dans RetrofitClient.java est 10.0.2.2 (localhost émulateur Android)
   → Pour un vrai téléphone, mettre l'IP de votre PC sur le réseau local
   → Ex: http://192.168.1.X:8080/api/

2. Le mot de passe doit être stocké hashé (BCrypt) dans la BD

3. Le token JWT doit être passé dans le header Authorization: Bearer <token>
