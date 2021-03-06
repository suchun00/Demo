package com.sc.greendao.greendao.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.sc.entity.RecordNum;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "RECORD_NUM".
*/
public class RecordNumDao extends AbstractDao<RecordNum, Long> {

    public static final String TABLENAME = "RECORD_NUM";

    /**
     * Properties of entity RecordNum.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Staff = new Property(1, String.class, "staff", false, "STAFF");
        public final static Property Partloc = new Property(2, String.class, "partloc", false, "PARTLOC");
        public final static Property Result = new Property(3, String.class, "result", false, "RESULT");
    };


    public RecordNumDao(DaoConfig config) {
        super(config);
    }
    
    public RecordNumDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"RECORD_NUM\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"STAFF\" TEXT," + // 1: staff
                "\"PARTLOC\" TEXT," + // 2: partloc
                "\"RESULT\" TEXT);"); // 3: result
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"RECORD_NUM\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, RecordNum entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String staff = entity.getStaff();
        if (staff != null) {
            stmt.bindString(2, staff);
        }
 
        String partloc = entity.getPartloc();
        if (partloc != null) {
            stmt.bindString(3, partloc);
        }
 
        String result = entity.getResult();
        if (result != null) {
            stmt.bindString(4, result);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, RecordNum entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String staff = entity.getStaff();
        if (staff != null) {
            stmt.bindString(2, staff);
        }
 
        String partloc = entity.getPartloc();
        if (partloc != null) {
            stmt.bindString(3, partloc);
        }
 
        String result = entity.getResult();
        if (result != null) {
            stmt.bindString(4, result);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public RecordNum readEntity(Cursor cursor, int offset) {
        RecordNum entity = new RecordNum( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // staff
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // partloc
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // result
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, RecordNum entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setStaff(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setPartloc(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setResult(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(RecordNum entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(RecordNum entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
