package com.cgu.ist303.project.dao;

import com.cgu.ist303.project.dao.model.Tribe;
import javafx.collections.ObservableList;

public interface TribeDAO {
    int insert(Tribe tribe) throws Exception;
    ObservableList<Tribe> query(int campSessionId) throws Exception;
    int update(Tribe tribe) throws Exception;
    int delete(Tribe tribe) throws Exception;
}
