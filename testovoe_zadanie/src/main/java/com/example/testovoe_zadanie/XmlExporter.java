package com.example.testovoe_zadanie;

import com.example.testovoe_zadanie.model.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class XmlExporter {

    private static final Logger logger = LoggerFactory.getLogger(XmlExporter.class);

    public static void exportToXml(String fileName, Properties props) {
        try {
            List<Position> positions = new ArrayList<>();

            try (Connection conn = DriverManager.getConnection(
                    props.getProperty("db.url"),
                    props.getProperty("db.user"),
                    props.getProperty("db.password")
            )) {
                logger.info("Чтение данных из БД...");
                PreparedStatement stmt = conn.prepareStatement("SELECT dep_code, dep_job, description FROM positions");
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    positions.add(new Position(
                            rs.getString("dep_code"),
                            rs.getString("dep_job"),
                            rs.getString("description")
                    ));
                }
            }

            logger.info("Создание XML-файла...");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document doc = builder.newDocument();
            Element root = doc.createElement("positions");
            doc.appendChild(root);

            for (Position pos : positions) {
                Element item = doc.createElement("position");

                Element depCode = doc.createElement("depCode");
                depCode.appendChild(doc.createTextNode(pos.getDepCode()));
                item.appendChild(depCode);

                Element depJob = doc.createElement("depJob");
                depJob.appendChild(doc.createTextNode(pos.getDepJob()));
                item.appendChild(depJob);

                Element description = doc.createElement("description");
                description.appendChild(doc.createTextNode(pos.getDescription()));
                item.appendChild(description);

                root.appendChild(item);
            }

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(new DOMSource(doc), new StreamResult(new File(fileName)));

            System.out.println("✅ Выгрузка завершена: " + fileName);
            logger.info("Файл XML успешно сохранён: " + fileName);

        } catch (Exception e) {
            System.err.println("❌ Ошибка при экспорте в XML");
            logger.error("Ошибка при экспорте", e);
        }
    }
}