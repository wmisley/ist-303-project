package com.cgu.ist303.project.dao;

import com.cgu.ist303.project.dao.model.CampSession;

import java.util.List;

public interface CampSessionDAO {
    int insert(CampSession campSession) throws Exception;
    List<CampSession> query(int year) throws Exception;
    void update(CampSession cs) throws Exception;
    void delete(int sessionId) throws Exception;
}
