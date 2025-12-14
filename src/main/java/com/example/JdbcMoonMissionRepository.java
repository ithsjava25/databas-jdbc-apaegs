package com.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of MoonMissionRepository.
 * Handles all database operations related to moon missions.
 */
public class JdbcMoonMissionRepository implements MoonMissionRepository {

    private final SimpleDataSource dataSource;

    /**
     * Creates a new JdbcMoonMissionRepository.
     *
     * @param dataSource the data source for database connections
     */
    public JdbcMoonMissionRepository(SimpleDataSource dataSource) {
        this.dataSource = java.util.Objects.requireNonNull(dataSource, "dataSource");
    }

    @Override
    public List<String> listAllSpacecrafts() {
        List<String> spacecrafts = new ArrayList<>();
        String sql = "SELECT spacecraft FROM moon_mission";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                spacecrafts.add(rs.getString("spacecraft"));
            }

            return spacecrafts;

        } catch (SQLException e) {
            throw new RuntimeException("Error listing missions", e);
        }
    }

    @Override
    public MoonMission getMissionById(int missionId) {
        String sql = "SELECT * FROM moon_mission WHERE mission_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, missionId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    MoonMission mission = new MoonMission();
                    mission.setMissionId(rs.getInt("mission_id"));
                    mission.setSpacecraft(rs.getString("spacecraft"));
                    mission.setLaunchDate(rs.getDate("launch_date"));
                    mission.setCarrierRocket(rs.getString("carrier_rocket"));
                    mission.setOperator(rs.getString("operator"));
                    mission.setMissionType(rs.getString("mission_type"));
                    mission.setOutcome(rs.getString("outcome"));
                    return mission;
                } else {
                    return null;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error getting mission", e);
        }
    }

    @Override
    public int countMissionsByYear(int year) {
        String sql = "SELECT COUNT(*) AS count FROM moon_mission WHERE YEAR(launch_date) = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, year);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count");
                } else {
                    return 0;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error counting missions", e);
        }
    }
}