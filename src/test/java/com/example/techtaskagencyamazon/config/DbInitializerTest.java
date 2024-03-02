package com.example.techtaskagencyamazon.config;

import com.example.techtaskagencyamazon.configurations.DbInitializer;
import com.example.techtaskagencyamazon.interfaces.ReportUpdateServiceInterface;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class DbInitializerTest {
    private final ReportUpdateServiceInterface reportUpdateService = Mockito.mock(ReportUpdateServiceInterface.class);
    private final DbInitializer dbInitializer = new DbInitializer(reportUpdateService);

    @Test
    public void testRun() {
        dbInitializer.run();
        Mockito.verify(reportUpdateService, Mockito.times(1)).loadAndSaveReport();
    }
}

