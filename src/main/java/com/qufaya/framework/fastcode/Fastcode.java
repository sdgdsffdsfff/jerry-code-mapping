/**
 * 
 */
package com.qufaya.framework.fastcode;

import com.qufaya.framework.fastcode.database.ConnectionFactory;
import com.qufaya.framework.fastcode.database.Table;
import com.qufaya.framework.fastcode.database.TableFactory;
import com.qufaya.framework.fastcode.template.TemplateEngine;
import com.qufaya.framework.fastcode.template.Utils;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.FileAlreadyExistsException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * @author zhihua
 *
 */
public class Fastcode {

    private static final Fastcode instance = new Fastcode();

    private Fastcode() {
    }

    public static Fastcode getInstance() {
        return instance;
    }

    enum Template {

        Model("", "model", "model.vm"),

        DAO("DAO", "dao", "dao.vm"),

        Manager("Manager", "manager", "manager.vm"),

        Service("Service", "service", "service.vm"),

        ;

        private final String suffix;

        private final String subpackage;

        private final String template;

        /**
         * @param suffix
         * @param subpackage
         * @param template
         */
        private Template(String suffix, String subpackage, String template) {
            this.suffix = suffix;
            this.subpackage = subpackage;
            this.template = template;
        }

        public String getSuffix() {
            return suffix;
        }

        public String getSubpackage() {
            return subpackage;
        }

        public String getTemplate() {
            return template;
        }

    }

    public void create(String sourceDirectory, String host, int port, String database, String user,
            String password, String tableName, String basePackage, boolean overwrite)
            throws Exception {
        Connection connection = null;
        try {
            connection = ConnectionFactory.getInstance().createConnection(host, port, database,
                    user, password);
            Table table = TableFactory.getInstance().createTable(connection, tableName);
            create(sourceDirectory, table, basePackage, overwrite);
        } catch (Exception e) {
            throw new RuntimeException("", e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void create(String sourceDirectory, Table table, String basePackage, boolean overwrite)
            throws IOException {
        for (Template template : Template.values()) {
            create(sourceDirectory, table, basePackage, template, overwrite);
        }
    }

    public void create(String sourceDirectory, Table table, String basePackage, Template template,
            boolean overwrite) throws IOException {

        String className = Utils.tableNameToClassName(table.getName()) + template.getSuffix();
        if (basePackage == null) {
            basePackage = "";
        }

        if (!basePackage.isEmpty()) {
            basePackage += ".";
        }
        basePackage += Utils.tableNameToClassName(table.getName()).toLowerCase();

        String packageName = basePackage;
        String subpackage = template.getSubpackage();
        if (subpackage != null && !subpackage.isEmpty()) {
            packageName = basePackage + "." + subpackage;
        }

        HashMap<String, Object> vars = new HashMap<>();
        vars.put("table", table);
        vars.put("utils", new Utils());
        vars.put("basePackage", basePackage);
        vars.put("specifyPackage", packageName);
        vars.put("specifyType", className);
        String text = TemplateEngine.execute(template.getTemplate(), vars);

        String filename = sourceDirectory + "/" + packageName.replace('.', '/') + "/" + className
                + ".java";
        createFile(filename, text, overwrite);
    }

    public void createFile(String fileName, String text, boolean overwrite) throws IOException {

        // 文件已存在
        File file = new File(fileName);
        if (file.exists()) {
            if (!overwrite) {
                throw new FileAlreadyExistsException(fileName);
            }
        } else {
            file.getParentFile().mkdirs();
        }

        FileOutputStream outputStream = null;
        BufferedWriter bufferedWriter = null;
        try {
            outputStream = new FileOutputStream(file);
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            bufferedWriter.write(text);
            bufferedWriter.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(outputStream);
            close(bufferedWriter);
        }

    }

    /**
     * @param closeable
     */
    public void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {}
        }
    }
}
