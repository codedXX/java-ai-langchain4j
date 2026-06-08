package com.dyx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dyx.pojo.Appointment;

public interface AppointmentService extends IService<Appointment> {
    Appointment getOne(Appointment appointment);
}