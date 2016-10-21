package com.cgu.ist303.project.dao;

import com.cgu.ist303.project.dao.model.Equipment;

import java.util.List;

/**
 * Created by will4769 on 10/19/16.
 */
public interface EquipmentDAO {
    public List<Equipment> query(int year) throws Exception;
}
