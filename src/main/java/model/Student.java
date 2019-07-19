package model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.Date;

//@Indexes(
//        @Index(fields = {@Field("index")}, options = @IndexOptions(unique = true))
//)
@XmlRootElement
public class Student {

//    @InjectLinks({
//            @InjectLink(
//                    resource = StudentResource.class,
//                    rel = "student_with_id",
//                    bindings = {@Binding(name="index", value="${instance.index}")}),
//            @InjectLink(
//                    resource = StudentsResource.class,
//                    rel = "students"),
//    })
//    @XmlElement(name="link")
//    @XmlElementWrapper(name = "links")
//    @XmlJavaTypeAdapter(Link.JaxbAdapter.class)
//    List<Link> links;
    private int id;
    @XmlTransient
    @Id
    ObjectId _id;
    private int index;
    private String name;
    private String lastName;
    @JsonFormat(shape=JsonFormat.Shape.STRING,
            pattern="yyyy-MM-dd", timezone="CET")
    private Date birthDate;

    /*@Reference (idOnly = true)
    private List<Grade> grades;*/

    public Student(){
        //grades = new ArrayList<>();
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @XmlJavaTypeAdapter(ObjectIdJaxbAdapter.class)
    public ObjectId getId() {
        return _id;
    }
    public void setId(ObjectId id) {
        this._id = id;
    }

}
