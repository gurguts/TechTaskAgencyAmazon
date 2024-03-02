package com.example.techtaskagencyamazon.services;

import com.example.techtaskagencyamazon.models.report.Report;
import com.example.techtaskagencyamazon.repositories.ReportRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.dao.DataAccessException;

import java.util.Collections;

public class ReportUpdateServiceTest {
    private final ReportRepository reportRepository = Mockito.mock(ReportRepository.class);
    private final CacheManager cacheManager = Mockito.mock(CacheManager.class);
    private final Cache cache = Mockito.mock(Cache.class);
    private final ReportUpdateService reportUpdateService = new ReportUpdateService(reportRepository, cacheManager);

    @Test
    public void testLoadAndSaveReport() {
        Report report = new Report();
        Mockito.when(reportRepository.save(report)).thenReturn(report);

        Assertions.assertDoesNotThrow(reportUpdateService::loadAndSaveReport);
    }

    @Test
    public void testLoadAndSaveReportFailureOnDeleteAll() {
        Mockito.doThrow(new DataAccessException("Test exception") {
        }).when(reportRepository).deleteAll();

        Assertions.assertThrows(DataAccessException.class, reportUpdateService::loadAndSaveReport);
    }

    @Test
    public void testClearAllCaches() {
        Mockito.when(cacheManager.getCacheNames()).thenReturn(Collections.singletonList("testCache"));
        Mockito.when(cacheManager.getCache("testCache")).thenReturn(cache);

        Assertions.assertDoesNotThrow(reportUpdateService::clearAllCaches);
    }
}
