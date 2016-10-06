package com.cgu.ist303.project.dao;

import com.cgu.ist303.project.dao.model.RejectedApplication;

public interface RejectedApplicationsDAO {
    void insert(RejectedApplication app) throws Exception;
}
