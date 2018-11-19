package com.dummy.myerp.consumer.dao;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.SequenceEcritureComptable;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;


public class ComptabiliteDaoMock {

    public List<CompteComptable> getListCompteComptable() {
        return null;
    }


    public List<JournalComptable> getListJournalComptable() {
        return null;
    }


    public List<EcritureComptable> getListEcritureComptable() {

        List<EcritureComptable> ecritureComptableList=new ArrayList<>();

        EcritureComptable ecritureComptable=new EcritureComptable();
        ecritureComptable.setLibelle("Cartouches d’imprimante");
        ecritureComptable.setId(-1);
        ecritureComptable.setJournal(new JournalComptable("AC","Acomptes"));
        ecritureComptable.setReference("AC-2016/00001");
        ecritureComptable.setDate(new Date(2016-12-31));
        ecritureComptableList.add(ecritureComptable);

        //INSERT INTO MYERP.ecriture_comptable (id,journal_code,reference,date,libelle) VALUES (	-2,	'VE',	'VE-2016/00002',	'2016-12-30',	'TMA Appli Xxx'	);
        //INSERT INTO MYERP.ecriture_comptable (id,journal_code,reference,date,libelle) VALUES (	-3,	'BQ',	'BQ-2016/00003',	'2016-12-29',	'Paiement Facture F110001'	);
        //INSERT INTO MYERP.ecriture_comptable (id,journal_code,reference,date,libelle) VALUES (	-4,	'VE',	'VE-2016/00004',	'2016-12-28',	'TMA Appli Yyy'	);
        //INSERT INTO MYERP.ecriture_comptable (id,journal_code,reference,date,libelle) VALUES (	-5,	'BQ',	'BQ-2016/00005',	'2016-12-27',	'Paiement Facture C110002'	);

        return ecritureComptableList;
    }


    public EcritureComptable getEcritureComptable(Integer pId) throws NotFoundException {
        return null;
    }


    public EcritureComptable getEcritureComptableByRef(String pReference) throws NotFoundException, FunctionalException {
        EcritureComptable vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setId(22);
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable
                .setReference("AC-" + new SimpleDateFormat("yyyy").format(vEcritureComptable.getDate()) + "/00022");
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture()
                .add(new LigneEcritureComptable(new CompteComptable(1), null, new BigDecimal(123), null));
        vEcritureComptable.getListLigneEcriture()
                .add(new LigneEcritureComptable(new CompteComptable(2), null, null, new BigDecimal(123)));

        return vEcritureComptable;
    }


    public void loadListLigneEcriture(EcritureComptable pEcritureComptable) {

    }


    public void insertEcritureComptable(EcritureComptable pEcritureComptable) {

    }


    public void updateEcritureComptable(EcritureComptable pEcritureComptable) {

    }


    public void deleteEcritureComptable(Integer pId) {

    }

    public SequenceEcritureComptable getSequenceByCodeAndAnneeCourante(SequenceEcritureComptable pSequence) throws NotFoundException {

        SequenceEcritureComptable sequenceEcritureComptable = new SequenceEcritureComptable("VE",2016,41);

        //INSERT INTO MYERP.sequence_ecriture_comptable (journal_code, annee, derniere_valeur) values (	'AC',	2016,	40	);
        //INSERT INTO MYERP.sequence_ecriture_comptable (journal_code, annee, derniere_valeur) values (	'VE',	2016,	41	);
        //INSERT INTO MYERP.sequence_ecriture_comptable (journal_code, annee, derniere_valeur) values (	'BQ',	2016,	51	);
        //INSERT INTO MYERP.sequence_ecriture_comptable (journal_code, annee, derniere_valeur) values (	'OD',	2016,	88	);

        return sequenceEcritureComptable;
    }

    public void upsertSequenceEcritureComptable(SequenceEcritureComptable pSequence) {

    }

}