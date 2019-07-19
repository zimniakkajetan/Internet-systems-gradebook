package model;


import org.bson.types.ObjectId;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class Grades {
    private List<Grade> grades;

    public Grades() {
        grades = new ArrayList<>();
    }
    public List<Grade> getGrades() {
        return grades;
    }
    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }
    public Grade getGrade(ObjectId id) {
        for(Grade c: grades) {
            if(c.get_id() == id) {
                return c;
            }
        }
        return null;
    }
    public void addGrade (Grade grade) {
        grades.add(grade);
    }

}
