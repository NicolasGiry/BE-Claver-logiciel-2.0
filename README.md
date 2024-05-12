# BE-Claver-logiciel-2.0

Ce projet est un bureau d'études réalisé avec l'aide de M. Raynal, chercheur à l'IRIT sur le domaine de l'amélioration des systèmes de saisie de texte. Le but est de réaliser un clavier logiciel à destination de personnes en situation de handicap moteur, utilisant un pointeur à la place d'un clavier physique. L'utilisation d'un système de prédiction linguistique sera utilisée pour simplifier la saisie. De plus, il est important de réduire au maximum la distance parcourue par le pointeur, pour éviter à l'utilisateur de s'épuiser.
Le projet a un but expérimental, me permettant de développer et d'imaginer un nouveau clavier qui répondra au mieux aux critères cités plus haut. J'ai réalisé un état de l'art des claviers logiciels existants, pour mieux appréhender l'élaboration de ma solution. Le clavier que je propose sera structuré selon 3 cercles concentriques. Rangeant les lettres les plus probables dans le cercle le plus au centre et les moins probables sur le cercle extérieur. À chaque touche pressée, on calculera les 3 lettres les plus probables d’être sélectionnée à la suite et on les placera dans le premier cercle. L’objectif est que la prochaine lettre choisie soit toujours le plus au centre pour que l’utilisateur bouge le moins possible son pointeur.

nouvelle version :

https://github.com/NicolasGiry/BE-Claver-logiciel-2.0/assets/114723956/fa4c3243-7ebb-4366-91f4-7b6f4c4bdcf9

https://github.com/NicolasGiry/BE-Claver-logiciel-2.0/assets/114723956/8e7cb3eb-5006-46b9-ba33-7385dbccefef

ancienne version :

https://github.com/NicolasGiry/BE-ClavierLogiciel-Prediction/assets/114723956/752615ff-5bdb-4b37-9d12-33ed5232ac14

premier prototype :
![Screenshot 2024-03-25 231542](https://github.com/NicolasGiry/BE-ClavierLogiciel-Prediction/assets/114723956/dfeabd96-a5b5-49fd-92f7-42cf5771a63c)
