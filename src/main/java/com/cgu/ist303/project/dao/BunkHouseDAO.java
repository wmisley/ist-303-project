package com.cgu.ist303.project.dao;

import com.cgu.ist303.project.dao.model.BunkHouse;
import com.cgu.ist303.project.dao.model.CamperRegistration;
import javafx.collections.ObservableList;

public interface BunkHouseDAO {
    int insert(BunkHouse bh) throws Exception;
    ObservableList<BunkHouse> query(int campSessionId) throws Exception;
    ObservableList<BunkHouse> query(int campSessionId, BunkHouse.Gender genderFilter) throws Exception;
}
