package model;

import org.bson.types.ObjectId;

import org.mongodb.morphia.annotations.Id;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement
public class Course {

    /*@InjectLinks({
            @InjectLink(
                    resource = CourseResource.class,
                    rel = "self",
                    bindings = {@Binding(name="id", value="${instance.id}")}),
    })
    @XmlElement(name="link")
    @XmlElementWrapper(name = "links")
    @XmlJavaTypeAdapter(Link.JaxbAdapter.class)
    List<Link> links;*/

    private int id;
    @XmlTransient
    @Id
    ObjectId _id;
    private String name;
    private String teacher;

    public Course() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    @XmlJavaTypeAdapter(ObjectIdJaxbAdapter.class)
    public ObjectId getId() {
        return _id;
    }
    public void setId(ObjectId id) {
        this._id = id;
    }

}
