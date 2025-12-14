package com.example;

import java.sql.Date;

/**
 * Represents a moon mission with details about the spacecraft and mission parameters.
 */
public class MoonMission {

    private Integer missionId;
    private String spacecraft;
    private Date launchDate;
    private String carrierRocket;
    private String operator;
    private String missionType;
    private String outcome;

    /**
     * Default constructor.
     */
    public MoonMission() {}


    /**
     * Creates a new MoonMission with all fields.
     *
     * @param missionId the unique mission identifier
     * @param spacecraft the name of the spacecraft
     * @param launchDate the date the mission was launched
     * @param carrierRocket the rocket used to launch the spacecraft
     * @param operator the organization operating the mission
     * @param missionType the type of mission
     * @param outcome the outcome of the mission
     */
    public MoonMission(Integer missionId, String spacecraft, Date launchDate, String carrierRocket, String operator, String missionType, String outcome) {
        this.missionId = missionId;
        this.spacecraft = spacecraft;
        this.launchDate = launchDate;
        this.carrierRocket = carrierRocket;
        this.operator = operator;
        this.missionType = missionType;
        this.outcome = outcome;
    }

    public Integer getMissionId() {
        return missionId;
    }

    public void setMissionId(Integer missionId) {
        this.missionId = missionId;
    }

    public String getSpacecraft() {
        return spacecraft;
    }

    public void setSpacecraft(String spacecraft) {
        this.spacecraft = spacecraft;
    }

    public Date getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(Date launchDate) {
        this.launchDate = launchDate;
    }

    public String getCarrierRocket() {
        return carrierRocket;
    }

    public void setCarrierRocket(String carrierRocket) {
        this.carrierRocket = carrierRocket;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getMissionType() {
        return missionType;
    }

    public void setMissionType(String missionType) {
        this.missionType = missionType;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }
}
