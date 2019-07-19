package resources;

import model.Course;
import model.Courses;
import model.Model;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/courses/")
public class CoursesResource {

    Model model = Model.getInstance( );

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getCourses(@QueryParam("course") String course, @QueryParam("teacher") String teacher, @QueryParam("course_name") String courseName) {
        Courses courses = new Courses();
        courses.setCourses(model.getCourses(course, teacher, courseName));
        return Response.status(200).entity(courses).build();
    }

    @POST
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response addCourse(Course course) {
        Object courseId = model.addCourse(course);
        String locationValue = "/courses/" + courseId;
        return Response.status(201).entity(course).header("Location", locationValue).build();
    }
}

