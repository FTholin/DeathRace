# Document d'architecture logicielle

Ce document fournit une **vue d'ensemble** de l'architecture de notre application,
en utilisant un certain nombres de vues architecturales différentes nous pouvons 
ainsi décrire avec une plus grande précision  ses différents aspects.
Celui-ci est également destiné à capturer et à transmettre les **décisions architecturales** 
importantes qui ont été prises par l'équipe durant la **phase de conception**.

## Résumé de l'architecture
Actuellement, notre application est composée de 4 packages:
- model
- view
- network
- controller

Ce découpage va nous permettre une grande tolérance au changement car il permet à chaque composant d'être indépendant du 
fonctionnement des autres.  
Ce qui veut dire que le model possède des données relative à des objets sans avoir de *vue* à proprement parler.  
Et la vue connais un modèle qui possède éventuellement des *getters* et des *setters* mais les données sont traitée de la même manière qu'importe les changements opéré sur le modèle.

### Model
![model-diagram](Ressources/Diagrammes/Class_diagram/modelDiagram.png)  

Pour le modèle nous nous sommes mis d'accord sur cette structure.  
Une **Race** est une *Map* et une liste de *Runner* (ce qui est contrôllable par un utilisateur donc).  
Une **Map** est une liste d'*AbstractPhysicBody* (tout objet physique qui possède une *hitbox*) et est munie d'une *taille*.  
Un **Runner** héritera de *AbstractAccelerableBody* et de *AbstractMobileBody* car nous nous sommes axé sur la réutilisabilité de notre code. Donc si a l'avenir nous souhaitons 
avoir une étoile par exemple, qui bouge mais qui n'est pas contrôlée par un joueur ou une IA, nous pourrons l'inclure dans *AbstractMobileBody*.  
Nous aurons également une **Enum** qui nous permettra de lister les différents type d'objets qui seront affiché sur une *Map*. Pour faciliter l'affichage.  

### View
![view-diagram](Ressources/Diagrammes/Class_diagram/viewDiagram.png)  

La vue se construira sur la base d'un affichage d'*AbstractBodyView*.  
Au fur et à mesure qu'on ajoutera des objets physiques dans le modèle on ajoutera des *Factory* de ces derniers dans la vue.  
Toute la vue est donc ré-utilisable/modulable selon les besoins d'affichages.

### Network
![network-diagram](Ressources/Diagrammes/Class_diagram/network.png)

Le package network permet de fournir des outils pour la communication en réseau basée sur la méthode de fonctionnement "client / serveur".  

Le protocole utilisé est **TCP** et ces classes permettent l'envoie et la réception d'objets.  

La classe **AbstractServer** fournit la base d'un serveur.
Lorsque celle-ci est instanciée, elle crée un socket serveur sur le port choisi. 
Il démarre un nouveau thread qui s'occupe d'*écouter* les nouvelles connexions (car la méthode pour accepter une nouvelle connexion est bloquante) et maintient une liste de connexions actives.  

La classe **ConnectionListener** permet d'*écouter* les connexions entrantes d'un serveur et de les notifier au serveur.
D'où l'agrégation avec la classe AbstractServer, un serveur peut fonctionner sans thread qui *écoute* les nouvelles connexions.
Par exemple s'il ne souhaite plus accepter de nouvelles connexions lorsque le nombre de joueurs maximum est atteint.  

Chaque connexion avec le serveur crée un objet socket, qui permet de récupérer les flux d'entrées et de sorties.
Puisqu'on souhaite communiquer des objets, il faut encapsuler ces flux dans d'autres flux gérant des objets.
Une classe **Connection** permet pour cela de stocker ces flux et d'envoyer un objet à cette instance de *Connexion*.
De plus, on peut vouloir écouter les messages provenant de cette connexion.
Or la encore, la méthode pour lire un objet d'un flux est bloquante, on a donc besoin d'un nouveau thread.  

La classe *MessageListener* permet de gérer ça. D'où l'agrégation de *Connection* à **MessageListener**.
Une fois un objet reçu, il faut le transmettre à la classe qui en a besoin. D'où l'utilité de l'interface **IMessageHandler**.  

Cette interface (composée de deux méthodes) permet de gérer les nouveaux messages, ainsi que de traiter les cas d'erreurs.  

On implémente donc cette interface à *AbstractServer* pour qu'elle puisse être notifiée si une erreur survient avec une connexion ou si un objet est reçu.  

La classe **AbstractClient** permet grâce à une méthode *connect* de se connecter à un serveur et de gérer cette connexion.
On ré-utilise donc la classe Connection ainsi que la classe MessageListener. 
Et par conséquent, la classe implémente l'interface *IMessageHandler* pour traiter les messages ou erreurs.  

### Controller

![controller-diagram](Ressources/Diagrammes/Class_diagram/Controller_Diagram.png)  

Pour ce qui est du **Controller**, nous nous basons sur l'idée d'une classe *Controller* qui est une *Race* et un *ControllerClient*.  
Dans le cas d'une partie a plusieurs sur différentes machines. Ce sera le rôle du *ControllerServer* avec sa méthode **update** de mettre à jours les modèles de toutes les machines (c'est un peu l'idée de la structure "*maître-esclave*").  
Les contrôleurs distants s'occuperons de notifier le contrôleur principal des changements qu'ils opèrent et également de recevoir les notifications de la machine principale.  

## Autres diagrammes UML

### Diagrammes de comportement
#### Diagramme de cas d'utilisation
![use-case-diagram](Ressources/Diagrammes/Use_case/Cas_d_utilisation_DeathRace.png)
#### Diagramme d'activité
![activity-diagram](Ressources/Diagrammes/ActivityDiagram/Diagramme_activite.png)

### Diagrammes dynamiques
#### Diagrammes de séquence
##### 1. ajouter une IA
![sequence-diagram-add-ai](Ressources/Diagrammes/Sequence_diagram/Ajouter_une_IA.png)
##### 2. créer partie multijoueur
![sequence-diagram-multiplayer-game-creation](Ressources/Diagrammes/Sequence_diagram/Créer_une_partie_mulltijoueur.png)
##### 3. expulser joueur
![sequence-diagram-evict-player](Ressources/Diagrammes/Sequence_diagram/Expulser_Joueur.png)
##### 4. gérer IA
![sequence-diagram-manage-ai](Ressources/Diagrammes/Sequence_diagram/Gérer_les_IA.png)
##### 5. préciser nombre joueurs
![sequence-diagram-set-player-number](Ressources/Diagrammes/Sequence_diagram/Préciser_le_nombre_de_joueurs.png)
##### 6. rejoindre partie multijoueur
![sequence-diagram-join-multiplayer-game](Ressources/Diagrammes/Sequence_diagram/Rejoindre_une_partie_multijoueur.png)
##### 7. renseigner pseudonyme
![sequence-diagram-pseudo-inform](Ressources/Diagrammes/Sequence_diagram/Renseigner_un_pseudonyme.png)
##### 8. saisir addresse & mot de passe
![sequence-diagram-set-addresse-and-password](Ressources/Diagrammes/Sequence_diagram/Saisir_l_adresse_et_le_mot_de_passe_.png)
##### 9. supprimer IA
![sequence-diagram-remove-ai](Ressources/Diagrammes/Sequence_diagram/Supprimer_une_IA.png)
##### 10. choisir circuit
![sequence-diagram-choose-circuit](Ressources/Diagrammes/Sequence_diagram/choisir_un_circuit.png)
##### 11. choisir mot de passe
![sequence-diagram-choose-password](Ressources/Diagrammes/Sequence_diagram/choisir_un_mot_de_passe_.png)
##### 12. choisir personnage
![sequence-diagram-choose-perso](Ressources/Diagrammes/Sequence_diagram/choisir_un_personnage.png)
##### 13. Lancer partie solo
![sequence-diagram-solo-game-start](Ressources/Diagrammes/Sequence_diagram/lancer_une_partie_solo.png)

## Mémos techniques
### Gestion des différentes interfaces

Nous sommes partis sur une base de différents menus inspiré de notre expérience dans les jeux vidéos.  
Nous noterons qu'il s'agit là d'une ébauche. Nous apporterons probablement des modifications durant la phase de développement.  

#### Le menu principal
![menuprincipale](Ressources/InterfacesGraphiques/Main%20Menu.png)

Le menu pricipal est la première interface que rencontre l'utilisateur.  
Elle permet de :
* Créer une nouvelle partie solo en cliquant sur le bouton "Nouvelle partie". 
* Créer ou rejoindre une partie en multijoueurs en cliquant sur le bouton "Multijoueur".
* Changer son peudonyme et mettre ses statistiques de jeu à zéro en cliquant sur le boutton "Options"
* Quitter le jeu en cliquant sur "Quitter".

#### Nouvelle partie solo 

![newGame](Ressources/InterfacesGraphiques/New%20Solo%20Game.png)

L'interface "Nouvelle partie solo" permet de créer une nouvelle partie solo avec zéro, une ou plusieurs IA.
* La case Pseudonyme affiche le pseudonyme du joueur ainsi que le pseudonyme des IA précédés par un numéro unique.
* La case circuit permet au joueur de sélectionner le circuit de la partie.
* La case personnage permet de choisir son propre personnge, il est également possible de choisir celui des IA.
* le boutton "+" permet d'ajouter des IA.
* le boutton "Retour" permet de revenir vers le menu principal.
* enfin, *lancer la partie* pour démarrer la course.

#### Héberger une partie multijoueurs

![host](Ressources/InterfacesGraphiques/Multijoueur%20host.png)

L'interface "Nouvelle partie multijoueurs" permet d'héberger une partie multijoueurs au sein d'un réseau local. 
* Le champ "Ip adresse" permet d'afficher l'adresse IP (permettant aux autres joureurs de se connecter)
* le champ pour le port utilisé pour cette session de jeu.
* un champ pour indiquer le nombre de joueurs de la partie.
* un champ mot de passe qui permet de sécuriser la partie.
* le bouton "Créer" permet créer une partie.
* le champ "pseudonyme" affiche les pseudonymes des joueurs.
* le champ "circuit" permet de choisir une map, **l'hébergeur est le seul à pouvoir choisir la map**.
* le champ "personnage" permet de choisir un personnage.
* le bouton "+" permet d'ajouter une IA, ainsi que le personnage de l'IA.
* un champ "prêt" qui permet d'indiquer qu'on est prêt à lancer la partie, ce champ est activé automatiquement pour les IA.
* un bouton "lancer" qui permet de lancer la partie si tout les joueurs sont prêts.
* le bouton "Retour" permet de retourner vers le menu principal.

### Rejoindre une partie multijoueurs

![join](Ressources/InterfacesGraphiques/Multijoueur%20join.png)

L'interface "Nouvelle partie multijoueurs" permet de rejoindre une partie multijoueurs au sein d'un réseau local.
* un champ "adresse ip" pour indiquer l'adresse IP du serveur.
* un champ port pour indiquer le port du serveur.
* un champ "mot de passe" pour indiquer le mot de passe du serveur.
* un bouton "Rejoindre" pour rejoindre un serveur. 
* un champ "pseudonyme" qui affiche les pseudonymes des joueurs dans le salon.
* un champ "circuit" qui affiche le circuit de la partie.
* un champ "personnage" qui permet au joueur de choisir son personnage.
* un champ "prêt" qui permet au joueur d'indiquer qu'il est prêt à jouer.
* un bouton "Quitter le salon" il permet au joueur de quitter le salon actuel.
* un bouton "Retour" il permet au joueur de revenir vers le menu principal.

#### Options

![options](Ressources/InterfacesGraphiques/Options.png)

cette interface permet au jouer de : 
* changer son pseudonyme.
* visionner ses statistiques de jeu.
* mettre à zéro ses statistiques.
* retourner vers le menu principal.