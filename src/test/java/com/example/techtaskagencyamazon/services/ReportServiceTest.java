package com.example.techtaskagencyamazon.services;

import com.example.techtaskagencyamazon.exception.ReportException;
import com.example.techtaskagencyamazon.models.report.Report;
import com.example.techtaskagencyamazon.models.report.salesAndTrafficByAsin.SalesAndTrafficByAsin;
import com.example.techtaskagencyamazon.models.report.salesAndTrafficByDate.SalesAndTrafficByDate;
import com.example.techtaskagencyamazon.repositories.ReportRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import org.springframework.dao.DataAccessException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ReportServiceTest {
    private final ReportRepository reportRepository = Mockito.mock(ReportRepository.class);
    private final ReportService reportService = new ReportService(reportRepository);

    @Test
    public void testFindByDate() {
        LocalDate date = LocalDate.now();
        SalesAndTrafficByDate salesAndTrafficByDate = new SalesAndTrafficByDate();
        Report report = new Report();
        report.setSalesAndTrafficByDate(Collections.singletonList(salesAndTrafficByDate));

        Mockito.when(reportRepository.findBySalesAndTrafficByDate_Date(date)).thenReturn(Collections.singletonList(report));

        Assertions.assertEquals(salesAndTrafficByDate, reportService.findByDate(date));
    }

    @Test
    public void testFindByDateBetween() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(1);
        SalesAndTrafficByDate salesAndTrafficByDate1 = new SalesAndTrafficByDate();
        salesAndTrafficByDate1.setDate(startDate);
        SalesAndTrafficByDate salesAndTrafficByDate2 = new SalesAndTrafficByDate();
        salesAndTrafficByDate2.setDate(endDate);
        Report report = new Report();
        report.setSalesAndTrafficByDate(Arrays.asList(salesAndTrafficByDate1, salesAndTrafficByDate2));

        Mockito.when(reportRepository.findAllBySalesAndTrafficByDate()).thenReturn(Collections.singletonList(report));

        List<SalesAndTrafficByDate> result = reportService.findByDateBetween(startDate, endDate);

        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.contains(salesAndTrafficByDate1));
        Assertions.assertTrue(result.contains(salesAndTrafficByDate2));
    }

    @Test
    public void testFindByAsin() {
        String asin = "testAsin";
        SalesAndTrafficByAsin salesAndTrafficByAsin = new SalesAndTrafficByAsin();
        Report report = new Report();
        report.setSalesAndTrafficByAsin(Collections.singletonList(salesAndTrafficByAsin));

        Mockito.when(reportRepository.findBySalesAndTrafficByAsin_Asin(asin)).thenReturn(Collections.singletonList(report));

        Assertions.assertEquals(salesAndTrafficByAsin, reportService.findByAsin(asin));
    }

    @Test
    public void testFindByAsinIn() {
        List<String> asins = Arrays.asList("testAsin1", "testAsin2");
        SalesAndTrafficByAsin salesAndTrafficByAsin1 = new SalesAndTrafficByAsin();
        salesAndTrafficByAsin1.setParentAsin("testAsin1");
        SalesAndTrafficByAsin salesAndTrafficByAsin2 = new SalesAndTrafficByAsin();
        salesAndTrafficByAsin2.setParentAsin("testAsin2");
        Report report = new Report();
        report.setSalesAndTrafficByAsin(Arrays.asList(salesAndTrafficByAsin1, salesAndTrafficByAsin2));

        Mockito.when(reportRepository.findAllBySalesAndTrafficByAsin()).thenReturn(Collections.singletonList(report));

        List<SalesAndTrafficByAsin> result = reportService.findByAsinIn(asins);

        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.contains(salesAndTrafficByAsin1));
        Assertions.assertTrue(result.contains(salesAndTrafficByAsin2));
    }

    @Test
    public void testFindAllBySalesAndTrafficByDate() {
        SalesAndTrafficByDate salesAndTrafficByDate = new SalesAndTrafficByDate();
        Report report = new Report();
        report.setSalesAndTrafficByDate(Collections.singletonList(salesAndTrafficByDate));

        Mockito.when(reportRepository.findAllBySalesAndTrafficByDate()).thenReturn(Collections.singletonList(report));

        Assertions.assertEquals(Collections.singletonList(salesAndTrafficByDate), reportService.findAllBySalesAndTrafficByDate());
    }

    @Test
    public void testFindAllBySalesAndTrafficByAsin() {
        SalesAndTrafficByAsin salesAndTrafficByAsin = new SalesAndTrafficByAsin();
        Report report = new Report();
        report.setSalesAndTrafficByAsin(Collections.singletonList(salesAndTrafficByAsin));

        Mockito.when(reportRepository.findAllBySalesAndTrafficByAsin()).thenReturn(Collections.singletonList(report));

        Assertions.assertEquals(Collections.singletonList(salesAndTrafficByAsin), reportService.findAllBySalesAndTrafficByAsin());
    }

    @Test
    public void testFindByDateFailure() {
        LocalDate date = LocalDate.now();

        Mockito.when(reportRepository.findBySalesAndTrafficByDate_Date(date)).thenReturn(Collections.emptyList());

        Assertions.assertThrows(ReportException.class, () -> reportService.findByDate(date));
    }

    @Test
    public void testFindByAsinFailure() {
        String asin = "testAsin";

        Mockito.when(reportRepository.findBySalesAndTrafficByAsin_Asin(asin)).thenReturn(Collections.emptyList());

        Assertions.assertThrows(ReportException.class, () -> reportService.findByAsin(asin));
    }

    @Test
    public void testFindAllBySalesAndTrafficByDateFailure() {
        Mockito.when(reportRepository.findAllBySalesAndTrafficByDate()).thenThrow(new DataAccessException("Test exception") {
        });

        Assertions.assertThrows(ReportException.class, reportService::findAllBySalesAndTrafficByDate);
    }

    @Test
    public void testFindAllBySalesAndTrafficByAsinFailure() {
        Mockito.when(reportRepository.findAllBySalesAndTrafficByAsin()).thenThrow(new DataAccessException("Test exception") {
        });

        Assertions.assertThrows(ReportException.class, reportService::findAllBySalesAndTrafficByAsin);
    }

    @Test
    public void testFindByDateBetweenFailure() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(1);

        Mockito.when(reportRepository.findAllBySalesAndTrafficByDate()).thenThrow(new DataAccessException("Test exception") {
        });

        Assertions.assertThrows(ReportException.class, () -> reportService.findByDateBetween(startDate, endDate));
    }

    @Test
    public void testFindByAsinInFailure() {
        List<String> asins = Collections.singletonList("testAsin");

        Mockito.when(reportRepository.findAllBySalesAndTrafficByAsin()).thenThrow(new DataAccessException("Test exception") {
        });

        Assertions.assertThrows(ReportException.class, () -> reportService.findByAsinIn(asins));
    }
}
