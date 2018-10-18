package com.dummy.myerp.model.bean.comptabilite;


import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


public class JournalComptableTest {

    private static JournalComptable vJournal  = new JournalComptable();
    private static List<JournalComptable> vList;


    @Test
    public void getByCode() {
        vJournal.setCode("AC");
        vJournal.setLibelle("Achat");
        vList = new ArrayList<>(0);
        assertNull(JournalComptable.getByCode(vList, "AD"));
        vList.add(vJournal);
        vList.add(new JournalComptable("BQ", "Banque"));
        assertEquals(JournalComptable.getByCode(vList, "AC"), vJournal);
        assertNull(JournalComptable.getByCode(vList, "AD"));
        vJournal.toString();
    }

    @Test
    public void GettersTest() {
        vJournal.setCode("pCode");
        assertTrue(vJournal.getCode().equals("pCode"));

        vJournal.setLibelle("pLibelle");
        assertTrue(vJournal.getLibelle().equals("pLibelle"));

    }
}