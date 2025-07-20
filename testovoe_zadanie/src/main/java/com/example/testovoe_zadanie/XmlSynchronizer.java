package com.example.testovoe_zadanie;

import com.example.testovoe_zadanie.model.Position;
import com.example.testovoe_zadanie.model.PositionKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.sql.*;
import java.util.*;

public class XmlSynchronizer {

    private static final Logger logger = LoggerFactory.getLogger(XmlSynchronizer.class);

    public static void syncFromXml(String fileName, Properties props) {
        Map<PositionKey, Position> xmlMap = new HashMap<>();

        try {
            logger.info("Загрузка XML-файла: " + fileName);
            File file = new File(fileName);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);

            NodeList nodeList = doc.getElementsByTagName("position");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element el = (Element) nodeList.item(i);

                String depCode = el.getElementsByTagName("depCode").item(0).getTextContent();
                String depJob = el.getElementsByTagName("depJob").item(0).getTextContent();
                String description = el.getElementsByTagName("description").item(0).getTextContent();

                PositionKey key = new PositionKey(depCode, depJob);
                Position pos = new Position(depCode, depJob, description);

                if (xmlMap.containsKey(key)) {
                    throw new RuntimeException("Дубликат в XML по ключу: " + depCode + " + " + depJob);
                }

                xmlMap.put(key, pos);
            }

            logger.info("XML-файл успешно загружен. Кол-во записей: " + xmlMap.size());

            try (Connection conn = DriverManager.getConnection(
                    props.getProperty("db.url"),
                    props.getProperty("db.user"),
                    props.getProperty("db.password")
            )) {
                conn.setAutoCommit(false);

                Map<PositionKey, Integer> dbMap = new HashMap<>();
                PreparedStatement select = conn.prepareStatement("SELECT id, dep_code, dep_job, description FROM positions");
                ResultSet rs = select.executeQuery();
                while (rs.next()) {
                    PositionKey key = new PositionKey(rs.getString("dep_code"), rs.getString("dep_job"));
                    dbMap.put(key, rs.getInt("id"));
                }

                for (PositionKey key : dbMap.keySet()) {
                    if (!xmlMap.containsKey(key)) {
                        PreparedStatement delete = conn.prepareStatement("DELETE FROM positions WHERE dep_code = ? AND dep_job = ?");
                        delete.setString(1, key.getDepCode());
                        delete.setString(2, key.getDepJob());
                        delete.executeUpdate();
                        logger.info("Удалена запись: " + key.getDepCode() + " / " + key.getDepJob());
                    }
                }

                for (Map.Entry<PositionKey, Position> entry : xmlMap.entrySet()) {
                    PositionKey key = entry.getKey();
                    Position value = entry.getValue();

                    if (dbMap.containsKey(key)) {
                        PreparedStatement update = conn.prepareStatement("UPDATE positions SET description = ? WHERE dep_code = ? AND dep_job = ?");
                        update.setString(1, value.getDescription());
                        update.setString(2, key.getDepCode());
                        update.setString(3, key.getDepJob());
                        update.executeUpdate();
                        logger.info("Обновлена запись: " + key.getDepCode() + " / " + key.getDepJob());
                    } else {
                        PreparedStatement insert = conn.prepareStatement("INSERT INTO positions (dep_code, dep_job, description) VALUES (?, ?, ?)");
                        insert.setString(1, value.getDepCode());
                        insert.setString(2, value.getDepJob());
                        insert.setString(3, value.getDescription());
                        insert.executeUpdate();
                        logger.info("Добавлена запись: " + key.getDepCode() + " / " + key.getDepJob());
                    }
                }

                conn.commit();
                System.out.println("✅ Синхронизация завершена.");
                logger.info("Синхронизация прошла успешно.");

            } catch (Exception e) {
                logger.error("Ошибка при синхронизации. Откат...", e);
                throw e;
            }

        } catch (Exception e) {
            System.err.println("❌ Ошибка при синхронизации: " + e.getMessage());
            logger.error("Исключение:", e);
        }
    }
}