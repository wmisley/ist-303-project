package com.cgu.ist303.project.dao;

import com.cgu.ist303.project.dao.model.TribeAssignment;

import java.util.List;

/**
 * Created by will4769 on 11/2/16.
 */
public interface TribeAssignmentDAO {
    void insert(int camperId, int tribeId) throws Exception;
    List<TribeAssignment> query(int sessionId) throws Exception;
}
