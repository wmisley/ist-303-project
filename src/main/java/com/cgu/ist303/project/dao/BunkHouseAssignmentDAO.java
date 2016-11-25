package com.cgu.ist303.project.dao;


import com.cgu.ist303.project.dao.model.BunkHouse;
import com.cgu.ist303.project.dao.model.BunkHouseAssignment;
import com.cgu.ist303.project.dao.model.BunkHouseAssignmentById;
import javafx.collections.ObservableList;

import java.util.List;

public interface BunkHouseAssignmentDAO {
    void insert(List<BunkHouseAssignmentById> assignments) throws Exception;
    void delete(List<BunkHouse> bhList) throws Exception;
    void swap(int camperId1, int bunkHouseId1, int camperId2, int bunkHouseId2) throws Exception;
    ObservableList<BunkHouseAssignment> query(int sessionId) throws Exception;
}