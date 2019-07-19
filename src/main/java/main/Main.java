package main;


import com.mongodb.MongoClient;
import model.*;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.linking.DeclarativeLinkingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import resources.*;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.text.SimpleDateFormat;

public class Main {
    public static void main(String[] args) throws Exception {
        int port = 8000;
        URI baseUri = UriBuilder.fromUri("http://localhost/").port(9998).build();
        ResourceConfig config = new ResourceConfig(StudentResource.class, StudentsResource.class,
                GradeResource.class, GradesResource.class, CourseResource.class, CoursesResource.class,
                DeclarativeLinkingFeature.class, AppExceptionMapper.class, CustomHeaders.class);

        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(baseUri, config);
        server.start();

        //RUN MONGODB
        //bin\mongod --dbpath ./data/db/ --port 8004
        MongoClient mongo = new MongoClient( "localhost" , 8004 );

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Model model = Model.getInstance();
        model.initDb();
    }
}
