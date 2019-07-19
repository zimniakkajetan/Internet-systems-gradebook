package resources;

import model.Model;
import model.Student;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/students/{index}")
public class StudentResource {

    Model model = Model.getInstance();

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Student getStudent(@PathParam("index") int index) {
        return model.getStudent(index);
    }

    @PUT
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response editStudent(@PathParam("index") int index, Student student) {
        model.editStudent(index, student);
        return Response.status(200).build();
    }

    @DELETE
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response deleteStudent(@PathParam("index") int index) {
        Student student = model.getStudent(index);
        if(student != null) {
            model.deleteStudent(index);
            return Response.status(200).build();
        }
        else {
            return Response.status(404).build();
        }
    }
}