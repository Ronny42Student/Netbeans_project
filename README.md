# Intranet 42 — Documentation Base de Données Oracle

## Sommaire
1. Prérequis
2. Connexion au conteneur Docker
3. Script de création complet (schéma + données de test)
4. Script des fonctionnalités additionnelles (deadlines, retries, pénalités, évaluations)
5. Corrections appliquées en cours de route
6. Requêtes de vérification utiles
7. Gestion des comptes admin

---

## 1. Prérequis

- Docker avec un conteneur nommé `oracle-db` exécutant Oracle AI Database 26ai Free
- Utilisateur `system`, mot de passe défini au lancement du conteneur (`SecretPassword123` dans cet environnement de développement — à changer en production, voir section 5)
- Pluggable database `FREEPDB1`

---

## 2. Connexion au conteneur

```bash
docker exec -it oracle-db sqlplus system/SecretPassword123@FREEPDB1
```

Pour exécuter un script depuis l'extérieur du conteneur :

```bash
docker cp ./script.sql oracle-db:/tmp/script.sql
docker exec -it oracle-db sqlplus system/SecretPassword123@FREEPDB1 @/tmp/script.sql
```

---

## 3. Script de création complet (schéma initial)

Ce script recrée toutes les tables depuis zéro. **Il supprime les tables existantes** — à n'exécuter que sur une base vide ou pour repartir de zéro.

```sql
-- ============================================
-- SUPPRESSION DES TABLES EXISTANTES (CASCADE)
-- ============================================
DROP TABLE user_logtime CASCADE CONSTRAINTS;
DROP TABLE evaluations CASCADE CONSTRAINTS;
DROP TABLE user_progress CASCADE CONSTRAINTS;
DROP TABLE event_registrations CASCADE CONSTRAINTS;
DROP TABLE projects CASCADE CONSTRAINTS;
DROP TABLE events CASCADE CONSTRAINTS;
DROP TABLE users CASCADE CONSTRAINTS;

-- ============================================
-- TABLE UTILISATEURS
-- ============================================
CREATE TABLE users (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username VARCHAR2(50) UNIQUE NOT NULL,
    email VARCHAR2(100) UNIQUE NOT NULL,
    password VARCHAR2(255) NOT NULL,
    current_level NUMBER DEFAULT 0,
    campus_location VARCHAR2(50),
    role VARCHAR2(20) DEFAULT 'USER',
    eta_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    avatar_path VARCHAR2(255),
    active NUMBER(1) DEFAULT 1
);

-- ============================================
-- TABLE PROJETS
-- ============================================
CREATE TABLE projects (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR2(100) NOT NULL,
    slug VARCHAR2(50) NOT NULL,
    milestone_order NUMBER DEFAULT 0,
    deadline_days NUMBER DEFAULT 15,
    max_retries NUMBER DEFAULT 3,
    description VARCHAR2(500),
    min_grade NUMBER(3,0) DEFAULT 60,
    active NUMBER(1) DEFAULT 1
);

-- ============================================
-- TABLE PROGRESSION (inscriptions aux projets)
-- ============================================
CREATE TABLE user_progress (
    user_id NUMBER REFERENCES users(id),
    project_id NUMBER REFERENCES projects(id),
    status VARCHAR2(20) DEFAULT 'IN_PROGRESS',
    grade NUMBER(3,0),
    enrolled_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deadline TIMESTAMP,
    attempt_number NUMBER DEFAULT 1,
    penalty_percent NUMBER(5,2) DEFAULT 0,
    failed_at TIMESTAMP,
    PRIMARY KEY (user_id, project_id)
);
-- Valeurs possibles pour `status` : IN_PROGRESS, COMPLETED, FAILED, RETRY_PENDING

-- ============================================
-- TABLE ÉVÉNEMENTS
-- ============================================
CREATE TABLE events (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title VARCHAR2(100) NOT NULL,
    event_type VARCHAR2(30),
    event_date TIMESTAMP NOT NULL,
    location VARCHAR2(100)
);

-- ============================================
-- TABLE INSCRIPTIONS AUX ÉVÉNEMENTS
-- ============================================
CREATE TABLE event_registrations (
    user_id NUMBER REFERENCES users(id),
    event_id NUMBER REFERENCES events(id),
    registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, event_id)
);

-- ============================================
-- TABLE LOGTIME (heures loguées)
-- ============================================
CREATE TABLE user_logtime (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id NUMBER REFERENCES users(id),
    log_date DATE NOT NULL,
    hours NUMBER(4,2) NOT NULL,
    description VARCHAR2(255)
);

-- ============================================
-- TABLE ÉVALUATIONS
-- ============================================
CREATE TABLE evaluations (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    project_id NUMBER REFERENCES projects(id),
    user_id NUMBER REFERENCES users(id),
    evaluator_id NUMBER REFERENCES users(id),
    status VARCHAR2(20) DEFAULT 'PENDING',
    scheduled_date TIMESTAMP,
    grade NUMBER(3,0),
    comments CLOB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    final_grade NUMBER(3,0),
    passed NUMBER(1)
);
-- NOTE : la colonne s'appelle `comments` (au pluriel), PAS `comment`,
-- car `comment` est un mot réservé Oracle (utilisé par COMMENT ON TABLE/COLUMN).

-- ============================================
-- DONNÉES DE TEST
-- ============================================
INSERT INTO projects (name, slug, milestone_order) VALUES ('Libft', 'libft', 1);
INSERT INTO projects (name, slug, milestone_order) VALUES ('Get Next Line', 'get_next_line', 2);
INSERT INTO projects (name, slug, milestone_order) VALUES ('Born2beroot', 'born2beroot', 3);
INSERT INTO projects (name, slug, milestone_order) VALUES ('Minitalk', 'minitalk', 4);
INSERT INTO projects (name, slug, milestone_order) VALUES ('Push_swap', 'push_swap', 5);

INSERT INTO events (title, event_type, event_date, location)
VALUES ('Onboarding AXIAN', 'Onboarding', TIMESTAMP '2026-07-22 09:00:00', 'Salle 1');
INSERT INTO events (title, event_type, event_date, location)
VALUES ('Session C', 'Cours', TIMESTAMP '2026-07-22 14:00:00', 'Salle 2');
INSERT INTO events (title, event_type, event_date, location)
VALUES ('Évaluation Piscine', 'Évaluation', TIMESTAMP '2026-08-06 10:00:00', 'Salle 3');

COMMIT;
```

---

## 4. Fonctionnalités additionnelles

Si tu es parti d'un schéma déjà créé sans les colonnes ci-dessus (deadlines, retries, pénalités, évaluations enrichies), voici les `ALTER TABLE` correspondants. **Ne pas exécuter si tu viens d'utiliser le script complet de la section 3** — ces colonnes y sont déjà incluses.

```sql
-- Extension de la table projects
ALTER TABLE projects ADD deadline_days NUMBER DEFAULT 15;
ALTER TABLE projects ADD max_retries NUMBER DEFAULT 3;
ALTER TABLE projects ADD description VARCHAR2(500);
ALTER TABLE projects ADD min_grade NUMBER(3,0) DEFAULT 60;
ALTER TABLE projects ADD active NUMBER(1) DEFAULT 1;

-- Extension de la table user_progress
ALTER TABLE user_progress ADD attempt_number NUMBER DEFAULT 1;
ALTER TABLE user_progress ADD penalty_percent NUMBER(5,2) DEFAULT 0;
ALTER TABLE user_progress ADD failed_at TIMESTAMP;

-- Extension de la table evaluations
ALTER TABLE evaluations ADD final_grade NUMBER(3,0);
ALTER TABLE evaluations ADD passed NUMBER(1);

-- Extension de la table users
ALTER TABLE users ADD avatar_path VARCHAR2(255);
ALTER TABLE users ADD active NUMBER(1) DEFAULT 1;

-- Table de liaison événements
CREATE TABLE event_registrations (
    user_id NUMBER REFERENCES users(id),
    event_id NUMBER REFERENCES events(id),
    registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, event_id)
);

COMMIT;
```

---

## 5. Corrections appliquées en cours de route

### 5.1 — Cascade sur `event_registrations`

Sans `ON DELETE CASCADE`, supprimer un utilisateur ou un événement échoue si des inscriptions existent. Trouve d'abord le nom réel de la contrainte :

```sql
SELECT constraint_name FROM user_constraints WHERE table_name = 'EVENT_REGISTRATIONS' AND constraint_type = 'R';
```

Puis recrée-la avec cascade (remplace `fk_evtreg_user` par le nom réel si besoin) :

```sql
ALTER TABLE event_registrations DROP CONSTRAINT <nom_trouve_user>;
ALTER TABLE event_registrations DROP CONSTRAINT <nom_trouve_event>;
ALTER TABLE event_registrations ADD CONSTRAINT fk_evtreg_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;
ALTER TABLE event_registrations ADD CONSTRAINT fk_evtreg_event FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE;
```

### 5.2 — Vérifier les colonnes réelles d'une table

Utile après plusieurs `ALTER TABLE` pour confirmer l'état exact avant de coder le DAO Java correspondant :

```sql
SELECT column_name, data_type FROM user_tab_columns WHERE table_name = 'EVALUATIONS' ORDER BY column_id;
```

Remplace `'EVALUATIONS'` par le nom de la table voulue (toujours en majuscules).

---

## 6. Requêtes de vérification utiles

```sql
-- Lister toutes les tables du schéma
SELECT table_name FROM user_tables ORDER BY table_name;

-- Compter les lignes par table
SELECT 'users' AS table_name, COUNT(*) AS total FROM users
UNION ALL SELECT 'projects', COUNT(*) FROM projects
UNION ALL SELECT 'user_progress', COUNT(*) FROM user_progress
UNION ALL SELECT 'events', COUNT(*) FROM events
UNION ALL SELECT 'event_registrations', COUNT(*) FROM event_registrations
UNION ALL SELECT 'user_logtime', COUNT(*) FROM user_logtime
UNION ALL SELECT 'evaluations', COUNT(*) FROM evaluations;

-- Voir les contraintes d'une table
SELECT constraint_name, constraint_type FROM user_constraints WHERE table_name = 'USER_PROGRESS';
```

---

## 7. Gestion des comptes admin

Il n'y a pas de compte admin créé automatiquement. Pour en créer un :

1. Inscris-toi normalement via `/register` dans l'application.
2. Passe le rôle en base :

```sql
UPDATE users SET role = 'ADMIN' WHERE username = 'ton_nom_utilisateur';
COMMIT;
```

3. Reconnecte-toi — le lien "Admin" apparaît dans la navbar.

Une fois un premier admin créé, les rôles des autres utilisateurs se gèrent depuis l'onglet **Utilisateurs** du panneau `/admin`.

---

## Notes de maintenance

- Le mot de passe Oracle (`SecretPassword123`) est en clair dans `DBConnection.java` par défaut à des fins de développement. En production, utilise une variable d'environnement `DB_PASSWORD` (voir la version sécurisée de `DBConnection.java` dans le code source).
- `comment` est un mot réservé Oracle — la colonne s'appelle `comments` partout dans ce projet pour l'éviter.
- Les suppressions d'utilisateurs et de projets sont des **suppressions douces** (`active = 0`) plutôt que des `DELETE`, pour préserver l'historique et éviter les violations de contraintes FK.
