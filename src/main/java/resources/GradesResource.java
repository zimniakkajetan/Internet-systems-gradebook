package resources;

import model.Grade;
import model.Grades;
import model.Model;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/students/{index}/grades")
public class GradesResource {

    Model model = Model.getInstance( );

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Object getGrades(@PathParam("index") int index, @QueryParam("course_id") String courseId, @QueryParam("course") String courseName, @QueryParam("grade") double value, @QueryParam("order") String order) {
        Grades grades = new Grades();
        grades.setGrades(model.getGrades(index, courseId, value, order, courseName));
        return Response.ok(grades).build();
    }

    @POST
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response addGrade(@PathParam("index") int index, Grade grade) {
        Object gradeId = model.addGrade(index, grade);
        if (gradeId == null) {
            return Response.status(404).entity("").build();
        }
        String locationValue = "/students/" + index + "/grades" + gradeId;
        return Response.status(201).entity(grade).header("Location", locationValue).build();
    }
}