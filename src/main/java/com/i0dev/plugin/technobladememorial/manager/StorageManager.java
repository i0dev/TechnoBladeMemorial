package com.i0dev.plugin.technobladememorial.manager;

import com.i0dev.plugin.technobladememorial.TechnoBladeMemorial;
import com.i0dev.plugin.technobladememorial.object.FinalDonation;
import com.i0dev.plugin.technobladememorial.object.QueuedDonation;
import com.i0dev.plugin.technobladememorial.template.AbstractManager;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Location;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.UUID;

/**
 * Storage manager, using SQLite as main storage system.
 *
 * @author Andrew Magnuson
 */
public class StorageManager extends AbstractManager {

    @Getter
    private static final StorageManager instance = new StorageManager();

    private Connection connection;

    @SneakyThrows
    @Override
    public void initialize() {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:" + TechnoBladeMemorial.getPlugin().getDataFolder() + "/storage.db");
        System.out.println("Connected to SQLite Database");
        createTables();
    }

    @SneakyThrows
    @Override
    public void deinitialize() {
        if (connection != null) connection.close();
    }

    /**
     * This method will create the necessary tables in order for the storage system to work properly.
     */
    @SneakyThrows
    public void createTables() {
        connection.prepareStatement("" +
                "CREATE TABLE IF NOT EXISTS `queued_donations` (" +
                "`id`          INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL, " +
                "`donationUUID` VARCHAR(36)                          NOT NULL, " +
                "`tier`         VARCHAR(36)                          NOT NULL, " +
                "`amountUSD`    VARCHAR(36)                          NOT NULL, " +
                "`donorUUID`    VARCHAR(36)                          NOT NULL," +
                "`timestamp`    BIGINT                               NOT NULL" +
                ");"
        ).execute();

        connection.prepareStatement("" +
                "CREATE TABLE IF NOT EXISTS `final_donations` (" +
                "`id`          INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL, " +
                "`donationUUID` VARCHAR(36)                          NOT NULL, " +
                "`tier`         VARCHAR(36)                          NOT NULL, " +
                "`amountUSD`    VARCHAR(36)                          NOT NULL, " +
                "`donorUUID`    VARCHAR(36)                          NOT NULL," +
                "`message`      VARCHAR(512)                         NOT NULL," +
                "`timestamp`    BIGINT                               NOT NULL" +
                ");"
        ).execute();

        connection.prepareStatement("" +
                "CREATE TABLE IF NOT EXISTS `spawn_locations` (" +
                "`id`    INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL, " +
                "`world` INTEGER                               NOT NULL, " +
                "`x`     INTEGER                               NOT NULL, " +
                "`y`     INTEGER                               NOT NULL, " +
                "`z`     INTEGER                               NOT NULL " +
                ");"
        ).execute();
    }


    @SneakyThrows
    public void addSpawnLocation(Location location) {
        connection.prepareStatement(String.format(
                "INSERT INTO spawn_locations (world, x, y, z)" +
                        "VALUES ('%s', %s, %s, %s);",
                location.getWorld().getName(),
                location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ()
        )).execute();
    }

    @SneakyThrows
    public void addQueuedDonation(QueuedDonation queuedDonation) {
        connection.prepareStatement(String.format(
                "INSERT INTO queued_donations (tier, amountUSD, donationUUID, donorUUID, timestamp)" +
                        "VALUES ('%s', '%s', '%s', '%s', %s);",
                queuedDonation.getTier().toString(),
                queuedDonation.getAmountUSD(),
                queuedDonation.getDonationUUID(),
                queuedDonation.getDonorUUID(),
                queuedDonation.getTimestamp()
        )).execute();
    }

    @SneakyThrows
    public void removeQueuedDonation(UUID purchaseUUID) {
        connection.prepareStatement(String.format(
                "DELETE FROM queued_donations WHERE donationUUID = '%s'",
                purchaseUUID.toString()
        )).execute();
    }

    @SneakyThrows
    public void addFinalDonation(FinalDonation finalDonation) {
        connection.prepareStatement(String.format(
                "INSERT INTO final_donations (donationUUID, tier, amountUSD, donorUUID, message, timestamp)" +
                        "VALUES ('%s', '%s', '%s', '%s', '%s', %s);",
                finalDonation.getDonationUUID().toString(),
                finalDonation.getTier().toString(),
                finalDonation.getAmountUSD(),
                finalDonation.getDonorUUID().toString(),
                finalDonation.getMessage(),
                finalDonation.getTimestamp()
        )).execute();
    }


    /**
     * Executes a specified query to the SQL database
     *
     * @param query The query to execute.
     * @return The results of the query.
     */
    @SneakyThrows
    public ResultSet executeQuery(String query) {
        return connection.prepareStatement(query).executeQuery();
    }

}
