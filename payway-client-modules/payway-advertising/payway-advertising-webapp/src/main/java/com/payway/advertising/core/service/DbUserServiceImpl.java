/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service;

import com.payway.advertising.core.service.exception.ServiceException;
import com.payway.advertising.data.dao.DbUserDao;
import com.payway.advertising.model.DbUser;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * DbUserServiceImpl
 *
 * @author Sergey Kichenko
 * @created 13.05.15 00:00
 */
@Slf4j
@Service(value = "dbUserService")
public class DbUserServiceImpl implements DbUserService {

    @Autowired
    private DbUserDao dbUserDao;

    @Override
    @Transactional
    public DbUser findUserByLogin(String login, boolean isCreate) throws ServiceException {
        DbUser user = dbUserDao.findByLogin(login);
        if (user == null && isCreate) {
            user = new DbUser(login, null, null);
            dbUserDao.save(user);
        }
        return user;
    }

    @Override
    public DbUser save(DbUser entity) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(DbUser entity) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DbUser getById(Long id) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DbUser> list() throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
