# 🛍️ E-commerce Microservices - Spring Cloud

## 📋 Table des Matières

- [📖 À Propos du Projet](#-à-propos-du-projet)
- [🏗️ Architecture du Système](#️-architecture-du-système)
- [🔧 Technologies Utilisées](#-technologies-utilisées)
- [📂 Structure du Projet](#-structure-du-projet)
- [🚀 Démarrage Rapide](#-démarrage-rapide)
- [⚙️ Configuration](#️-configuration)
- [📡 API Endpoints](#-api-endpoints)
- [📚 Ressources Pédagogiques](#-ressources-pédagogiques)
- [👤 Auteur](#-auteur)

---

## 📖 À Propos du Projet

Plateforme académique de **microservices e-commerce** bâtie sur **Spring Cloud** : gestion clients, catalogue produits et facturation avec **Eureka**, **Gateway**, **Config Server** et échanges Feign.

### Fonctionnalités Principales

- 🧭 **Service Discovery** : registre Eureka (@8761)
- 🛂 **API Gateway** : routage dynamique par découverte de services (@8080)
- 📇 **Customer Service** : CRUD REST (Spring Data REST) sur les clients (@8081)
- 📦 **Inventory Service** : CRUD REST (Spring Data REST) sur les produits (@8082)
- 🧾 **Billing Service** : factures enrichies via Feign (@8083)
- ⚙️ **Config Server** : propriétés centralisées depuis `config-repo` (@9999)
- 🛠️ **Profiles & H2** : données en mémoire pour chaque service

### Objectifs Pédagogiques

Réalisé dans le cadre du cours **J2EE** pour mettre en pratique :

- ✅ Architecture microservices Spring Cloud (Config, Eureka, Gateway)
- ✅ Exposition REST avec Spring Data REST
- ✅ Clients HTTP inter-services avec OpenFeign
- ✅ Initialisation de données et projections Spring Data
- ✅ Configuration centralisée et discovery-based routing

---

## 🏗️ Architecture du Système

```text
┌───────────────────────────────────────────────────────────────────────────────────────┐
│                     ARCHITECTURE E-COMMERCE SPRING  CLOUD                             │
├───────────────────────────────────────────────────────────────────────────────────────┤
│                                                                                       │
│    ┌─────────────────┐        ┌───────────────────────────┐                           │
│    │  Front / API    │──────▶│     API Gateway (8080)    │──────────┐                 │
│    │  Client         │        │  Discovery-based routing  │          │                │
│    └─────────────────┘        └─────────────┬─────────────┘          │                │
│                                             ▼                        ▼                │
│                                   ┌─────────────────┐       ┌─────────────────┐       |
│                                   │ Customer (8081) │       │ Inventory (8082)│       |
│                                   │ Spring Data REST│       │ Spring Data REST│       |
│                                   └────────┬────────┘       └────────┬────────┘       |
│                                            │                         │                |
│                                            ▼                         ▼                |
│                                   ┌─────────────────┐       ┌──────────────────────┐  |
│                                   │  Billing (8083) │◀─────▶│  Feign Clients      │  |
│                                   │  Feign + JPA    │       │  (customers/products)|  |
│                                   └────────┬────────┘       └──────────────────────┘  |
│                                            │                                          |
│                                            ▼                                          |
│                             ┌────────────────────────┐                                │
│                             │ Eureka (8761)          │                                │
│                             │ Config Server (9999)   │                                │
│                             │ + Config Repo (file)   │                                │
│                             └────────────────────────┘                                │
│                                                                                       │
└───────────────────────────────────────────────────────────────────────────────────────┘
```

### Flux de Données

1. Les services chargent leur config depuis **Config Server** (profil `native`, dossier `config-repo`).
2. Chaque service se **registre dans Eureka** et le Gateway découvre les routes dynamiquement.
3. Le **Gateway** expose les APIs publiques et reverse-proxy vers les services métiers.
4. **Billing** appelle **Customer** et **Inventory** via Feign pour enrichir les factures avant réponse.

### Services & Ports

| Service | Rôle | Port | Notes |
| ------- | ---- | ---- | ----- |
| **gateway-service** | Gateway WebFlux + discovery locator | 8080 | CORS ouvert pour `http://localhost:4200` |
| **discovery-service** | Eureka Server | 8761 | Dashboard d’enregistrement |
| **config-service** | Spring Cloud Config (native) | 9999 | Source `config-repo/` |
| **customer-service** | Gestion clients (Spring Data REST) | 8081 | Base path `/api` |
| **inventory-service** | Gestion produits (Spring Data REST) | 8082 | Base path `/api` |
| **billing-service** | Factures enrichies via Feign | 8083 | Base path `/api` |

---

## 🔧 Technologies Utilisées

### Backend & Cloud

| Technologie | Version | Description |
| ----------- | ------- | ----------- |
| **Java** | 21 | Langage principal (LTS) |
| **Spring Boot** | 3.5.7 | Base applicative |
| **Spring Cloud** | 2025.x | Config Server, Gateway, Eureka, OpenFeign |
| **Spring Data JPA / REST** | - | Persistence + exposition auto des entités |
| **WebFlux Gateway** | - | Routage réactif côté gateway |

### Infrastructure & Data

| Technologie | Description |
| ----------- | ----------- |
| **H2** | Bases en mémoire pour chaque service |
| **Config Repo (git-like local)** | `config-repo/` (profil `native`) |
| **Eureka Discovery** | Registre des services |

### Outils

| Outil | Description |
| ----- | ----------- |
| **Maven** | Build & dépendances |
| **Lombok** | Réduction du boilerplate |
| **OpenFeign** | Clients HTTP inter-services |

---

## 📂 Structure du Projet

```text
ecom-app-backend-meryem/
│
├── billing-service/
│   ├── src/main/java/ma/hamidi/...
│   │   ├── BillingServiceApplication.java
│   │   ├── billingservice/web/BillRestController.java
│   │   └── billingservice/feign/*RestClient.java
│   └── src/main/resources/application.properties
│
├── customer-service/
│   ├── src/main/java/ma/hamidi/customerservice/CustomerServiceApplication.java
│   ├── src/main/java/ma/hamidi/customerservice/repository/CustomerRepository.java
│   └── src/main/resources/application.properties
│
├── inventory-service/
│   ├── src/main/java/ma/hamidi/inventoryservice/InventoryServiceApplication.java
│   ├── src/main/java/ma/hamidi/inventoryservice/repository/ProductRepository.java
│   └── src/main/resources/application.properties
│
├── gateway-service/
├── discovery-service/
├── config-service/
├── config-repo/
└── pom.xml
```

---

## 🚀 Démarrage Rapide

### Prérequis

| Outil | Version | Vérification |
| ----- | ------- | ------------ |
| **Java JDK** | 21+ | `java -version` |
| **Maven** | 3.9+ | `mvn -version` |
| **RAM** | 2 GB+ | Pour lancer plusieurs services |

### Étape 1 : Lancer le Config Server

```bash
mvn -pl config-service spring-boot:run
# profils natifs chargent config-repo/ automatiquement
```

### Étape 2 : Lancer Eureka (Discovery)

```bash
mvn -pl discovery-service spring-boot:run
```

### Étape 3 : Démarrer les services métiers

```bash
mvn -pl customer-service spring-boot:run
mvn -pl inventory-service spring-boot:run
mvn -pl billing-service spring-boot:run
mvn -pl gateway-service spring-boot:run
```

> Astuce : lancer Config puis Eureka avant les services métiers.

### Étape 4 : Tester

| Action | URL/Commande | Description |
| ------ | ------------ | ----------- |
| **Eureka Dashboard** | http://localhost:8761 | Voir les services enregistrés |
| **Gateway proxy** | http://localhost:8080 | Point d’entrée (routes dynamiques) |
| **Clients REST (customer)** | http://localhost:8080/CUSTOMER-SERVICE/api/customers | Liste des clients |
| **Produits REST (inventory)** | http://localhost:8080/INVENTORY-SERVICE/api/products | Liste des produits |
| **Facture enrichie** | http://localhost:8080/BILLING-SERVICE/bills/1 | Détail facture + clients/produits |

---

## ⚙️ Configuration

### Propriétés communes (config-repo/application.properties)

```properties
spring.h2.console.enabled=true
spring.data.rest.base-path=/api
spring.cloud.discovery.enabled=true
eureka.client.service-url.defaultZone=http://localhost:8761/eureka
management.endpoints.web.exposure.include=*
```

### Exemples par service

- **config-service** (`server.port=9999`, profil `native`, repo `file:../config-repo`) @d:/5IIR/J2EE Final/ecom-app-backend-meryem/config-service/src/main/resources/application.properties#1-4
- **customer-service** (`server.port=8081`, import config server, base-path `/api`) @d:/5IIR/J2EE Final/ecom-app-backend-meryem/customer-service/src/main/resources/application.properties#1-4
- **inventory-service** (`server.port=8082`, import config server, base-path `/api`) @d:/5IIR/J2EE Final/ecom-app-backend-meryem/inventory-service/src/main/resources/application.properties#1-4
- **billing-service** (`server.port=8083`, base-path `/api`, import config server) @d:/5IIR/J2EE Final/ecom-app-backend-meryem/billing-service/src/main/resources/application.properties#1-4
- **gateway-service** : CORS autorise `http://localhost:4200` @d:/5IIR/J2EE Final/ecom-app-backend-meryem/gateway-service/src/main/resources/application.yml#1-15

---

## 📡 API Endpoints

### Gateway (reverse-proxy)

- Les routes sont résolues dynamiquement via Eureka. Exemple : `/CUSTOMER-SERVICE/**` → customer-service.

### Customer Service (Spring Data REST)

| Méthode | Endpoint | Description |
| ------- | -------- | ----------- |
| `GET` | `/api/customers` | Liste paginée des clients |
| `GET` | `/api/customers/{id}` | Détail d’un client |

### Inventory Service (Spring Data REST)

| Méthode | Endpoint | Description |
| ------- | -------- | ----------- |
| `GET` | `/api/products` | Liste paginée des produits |
| `GET` | `/api/products/{id}` | Détail d’un produit |

### Billing Service

| Méthode | Endpoint | Description |
| ------- | -------- | ----------- |
| `GET` | `/bills/{id}` | Retourne la facture et enrichit client + produits via Feign @d:/5IIR/J2EE Final/ecom-app-backend-meryem/billing-service/src/main/java/ma/hamidi/billingservice/web/BillRestController.java#30-37 |

### Exemples d’Utilisation (via Gateway)

```bash
curl "http://localhost:8080/CUSTOMER-SERVICE/api/customers"
curl "http://localhost:8080/INVENTORY-SERVICE/api/products"
curl "http://localhost:8080/BILLING-SERVICE/bills/1"
```

### Format des Données (exemples)

- **Customer** : `{ "id": 1, "name": "Mohamed", "email": "..." }`
- **Product** : `{ "id": "<uuid>", "name": "Computer", "price": 3200, "quantity": 12 }`
- **Bill** : inclut `customerId`, `productItems[]` avec `productId`, `quantity`, `unitPrice`, puis est enrichi côté service.

---

## 🔍 Détails Techniques

### Focus techniques

- **Gateway** : découverte dynamique des routes via Eureka (WebFlux).
- **Billing** : Feign pour récupérer clients/produits avant de retourner la facture enrichie.
- **Seeds** : données de démo injectées au démarrage dans chaque service via H2.

---

## 📚 Ressources Pédagogiques

| Ressource | Lien |
| --------- | ---- |
| Spring Cloud Config | https://spring.io/projects/spring-cloud-config |
| Spring Cloud Gateway | https://spring.io/projects/spring-cloud-gateway |
| Spring Cloud Netflix Eureka | https://spring.io/projects/spring-cloud-netflix |
| Spring Data REST | https://spring.io/projects/spring-data-rest |
| OpenFeign | https://spring.io/projects/spring-cloud-openfeign |

### Concepts Clés

| Concept | Description |
| ------- | ----------- |
| **Service Discovery** | Registre Eureka pour localiser les instances |
| **Config Centralisée** | Config Server (profil `native`) sur repo local |
| **Reverse Proxy** | Gateway WebFlux avec discovery locator |
| **Feign Clients** | Abstraction HTTP déclarative entre services |
| **H2 In-Memory** | Bases volatiles pour dev/test |

---

## 👤 Auteur

<div align="center">

**Meryem HAMIDI**  
Étudiante en 5ème année Ingénierie Informatique et Réseaux (5IIR)  
École Marocaine des Sciences de l'Ingénieur (EMSI)

[![GitHub](https://img.shields.io/badge/GitHub-MeryemHamidi-181717?style=for-the-badge&logo=github)](https://github.com/MeryemHamidi)

</div>
