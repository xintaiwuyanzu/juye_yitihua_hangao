package com.dr.archive.fuzhou.portal.controller;

import com.dr.archive.fuzhou.portal.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/appointment")
public class AppointmentController {


    @Autowired
    AppointmentService appointmentService;

   /* @RequestMapping("/addConsult")
    public ResultEntity addConsult(ConsultApply consultApply, String targetPersonId){
        appointmentService.addConsult(consultApply,targetPersonId);
        return ResultEntity.success();
    }*/
}
