package com.vetsync.backend.contorller;

import com.vetsync.backend.service.ManagementChartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ManagementChartController {
    private final ManagementChartService managementChartService;

    @GetMapping("/management-charts")
    public String getManagementCharts() {
        return "Management Charts Data";
    }
}
