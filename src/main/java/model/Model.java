package model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;

import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class Model {
    public static Model getOurInstance() {
        return ourInstance;
    }

    private Students students;
    private Courses courses;
    private Datastore datastore;
    Db db;
    public Datastore getDatastore() {
        return this.datastore;
    }

    public void setDatastore(Datastore datastore) {
        this.datastore = datastore;
    }

    /*@InjectLinks({
            @InjectLink(resource = StudentResource.class, rel = "self"),
            @InjectLink(resource = StudentsResource.class, rel = "parent"),
    })
    @XmlElement(name="link")
    @XmlElementWrapper(name = "links")
    @XmlJavaTypeAdapter(Link.JaxbAdapter.class)
    List<Link> links;*/

    public static void setOurInstance(Model ourInstance) {
        Model.ourInstance = ourInstance;
    }

    public void setStudents(Students students) {
        this.students = students;
    }


    public void setCourses(Courses courses) {
        this.courses = courses;
    }

    public static Model ourInstance = new Model();

    public static Model getInstance() {
        return ourInstance;
    }

    public List<Grade> getGrades(int index, String courseId, double value, String order, String courseName) {
        Query<Grade> query = this.datastore.createQuery(Grade.class).field("studentIndex").equal(index); //TODO add specyfic student
        Course course = null;
        if (courseName != null && courseName.length() > 0) {
            course = getCourseByName(courseName);
        }
        if (course != null) {
            query.field("courseId").equal(course.getId());
        }
        else if (courseId != null && courseId.length() > 0)
            query.field("courseId").equal(new ObjectId(courseId));
        if (value > 0.0) {
            if (order != null && order.equals("equals")) {
                query.field("value").equal(value);
            } else if (order != null && order.equals("greater")) {
                query.field("value").greaterThan(value);
            } else if (order != null && order.equals("smaller")) {
                query.field("value").lessThan(value);
            }
        }
        return query.asList();
    }
    public Course getCourseByName(String name) {
        Query<Course> query = this.datastore.createQuery(Course.class);
        query.field("name").containsIgnoreCase(name);
        if (query.asList().size() > 0)
            return query.asList().get(0);
        else
            return null;
    }

    public Grade getGrade(int index, String gradeId) {
        Grade grade =
                this.datastore.find(Grade.class).field("studentIndex").equal(index).
                        field("_id").equal(new ObjectId(gradeId)).get();
        return grade;
    }

    public Model() {
    }

    public Student getStudent(int index) {
        Student student = this.datastore.find(Student.class).field("index").equal(index).get();
        return student;
    }
    public List<Student> getStudents(Date date, String order, String firstName, String lastName) {
        Query<Student> query = this.datastore.createQuery(Student.class);
        if (firstName != null && !firstName.isEmpty())
            query.field("name").containsIgnoreCase(firstName);
        if (lastName != null && !lastName.isEmpty())
            query.field("lastName").containsIgnoreCase(lastName);
        if (date != null) {
            if (order != null && order.equals("equals")) {
                query.field("birthDate").equal(date);
            } else if (order != null && order.equals("greater")) {
                query.field("birthDate").greaterThan(date);
            } else if (order != null && order.equals("smaller")) {
                query.field("birthDate").lessThan(date);
            }
        }
        return query.asList();
    }
    public Course getCourse(String id) {
        ObjectId courseIdObjectId = new ObjectId(id);
        Course course = this.datastore.find(Course.class).field("_id").equal(courseIdObjectId).get();
        return course;
    }
    public void editCourse(String courseId, Course course) {
        Course courseFromDb = getCourse(courseId);
        if(course.getName() != null) {
            courseFromDb.setName(course.getName());
        }
        if(course.getTeacher() != null) {
            courseFromDb.setTeacher(course.getTeacher());
        }
        this.datastore.save(courseFromDb);
    }
    public void deleteCourse(String courseId) {
        datastore.delete(this.datastore.find(Course.class).field("_id").equal(new ObjectId(courseId)).get());
    }
    public void deleteGradesFromCourse(ObjectId courseId) {
        this.datastore.delete(this.datastore.find(Grade.class).field("courseId").equal(courseId));
    }
    public List<Course> getCourses(String course, String teacher, String courseName) {
        Query<Course> query = this.datastore.createQuery(Course.class);
        if (courseName != null && !courseName.isEmpty()) {
            query.field("name").containsIgnoreCase(courseName);
        }
        if (teacher != null && !teacher.isEmpty()) {
            query.field("teacher").containsIgnoreCase(teacher);
        }

        return query.asList();
    }
    public Object addCourse(Course course) {
        return this.datastore.save(course).getId();
    }
    public Object addStudent(Student student) {
        int index = incStudentIndex();
        student.setIndex(index);
        return this.datastore.save(student).getId();
    }
    public int incStudentIndex() {

        Query<Student> query = this.datastore.createQuery(Student.class);
        int index = query.asList().get(query.asList().size() - 1).getIndex();
        return index + 1;
    }
    public void editStudent(int studentIndex, Student student) {
        Student studentFromDb = getStudent(studentIndex);
        if(student.getName() != null) {
            studentFromDb.setName(student.getName());
        }
        if(student.getBirthDate() != null) {
            studentFromDb.setBirthDate(student.getBirthDate());
        }
        if(student.getLastName() != null) {
            studentFromDb.setLastName(student.getLastName());
        }
        this.datastore.save(studentFromDb);
    }
    public void deleteStudent(int index) {
        datastore.delete(this.datastore.find(Student.class).field("index").equal(index).get());
    }
    public void editGrade(int index, String gradeId, Grade grade) {
        Grade gradeFromDb = getGrade(index, gradeId);
        Course course = getCourse(String.valueOf(grade.getCourseId()));

        if(course != null) {
            gradeFromDb.setCourse(course);
            gradeFromDb.setCourseId(course.getId());
        }
        if(grade.getDate() != null) {
            gradeFromDb.setDate(grade.getDate());
        }

        gradeFromDb.setValue(grade.getValue());

        this.datastore.save(gradeFromDb);
    }
    public Object addGrade(int index, Grade grade) {
        Student student = getStudent(index);
        grade.setStudent(student);
        grade.setStudentIndex(index);
        if (getCourse(grade.getCourseId().toString()) != null){
            grade.setCourseId(grade.getCourseId());
            grade.setCourse(getCourse(grade.getCourseId().toString()));
            return this.datastore.save(grade).getId();
        }
        return null;
    }
    public void deleteGrade(int index, ObjectId gradeId) {
        datastore.delete(this.datastore.find(Grade.class).field("_id").equal(gradeId).get());
    }
    public void initDb() throws ParseException, UnknownHostException {
        Db db = new Db();
        this.datastore = db.getDataStore();

    }
}
