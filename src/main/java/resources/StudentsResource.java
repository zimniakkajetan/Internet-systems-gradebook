package resources;

import model.Model;
import model.Student;
import model.Students;
import org.bson.types.ObjectId;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Date;

@Path("/students/")
public class StudentsResource {

    Model model = Model.getInstance( );

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getStudents(@QueryParam("birth_date") String date, @QueryParam("order") String order, @QueryParam("name") String firstName, @QueryParam("last_name") String lastName) {
        Students students = new Students();
        Date bDate = null;
        if (date != null && date.length() > 0) {
            bDate = new DateParamConverterProvider("yyyy-MM-dd").getConverter(Date.class, Date.class, null).fromString(date);
        }
        students.setStudents(model.getStudents(bDate, order, firstName, lastName));
        return Response.ok(students).build();
    }

    @POST
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response addStudent(Student student) {
        Object studentId = model.addStudent(student);
        String locationValue = "/students/" + studentId;
        student.setId((ObjectId) studentId);
        return Response.status(200).entity(student).header("Location", locationValue).build();
    }
}

