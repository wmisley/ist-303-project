package com.cgu.ist303.project.dao;


import com.cgu.ist303.project.dao.model.Camper;
import com.cgu.ist303.project.dao.model.CamperRegistration;

public interface CamperRegistrationDAO {
    void insert(CamperRegistration registration) throws Exception;
    int queryGenderCount(int year, Camper.Gender gender) throws Exception;
    boolean queryIsCamperRegisterdForYear(Camper camper, int year) throws Exception;
}
