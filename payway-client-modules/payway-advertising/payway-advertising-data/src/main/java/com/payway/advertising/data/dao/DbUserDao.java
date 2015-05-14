/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.data.dao;

import com.payway.advertising.model.DbUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * DbUserDao
 *
 * @author Sergey Kichenko
 * @created 15.05.15 00:00
 */
public interface DbUserDao extends JpaRepository<DbUser, Long> {

    @Query(value = "select u from DbUser u where u.login = :login")
    DbUser findByLogin(@Param("login") String login);
}
