package dev.danilosantos.infrastructure.tomcat;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;

public class TomcatServer {

    public void start() {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);

        System.out.println("base directory: " + new File("./src/main/webapp/").getAbsolutePath() + "...");

        StandardContext context = (StandardContext) tomcat.addWebapp("", new File("src/main/webapp/").getAbsolutePath());
        File webInfClasses = new File("target/classes");
        WebResourceRoot resources = new StandardRoot(context);
        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes",
                webInfClasses.getAbsolutePath(), "/"));
        context.setResources(resources);
        tomcat.enableNaming();
        tomcat.getConnector();

        try {
            tomcat.start();
        } catch (LifecycleException e) {
            System.out.println(e.getMessage());
        }
        tomcat.getServer().await();
    }
}