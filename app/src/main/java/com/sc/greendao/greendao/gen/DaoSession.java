package com.sc.greendao.greendao.gen;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.sc.entity.NetAddress;
import com.sc.entity.NEWS;

import com.sc.greendao.greendao.gen.NetAddressDao;
import com.sc.greendao.greendao.gen.NEWSDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig netAddressDaoConfig;
    private final DaoConfig nEWSDaoConfig;

    private final NetAddressDao netAddressDao;
    private final NEWSDao nEWSDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        netAddressDaoConfig = daoConfigMap.get(NetAddressDao.class).clone();
        netAddressDaoConfig.initIdentityScope(type);

        nEWSDaoConfig = daoConfigMap.get(NEWSDao.class).clone();
        nEWSDaoConfig.initIdentityScope(type);

        netAddressDao = new NetAddressDao(netAddressDaoConfig, this);
        nEWSDao = new NEWSDao(nEWSDaoConfig, this);

        registerDao(NetAddress.class, netAddressDao);
        registerDao(NEWS.class, nEWSDao);
    }
    
    public void clear() {
        netAddressDaoConfig.getIdentityScope().clear();
        nEWSDaoConfig.getIdentityScope().clear();
    }

    public NetAddressDao getNetAddressDao() {
        return netAddressDao;
    }

    public NEWSDao getNEWSDao() {
        return nEWSDao;
    }

}
