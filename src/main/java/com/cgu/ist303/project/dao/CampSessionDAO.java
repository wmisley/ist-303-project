package com.cgu.ist303.project.dao;

import com.cgu.ist303.project.dao.model.CampSession;

import java.util.List;

/**
 * Created by will4769 on 9/29/16.
 */
public interface CampSessionDAO {
    int insert(CampSession campSession) throws Exception;
    List<CampSession> query(int year) throws Exception;
}
