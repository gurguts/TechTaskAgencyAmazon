package com.example.techtaskagencyamazon.configurations;

import com.example.techtaskagencyamazon.interfaces.ReportUpdateServiceInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Component that initializes the database at application startup.
 */
@Component
public class DbInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DbInitializer.class);
    private final ReportUpdateServiceInterface reportUpdateService;

    public DbInitializer(ReportUpdateServiceInterface reportUpdateService) {
        this.reportUpdateService = reportUpdateService;
    }

    /**
     * Method that runs at application startup. It loads and saves a report to the database.
     *
     * @param args command line arguments
     */
    @Override
    public void run(String... args) {
        reportUpdateService.loadAndSaveReport();
        logger.info("Database initialization completed.");
    }
}
