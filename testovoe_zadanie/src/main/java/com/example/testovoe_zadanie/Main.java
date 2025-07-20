package com.example.testovoe_zadanie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.testovoe_zadanie.XmlExporter;



public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            Properties props = new Properties();
            try (InputStream input = Main.class.getClassLoader().getResourceAsStream("application.properties")) {
                props.load(input);
            }

            String dbUrl = props.getProperty("db.url");
            String dbUser = props.getProperty("db.user");
            String dbPassword = props.getProperty("db.password");

            try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
                System.out.println("✅ Подключение к БД установлено.");
                logger.info("Успешное подключение к БД: " + dbUrl);
            }

            if (args.length == 2 && args[0].equalsIgnoreCase("export")) {
                XmlExporter.exportToXml(args[1], props);
            }
            if (args.length == 2 && args[0].equalsIgnoreCase("sync")) {
                XmlSynchronizer.syncFromXml(args[1], props);
            }

        } catch (Exception e) {
            System.err.println("❌ Ошибка при подключении к БД.");
            logger.error("Ошибка при подключении к БД", e);
        }
    }
}