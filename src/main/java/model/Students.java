package model;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class Students implements Serializable {

    /*@InjectLinks({
            @InjectLink(
                    resource = StudentsResource.class,
                    rel = "self"),
    })
    @XmlElement(name="link")
    @XmlElementWrapper(name = "links")
    @XmlJavaTypeAdapter(Link.JaxbAdapter.class)
    List<Link> links;*/

    private List<Student> students;

    public Students() {
        students = new ArrayList<>();

    }
    @XmlElement
    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
    public Student getStudent(int index) {
        for( Student s: students) {
            if(s.getIndex() == index) {
                return s;
            }
        }
        return null;
    }
    public void addStudent(Student student) {
        students.add(student);
    }
}
