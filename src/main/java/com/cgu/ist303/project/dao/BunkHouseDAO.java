package com.cgu.ist303.project.dao;

import com.cgu.ist303.project.dao.model.BunkHouse;
import com.cgu.ist303.project.dao.model.CamperRegistration;
import javafx.collections.ObservableList;

/**
 * Created by will4769 on 11/2/16.
 */
public interface BunkHouseDAO {
    int insert(BunkHouse bh) throws Exception;
    ObservableList<BunkHouse> query(int campSessionId) throws Exception;
}
