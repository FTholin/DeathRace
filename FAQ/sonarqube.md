# Utilisation de sonarqube

## Accès au serveur web

### Depuis l'extérieur

Pour accéder au serveur web depuis un réseau extérieur à la fac
Le port 8000 peut être remplacé par le port de votre choix.  
L'id correspond à votre numéro étudiant (pXXXXXX).  

```ssh -L 8000:192.168.74.196:8080 id@linuxetu.univ-lyon1.fr```

Vous devriez maintenant être connecté sur votre compte étudiant sur le réseau de la fac.  
Laissé le terminal ouvert temps que vous souhaitez accéder à sonarqube.  

Vous pouvez maintenant accéder au serveur web, il suffit d'accéder depuis votre navigateur à localhost:8000.

Poursuivre avec la partie **Login**.  

### Depuis le réseau de la fac

Il vous suffit d'accéder depuis votre navigateur à 192.168.74.196:8080. 

## Login

Pour vous connecter :
- votre numéro étudiant (avec un p)
- le mot de passe par défaut est 123456789.


## Utilisation

Une analyse par sonarqube est effectuée automatiquement à chaque push sur gitlab si les tests n'échouent pas !

### Analyse manuelle

Il faut générer un token dans votre compte.

Pour faire analyser le projet par sonarqube, avec tokenGenere le token que vous avez généré :

Si vous êtes connecté depuis l'exétieur :  
```mvn sonar:sonar -Dsonar.host.url=http://localhost:8000 -Dsonar.login=tokenGenere```

Si vous êtes sur le réseau de la fac :  
```mvn sonar:sonar -Dsonar.host.url=http://192.168.74.196:8080 -Dsonar.login=tokenGenere```

Un lien apparait à la fin pour accéder directement à la page de résultat.


## Problèmes

Connecté vous à la VM (depuis le réseau de la fac).  
Le fichier cle_prive contient la clé fournie sur TOMUSS.  

```ssh -i cle_prive ubuntu@192.168.74.196```

Si il y a un problème avec sonarqube, vérifier que sonarqube est up : 
```bash /opt/sonarqube/bin/linux-x86-64/sonar.sh status```

Si sonarqube n'est pas démarré, alors :
```bash /opt/sonarqube/bin/linux-x86-64/sonar.sh start```
