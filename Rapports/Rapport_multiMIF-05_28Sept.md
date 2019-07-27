# Rapport  du 28/09/2018
## Résumé

Discussions sur la transmission des données par le réseau.  
  
Chaque joueur utlisera le modèle MVC pour le réseau.
A voir :
- Soit les commandes des clients sont envoyés au serveur qui l'applique à son modèle et envoi de manière régulière l'état du jeu (Bien penser aux accès concurrents).
- Soit les clients appliquent les commandes sur leur modèle et le serveur récupère à intervalles réguliers les modèles de tous les clients et les combines puis le renvoie aux autres.

Commantaire Antoine : 
La méthode un à l'avantage d'être simple à mettre en oeuvre et appliquer au modèle MVC. Tout passe par le contrôleur, comme si un joueur avait appuyé localement sur la touche de son personnage.
La méthode deux sera plus dépendante du modèle, il faudra faire des algorithmes pour combiner les vues (j'ai du mal à voir comment combiner les vues).

Discussions et ébauche de l'architecture de la partie modèle.

Choix de la réprésentation du jeu, gérer dans le modèle comme si l'on voyait le jeu du haut en 2D. Mais rendu en 2.5D du côté de la vue.  

Mise en forme du rapport à rendre et complétion.

Recherche sur les diagrammes demandés pour le prochain rapport à rendre.



## Objectifs
Finalisation de la configuration de nos espaces de travail (à finir aujourd'hui).  
Base de code du dépôt git finalisé.


## To do List
- [ ] Mise en place de SonarQube

## Présents/Absents
### Présents

Toute l'équipe

### Absents

néant


| **Date** |  **Personne** |         **Action**        |
|:--------:|:-------------:|:-------------------------:|
| 28/09/18 |  A. Oleksiak  | Première version complète |
| 01/09/18 | N. Condomitti |  Relecture et validation  |
