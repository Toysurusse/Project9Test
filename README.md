# MyERP 

Travis status : [![Build Status](https://travis-ci.org/Toysurusse/Project9Test.svg?branch=master)](https://travis-ci.org/Toysurusse/Project9Test)

CodeCov : [![codecov](https://codecov.io/gh/Toysurusse/Project9Test/branch/master/graph/badge.svg)](https://codecov.io/gh/Toysurusse/Project9Test)


## Organisation du répertoire

*   `doc` : documentation
*   `docker` : répertoire relatifs aux conteneurs _docker_ utiles pour le projet
    *   `dev` : environnement de développement
*   `src` : code source de l'application


## Environnement de développement

Les composants nécessaires lors du développement sont disponibles via des conteneurs _docker_.
L'environnement de développement est assemblé grâce à _docker-compose_
(cf docker/dev/docker-compose.yml).

Il comporte :

*   une base de données _PostgreSQL_ contenant un jeu de données de démo (`postgresql://127.0.0.1:9032/db_myerp`)



### Lancement

    cd docker/dev
    docker-compose up


### Arrêt

    cd docker/dev
    docker-compose stop


### Remise à zero

    cd docker/dev
    docker-compose stop
    docker-compose rm -v
    docker-compose up

## Règles de gestion

*   RG_Compta_1 : Le solde d'un compte comptable est égal à la somme des montants au débit des lignes d'écriture diminuées de la somme des montants au crédit. Si le résultat est positif, le solde est dit "débiteur", si le résultat est négatif le solde est dit "créditeur".


    La couche Business permet de générer le solde d'un compte comptable conformément à la règle demandée.



*   RG_Compta_2 : Pour qu'une écriture comptable soit valide, elle doit être équilibrée : la somme des montants au crédit des lignes d'écriture doit être égale à la somme des montants au débit.

    
    Les écritures comptables sont gérées par la classe Ecriture comptable. Les fonctions "BigDecimal getTotalDebit()" et "BigDecimal getTotalCrédit()" assurent le calcul de la somme des débits et de la somme des crédtis.
    
    La fonction IsEquilibre () permet de s'assurer que le total des crédit est égal au total des débits. 

*   RG_Compta_3 : Une écriture comptable doit contenir au moins deux lignes d'écriture : une au débit et une au crédit.


    La fonction "checkEcritureComptableUnit" contenue dans la classe ComptabiliteManagerImpl de la couche Business contrôle que l'écriture comptable contienne au moins deux lignes d'écriture.



*   RG_Compta_4 :  	Les montants des lignes d'écriture sont signés et peuvent prendre des valeurs négatives (même si cela est peu fréquent).


    Les lignes d'écritures comptables peuvent bien comprendre des lignes négatives. Le test de la couche model comprend un test unitaire l'attestant.



*   RG_Compta_5 : La référence d'une écriture comptable est composée du code du journal dans lequel figure l'écriture suivi de l'année et d'un numéro de séquence (propre à chaque journal) sur 5 chiffres incrémenté automatiquement à chaque écriture. Le formatage de la référence est : XX-AAAA/#####.
    Ex : Journal de banque (BQ), écriture au 31/12/2016
    --> BQ-2016/00001


    La couche model contraint le formatage de la référence d'une écriture comptable

    La couche Business contrôle via la fonction checkEcritureComptableUnit de la classe ComptabiliteManagerImpl que l'année dans la référence correspond bien à la date de l'écriture, et que le code journal correspond au journal de banque.
    
    L'incrémentation du numéro de séquence est incorporé automatiquement à l'aide de la méthode addréférence.

*   RG_Compta_6 : La référence d'une écriture comptable doit être unique, il n'est pas possible de créer plusieurs écritures ayant la même référence.


    La couche Business contrôle via la fonction checkEcritureComptableContext de la classe ComptabiliteManagerImpl contrôle l'unicité de l'écriture comptable.

    La méthode addRéférence permet de s'assurer de l'unicité d'un référence et de générer automatique le numéro d'index de cette référence

*   RG_Compta_7 : Les montants des lignes d'écritures peuvent comporter 2 chiffres maximum après la virgule.


    La couche model contrôle via la classe LigneEcritureComptable que l'écriture comptable comporte moins de 3 décimales. 
    
    
## Correction bugs :


*   Dans l'entité EcritureComptable, correction de la méthode getTotalCredit() qui accédait à la méthode getDebit() au lieu de getCredit()


*   Dans l'entité EcritureComptable, correction de la méthode isEquilibree() qui retournait le résultat d'une égalité à l'aide de equals() au lieu de faire une comparaison avec compareTo()


*   Dans le fichier sqlContext.xml, corriger la propriété SQLinsertListLigneEcritureComptable. Il manquait une virgule dans le INSERT entre les colonnes debit et credit


*   Dans la classe ComptabiliteManagerImpl, correction de la méthode updateEcritureComptable(). Ajouter la ligne this.checkEcritureComptable(pEcritureComptable); en haut afin de vérifier que la référence de l'écriture comptable respecte les règles de comptabilité 5 et 6


*   Dans la classe SpringRegistry de la couche business, modification de la variable CONTEXT_APPLI_LOCATION afin d'adapter le chemin d'accès au fichier bootstrapContext.xml qui est un conteneur Spring IoC, dans lequel on importe le businessContext.xml, consumerContext.xml et le datasourceContext.xml qui va redéfinir le bean dataSourceMYERP pour les tests



