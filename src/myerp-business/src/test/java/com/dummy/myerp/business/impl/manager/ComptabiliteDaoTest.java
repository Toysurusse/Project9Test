package com.dummy.myerp.business.impl.manager;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.technical.exception.NotFoundException;

public class ComptabiliteDaoTest implements ComptabiliteDao {

    @Override
    public List<CompteComptable> getListCompteComptable() {
        return null;
    }


    @Override
    public List<JournalComptable> getListJournalComptable() {
        return null;
    }


    @Override
    public List<EcritureComptable> getListEcritureComptable() {
        return null;
    }


    @Override
    public EcritureComptable getEcritureComptable(Integer pId) throws NotFoundException {
        return null;
    }


    @Override
    public EcritureComptable getEcritureComptableByRef(String pReference) throws NotFoundException {
        EcritureComptable vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setId(22);
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(vEcritureComptable.getDate());
        vEcritureComptable.setReference("AC-" + calendar.get(Calendar.YEAR) + "/00022");
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture()
                .add(new LigneEcritureComptable(new CompteComptable(1), null, new BigDecimal(123), null));
        vEcritureComptable.getListLigneEcriture()
                .add(new LigneEcritureComptable(new CompteComptable(2), null, null, new BigDecimal(123)));

        return vEcritureComptable;
    }


    @Override
    public void loadListLigneEcriture(EcritureComptable pEcritureComptable) {

    }


    @Override
    public void insertEcritureComptable(EcritureComptable pEcritureComptable) {

    }


    @Override
    public void updateEcritureComptable(EcritureComptable pEcritureComptable) {

    }


    @Override
    public void deleteEcritureComptable(Integer pId) {

    }

}