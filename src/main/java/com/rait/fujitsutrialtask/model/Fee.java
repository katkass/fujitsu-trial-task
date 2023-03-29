package com.rait.fujitsutrialtask.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;

/**
 * Plain-old Java object for total fee storage. Could be rolled into the weather object,
 * but perhaps more complex functionality might some day be needed.
 */
@Entity
@Table
public class Fee {

    /**
     * Data fields.
     */
    private @Id @GeneratedValue Long id; //Primary key, auto-populated.
    private @Column double total; //The total fee.
    private @Column double vehicleType; //The type of vehicle, based on Pärnu rates.

    /**
     * Constructors for instantiation.
     */
    public Fee() {}
    public Fee(double total) {

        this.total = total;
    }

    /**
     * Getter for fee object ID.
     *
     * @return object ID
     */
    public Long getId() {
        return id;
    }
    /**
     * Setter for fee object ID.
     *
     * @param id ID code
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Getter for fee object total.
     *
     * @return total fee
     */
    public double getTotal() {
        return total;
    }
    /**
     * Setter for fee object total.
     *
     * @param total total fee
     */
    public void setTotal(double total) {
        this.total = total;
    }

    /**
     * Getter for fee object vehicle type.
     *
     * @return vehicle type
     */
    public double getVehicleType() {

        return vehicleType;
    }
    /**
     * Setter for fee object vehicle type.
     *
     * @param vehicleType vehicle type
     */
    public void setVehicleType(double vehicleType) {
        this.vehicleType = vehicleType;
    }

    /**
     * Returns the fee total in a nicely formatted string.
     *
     * @return the fee information
     */
    @Override
    public String toString(){
        return "Total fee: " + this.total + "€";
    }
}
