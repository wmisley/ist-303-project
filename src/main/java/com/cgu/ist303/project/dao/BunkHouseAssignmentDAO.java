package com.cgu.ist303.project.dao;


import com.cgu.ist303.project.dao.model.BunkHouseAssignment;

import java.util.List;

public interface BunkHouseAssignmentDAO {
    void insert(List<BunkHouseAssignment> assignments) throws Exception;
}
