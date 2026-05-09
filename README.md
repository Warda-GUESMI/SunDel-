# Application Mobile — Supervision des Livraisons

Application Android dédiée à la gestion, au suivi et à la supervision des livraisons en temps réel. Elle adresse deux profils distincts : les contrôleurs, responsables de la planification et de la supervision, et les livreurs, chargés de l'exécution des livraisons sur le terrain. L'objectif est d'optimiser l'organisation des opérations, de fluidifier la communication interne et d'offrir une visibilité complète sur l'activité de livraison.

---

## Architecture technique

Le projet repose sur une architecture trois tiers découplée :

- **Application mobile** : développée en Java avec l'Android SDK
- **Backend** : API REST construite avec Spring Boot
- **Base de données** : MySQL
- **Authentification** : sécurisée par JSON Web Token (JWT)

---

## Structure du projet

```
DeliveryApp/
├── app/        → Application Android (interface utilisateur)
├── backend/    → Documentation de l'API REST
├── database/   → Scripts de création et d'initialisation de la base de données
└── README.md
```

---

## Fonctionnalités

### Profil Contrôleur

- Accès sécurisé avec gestion des rôles
- Visualisation et filtrage des livraisons selon plusieurs critères : date, état, client, commande
- Tableau de bord statistique par livreur et par client
- Système de messagerie interne pour communiquer avec les livreurs

### Profil Livreur

- Consultation des livraisons assignées, triées par zone géographique
- Accès aux détails complets de chaque livraison : client, adresse, mode de paiement, articles
- Navigation intégrée via Google Maps
- Mise à jour du statut des livraisons avec possibilité d'ajouter des remarques
- Messagerie vers le contrôleur

---

## Sécurité

L'accès à l'application est conditionné par une authentification JWT. Les droits sont segmentés selon le rôle de l'utilisateur (Contrôleur ou Livreur), garantissant que chaque profil n'accède qu'aux fonctionnalités qui lui sont attribuées. Les données sensibles sont protégées en transit et au repos.

---

## Design et expérience utilisateur

L'interface adopte une approche minimaliste (flat design), pensée pour un usage terrain : lisibilité immédiate, navigation simple et interactions rapides. La palette graphique s'articule autour du jaune, du blanc et du gris, en accord avec les principes modernes d'ergonomie mobile.

---

## Objectifs

- Centraliser la gestion des livraisons au sein d'une seule plateforme
- Améliorer la coordination entre les équipes terrain et la supervision
- Réduire les erreurs opérationnelles liées aux communications fragmentées
- Fournir une vision claire et exploitable des performances individuelles et collectives

---

## Technologies

| Composant | Technologie |
|---|---|
| Application mobile | Java, Android SDK |
| Backend | Spring Boot, API REST |
| Base de données | MySQL |
| Authentification | JSON Web Token (JWT) |
| Navigation | Google Maps SDK |
