package com.cgu.ist303.project.dao;

import com.cgu.ist303.project.dao.model.Payment;

public interface PaymentDAO {
    void insert(Payment payment) throws Exception;
}
