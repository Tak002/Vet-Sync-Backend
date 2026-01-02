package com.vetsync.backend.controller;

import com.vetsync.backend.service.ManagementChartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ManagementChartController {
    private final ManagementChartService managementChartService;

}
