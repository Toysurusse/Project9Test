package com.dummy.myerp.consumer.dao.impl.db.dao;

import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;

import com.dummy.myerp.consumer.dao.impl.db.rowmapper.CompteComptableRM;
import com.dummy.myerp.consumer.db.DataSourcesEnum;
import com.dummy.myerp.consumer.db.helper.ResultSetHelper;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.SequenceEcritureComptable;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.*;
import java.util.List;

public class ResultSethelperImp extends ResultSetHelper {


    // ==================== Constructeurs ====================
    /** Instance unique de la classe (design pattern Singleton) */
    private static final ResultSethelperImp INSTANCE = new ResultSethelperImp();

    /**
     * Renvoie l'instance unique de la classe (design pattern Singleton).
     *
     * @return {@link ResultSethelperImp}
     */
    public static ResultSethelperImp getInstance() {
        return ResultSethelperImp.INSTANCE;
    }

    /**
     * Constructeur.
     */
    protected ResultSethelperImp() {
        super();
    }



    //myerp.datasource.url=jdbc:postgresql://localhost:9032/db_myerp

    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:postgresql://127.0.0.1:5432/my_erp";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, "usr_myerp","myerp");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }


    /**
     * select all rows in the warehouses table
     */
    public void Selectcolumn(){
        String sql = "SELECT * FROM myerp.compte_comptable";

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
             getLong(rs,"numero");
             getInteger(rs,"numero");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}