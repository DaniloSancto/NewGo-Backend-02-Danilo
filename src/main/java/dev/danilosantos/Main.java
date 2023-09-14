package dev.danilosantos;

import dev.danilosantos.infrastructure.tomcat.TomcatServer;

public class Main {

    public static void main(String[] args) {
        TomcatServer server = new TomcatServer();
        server.start();
    }

}