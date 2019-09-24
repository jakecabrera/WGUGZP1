/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wgugzp1;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

/**
 *
 * @author jakes
 */
public abstract class Record {
    private Optional<Integer> id = Optional.empty();
    private String createdBy = "";
    private String lastUpdateBy = "";
    private ZonedDateTime lastUpdate;
    private ZonedDateTime createDate;

    /**
     * @return the createdBy
     */
    public String getCreatedBy() {
        try {
            if (createdBy.isEmpty()) createdBy = Database.getInstance().getLoggedInUser().getUserName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return createdBy;
    }

    /**
     * @param createdBy the createdBy to set
     */
    public void setCreatedBy(String createdBy) {
        if (createdBy.length() > 40) throw new IllegalArgumentException();
        this.createdBy = createdBy;
    }

    /**
     * @return the lastUpdatedBy
     */
    public String getLastUpdateBy() {
        try {
            if (lastUpdateBy.isEmpty()) lastUpdateBy = Database.getInstance().getLoggedInUser().getUserName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lastUpdateBy;
    }

    /**
     * @param lastUpdatedBy the lastUpdatedBy to set
     */
    public void setLastUpdateBy(String lastUpdateBy) {
        if (lastUpdateBy.length() > 40) throw new IllegalArgumentException();
        this.lastUpdateBy = lastUpdateBy;
    }

    /**
     * @return the lastUpdate
     */
    public ZonedDateTime getLastUpdate() {
        if (lastUpdate == null) lastUpdate = ZonedDateTime.now(ZoneId.of("GMT"));
        return lastUpdate;
    }

    /**
     * @param lastUpdate the lastUpdate to set
     */
    public void setLastUpdate(ZonedDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**
     * @return the createDate
     */
    public ZonedDateTime getCreateDate() {
        if (createDate == null) createDate = ZonedDateTime.now(ZoneId.of("GMT"));
        return createDate;
    }

    /**
     * @param createDate the createDate to set
     */
    public void setCreateDate(ZonedDateTime createDate) {
        this.createDate = createDate;
    }

    /**
     * @return the id
     */
    public Optional<Integer> getId() {
        return id;
    }
    
    public int getIdAsInt() {
        return id.orElseThrow(RuntimeException::new);
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = Optional.of(id);
    }
    
    public void setRecord(String createdBy, ZonedDateTime createDate, String lastUpdateBy, ZonedDateTime lastUpdate, int id) {
        setId(id);
        setRecord(createdBy, createDate, lastUpdateBy, lastUpdate);
    }
    
    public void setRecord(String createdBy, ZonedDateTime createDate, String lastUpdateBy, ZonedDateTime lastUpdate) {
        setCreatedBy(createdBy);
        setCreateDate(createDate);
        setLastUpdateBy(lastUpdateBy);
        setLastUpdate(lastUpdate);
    }
    
}
