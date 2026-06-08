package com.dyx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dyx.pojo.Appointment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AppointmentMapper extends BaseMapper<Appointment> {
}