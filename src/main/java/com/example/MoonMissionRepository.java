package com.example;

import java.util.List;

/**
 * Repository interface for MoonMission operations.
 */
public interface MoonMissionRepository {

    /**
     * List all spacecraft names.
     */
    List<String> listAllSpacecrafts();

    /**
     * Get a mission by its ID.
     * @return MoonMission object or null if not found
     */
    MoonMission getMissionById(int missionId);

    /**
     * Count missions launched in a given year.
     */
    int countMissionsByYear(int year);
}