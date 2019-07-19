package resources;

import model.*;
import org.bson.types.ObjectId;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/courses/{id}")
public class CourseResource {

    Model model = Model.getInstance( );

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Object getCourse(@PathParam("id") String courseId) {
        return model.getCourse(courseId);
    }

    @PUT
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response editCourse(@PathParam("id") String courseId, Course course) {
        if (model.getCourse(courseId) == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        else {
            model.editCourse(courseId, course);
            return Response.status(200).build();
        }
    }

    @DELETE
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response deleteCourse(@PathParam("id") String courseId) {
        if (model.getCourse(courseId) == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        else {
            model.deleteGradesFromCourse(new ObjectId(courseId));
            model.deleteCourse(courseId);
            return Response.status(200).build();
        }
    }
}