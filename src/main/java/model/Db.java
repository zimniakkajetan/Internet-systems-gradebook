package model;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;

import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class Db {
    private Datastore dataStore;

    public Datastore getDataStore() {
        return this.dataStore;
    }

    public void setDataStore(Datastore dataStore) {
        this.dataStore = dataStore;
    }
    public List<Course> getCourses() {
        Query<Course> query = this.dataStore.createQuery(Course.class);
        return query.asList();
    }
    public Db() throws UnknownHostException, MongoException, ParseException {

        String dbName = new String("bank");
        MongoClient mongo = new MongoClient(new MongoClientURI("mongodb://localhost:8004"));
        Morphia morphia = new Morphia();
        Datastore datastore = morphia.createDatastore(mongo, dbName);
        morphia.mapPackage("com.city81.mongodb.morphia.entity");

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        datastore.delete(datastore.createQuery(Course.class));
        datastore.delete(datastore.createQuery(Student.class));
        datastore.delete(datastore.createQuery(Grade.class));
        if (datastore.getCount(Course.class) == 0 && datastore.getCount(Student.class) == 0) {

            // Add data to database for testing
            Students studentsList = new Students();
            Student student = new Student();
            student.setName("Andrzej");
            student.setLastName("Kowalski");
            student.setIndex(1);
            student.setBirthDate(format.parse("2009-12-31"));
            datastore.save(student);

            student = new Student();
            student.setName("Magdalena");
            student.setLastName("Nowak");
            student.setIndex(2);
            student.setBirthDate(format.parse("2000-02-05"));
            datastore.save(student);

            Course course = new Course();
            course.setName("Matematyka");
            course.setTeacher("Nauczyciel1");

            Course course2 = new Course();
            course2.setName("Narzędzie informatyki");
            course2.setTeacher("Nauczyciel2");

            Courses courses = new Courses();
            courses.addCourse(course);
            courses.addCourse(course2);
            Object courseId = datastore.save(course).getId();
            Object course2Id = datastore.save(course2).getId();

            Grade grade = new Grade();
            grade.setCourse(course);
            grade.setDate(format.parse("2019-12-31"));
            grade.setValue(5.0);
            grade.setStudent(student);
            grade.setStudentIndex(student.getIndex());
            grade.setCourseId((ObjectId) courseId);
            datastore.save(grade);

            grade = new Grade();
            grade.setCourse(course2);
            grade.setDate(format.parse("2019-12-31"));
            grade.setValue(4.0);
            grade.setStudent(student);
            grade.setStudentIndex(student.getIndex());
            grade.setCourseId((ObjectId) course2Id);
            datastore.save(grade);

            grade = new Grade();
            grade.setCourse(course2);
            grade.setDate(format.parse("2019-12-31"));
            grade.setValue(4.0);
            grade.setStudent(student);
            grade.setStudentIndex(student.getIndex());
            grade.setCourseId((ObjectId) course2Id);
            datastore.save(grade);

            this.dataStore = datastore;

            studentsList.addStudent(student);
            Query<Course> query = datastore.createQuery(Course.class);
            System.out.println(query.asList().get(0).getName() + "/n");
        }
    }
}
