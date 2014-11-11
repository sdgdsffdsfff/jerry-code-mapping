package com.qufaya.framework.fastcode;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 *
 * @goal generate
 * 
 */
public class FastcodeMojo extends AbstractMojo {

    /**
     * @parameter expression="${fc.host}" default-value="127.0.0.1"
     */
    private String host;

    /**
     * @parameter expression="${fc.port}" default-value="3306"
     */
    private int port;

    /**
     * @parameter expression="${fc.database}"
     */
    private String database;

    /**
     * @parameter expression="${fc.user}" default-value="root"
     */
    private String user;

    /**
     * @parameter expression="${fc.password}" default-value=""
     */
    private String password;

    /**
     * @parameter expression= "${project.build.sourceDirectory}"
     */
    private String sourceDirectory;

    /**
     * @parameter expression="${fc.basePackage}"
     */
    private String basePackage;

    /**
     * @parameter expression="${fc.table}"
     */
    private String tableName;

    /**
     * @parameter expression="${fc.overwrite}" default-value="false"
     */
    private boolean overwrite;

    public void execute() throws MojoExecutionException {

        getLog().info("connection = " + "jdbc://" + host + ":" + port + "/" + database);
        getLog().info("user = " + user);
        getLog().info("password = " + password);
        getLog().info("sourceDirectory = " + sourceDirectory);
        getLog().info("basePackage = " + basePackage);
        getLog().info("overwrite = " + overwrite);

        if (host == null) {
            throw new MojoExecutionException("invalid host");
        }

        if (database == null) {
            throw new MojoExecutionException("invalid database");
        }

        if (user == null) {
            throw new MojoExecutionException("invalid user");
        }

        if (sourceDirectory == null) {
            throw new MojoExecutionException("invalid sourceDirectory");
        }

        if (basePackage == null) {
            throw new MojoExecutionException("invalid basePackage");
        }

        try {
            Fastcode.getInstance().create(sourceDirectory, host, port, database, user, password,
                    tableName, basePackage, overwrite);
        } catch (Exception e) {
            throw new MojoExecutionException("", e);
        }
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public boolean isOverwrite() {
        return overwrite;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public String getTableName() {
        return tableName;
    }

    public String getSourceDirectory() {
        return sourceDirectory;
    }

}
