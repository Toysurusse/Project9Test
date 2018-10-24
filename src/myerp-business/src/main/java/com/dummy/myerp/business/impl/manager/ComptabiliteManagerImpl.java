package com.dummy.myerp.business.impl.manager;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import com.dummy.myerp.model.bean.comptabilite.*;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.TransactionStatus;
import com.dummy.myerp.business.contrat.manager.ComptabiliteManager;
import com.dummy.myerp.business.impl.AbstractBusinessManager;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;


/**
 * Comptabilite manager implementation.
 */
public class ComptabiliteManagerImpl extends AbstractBusinessManager implements ComptabiliteManager {

    // ==================== Attributs ====================
    public final static String RG2_EXCEPTION = "L'écriture comptable n'est pas équilibrée.",
            RG3_EXCEPTION = "L'écriture comptable doit avoir au moins deux lignes : une ligne au débit et une ligne au crédit.",
            RG5_CODE_EXCEPTION = "Le code journal doit être le même dans la référence",
            RG5_DATE_EXCEPTION = "La date de l'écriture comptable doit être la même dans la référence",
            VIOLATION_EXCEPTION = "L'écriture comptable ne respecte pas les règles de gestion.",
            RG6_EXCEPTION ="La référence d'une écriture comptable doit être unique.";

    // ==================== Constructeurs ====================

    /**
     * Instantiates a new Comptabilite manager.
     */
    public ComptabiliteManagerImpl() {
    }


    // ==================== Getters/Setters ====================
    @Override
    public List<CompteComptable> getListCompteComptable() {
        return getDaoProxy().getComptabiliteDao().getListCompteComptable();
    }


    @Override
    public List<JournalComptable> getListJournalComptable() {
        return getDaoProxy().getComptabiliteDao().getListJournalComptable();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EcritureComptable> getListEcritureComptable() {
        return getDaoProxy().getComptabiliteDao().getListEcritureComptable();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void addReference(EcritureComptable pEcritureComptable) throws NotFoundException, FunctionalException {
        // Bien se réferer à la JavaDoc de cette méthode !
        /* Le principe :
                1.  Remonter depuis la persitance la dernière valeur de la séquence du journal pour l'année de l'écriture
                    (table sequence_ecriture_comptable)
        */
        Integer vEcritureComptableYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(pEcritureComptable.getDate()));
        SequenceEcritureComptable vRechercheSequence = new SequenceEcritureComptable();
        vRechercheSequence.setJournalCode(pEcritureComptable.getJournal().getCode());
        vRechercheSequence.setAnnee(vEcritureComptableYear);
        SequenceEcritureComptable vExistingSequence = getDaoProxy().getComptabiliteDao().getSequenceByCodeAndAnneeCourante(vRechercheSequence);
        /*        2.  * S'il n'y a aucun enregistrement pour le journal pour l'année concernée :
                        1. Utiliser le numéro 1.
                    * Sinon :
                        1. Utiliser la dernière valeur + 1
        */
        Integer vNumeroSequence;
        if (vExistingSequence == null) vNumeroSequence = 1;
        else vNumeroSequence = vExistingSequence.getDerniereValeur() + 1;
        /*
                3.  Mettre à jour la référence de l'écriture avec la référence calculée (RG_Compta_5)
        */
        String vReference = pEcritureComptable.getJournal().getCode() +
                "-" + vEcritureComptableYear +
                "/" + String.format("%05d", vNumeroSequence);
        pEcritureComptable.setReference(vReference);

        this.insertEcritureComptable(pEcritureComptable);
        /*
                4.  Enregistrer (insert/update) la valeur de la séquence en persistance
                    (table sequence_ecriture_comptable)
        */
        SequenceEcritureComptable vNewSequence = new SequenceEcritureComptable();
        vNewSequence.setJournalCode(pEcritureComptable.getJournal().getCode());
        vNewSequence.setAnnee(vEcritureComptableYear);
        vNewSequence.setDerniereValeur(vNumeroSequence);
        this.upsertSequenceEcritureComptable(vNewSequence);
    }

    /**
     * {@inheritDoc}
     */
    // TODO à tester
    @Override
    public void checkEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
        this.checkEcritureComptableContext(pEcritureComptable);
        this.checkEcritureComptableUnit(pEcritureComptable);
    }


    /**
     * Vérifie que l'Ecriture comptable respecte les règles de gestion unitaires,
     * c'est à dire indépendemment du contexte (unicité de la référence, exercie comptable non cloturé...)
     *
     * @param pEcritureComptable -
     * @throws FunctionalException Si l'Ecriture comptable ne respecte pas les règles de gestion
     */
    // TODO tests à compléter
    protected void checkEcritureComptableUnit(EcritureComptable pEcritureComptable) throws FunctionalException {
        // ===== Vérification des contraintes unitaires sur les attributs de l'écriture
        Set<ConstraintViolation<EcritureComptable>> vViolations = getConstraintValidator().validate(pEcritureComptable);
        if (!vViolations.isEmpty()) {
            throw new FunctionalException(VIOLATION_EXCEPTION, new ConstraintViolationException(
                    "L'écriture comptable ne respecte pas les contraintes de validation", vViolations));
        }

        // ===== RG_Compta_2 : Pour qu'une écriture comptable soit valide, elle doit être équilibrée
        if (!pEcritureComptable.isEquilibree()) {
            throw new FunctionalException(RG2_EXCEPTION);
        }

        // ===== RG_Compta_3 : une écriture comptable doit avoir au moins 2 lignes d'écriture (1 au débit, 1 au crédit)
        int vNbrCredit = 0;
        int vNbrDebit = 0;
        for (LigneEcritureComptable vLigneEcritureComptable : pEcritureComptable.getListLigneEcriture()) {
            if (BigDecimal.ZERO.compareTo(ObjectUtils.defaultIfNull(vLigneEcritureComptable.getCredit(),
                    BigDecimal.ZERO)) != 0) {
                vNbrCredit++;
            }
            if (BigDecimal.ZERO.compareTo(ObjectUtils.defaultIfNull(vLigneEcritureComptable.getDebit(),
                    BigDecimal.ZERO)) != 0) {
                vNbrDebit++;
            }
        }
        // On test le nombre de lignes car si l'écriture à une seule ligne
        //      avec un montant au débit et un montant au crédit ce n'est pas valable
        if (pEcritureComptable.getListLigneEcriture().size() < 2
                || vNbrCredit < 1
                || vNbrDebit < 1) {
            throw new FunctionalException(RG3_EXCEPTION);
        }
        // vérifier que l'année dans la référence correspond bien à la date de l'écriture, idem pour le code journal...
        if (pEcritureComptable.getReference() != null) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(pEcritureComptable.getDate());
            if (!pEcritureComptable.getReference().substring(3, 7).equals(Integer.toString(calendar.get(Calendar.YEAR)))) {
                throw new FunctionalException(RG5_DATE_EXCEPTION);
            }

            if (!pEcritureComptable.getReference().substring(0, 2).equals(pEcritureComptable.getJournal().getCode())) {
                throw new FunctionalException(RG5_CODE_EXCEPTION);
            }
        }
    }


    /**
     * Vérifie que l'Ecriture comptable respecte les règles de gestion liées au contexte
     * (unicité de la référence, année comptable non cloturé...)
     *
     * @param pEcritureComptable -
     * @throws FunctionalException Si l'Ecriture comptable ne respecte pas les règles de gestion
     */
    protected void checkEcritureComptableContext(EcritureComptable pEcritureComptable) throws FunctionalException {
        // ===== RG_Compta_6 : La référence d'une écriture comptable doit être unique

        if (StringUtils.isNoneEmpty(pEcritureComptable.getReference())) {
            // Recherche d'une écriture ayant la même référence
            List<EcritureComptable> vECRef = getDaoProxy().getComptabiliteDao().getListEcritureComptable();
            // Si l'écriture à vérifier est une nouvelle écriture (id == null),
            // ou si elle ne correspond pas à l'écriture trouvée (id != idECRef),
            // c'est qu'il y a déjà une autre écriture avec la même référence
            if (pEcritureComptable.getId() == null) {
                throw new FunctionalException("La référence d'une écriture comptable doit être unique.");
            }
            for (EcritureComptable ec:vECRef
                 ) {
                if (ec.getId()==pEcritureComptable.getId()){
                    throw new FunctionalException("La référence d'une écriture comptable doit être unique.");
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
        this.checkEcritureComptable(pEcritureComptable);
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().insertEcritureComptable(pEcritureComptable);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().updateEcritureComptable(pEcritureComptable);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteEcritureComptable(Integer pId) {
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().deleteEcritureComptable(pId);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void upsertSequenceEcritureComptable(SequenceEcritureComptable pSequence) {
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().upsertSequenceEcritureComptable(pSequence);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }

}
