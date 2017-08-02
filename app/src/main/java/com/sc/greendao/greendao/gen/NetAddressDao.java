package com.sc.greendao.greendao.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.sc.entity.NetAddress;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "NET_ADDRESS".
*/
public class NetAddressDao extends AbstractDao<NetAddress, Long> {

    public static final String TABLENAME = "NET_ADDRESS";

    /**
     * Properties of entity NetAddress.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property NetName = new Property(1, String.class, "netName", false, "NET_NAME");
        public final static Property NetAddress = new Property(2, String.class, "netAddress", false, "NET_ADDRESS");
    };


    public NetAddressDao(DaoConfig config) {
        super(config);
    }
    
    public NetAddressDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"NET_ADDRESS\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"NET_NAME\" TEXT," + // 1: netName
                "\"NET_ADDRESS\" TEXT);"); // 2: netAddress
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"NET_ADDRESS\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, NetAddress entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String netName = entity.getNetName();
        if (netName != null) {
            stmt.bindString(2, netName);
        }
 
        String netAddress = entity.getNetAddress();
        if (netAddress != null) {
            stmt.bindString(3, netAddress);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, NetAddress entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String netName = entity.getNetName();
        if (netName != null) {
            stmt.bindString(2, netName);
        }
 
        String netAddress = entity.getNetAddress();
        if (netAddress != null) {
            stmt.bindString(3, netAddress);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public NetAddress readEntity(Cursor cursor, int offset) {
        NetAddress entity = new NetAddress( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // netName
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2) // netAddress
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, NetAddress entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setNetName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setNetAddress(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(NetAddress entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(NetAddress entity) {
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
