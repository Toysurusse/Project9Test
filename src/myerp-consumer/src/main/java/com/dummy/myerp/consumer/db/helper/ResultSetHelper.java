package com.dummy.myerp.consumer.db.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import com.dummy.myerp.consumer.db.AbstractDbConsumer;
import org.apache.commons.lang3.time.DateUtils;


/**
 * Classe utilitaire travaillant sur les ResultSet
 */
public abstract class ResultSetHelper extends AbstractDbConsumer {

    // ==================== Constructeurs ====================
    /**
     * Constructeur.
     */
    protected ResultSetHelper() {
        super();
    }


    // ==================== Méthodes ====================
    /**
     * Renvoie la valeur de la colonne pColName dans un <code>Integer</code>.
     * Si la colonne vaut <code>null</code>, la méthode renvoie <code>null</code>
     *
     * @param pRS : Le ResultSet à intéroger
     * @param pColName : Le nom de la colonne dans le retour de la requête SQL
     * @return <code>Integer</code> ou <code>null</code>
     * @throws SQLException sur erreur SQL
     */
    public static Integer getInteger(ResultSet pRS, String pColName) throws SQLException {
        Integer vRetour = null;
        int vInt = pRS.findColumn(pColName);
            vRetour = new Integer(vInt);
        return vRetour;
    }

    /**
     * Renvoie la valeur de la colonne pColName dans un <code>Long</code>.
     * Si la colonne vaut <code>null</code>, la méthode renvoie <code>null</code>
     *
     * @param pRS : Le ResultSet à intéroger
     * @param pColName : Le nom de la colonne dans le retour de la requête SQL
     * @return <code>Long</code> ou <code>null</code>
     * @throws SQLException sur erreur SQL
     */
    public static Long getLong(ResultSet pRS, String pColName) throws SQLException {

        Long vRetour = null;
        Long vLong = (long) pRS.findColumn(pColName);
            vRetour = new Long(vLong);
        return vRetour;
    }
}
