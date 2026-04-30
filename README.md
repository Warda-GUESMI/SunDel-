# Application Mobile - Supervision des Livraisons

## Description
Application Android pour la supervision et le suivi des livraisons, dédiée à deux types d'utilisateurs : **Contrôleurs** et **Livreurs**.

## Architecture
- **Frontend** : Android (Java, XML)
- **Backend** : Spring Boot (API REST)
- **Base de données** : MySQL (`BDG_LivraisonCom_25`)
- **Auth** : JWT (JSON Web Token)

## Structure du projet

```
DeliveryApp/
├── app/                          ← Module Android
│   └── src/main/
│       ├── java/com/delivery/app/
│       │   ├── api/              ← Retrofit + ApiService
│       │   ├── models/           ← User, Livraison, Message, DashboardStats
│       │   ├── ui/
│       │   │   ├── login/        ← LoginActivity
│       │   │   ├── controller/   ← ControllerActivity + Fragments + Adapters
│       │   │   └── livreur/      ← LivreurActivity + Fragments + Adapters
│       │   └── utils/            ← SessionManager
│       └── res/                  ← Layouts, menus, drawables, values
├── database/
│   └── schema.sql                ← Script SQL complet avec données de test
├── backend/
│   └── BACKEND_GUIDE.md          ← Guide pour créer l'API Spring Boot
└── README.md
```

## Fonctionnalités

### Contrôleur
- Connexion avec redirection automatique selon le rôle
- Liste des livraisons avec filtres (date, état, client, N° commande)
- Tableau de bord : graphiques par livreur et par client (PieChart + BarChart)
- Messagerie temps réel vers les livreurs

### Livreur
- Liste de ses livraisons du jour (triées par zone)
- Détail complet : client, adresse, montant, articles, mode de paiement
- Lien direct Google Maps
- Modification de l'état + remarques
- Envoi de messages d'urgence au contrôleur

## Étapes pour lancer le projet

### 1. Base de données
```bash
mysql -u root -p < database/schema.sql
```

### 2. Backend Spring Boot
Voir `backend/BACKEND_GUIDE.md` pour créer l'API.

### 3. Configuration de l'URL API
Ouvrir `app/src/main/java/com/delivery/app/api/RetrofitClient.java` et modifier :
```java
// Pour émulateur Android Studio :
private static final String BASE_URL = "http://10.0.2.2:8080/api/";

// Pour un vrai téléphone sur le même réseau Wi-Fi :
private static final String BASE_URL = "http://192.168.1.XXX:8080/api/";
```

### 4. Android Studio
1. Ouvrir Android Studio
2. `File > Open` → sélectionner le dossier `DeliveryApp`
3. Attendre la synchronisation Gradle
4. Brancher un émulateur ou téléphone
5. Cliquer sur `Run`

> **Note** : MPAndroidChart est sur JitPack. Ajouter dans `settings.gradle` :
> ```groovy
> maven { url 'https://jitpack.io' }
> ```

## Comptes de test (après import SQL)

| Login  | Mot de passe | Rôle        |
|--------|--------------|-------------|
| ctrl1  | password123  | Contrôleur  |
| livr1  | password123  | Livreur     |
| livr2  | password123  | Livreur     |

*(Les mots de passe doivent être hashés en BCrypt dans la BD)*

## Design
- Couleurs : Jaune (#FFC107), Blanc, Gris
- Style flat / minimaliste
- Material Design 3
