package com.cgu.ist303.project.dao;


import com.cgu.ist303.project.dao.model.Camper;
import com.cgu.ist303.project.dao.model.CamperRegistration;
import com.cgu.ist303.project.dao.model.CamperRegistrationRecord;
import javafx.collections.ObservableList;

public interface CamperRegistrationDAO {
    void insert(CamperRegistrationRecord registration) throws Exception;
    int queryGenderCount(int year, Camper.Gender gender) throws Exception;
    boolean queryIsCamperRegisterdForYear(Camper camper, int year) throws Exception;
    ObservableList<CamperRegistration> queryRegisteredCampers(int year) throws Exception;
    ObservableList<CamperRegistration> queryRegisteredCampers(int year, int sessionId) throws Exception;
}
