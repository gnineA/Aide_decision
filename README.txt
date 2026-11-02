 Rendu 1 : TP "Représentation de variables et de contraintes" et TP "Planification"
1. MEMBRES DU GROUPE 2A

Numéro de groupe CC : 21

- NOM : KOTIN  Prénom : OLAMIDE NAOMIE  N° étudiant : 22513540
- NOM : OUATTARA  Prénom : GNINE ASMAOU  N° étudiant : 22513173 




2. CONTENU DE L'ARCHIVE
-----------------------
Sources Java uniquement, organisées en répertoires correspondant aux packages.


modelling/  (TP "Représentation de variables et de contraintes")
  - Variable.java              variable (nom + domaine) ; equals/hashCode sur le nom
  - BooleanVariable.java       variable de domaine {true, false}
  - Constraint.java            interface : getScope(), isSatisfiedBy(Map)
  - DifferenceConstraint.java  contrainte v1 != v2
  - Implication.java           contrainte (v1 in S1) -> (v2 in S2)
  - UnaryConstraint.java       contrainte v in S
  - Demo.java                  classe exécutable 

planning/  (TP "Planification")
  - Action.java                interface : isApplicable, successor, getCost
  - BasicAction.java           action basique (précondition, effet, coût)
  - Goal.java                  interface : isSatisfiedBy
  - BasicGoal.java             but basique (instanciation partielle)
  - Planner.java               interface : plan, getInitialState, getActions, getGoal
  - DFSPlanner.java            recherche en profondeur (récursive)
  - BFSPlanner.java            recherche en largeur (plan le plus court)
  - DijkstraPlanner.java       plan de coût minimal
  - Heuristic.java             interface : float estimate(Map)
  - AStarPlanner.java          A* (plan de coût minimal si heuristique admissible)
  - Experiences.java           classe exécutable (exercice 12)

Le package planning dépend de modelling.Variable.

3. COMPILATION ET EXÉCUTION
---------------------------
Depuis la racine de l'archive,

-cd src 

java -cp .:../build/:../lib/tests.jar modelling.Demo
java -cp .:../build/:../lib/tests.jar planning.Experiences

 

Classes exécutables :
  - modelling.Demo        : instancie variables, contraintes et instanciations, et affiche
                            si chaque instanciation satisfait chaque contrainte (cas vrais
                            et faux pour chaque type de contrainte, plus un cas d'exception).
  - planning.Experiences  : compare le nombre de noeuds explorés par les planificateurs.


4. CHOIX D'IMPLÉMENTATION
-------------------------
- Un état est une Map<Variable, Object> ; successor() crée toujours une nouvelle instance.
- Contraintes : IllegalArgumentException si une variable du scope n'est pas affectée.
- DFSPlanner : méthode récursive privée search(), ensemble des états visités.
- BFSPlanner : file FIFO, dictionnaires parent / action associée, méthode buildPlan()
  appelée en fin de recherche.
- DijkstraPlanner et AStarPlanner : file de priorité avec nœuds périmés ignorés (pas de
  modification d'éléments dans la file) ; le but est testé à la sortie de la file, ce qui
  garantit l'optimalité. A* avec h = 0 se comporte comme Dijkstra.
- Planificateurs : plan() retourne null s'il n'y a pas de solution, et une liste vide si
  l'état initial satisfait déjà le but.


5. SONDES (exercice 11)
-----------------------
Chaque planificateur (DFS, BFS, Dijkstra, A*) possède :
  - activateNodeCount(boolean activate) : active/désactive le comptage (désactivé par défaut) ;
  - getNodeCount() : nombre de noeuds explorés lors du dernier appel à plan().

Un noeud est compté lorsqu'il est retiré de la file (ou visité, pour le DFS) ; les entrées
périmées des files de priorité ne sont pas comptées.


6. EXPÉRIENCES (exercice 12)
----------------------------
Problème : grille 10x10 avec un mur vertical percé en bas ; départ (0,9), but (9,9) ;
4 déplacements de coût 1 ; heuristique de Manhattan pour A*.

  Planificateur     Longueur du plan   Noeuds explorés
  DFS                     63                 72
  BFS                     27                 91
  Dijkstra                27                 91
  A* (h = 0)              27                 91
  A* (Manhattan)          27                 76

Observations : le DFS explore peu mais ne garantit pas un bon plan ; BFS, Dijkstra et
A*(h=0) coïncident car tous les coûts valent 1 ; l'heuristique de Manhattan, admissible ici,
réduit le nombre de noeuds explorés sans perdre l'optimalité.

