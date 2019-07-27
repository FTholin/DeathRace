Le package network permet de fournir des outils pour la communication en réseau de manière client / serveur.  

Le protocole utilisé est TCP et ces classes permettent l'envoie et la réception d'objets.  

La classe **AbstractServer** fournit la base d'un serveur.  
Lorsqu'instancié, elle crée un socket serveur sur le port choisi. 
Elle démarre un nouveau thread qui s'occupe d'écouter les nouvelles connexions (car la méthode pour accepter une nouvelle connexion est bloquante) et maintiens une liste des connexions activent.  

La classe **ConnectionListener** permet d'écouter les connexions entrantes d'un serveur et de la notifier au serveur.  
D'où l'agrégation avec la classe **AbstractServer**, un serveur peut fonctionner sans thread qui écoute les nouvelles connexions.  
Par exemple s'il ne souhaite plus accepté de nouvelles connexions lorsque le nombre de joueur maximum est atteint.  

Chaque connexion avec le serveur crée un objet socket, qui permet de récupérer les flux d'entrées et de sorties.  
Puisque l'on souhaite communiquer des objets, il faut encapsuler ses flux dans des flux gérant les objets.  
Une classe **Connection** permet donc de stocker ses flux et d'envoyer un objet à cette connexion.  
De plus, on peut vouloir écouter les messages provenant de cette connexion.  
Or la encore, la méthode pour lire un objet d'un flux est bloquante, on a donc besoin d'un nouveau thread.  

La classe **MessageListener** permet de gérer ça. D'où l'agrégation de **Connection** à **MessageListener**.  
Une fois un objet reçu, il faut le transmettre à la classe qui le souhaite. D'où l'utilité de l'interface **IMessageHandler**.  

Cette interface composé de deux méthodes, permet de gérer un nouveau message, ainsi que de traiter les cas d'erreurs.  

On implémente donc cette interface à **AbstractServer** pour qu'il puisse être notifié si une erreur sourvient avec une connexion ou un objet est reçu.  

La classe **AbstractClient** permet grâce à une méthode connect de se connecter à un serveur et de gérer cette connexion.  
On réutilise donc la classe **Connection** ainsi que la classe **MessageListener**. 
Et par conséquent, la classe implémente l'interface **IMessageHandler** pour traiter les messages ou erreurs.  