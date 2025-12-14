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

    /**
     * Returns the unique ID of the mission.
     * @return the mission ID
     */
    public Integer getMissionId() {
        return missionId;
    }

    /**
     * Sets the unique ID of the mission.
     * @param missionId the mission ID to set
     */
    public void setMissionId(Integer missionId) {
        this.missionId = missionId;
    }

    /**
     * Returns the name of the spacecraft.
     * @return the spacecraft name
     */
    public String getSpacecraft() {
        return spacecraft;
    }

    /**
     * Sets the name of the spacecraft.
     * @param spacecraft the spacecraft name to set
     */
    public void setSpacecraft(String spacecraft) {
        this.spacecraft = spacecraft;
    }

    /**
     * Returns the launch date of the mission.
     * @return the launch date
     */
    public Date getLaunchDate() {
        return launchDate;
    }

    /**
     * Sets the launch date of the mission.
     * @param launchDate the launch date to set
     */
    public void setLaunchDate(Date launchDate) {
        this.launchDate = launchDate;
    }

    /**
     * Returns the carrier rocket used for the mission.
     * @return the carrier rocket name
     */
    public String getCarrierRocket() {
        return carrierRocket;
    }

    /**
     * Sets the carrier rocket used for the mission.
     * @param carrierRocket the carrier rocket name to set
     */
    public void setCarrierRocket(String carrierRocket) {
        this.carrierRocket = carrierRocket;
    }

    /**
     * Returns the operator of the mission.
     * @return the operator name
     */
    public String getOperator() {
        return operator;
    }

    /**
     * Sets the operator of the mission.
     * @param operator the operator name to set
     */
    public void setOperator(String operator) {
        this.operator = operator;
    }

    /**
     * Returns the type of the mission.
     * @return the mission type
     */
    public String getMissionType() {
        return missionType;
    }

    /**
     * Sets the type of the mission.
     * @param missionType the mission type to set
     */
    public void setMissionType(String missionType) {
        this.missionType = missionType;
    }

    /**
     * Returns the outcome of the mission.
     * @return the mission outcome
     */
    public String getOutcome() {
        return outcome;
    }

    /**
     * Sets the outcome of the mission.
     * @param outcome the mission outcome to set
     */
    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

}
