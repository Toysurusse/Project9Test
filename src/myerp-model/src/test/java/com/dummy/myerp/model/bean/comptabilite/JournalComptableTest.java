package com.dummy.myerp.model.bean.comptabilite;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class JournalComptableTest {

    private static JournalComptable vJournal  = new JournalComptable();
    private static List<JournalComptable> vList;


    @Test
    public void getByCode() {
        vJournal.setCode("AC");
        vJournal.setLibelle("Achat");
        vList = new ArrayList<>(0);
        Assert.assertNull(JournalComptable.getByCode(vList, "AD"));
        vList.add(vJournal);
        vList.add(new JournalComptable("BQ", "Banque"));
        Assert.assertEquals(JournalComptable.getByCode(vList, "AC"), vJournal);
        Assert.assertNull(JournalComptable.getByCode(vList, "AD"));
        vJournal.toString();
    }

    @Test
    public void GettersTest() {
        vJournal.setCode("pCode");
        Assert.assertTrue(vJournal.getCode().equals("pCode"));

        vJournal.setLibelle("pLibelle");
        Assert.assertTrue(vJournal.getLibelle().equals("pLibelle"));

    }
}