package model;

import org.bson.types.ObjectId;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;
import org.mongodb.morphia.annotations.Transient;
import resources.GradesResource;
import resources.StudentResource;
import resources.StudentsResource;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;
import java.util.List;

@XmlRootElement
public class Grade {

    @InjectLinks({
            @InjectLink(
                    resource = StudentResource.class,
                    rel = "student_with_id"),
            @InjectLink(
                    resource = StudentsResource.class,
                    rel = "students"),
            @InjectLink(
                    resource = GradesResource.class,
                    rel = "grades"),
    })
    @XmlElement(name="link")
    @XmlElementWrapper(name = "links")
    @XmlJavaTypeAdapter(Link.JaxbAdapter.class)
    @Transient
    List<Link> links;

    @Id
    ObjectId _id;
    private double value;
    private Date date;
    private ObjectId courseId;
    @Reference
    private Course course;
    private int studentIndex;
    @Reference
    private Student student;

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Grade() {
    }
    @XmlJavaTypeAdapter(ObjectIdJaxbAdapter.class)
    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course idCourse) {
        this.course = idCourse;
    }

    public int getStudentIndex() {
        return studentIndex;
    }

    public void setStudentIndex(int studentIndex) {
        this.studentIndex = studentIndex;
    }

    public ObjectId getCourseId() {
        return courseId;
    }

    public void setCourseId(ObjectId courseId) {
        this.courseId = courseId;
    }
}
