# gameOfLife
Projet de Systèmes Distribués

## Préparation du lancement
Se placer dans le dossier `/out/production/jeuDeLaVie` pour exécuter les commandes ci-dessous :

## Lancement du serveur

1) Lancer le rmiregistry
```
rmiregistry
``` 

2) Lancer le rmid : 
```
rmid -port 50000 -J-Djava.security.policy=../../../gameOfLife.policy
```
