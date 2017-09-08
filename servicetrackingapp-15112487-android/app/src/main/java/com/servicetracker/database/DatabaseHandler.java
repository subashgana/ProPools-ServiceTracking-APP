package com.servicetracker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.servicetracker.constants.Constants;
import com.servicetracker.model.UserListModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    // Contacts table name
    public static final String TABLE_CUSTOMER_INFO = Constants.CUSTOMER_INFO;
    public static final String USER_TABLE = "UserTable";
    public static final String USER_ID = "user_id";
    public static final String CUSTOMER_ID = "customer_id";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    //Customer details Information
    public static final String HARDNESS = "hardness";
    public static final String NAME = "name";
    public static final String NFC_ID = "nfcid";
    public static final String ADDRESS = "address";
    public static final String TOTALCHLORINE = "totalchlorine";
    public static final String FREECHLORINE = "freechlorine";
    public static final String PH = "ph";
    public static final String ALKALINITY = "alkalinity";
    public static final String CONDITIONER = "conditioner";
    public static final String CHLPOWDER = "chlpowder";
    public static final String CHLTABLETS = "chltablets";
    public static final String SODIUMCHL = "sodiumchl";
    public static final String PHMINUS = "phminus";
    public static final String PHPLUS = "phplus";
    public static final String CHEMICALCONDITIONER = "chemicalconditioner";
    public static final String SUPERBLUE = "superblue";
    public static final String METALGONE = "metalgone";
    public static final String METALOUT = "metalout";
    public static final String ALGQECIDE = "algaecide";
    public static final String FIELD_ID = "field_id";
    public static final String ALKANINITY_COUNT = "alkaninitycont";
    public static final String ACID = "acid";
    public static final String MIS_NOTE = "miscnotes";
    public static final String IMAGE1 = "image1";
    public static final String IMAGE2 = "image2";
    public static final String IMAGE3 = "image3";
    public static final String IMAGE4 = "image4";
    public static final String SERVER_IMAGE = "image5";
    public static final String TIMESTAMP = "timestamp";
    public static final String SYNC = "sync";
    public static final String ENDTIME = "endtime";
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = Constants.DATABASE_NAME;
    public static String CREATE_CUSTOMER_DETAIL_TABLE = "CREATE  TABLE \"customer_info\" (\"user_id\" TEXT  , \"hardness\" TEXT,\"field_id\" TEXT, \"customer_id\" TEXT PRIMARY KEY  NOT NULL, \"name\" TEXT, \"nfcid\" TEXT, \"address\" TEXT, \"totalchlorine\" TEXT, \"freechlorine\" TEXT, \"ph\" TEXT, \"alkalinity\" TEXT, \"conditioner\" TEXT, \"chlpowder\" TEXT, \"chltablets\" TEXT, \"sodiumchl\" TEXT, \"phminus\" TEXT, \"phplus\" TEXT, \"chemicalconditioner\" TEXT, \"superblue\" TEXT, \"metalgone\" TEXT, \"metalout\" TEXT, \"algaecide\" TEXT, \"alkaninitycont\" TEXT, \"acid\" TEXT, \"miscnotes\" TEXT, \"image1\" TEXT, \"image2\" TEXT, \"image3\" TEXT, \"image4\" TEXT, \"image5\" TEXT, \"timestamp\" TEXT,\"endtime\" TEXT, \"sync\" TEXT)";
    public static String CREATE_USER_TABLE = "CREATE  TABLE \"UserTable\" (\"user_id\" TEXT NOT NULL , \"email\" TEXT NOT NULL , \"password\" TEXT NOT NULL , PRIMARY KEY (\"user_id\", \"email\", \"password\"))";
    private String TAG = DatabaseHandler.class.getSimpleName();

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CUSTOMER_DETAIL_TABLE);
        db.execSQL(CREATE_USER_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMER_INFO);
        onCreate(db);
    }

    // Adding new contact
    public void addUsersList(List<UserListModel> arlCustomerDetails, String sync, String user_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        for (int i = 0; i < arlCustomerDetails.size(); i++) {
            cv.put(CUSTOMER_ID, arlCustomerDetails.get(i).getUserid());
            cv.put(USER_ID, user_id);
            cv.put(NAME, arlCustomerDetails.get(i).getName());
            cv.put(HARDNESS, arlCustomerDetails.get(i).getHardness());
            cv.put(NFC_ID, arlCustomerDetails.get(i).getField_nfc_id().toUpperCase());
            cv.put(ADDRESS, arlCustomerDetails.get(i).getAddress());
            cv.put(TOTALCHLORINE, arlCustomerDetails.get(i).getTotalchlorine());
            cv.put(FREECHLORINE, arlCustomerDetails.get(i).getFreechlorine());
            cv.put(PH, arlCustomerDetails.get(i).getPh());
            cv.put(ALKALINITY, arlCustomerDetails.get(i).getAlkalinity());
            cv.put(CONDITIONER, arlCustomerDetails.get(i).getConditioner());
            cv.put(FIELD_ID, arlCustomerDetails.get(i).getField_id_no());
            cv.put(CHLPOWDER, arlCustomerDetails.get(i).getChlpowder());
            cv.put(CHLTABLETS, arlCustomerDetails.get(i).getChltablets());
            cv.put(SODIUMCHL, arlCustomerDetails.get(i).getSodiumchl());
            cv.put(PHMINUS, arlCustomerDetails.get(i).getPhminus());
            cv.put(PHPLUS, arlCustomerDetails.get(i).getPhplus());
            cv.put(CHEMICALCONDITIONER, arlCustomerDetails.get(i).getChemicalconditioner());
            cv.put(SUPERBLUE, arlCustomerDetails.get(i).getSuperblue());
            cv.put(METALGONE, arlCustomerDetails.get(i).getMetalgone());
            cv.put(METALOUT, arlCustomerDetails.get(i).getMetalout());
            cv.put(ALGQECIDE, arlCustomerDetails.get(i).getAlgaecide());
            cv.put(ALKANINITY_COUNT, arlCustomerDetails.get(i).getAlkaninitycont());
            cv.put(ACID, arlCustomerDetails.get(i).getAcid());
            cv.put(MIS_NOTE, arlCustomerDetails.get(i).getMiscnotes());
            cv.put(IMAGE1, arlCustomerDetails.get(i).getImage1());
            cv.put(IMAGE2, arlCustomerDetails.get(i).getImage2());
            cv.put(IMAGE3, arlCustomerDetails.get(i).getImage3());
            cv.put(IMAGE4, arlCustomerDetails.get(i).getImage4());
            cv.put(SERVER_IMAGE, arlCustomerDetails.get(i).getImage1());
            cv.put(TIMESTAMP, arlCustomerDetails.get(i).getTimestamp());
            cv.put(ENDTIME, arlCustomerDetails.get(i).getEndtime());
            cv.put(SYNC, sync);
            db.insertWithOnConflict(TABLE_CUSTOMER_INFO, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        }
        db.close(); // Closing database connection
    }

    public void updateCustomerInformation(UserListModel arlCustomerDetails, String sync, String user_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CUSTOMER_ID, arlCustomerDetails.getUserid());
        cv.put(USER_ID, user_id);
        cv.put(NAME, arlCustomerDetails.getName());
        cv.put(HARDNESS, arlCustomerDetails.getHardness());
        cv.put(NFC_ID, arlCustomerDetails.getField_nfc_id());
        cv.put(ADDRESS, arlCustomerDetails.getAddress());
        cv.put(TOTALCHLORINE, arlCustomerDetails.getTotalchlorine());
        cv.put(FREECHLORINE, arlCustomerDetails.getFreechlorine());
        cv.put(PH, arlCustomerDetails.getPh());
        cv.put(ALKALINITY, arlCustomerDetails.getAlkalinity());
        cv.put(FIELD_ID, arlCustomerDetails.getField_id_no());
        cv.put(CONDITIONER, arlCustomerDetails.getConditioner());
        cv.put(CHLPOWDER, arlCustomerDetails.getChlpowder());
        cv.put(CHLTABLETS, arlCustomerDetails.getChltablets());
        cv.put(SODIUMCHL, arlCustomerDetails.getSodiumchl());
        cv.put(PHMINUS, arlCustomerDetails.getPhminus());
        cv.put(PHPLUS, arlCustomerDetails.getPhplus());
        cv.put(CHEMICALCONDITIONER, arlCustomerDetails.getChemicalconditioner());
        cv.put(SUPERBLUE, arlCustomerDetails.getSuperblue());
        cv.put(METALGONE, arlCustomerDetails.getMetalgone());
        cv.put(METALOUT, arlCustomerDetails.getMetalout());
        cv.put(ALGQECIDE, arlCustomerDetails.getAlgaecide());
        cv.put(ALKANINITY_COUNT, arlCustomerDetails.getAlkaninitycont());
        cv.put(ACID, arlCustomerDetails.getAcid());
        cv.put(MIS_NOTE, arlCustomerDetails.getMiscnotes());
        cv.put(IMAGE1, arlCustomerDetails.getImage1());
        cv.put(IMAGE2, arlCustomerDetails.getImage2());
        cv.put(IMAGE3, arlCustomerDetails.getImage3());
        cv.put(IMAGE4, arlCustomerDetails.getImage4());
        cv.put(TIMESTAMP, arlCustomerDetails.getTimestamp());
        cv.put(ENDTIME, arlCustomerDetails.getEndtime());
        cv.put(SERVER_IMAGE, arlCustomerDetails.getImage1());
        cv.put(SYNC, sync);
        db.insertWithOnConflict(TABLE_CUSTOMER_INFO, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        db.close(); // Closing database connection
    }

    public void saveUserDetails(String Email, String UserId, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(USER_ID, UserId);
        cv.put(EMAIL, Email);
        cv.put(PASSWORD, password);
        db.insertWithOnConflict(USER_TABLE, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        db.close(); // Closing database connection
    }

    public boolean checkVaildUser(String email, String password) {
        // Select All Query
        String selectQuery = "SELECT  * FROM " + USER_TABLE + " where " + EMAIL + " ='" + email + "' AND " + PASSWORD + " ='" + password + "'";
        Log.e(TAG, "selectQuery::::: " + selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        String emailDb;
        String passwordDb;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                emailDb = cursor.getString(cursor.getColumnIndex(EMAIL));
                passwordDb = cursor.getString(cursor.getColumnIndex(PASSWORD));
                if (emailDb.equalsIgnoreCase(email) && passwordDb.equalsIgnoreCase(password)) {
                    return true;
                }
            } else {
                return false;
            }
        }
        return false;
    }

    public String getUserId(String email, String password) {
        // Select All Query
        String selectQuery = "SELECT  * FROM " + USER_TABLE + " where " + EMAIL + " ='" + email + "' AND " + PASSWORD + " ='" + password + "'";
        Log.e(TAG, "selectQuery::::: " + selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        String emailDb;
        String passwordDb, user_id;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                emailDb = cursor.getString(cursor.getColumnIndex(EMAIL));
                passwordDb = cursor.getString(cursor.getColumnIndex(PASSWORD));
                user_id = cursor.getString(cursor.getColumnIndex(USER_ID));
                if (emailDb.equalsIgnoreCase(email) && passwordDb.equalsIgnoreCase(password)) {
                    return user_id;
                }
            } else {
                return "";
            }
        }
        return "";
    }

    public boolean checkVaildNFCId(String nfc_id, String customer_id) {
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMER_INFO + " where " + NFC_ID + " ='" + nfc_id + "' AND " + CUSTOMER_ID + " ='" + customer_id + "'";
        Log.e(TAG, "selectQuery::::: " + selectQuery);
        Log.e(TAG,"INFO"+TABLE_CUSTOMER_INFO);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        String nfc;
        String customer;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                nfc = cursor.getString(cursor.getColumnIndex(NFC_ID));
                Log.e(TAG, "selectQuery::::: " + nfc);
                customer = cursor.getString(cursor.getColumnIndex(CUSTOMER_ID));
                if (nfc.toUpperCase().equalsIgnoreCase(nfc_id.toUpperCase()) && customer.equalsIgnoreCase(customer_id)) {
                    return true;
                }
            } else {
                return false;
            }
        }
        return false;
    }

    public void delete(String user_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_CUSTOMER_INFO + " where user_id " + " = " + user_id);
        db.close(); // Closing database connection
    }

    public List<UserListModel> getSingleContactDetails(String user_id) {
        List<UserListModel> leadsDetails = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMER_INFO;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                if (user_id.equalsIgnoreCase(cursor.getString(cursor.getColumnIndex(CUSTOMER_ID)))) {
                    UserListModel detail = new UserListModel();
                    detail.setUserid(cursor.getString(cursor.getColumnIndex(CUSTOMER_ID)));
                    detail.setName(cursor.getString(cursor.getColumnIndex(NAME)));
                    detail.setField_nfc_id(cursor.getString(cursor.getColumnIndex(NFC_ID)));
                    detail.setAddress(cursor.getString(cursor.getColumnIndex(ADDRESS)));
                    detail.setHardness(cursor.getString(cursor.getColumnIndex(HARDNESS)));
                    detail.setTotalchlorine(cursor.getString(cursor.getColumnIndex(TOTALCHLORINE)));
                    detail.setFreechlorine(cursor.getString(cursor.getColumnIndex(FREECHLORINE)));
                    detail.setPh(cursor.getString(cursor.getColumnIndex(PH)));
                    detail.setAlkalinity(cursor.getString(cursor.getColumnIndex(ALKALINITY)));
                    detail.setConditioner(cursor.getString(cursor.getColumnIndex(CONDITIONER)));
                    detail.setChlpowder(cursor.getString(cursor.getColumnIndex(CHLPOWDER)));
                    detail.setChltablets(cursor.getString(cursor.getColumnIndex(CHLTABLETS)));
                    detail.setSodiumchl(cursor.getString(cursor.getColumnIndex(SODIUMCHL)));
                    detail.setPhminus(cursor.getString(cursor.getColumnIndex(PHMINUS)));
                    detail.setPhplus(cursor.getString(cursor.getColumnIndex(PHPLUS)));
                    detail.setField_id_no(cursor.getString(cursor.getColumnIndex(FIELD_ID)));
                    detail.setChemicalconditioner(cursor.getString(cursor.getColumnIndex(CHEMICALCONDITIONER)));
                    detail.setSuperblue(cursor.getString(cursor.getColumnIndex(SUPERBLUE)));
                    detail.setMetalgone(cursor.getString(cursor.getColumnIndex(METALGONE)));
                    detail.setMetalout(cursor.getString(cursor.getColumnIndex(METALOUT)));
                    detail.setAlgaecide(cursor.getString(cursor.getColumnIndex(ALGQECIDE)));
                    detail.setAlkaninitycont(cursor.getString(cursor.getColumnIndex(ALKANINITY_COUNT)));
                    detail.setAcid(cursor.getString(cursor.getColumnIndex(ACID)));
                    detail.setMiscnotes(cursor.getString(cursor.getColumnIndex(MIS_NOTE)));
                    detail.setImage1(cursor.getString(cursor.getColumnIndex(IMAGE1)));
                    detail.setImage2(cursor.getString(cursor.getColumnIndex(IMAGE2)));
                    detail.setImage3(cursor.getString(cursor.getColumnIndex(IMAGE3)));
                    detail.setImage4(cursor.getString(cursor.getColumnIndex(IMAGE4)));
                    detail.setTimestamp(cursor.getString(cursor.getColumnIndex(TIMESTAMP)));
                    detail.setEndtime(cursor.getString(cursor.getColumnIndex(ENDTIME)));
                    detail.setServer_image(cursor.getString(cursor.getColumnIndex(IMAGE1)));
                    leadsDetails.add(detail);

                    return leadsDetails;
                }
            } while (cursor.moveToNext());
        }
        return null;
    }

    public List<UserListModel> getNewUserList(String user_id) {
        List<UserListModel> leadsDetails = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMER_INFO + " where " + USER_ID + "='" + user_id + "'";


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                UserListModel detail = new UserListModel();
                detail.setUserid(cursor.getString(cursor.getColumnIndex(CUSTOMER_ID)));
                detail.setTech_id(user_id);
                detail.setName(cursor.getString(cursor.getColumnIndex(NAME)));
                detail.setField_nfc_id(cursor.getString(cursor.getColumnIndex(NFC_ID)));
                detail.setAddress(cursor.getString(cursor.getColumnIndex(ADDRESS)));
                detail.setHardness(cursor.getString(cursor.getColumnIndex(HARDNESS)));
                detail.setTotalchlorine(cursor.getString(cursor.getColumnIndex(TOTALCHLORINE)));
                detail.setFreechlorine(cursor.getString(cursor.getColumnIndex(FREECHLORINE)));
                detail.setPh(cursor.getString(cursor.getColumnIndex(PH)));
                detail.setAlkalinity(cursor.getString(cursor.getColumnIndex(ALKALINITY)));
                detail.setConditioner(cursor.getString(cursor.getColumnIndex(CONDITIONER)));
                detail.setChlpowder(cursor.getString(cursor.getColumnIndex(CHLPOWDER)));
                detail.setChltablets(cursor.getString(cursor.getColumnIndex(CHLTABLETS)));
                detail.setSodiumchl(cursor.getString(cursor.getColumnIndex(SODIUMCHL)));
                detail.setPhminus(cursor.getString(cursor.getColumnIndex(PHMINUS)));
                detail.setPhplus(cursor.getString(cursor.getColumnIndex(PHPLUS)));
                detail.setChemicalconditioner(cursor.getString(cursor.getColumnIndex(CHEMICALCONDITIONER)));
                detail.setSuperblue(cursor.getString(cursor.getColumnIndex(SUPERBLUE)));
                detail.setMetalgone(cursor.getString(cursor.getColumnIndex(METALGONE)));
                detail.setField_id_no(cursor.getString(cursor.getColumnIndex(FIELD_ID)));
                detail.setMetalout(cursor.getString(cursor.getColumnIndex(METALOUT)));
                detail.setAlgaecide(cursor.getString(cursor.getColumnIndex(ALGQECIDE)));
                detail.setAlkaninitycont(cursor.getString(cursor.getColumnIndex(ALKANINITY_COUNT)));
                detail.setAcid(cursor.getString(cursor.getColumnIndex(ACID)));
                detail.setMiscnotes(cursor.getString(cursor.getColumnIndex(MIS_NOTE)));
                detail.setImage1(cursor.getString(cursor.getColumnIndex(IMAGE1)));
                detail.setImage2(cursor.getString(cursor.getColumnIndex(IMAGE2)));
                detail.setImage3(cursor.getString(cursor.getColumnIndex(IMAGE3)));
                detail.setImage4(cursor.getString(cursor.getColumnIndex(IMAGE4)));
                detail.setTimestamp(cursor.getString(cursor.getColumnIndex(TIMESTAMP)));
                detail.setEndtime(cursor.getString(cursor.getColumnIndex(ENDTIME)));
                detail.setServer_image(cursor.getString(cursor.getColumnIndex(IMAGE1)));
                detail.setSync(cursor.getString(cursor.getColumnIndex(SYNC)));
                leadsDetails.add(detail);
            } while (cursor.moveToNext());
        }

        return leadsDetails;
    }

}