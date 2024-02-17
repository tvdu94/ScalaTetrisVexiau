Projet de Scala - Thomas VEXIAU

Jeu réalisé avec Indigo : Tetris

Comment lancer le jeu ?
en terminal, placez vous au niveau de ./ScalaTetrisVexiau
tapez la commande suivante : "sbt runGame"
Cette commande permet de compiler puis lancer le jeu


Comment jouer?
une fois le jeu lancé, des pieces vont tomber au fur et à mesure
votre objectif sera de faire des lignes completes, avant qu'aucune piece ne puisse etre posée
chaque ligne terminée vous rapportera des points
Vous pouvez déplacer les pieces vers la gauche ou vers la droite (en utilisant les fleches du clavier)
en appuyant sur la touche "bas", la piece descendra plus vite
La musique de fond est la musique de Tetris. Pour le bien de votre cerveau, je conseille fortement de couper si vous souhaitez jouer longtemps :)

Compréhension du code :
La plupart des méthodes ont une petite description qui explique son role dans le projet



Quelles fonctionnalités j'aurais aimé implémenter?
Je n'ai pas pu implémenter la rotation des pieces, qui est un des mechnismes de base du tetris
    Cette absence de fonctionnalité est lié à une perte de temps sur la compréhension de Indigo
    En effet, trouver des 'TO DO' dans la doc officielle, et avoir très peu d'exemples simples de code, cela a rendu le travail assez difficile
    Pour palier au maximum ce problème, j'ai rejoins le Discord de Indigo (qui contient quelques bouts de code)
J'aurais également souhaité afficher la prévisualisation de la piece suivante
Une autre fonctionnalité aurait été la sauvegarde du score, avec un classement des meilleurs joueurs



