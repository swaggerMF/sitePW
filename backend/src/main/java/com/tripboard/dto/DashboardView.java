package com.tripboard.dto;
import java.math.BigDecimal;import java.util.List;
public record DashboardView(BigDecimal totalExpenses,BigDecimal currentBalance,long activityCount,long openIdeas,List<ActivityDtos.View> upcoming){}
