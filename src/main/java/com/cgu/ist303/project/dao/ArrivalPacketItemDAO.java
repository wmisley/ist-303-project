package com.cgu.ist303.project.dao;

import com.cgu.ist303.project.dao.model.ArrivalPacketItem;

import java.util.List;

/**
 * Created by will4769 on 10/19/16.
 */
public interface ArrivalPacketItemDAO {
    List<ArrivalPacketItem> query(int year) throws Exception;
}
