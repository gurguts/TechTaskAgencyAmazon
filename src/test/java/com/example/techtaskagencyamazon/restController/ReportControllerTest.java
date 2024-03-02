package com.example.techtaskagencyamazon.restController;

import com.example.techtaskagencyamazon.interfaces.AsinValidatorInterface;
import com.example.techtaskagencyamazon.interfaces.ReportServiceInterface;
import com.example.techtaskagencyamazon.models.report.salesAndTrafficByAsin.SalesAndTrafficByAsin;
import com.example.techtaskagencyamazon.models.report.salesAndTrafficByDate.SalesAndTrafficByDate;
import com.example.techtaskagencyamazon.restControllers.ReportController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ReportControllerTest {

    @Autowired
    private ReportController reportController;

    @MockBean
    private ReportServiceInterface reportService;

    @MockBean
    private AsinValidatorInterface asinValidator;

    @Test
    public void testFindByDate() {
        LocalDate testDate = LocalDate.of(2022, 1, 1);
        SalesAndTrafficByDate expectedReport = new SalesAndTrafficByDate();
        when(reportService.findByDate(testDate)).thenReturn(expectedReport);

        SalesAndTrafficByDate actualReport = reportController.findByDate(testDate);

        verify(reportService, times(1)).findByDate(testDate);
        assertEquals(expectedReport, actualReport);
    }

    @Test
    public void testFindByDateBetween() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(1);
        SalesAndTrafficByDate report1 = new SalesAndTrafficByDate();
        SalesAndTrafficByDate report2 = new SalesAndTrafficByDate();
        List<SalesAndTrafficByDate> expectedReports = Arrays.asList(report1, report2);
        when(reportService.findByDateBetween(startDate, endDate)).thenReturn(expectedReports);

        List<SalesAndTrafficByDate> actualReports = reportController.findByDateBetween(startDate, endDate);

        verify(reportService, times(1)).findByDateBetween(startDate, endDate);
        assertEquals(expectedReports, actualReports);
    }

    @Test
    public void testFindByAsin() {
        String testAsin = "B08L5WGQGJ";
        SalesAndTrafficByAsin expectedReport = new SalesAndTrafficByAsin();
        when(reportService.findByAsin(testAsin)).thenReturn(expectedReport);

        SalesAndTrafficByAsin actualReport = reportController.findByAsin(testAsin);

        verify(asinValidator, times(1)).validate(testAsin);
        verify(reportService, times(1)).findByAsin(testAsin);
        assertEquals(expectedReport, actualReport);
    }

    @Test
    public void testFindByAsinIn() {
        List<String> testAsins = Arrays.asList("B08L5WGQGJ", "B07XJ8C8F7");
        SalesAndTrafficByAsin report1 = new SalesAndTrafficByAsin();
        SalesAndTrafficByAsin report2 = new SalesAndTrafficByAsin();
        List<SalesAndTrafficByAsin> expectedReports = Arrays.asList(report1, report2);
        when(reportService.findByAsinIn(testAsins)).thenReturn(expectedReports);

        List<SalesAndTrafficByAsin> actualReports = reportController.findByAsinIn(testAsins);

        verify(asinValidator, times(1)).validate(testAsins);
        verify(reportService, times(1)).findByAsinIn(testAsins);
        assertEquals(expectedReports, actualReports);
    }

    @Test
    public void testFindAllBySalesAndTrafficByDate() {
        SalesAndTrafficByDate report1 = new SalesAndTrafficByDate();
        SalesAndTrafficByDate report2 = new SalesAndTrafficByDate();
        List<SalesAndTrafficByDate> expectedReports = Arrays.asList(report1, report2);
        when(reportService.findAllBySalesAndTrafficByDate()).thenReturn(expectedReports);

        List<SalesAndTrafficByDate> actualReports = reportController.findAllBySalesAndTrafficByDate();

        verify(reportService, times(1)).findAllBySalesAndTrafficByDate();
        assertEquals(expectedReports, actualReports);
    }

    @Test
    public void testFindAllBySalesAndTrafficByAsin() {
        SalesAndTrafficByAsin report1 = new SalesAndTrafficByAsin();
        SalesAndTrafficByAsin report2 = new SalesAndTrafficByAsin();
        List<SalesAndTrafficByAsin> expectedReports = Arrays.asList(report1, report2);
        when(reportService.findAllBySalesAndTrafficByAsin()).thenReturn(expectedReports);

        List<SalesAndTrafficByAsin> actualReports = reportController.findAllBySalesAndTrafficByAsin();

        verify(reportService, times(1)).findAllBySalesAndTrafficByAsin();
        assertEquals(expectedReports, actualReports);
    }
}
