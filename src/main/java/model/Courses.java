package model;


import org.bson.types.ObjectId;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class Courses {
    private List<Course> courses;

    public Courses () {
            courses = new ArrayList<>();
    }
    public List<Course> getCourses() {
        return courses;
    }
    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }
    public Course getCourse(ObjectId id) {
        for(Course c: courses) {
            if(c.getId() == id) {
                return c;
            }
        }
        return null;
    }
    public void addCourse(Course course) {
        courses.add(course);
    }

}
