
Group members :
Quentin Deme 21507097 L3-info
Sami Zaizafoun 21600538 L3-info
Martin Jacqueline 21507982 L3-info
Valentin Leblond 21609038 L3-info

Arborescence :
    |__ databases/
    |    |__ all databases
    |
    |__ scripts/
    |    |__ all scripts
    |
    |__ src/
         |__ all packages

Informations par package :
    # ppc
        - avec l'heuristic DomainMaxHeuristic, on a un resultat particulier, le
        nombre de noeuds parcourus par le backtrack est superieur pour celui qui
        filtre que celui qui ne filtre pas quand il y a 3 couleurs dans le domaine
        des couleurs mais quand il y a un nombre de couleurs superieur ou egales
        a 7 c'est le backtrack qui filtre qui parcours le moins de noeuds, etrange...

    # datamining
        - en utilisant la base de donnee de ecampus ("example_db.csv") on obtient autant de motifs
        frequents que de motifs fermes frequents, alors que si on test avec "example2_db.csv"
        qui est la base de donnee "example_db.csv" avec que les 10 premieres transactions,
        on obtient bien moins de motifs fermes frequents que de motifs uniquement
        frequents

Scripts :
    runners (the name of runner of package is "run[package].sh" and all runners compile before execute):
        # representations :
            - runrepresentations.sh
            execute le Main de representations qui est un test sur la satisfaction de
            contraintes sur des exemples de voitures

        # ppc
            - runppc.sh
            execute le Main de ppc qui test l'algorithme de backtracking avec et sans filtrage,
            il executera 4 backtrack 2 qui renvoient la premiere solution qu'ils trouvent un
            avec filtrage et l'autre sans et 2 autres backtrack qui renvoient toutes les
            solutions de voitures possible un avec filtrage et l'autre sans

        # planning
            - runplanning.sh [useDijkstra]
            execute le Main de planning qui va lancer l'ago Dijkstra (si useDijkstra = true),
            A* et WA* avec SimpleHeuristic, A* et WA* avec InformedHeuristic
            @ useDijkstra : boolean true si vous voulez executer dijkstra ou false si vous
            voulez juste executer A* et WA*

            > example, lancer avec dijkstra : sh runplanning.sh true
            > example, lancer sans dijkstra : sh runplanning.sh false

            - runTestDfsBfs.sh
            execute TestDfsBfs de planning qui permet de tester l'algorithme dfs
            recursif, dfs iteratif et bfs iteratif on print uniquement le nombre
            d'action que chaqu'un trouve car ils en trouve beaucoup trop et
            l'affichage de ces actions n'est pas interessant

        # datamining
            - rundatamining.sh [database] [minfr] [minconf]
            execute le Main de datamining qui trouve les motifs frequents de la base de donnee
            et qui calculs les motifs fermes et genere les regles a partir de ces motifs fermes
            @ database : le fichier de la base de donnee du dossier databases/ example "example_db.csv"
            @ minfr : le minimum de frequence pour que les motifs soient frequents
            @ minconf : le minimum de confiance des motifs

            > example :
            sh rundatamining.sh "example_db.csv" 1500 0.9
            sh rundatamining.sh "example2_db.csv" 5 0.9

            - rundataminingDefault.sh
            execute le script rundatamining.sh avec database="databases/example_db.csv",
            minfr = 1500 et minconf = 0.9

        # diagnosis
            - rundiagnosis.sh
            execute le Main de diagnosis qui va trouver l'explication minimale pour l'inclusion,
            generer toutes les expliquations et trouver l'explication minimale au sens de la
            cardinalite

        # all packages
            - runClass.sh [MyClass]
            compile and execute la classe voulue
            @ MyClass : le nom de la classe

            > example :
            sh runClass.sh "representations.Main"
            sh runClass.sh "planning.TestDfsBfs"

    utiles :
        - compile.sh
        compiles the program in a folder build/ and copy the folder of databases into build/

        - makedoc.sh
        builds the javadoc in a folder doc/

        - makejar.sh
        compile and create the jar of the program in a folder jar/

        - clean.sh
        deletes build/ , doc/ and jar/
