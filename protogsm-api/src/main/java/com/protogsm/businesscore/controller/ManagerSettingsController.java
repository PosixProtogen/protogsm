package com.protogsm.businesscore.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.protogsm.architecture.controller.CrudController;
import com.protogsm.businesscore.dto.ManagerSettingsDto;
import com.protogsm.businesscore.model.ManagerSettings;
import com.protogsm.businesscore.service.ManagerSettingsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/manager-settings")
@RequiredArgsConstructor
public class ManagerSettingsController extends CrudController<ManagerSettings, ManagerSettingsDto, ManagerSettingsService> {

}
