Application Mobile – Supervision des Livraisons
Description

Cette application Android permet la gestion, le suivi et la supervision des livraisons en temps réel.
Elle est conçue pour deux profils d’utilisateurs : les contrôleurs (gestion et supervision) et les livreurs (exécution des livraisons).

L’objectif est d’optimiser l’organisation des livraisons, d’améliorer la communication interne et d’offrir une meilleure visibilité sur les opérations.

Architecture

Le projet repose sur une architecture en trois couches :

Application mobile : développée en Java avec Android SDK
Backend : API REST basée sur Spring Boot
Base de données : MySQL
Authentification : sécurisée via JSON Web Token (JWT)
Structure du projet
DeliveryApp/
├── app/           → Application Android (interface utilisateur)
├── backend/       → Documentation pour l’API
├── database/      → Script de base de données
└── README.md
Fonctionnalités principales
Contrôleur
Accès sécurisé avec gestion des rôles
Visualisation et filtrage des livraisons (date, état, client, commande)
Tableau de bord avec statistiques par livreur et par client
Système de messagerie pour communiquer avec les livreurs
Livreur
Consultation des livraisons assignées, triées par zone
Accès aux détails complets (client, adresse, paiement, articles)
Navigation via intégration avec Google Maps
Mise à jour du statut des livraisons avec remarques
Envoi de messages au contrôleur
Sécurité
Authentification basée sur JWT
Gestion des rôles (Contrôleur / Livreur)
Protection des données sensibles
Design et expérience utilisateur
Interface simple, claire et intuitive
Approche minimaliste (flat design)
Couleurs principales : jaune, blanc et gris
Respect des principes modernes d’ergonomie mobile
Objectifs du projet
Centraliser la gestion des livraisons
Améliorer la coordination entre équipes
Réduire les erreurs opérationnelles
Fournir une vision claire des performances
