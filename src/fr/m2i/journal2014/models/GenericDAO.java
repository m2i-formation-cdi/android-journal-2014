package fr.m2i.journal2014.models;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TreeMap;

/**
 *
 * @author seb
 */
public class GenericDAO implements IDAO<Object>{

    private String tableName;
    private Connection dbConnexion;
    private String pkName;

    public GenericDAO() {
    }

    public GenericDAO(String tableName, Connection dbConnexion) {
        //définit le nom de la table sécurisée contre les injections sql
        this.tableName = this.sanitizeParam(tableName);
        //Définit la clef primaire par défaut
        this.setDefaultPkName();

        //Définit la connexion à la bdd
        this.dbConnexion = dbConnexion;
    }

    /**
     * retourne le nom de la colonne correspondant à la clef primaire
     *
     * @return
     */
    public String getpkName() {
        return this.pkName;
    }

    /**
     * Définit la clef primaire par défaut
     */
    private void setDefaultPkName() {
        //Par défaut la pk est "id_" + le nom de la table
        if (this.pkName == null || this.pkName.equals("")) {
            StringBuilder sb = new StringBuilder("id_");
            sb.append(this.tableName.toLowerCase());
            this.pkName = sb.toString();
        }
    }

    /**
     * Permet de définir une clef primaire différente de la valeur par défaut
     *
     * @param pkName
     */
    public void setPkName(String pkName) {
        this.pkName = pkName;
    }

    /**
     * Sécurise une chaîne contre l'injection sql
     *
     * @param name
     * @return
     */
    private String sanitizeParam(String name) {
        return name.replaceAll(";", "");
    }

    /**
     * Examine une classe pojo et retourne un map de nom de colonne et valeurs
     *
     * @param pojo
     * @return
     */
    private Map<String, String> pojoToMap(Object pojo) {
        //Le tabeau des nomns de colonnes et des valeurs
        Map<String, String> dbFieldsMap = new TreeMap<String,String>();
        //La liste des méthodes publiques du pojo sous forme de tableau de String
        Method[] methods = pojo.getClass().getMethods();
        java.lang.reflect.Method getterMethod;
        //int nbMethods = methods.length;
        String methodName;
        String getterValue;
        String dbFieldName;
        //Boucle sur la liste des méthodes
        for (Method method : methods) {
            //nom de la méthode
            methodName = method.getName();
            //Filtre, on ne conserver que les getters et on élimine la méthode getClass
            if (methodName.startsWith("get") && !methodName.endsWith("Class")) {
                try {
                    //Récupération de la méthode dans un objet Method
                    getterMethod = pojo.getClass().getMethod(methodName);
                    //Récupération de la valeur retournée par l'appel au getter
                    getterValue = this.getValueFromPojo(pojo, methodName);
                    //Transformation du nom de la méthode en nom de colonne
                    //on élimine les trois premiers caractère, on underscorise
                    // et on convertit le tout en bas de casse
                    dbFieldName = this.camelToUnderscore(methodName.substring(3));
                    //Enregistrement de la paire nom de colonne/valeur dans le map
                    dbFieldsMap.put(dbFieldName, getterValue);
                } catch (Exception ex) {
                    Logger.getLogger(GenericDAO.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println(ex.getMessage());
                }
            }
        }
        return dbFieldsMap;
    }

    /**
     * Retourne le résultat de l'appel à un getter sur le pojo sous le forme
     * d'une chaîne de caractère exploitable en sql
     *
     * @param pojo
     * @param methodName
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    private String getValueFromPojo(Object pojo, String methodName) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String returnValue = null;
        String returnType;
        java.lang.reflect.Method getterMethod;
        //Récupération de la méthode dans un objet Method
        getterMethod = pojo.getClass().getMethod(methodName);
        //Récupération du type retournée par la méthode sous la forme d'une chaîne
        returnType = getterMethod.getReturnType().toString();
        
        
        Map mapReturnType = new HashMap<String, Integer>();
        mapReturnType.put("int", 1);
        mapReturnType.put("class java.util.Calendar", 2);
        
        /*
        String[] tReturnType = new String[2];
        tReturnType[0] = "int";
        tReturnType[1] = "class java.util.Calendar";
        */
        
        
        int intReturnType = Integer.valueOf(mapReturnType.get(returnType).toString());

        //En fonction du type on définit les règles de conersion en String
        switch (intReturnType) {
            case 1:
                Object getterValue =  getterMethod.invoke(pojo);
                int intGetterValue = Integer.valueOf(getterValue.toString());
                if (intGetterValue == 0) {
                    returnValue = "null";
                } else {
                    returnValue = String.valueOf(intGetterValue);
                }
                break;
            case 2:
                Object calendarGetterValue = getterMethod.invoke(pojo);
                returnValue = this.CalendarToMysqlDate((Calendar) calendarGetterValue);
                break;
            default:
                returnValue = String.valueOf(getterMethod.invoke(pojo));

        }

        return returnValue;
    }

    /**
     * Conversion d'un Calendar en date MySql
     *
     * @param date
     * @return
     */
    private String CalendarToMysqlDate(Calendar date) {
        if(date != null){
        StringBuilder sb = new StringBuilder();
        sb.append(date.get(Calendar.YEAR));
        sb.append("-");
        sb.append(date.get(Calendar.MONTH) + 1);
        sb.append("-");
        sb.append(date.get(Calendar.DAY_OF_MONTH));
        return sb.toString();
        } else {
            return "null";
        }
    }

    /**
     * Conversion d'un nom de méthode en nom de colonne sql
     *
     * @param memberName
     * @return
     */
    private String camelToUnderscore(String memberName) {
        String dbFieldName;
        String regex = "([A-Z][a-z]+)";
        String replacement = "$1_";
        dbFieldName = memberName.replaceAll(regex, replacement).toLowerCase();
        dbFieldName = dbFieldName.substring(0, dbFieldName.length() - 1);
        return dbFieldName;
    }

    /**
     * Exécution d'un requête Select en utilisant la clef primaire comme unique
     * critère
     *
     * @param pojo
     * @return
     */
    public Map<String, String> selectOneByPk(Object pojo) {
        //Les données du pojo
        Map<String, String> dbFieldsMap = this.pojoToMap(pojo);
        //L'enregistrement retourné
        Map<String, String> recordMap = new HashMap<String,String>();

        //Constitution de la chaîne sql
        //TODO : factoriser
        String sql;
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ");
        sb.append(this.tableName);
        sb.append(" WHERE ");
        //Récupération des critères, le deuxième argument à true
        //indique que l'on ne souhaite prendre en compte que la clef primaire
        sb.append(this.getWhere(dbFieldsMap, true));
        sql = sb.toString();
        //Exécution de la requête
        try {
            PreparedStatement pStatement = this.dbConnexion.prepareStatement(sql);
            pStatement.setString(1, dbFieldsMap.get(this.pkName));
            ResultSet rs = pStatement.executeQuery();
            //conversion du ResultSet en Map
            recordMap = this.resultSetToMap(rs);
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(GenericDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return recordMap;
    }
    

    /**
     * Exécution d'une requête select en utlisant toutes les données du pojo
     *
     * @param pojo
     * @return
     */
    public Map<String, String> selectOne(Object pojo) {
        Map<String, String> dbFieldsMap = this.pojoToMap(pojo);
        Map<String, String> recordMap = new HashMap<String,String>();

        //Constitution de la chaîne sql
        String sql;
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ");
        sb.append(this.tableName);
        sb.append(" WHERE ");
        //Récupération des critères
        sb.append(this.getWhere(dbFieldsMap, false));
        sql = sb.toString();

        //Exécution de la requête
        try {
            PreparedStatement pStatement = this.getStatement(dbFieldsMap, sql);
            ResultSet rs = pStatement.executeQuery();
            //Conversion du ResultSet en Map
            recordMap = this.resultSetToMap(rs);
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(GenericDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return recordMap;
    }

    /**
     * Exécution d'une requête retournant tous les enregistrements de la table
     * sous forme de List de Map
     *
     * @return
     */
    public List<Map<String, String>> selectAll() {
        List<Map<String, String>> records = new ArrayList<Map<String,String>>();

        //Constitution de la chaîne sql
        String sql;
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ");
        sb.append(this.tableName);
        sql = sb.toString();

        //Exécution de la requête
        try {
            PreparedStatement pStatement = this.dbConnexion.prepareStatement(sql);
            ResultSet rs = pStatement.executeQuery();
            //Conversion du ResultSet en List de Map
            records = this.resultSetToList(rs);
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(GenericDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return records;
    }

    /**
     * Conversion d'un ResultSet en List de Map
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    private List<Map<String, String>> resultSetToList(ResultSet rs) throws SQLException {
        List<Map<String, String>> records = new ArrayList<Map<String,String>>();

        //Récupération des infos sur les colonnes du ResultSet
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();

        //Boucle sur les enregistrements du ResultSet
        while (rs.next()) {
            //Map des noms de colonnes et des valeurs
            Map<String, String> recordMap = new HashMap<String,String>();
            //Boucle sur les colonnes de l'enregistrement en cours
            for (int i = 1; i <= columns; i++) {
                recordMap.put(md.getColumnName(i), rs.getString(i));
            }
            //Ajout d'un enregistrement dans la List
            records.add(recordMap);
        }
        return records;
    }

    /**
     * Conversion du premier enregistrement d'un ResultSet en Map
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    private Map<String, String> resultSetToMap(ResultSet rs) throws SQLException {
        //Récupération des informations sur les colonnes
        Map<String, String> recordMap = new HashMap<String,String>();
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        //Positionnement sur le premier enregistrement
        rs.first();
        //Boucle sur les colonnes et population du Map
        for (int i = 1; i <= columns; i++) {
            recordMap.put(md.getColumnName(i), rs.getString(i));
        }
        return recordMap;
    }

    /**
     * Supression d'un enregistrement
     *
     * @param pojo
     * @return
     */
    public int delete(Object pojo) {
        Map<String, String> dbFieldsMap = this.pojoToMap(pojo);
        int affectedRows = -1;
        //Constitution de la chaîne sql
        String sql;
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM ");
        sb.append(this.tableName);
        sb.append(" WHERE ");
        sb.append(this.getWhere(dbFieldsMap, false));
        sql = sb.toString();

        //Exécution de la requête
        try {
            //Factorisation du Statement et du passage des paramètres
            PreparedStatement pStatement = this.getStatement(dbFieldsMap, sql);
            affectedRows = pStatement.executeUpdate();
            pStatement.close();
        } catch (SQLException ex) {
            Logger.getLogger(GenericDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return affectedRows;
    }

    /**
     * Insertion d'un enregistrement
     *
     * @param pojo
     * @return
     */
    public int insert(Object pojo) throws SQLException {
        int affectedRows = -1;
        Map<String, String> dbFieldsMap = this.pojoToMap(pojo);

        //Constitution de la chaîne sql
        String sql;
        StringBuilder sb = new StringBuilder();
        StringBuilder sbParams = new StringBuilder("(");
        sb.append("INSERT INTO ");
        sb.append(this.tableName);
        sb.append(" (");
        //Boucle pour récupérer les noms de champs et constituer la chaine des substitutions
        for (Map.Entry<String, String> entrySet : dbFieldsMap.entrySet()) {
            String fieldName = entrySet.getKey();
            String value = entrySet.getValue();
            if (!value.equals("") && !(value.equals("null"))) {
                sb.append(fieldName);
                sb.append(",");
                sbParams.append("?,");
            }
        }
        sb.replace(sb.length() - 1, sb.length(), ")");
        sbParams.replace(sbParams.length() - 1, sbParams.length(), ")");

        sb.append(" VALUES ");
        sb.append(sbParams.toString());
        sql = sb.toString();

        //Exécution de la requêtes
       
            PreparedStatement pStatement;
            pStatement = this.getStatement(dbFieldsMap, sql);
            affectedRows = pStatement.executeUpdate();
            
            //Récupération de la clef primaire générée
            ResultSet pkRs = pStatement.getGeneratedKeys();
            int pk = 0;
            if(pkRs.next()){
                pk = Integer.valueOf(pkRs.getString(1));
            }
            
            pStatement.close();
        
        //System.out.println(sb.toString());
        return pk;
    }

    /**
     * Mise à jour d'un enregistrement
     *
     * @param pojo
     * @return
     */
    public int update(Object pojo) {
        int affectedRows = -1;
        Map<String, String> dbFieldsMap = this.pojoToMap(pojo);

        //Constittion de la chaîne sql
        String sql;
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE ");
        sb.append(this.tableName);
        sb.append(" SET ");
        //Boucle pour constituer la chaine des noms de colonne et des valeurs
        for (Map.Entry<String, String> entrySet : dbFieldsMap.entrySet()) {
            String fieldName = entrySet.getKey();
            String value = entrySet.getValue();
            if (!value.equals("") && !(value.equals("null")) && !fieldName.equals(this.pkName)) {
                sb.append(fieldName);
                sb.append("=");
                sb.append("?,");
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(" WHERE ");
        //Clause where ne prenant en compte que la clef primaire
        sb.append(this.getWhere(dbFieldsMap, true));

        sql = sb.toString();
        //Exécution de la requête
        try {
            PreparedStatement pStatement;
            //Méthode particulière, car il faut ajouter la clef primaire à la fin de la liste des paramètres
            pStatement = this.getStatementForUpdate(dbFieldsMap, sql);
            affectedRows = pStatement.executeUpdate();
            pStatement.close();
        } catch (SQLException ex) {
            Logger.getLogger(GenericDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return affectedRows;
    }

    /**
     * Constitution de la clause WHERE
     *
     * @param dbFieldMap
     * @param onlyPk
     * @return
     */
    private String getWhere(Map<String, String> dbFieldMap, boolean onlyPk) {
        StringBuilder sb = new StringBuilder();
        //Boucle sur les paires colonne/valeur récupérées depuis le pojo
        for (Map.Entry<String, String> entrySet : dbFieldMap.entrySet()) {
            String key = entrySet.getKey();
            String value = entrySet.getValue();
            //Les valeurs vides sont ignorées
            if (!value.equals("") && !(value.equals("null"))) {
                if (onlyPk) {
                    if (key.equals(this.pkName)) {
                        sb.append(key);
                        sb.append("=?");
                    }
                } else {
                    sb.append(key);
                    sb.append("=? AND ");
                }
            }
        }

        if (!onlyPk) {
            //Suppression du dernier AND
            sb.delete(sb.length() - 4, sb.length());
        }
        return sb.toString();
    }

    /**
     * Instanciation d'un objet Prepared Statement et définition des paramètres
     * du Statement en fonction des données du Pojo
     *
     * @param dbFieldMap
     * @param sql
     * @return
     * @throws SQLException
     */
    private PreparedStatement getStatement(Map<String, String> dbFieldMap, String sql) throws SQLException {
        int paramIndex = 1;
        //Instanciation d'un objet Prepared Statement
        PreparedStatement pStatement = this.dbConnexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        //Boucle sur les colonnes et le valeurs pour passer celles ci en paramètre du Statement
        for (Map.Entry<String, String> entrySet : dbFieldMap.entrySet()) {
            String key = entrySet.getKey();
            String value = entrySet.getValue();
            if (!value.equals("") && !(value.equals("null"))) {
                pStatement.setString(paramIndex, value);
                paramIndex++;
            }
        }
        return pStatement;
    }

    /**
     * Instanciation d'un objet Prepared Statement et définition des paramètres
     * du Statement en fonction des données du Pojo Ajout de la clef primaire en
     * dernier paramètre
     *
     * @param dbFieldMap
     * @param sql
     * @return
     * @throws SQLException
     */
    private PreparedStatement getStatementForUpdate(Map<String, String> dbFieldMap, String sql) throws SQLException {
        //Récupération d'un Statement hydraté avec les valeurs du pojo
        PreparedStatement pStatement = this.getStatement(dbFieldMap, sql);
        //Récupération du nombre de paramètres du Statement
        ParameterMetaData pStatementData = pStatement.getParameterMetaData();
        int paramIndex = pStatementData.getParameterCount();
        paramIndex++;
        //Passage de la clef primaire en dernier paramètre
        pStatement.setString(paramIndex, dbFieldMap.get(this.pkName));

        return pStatement;
    }

    public static void main(String[] args) {
        
   }

}
