package com.cgu.ist303.project.dao;


import com.cgu.ist303.project.dao.model.BunkHouse;
import com.cgu.ist303.project.dao.model.BunkHouseAssignment;
import com.cgu.ist303.project.dao.model.BunkHouseAssignmentById;
import javafx.collections.ObservableList;

import java.util.List;

public interface BunkHouseAssignmentDAO {
    void insert(List<BunkHouseAssignmentById> assignments) throws Exception;
    void delete(List<BunkHouse> bhList) throws Exception;
    ObservableList<BunkHouseAssignment> query(int sessionId) throws Exception;
}