package com.example.techtaskagencyamazon.services;

import com.example.techtaskagencyamazon.interfaces.ReportUpdateServiceInterface;
import com.example.techtaskagencyamazon.models.report.Report;
import com.example.techtaskagencyamazon.repositories.ReportRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * Service for updating reports.
 */
@Service
public class ReportUpdateService implements ReportUpdateServiceInterface {

    private static final Logger logger = LoggerFactory.getLogger(ReportUpdateService.class);
    /**
     * The report file to update.
     */
    private final File reportFile = new File("src/main/resources/test_report.json");
    /**
     * Update time in milliseconds.
     */
    private final int UPDATE_TIME = 60000;
    private long lastModified = reportFile.lastModified();

    private final ReportRepository reportRepository;
    private final CacheManager cacheManager;

    public ReportUpdateService(ReportRepository reportRepository, CacheManager cacheManager) {
        this.reportRepository = reportRepository;
        this.cacheManager = cacheManager;
    }

    /**
     * Scheduled task to update reports.
     */
    @Scheduled(fixedRate = UPDATE_TIME)
    public void updateReports() {

        logger.info("Running scheduled task: updateReports");

        long currentModified = reportFile.lastModified();

        logger.info("Current modified time: " + currentModified);
        logger.info("Last modified time: " + lastModified);

        if (currentModified > lastModified) {

            logger.info("Detected changes in the report file. Updating reports...");

            loadAndSaveReport();
            clearAllCaches();
            lastModified = currentModified;
            logger.info("Reports updated successfully.");
        } else {
            logger.info("No changes detected in the report file. No update needed.");
        }
    }

    /**
     * Method for loading and saving the report.
     */
    public void loadAndSaveReport() {
        try {
            logger.info("Deleting all reports from the database...");
            reportRepository.deleteAll();
            logger.info("Reading test_report.json...");
            File reportFile = new File("src/main/resources/test_report.json");
            FileInputStream fis = new FileInputStream(reportFile);
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            logger.info("Deserializing JSON to Report object...");
            Report report = mapper.readValue(fis, Report.class);
            logger.info("Saving report to the database...");
            reportRepository.save(report);
        } catch (IOException exception) {
            logger.error("Error occurred while processing the report", exception);
        }
    }

    /**
     * Method to clear all caches.
     */
    public void clearAllCaches() {
        for (String name : cacheManager.getCacheNames()) {
            Objects.requireNonNull(cacheManager.getCache(name)).clear();
        }
        logger.info("All caches cleared.");
    }
}
