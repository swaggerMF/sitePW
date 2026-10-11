package com.tripboard.controller;
import com.tripboard.dto.DashboardView;import com.tripboard.service.DashboardService;import lombok.RequiredArgsConstructor;import org.springframework.web.bind.annotation.*;
@RestController @RequiredArgsConstructor public class DashboardController {
 private final DashboardService dashboard;
 @GetMapping("/api/trips/{tripId}/dashboard") public DashboardView get(@PathVariable Long tripId){return dashboard.get(tripId);}
}
