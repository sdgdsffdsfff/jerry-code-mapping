package com.qufaya.framework.fastcode.template;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class TemplateEngine {

    private static final String TEMPLATE_LOC = "/template/";

    private static final VelocityEngine velocityEngine = new VelocityEngine();

    static {

        ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

        Thread.currentThread().setContextClassLoader(velocityEngine.getClass().getClassLoader());

        Properties properties = new Properties();
        properties.setProperty("resource.loader", "class");

        properties.setProperty("class.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        properties.setProperty("class.resource.loader.cache", "true");
        // properties.setProperty("jar.resource.loader.path", "/templates/");

        properties.setProperty("overrideLogging", "true");
        properties.setProperty("preferFileSystemAccess", "false");
        properties.setProperty("overrideLogging", "true");

        properties.setProperty("input.encoding", "UTF-8");
        properties.setProperty("output.encoding", "UTF-8");
        properties.setProperty("directive.foreach.counter.name", "velocityCount");
        //        properties.setProperty("directive.foreach.counter.initial.value", "0");

        //        properties.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,
        //                "org.apache.velocity.runtime.log.Log4JLogChute");
        //        properties.setProperty("runtime.log.logsystem.log4j.logger", Compiler.class.getName());

        try {
            velocityEngine.init(properties);
        } finally {
            Thread.currentThread().setContextClassLoader(oldClassLoader);
        }
    }

    public static String execute(String templateName, HashMap<String, Object> vars) {

        VelocityContext context = new VelocityContext();
        Iterator<Map.Entry<String, Object>> iter = vars.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, Object> entry = iter.next();
            context.put(entry.getKey(), entry.getValue());
        }
        Template template = null;
        try {
            template = velocityEngine.getTemplate(TEMPLATE_LOC + templateName);
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        } catch (ParseErrorException e) {
            throw new RuntimeException(e);
        } catch (MethodInvocationException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        StringWriter sw = new StringWriter();
        template.merge(context, sw);
        return sw.toString();
    }

    public static void main(String[] args) {

    }
}
