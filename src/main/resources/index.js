'use strict';

let dataGlobal = [];
let gradeStudentId;
function DataController(data) {

    this.students = ko.observableArray('');
    this.courses = ko.observableArray('');
    this.grades = ko.observableArray('');

    this.newStudent = {
        index: ko.observable(''),
        name: ko.observable(''),
        lastName: ko.observable(''),
        birthDate: ko.observable(''),
    };
    this.newCourse = {
        name: ko.observable(''),
        teacher: ko.observable(''),
    };
    this.newGrade = {
        value: ko.observable(''),
        courseId: ko.observable(''),
    };
    this.currentStudent = ko.observable('');
    this.currentStudentObj = ko.observable('');
    this.index = ko.observable('');

    this.studentsQueryParams = {
        index: ko.observable(''),
        name: ko.observable(''),
        last_name: ko.observable(''),
        birth_date: ko.observable('')
    };
    this.courseQueryParams = {
        teacher: ko.observable(''),
        course_name: ko.observable(''),
    };
    this.gradeQueryParams = {
        grade: ko.observable(''),
        course: ko.observable(''),
    };
    const self = this;
    const selff = this;
    Object.keys(this.studentsQueryParams).forEach(function(key) {
        self.studentsQueryParams[key].subscribe(function() {
            self.showStudents();
        });
    });
    Object.keys(this.courseQueryParams).forEach(function(key) {
        self.courseQueryParams[key].subscribe(function() {
            self.showCourses();
        });
    });
    Object.keys(this.gradeQueryParams).forEach(function(key) {
        self.gradeQueryParams[key].subscribe(function() {
            self.showGrades();
        });
    });
    this.showStudents = function() {
        let self = this;
        console.log('http://localhost:9998/students?' + $.param(ko.mapping.toJS(self.studentsQueryParams)));
        $.ajax({
            type: 'GET',
            url: 'http://localhost:9998/students?' + $.param(ko.mapping.toJS(self.studentsQueryParams)) + '&order=equals',
            contentType: "application/json",
            dataType: "json",
            success: function(data) {
                //let self = this
                ko.mapping.fromJS(data, {}, self);
                ko.computed(function() {
                    return ko.mapping.toJSON(self);
                }).subscribe(function(newValue) {
                    let stude =  JSON.parse(newValue)['students']
                    stude.forEach(function (record) {
                        self.putOnServer('students/' + record['index'], record)
                    });
                });
            },
            error:function(jq, st, error){
                console.log(error);
            }
        });
    };
    this.showCourses = function() {
        let self = this;
        $.ajax({
            type: 'GET',
            url: 'http://localhost:9998/courses?' + $.param(ko.mapping.toJS(self.courseQueryParams)),
            contentType: "application/json",
            dataType: "json",
            success: function(data) {
                let co = JSON.stringify(data['courses'])
                this.courses = JSON.parse(co)
                ko.mapping.fromJS(data, {}, self);
                ko.computed(function() {
                    return ko.mapping.toJSON(self);
                }).subscribe(function(newValue) {
                    let stude =  JSON.parse(newValue)['courses']
                    stude.forEach(function (record) {
                        console.log(record)
                        self.putOnServer('courses/' + record['id'], record)
                    });
                });
            },
            error:function(jq, st, error){
                console.log(error);
            }
        });
    };
    this.showStudents();
    this.showCourses();
    this.showGrades = function(index) {
        let self = this;
        let studentId = ko.mapping.toJSON(this.currentStudent);
        if (index != undefined){
            window.open('#grades_form', '_self');
            this.currentStudent = JSON.parse(ko.mapping.toJSON(index))['index'];
            studentId = JSON.parse(ko.mapping.toJSON(index))['index'];
            ko.mapping.fromJS(JSON.parse(ko.mapping.toJSON(index)), {}, self);
            ko.computed(function() {
                return ko.mapping.toJSON(self);
            })        }

        console.log('http://localhost:9998/students/' + studentId + '/grades' + '?' + $.param(ko.mapping.toJS(self.gradeQueryParams)) + '&order=equals')
        $.ajax({
            type: 'GET',
            url: 'http://localhost:9998/students/' + studentId + '/grades' + '?' + $.param(ko.mapping.toJS(self.gradeQueryParams)) + '&order=equals',
            contentType: "application/json",
            dataType: "json",
            success: function(data) {
                console.log(data);
                gradeStudentId = data['id']
                self.subscribeGrade(data, self);
            },
            error:function(jq, st, error){
            }
        });

    }.bind(this);  // Ensure that "this" is always this view model
    this.deleteStudent = function(student, url) {
        console.log(student, url, JSON.parse(ko.mapping.toJSON(student))['id'])
        let self = this
        $.ajax({
            type: 'DELETE',
            url: 'http://localhost:9998/students' + '/' + JSON.parse(ko.mapping.toJSON(student))['index'],
            contentType: "application/json",
            dataType: "json",
            success: function(data) {
                location.reload();
            },
            error:function(jq, st, error){
                console.log(error);
                location.reload();
            }
        });
    }.bind(this)
    this.deleteCourse = function(url) {
        let self = this;
        $.ajax({
            type: 'DELETE',
            url: 'http://localhost:9998/courses' + '/' + JSON.parse(ko.mapping.toJSON( url['id'])),
            contentType: "application/json",
            dataType: "json",
            success: function(data) {
                location.reload();
            },
            error:function(jq, st, error){
                console.log(error);
                location.reload();
            }
        });
    }.bind(this)
    this.deleteGrade = function(url) {
        let self = this;
        console.log(JSON.parse(ko.mapping.toJSON(url)))
        console.log('http://localhost:9998/students' + '/' + self.currentStudent + '/grades/' + JSON.parse(ko.mapping.toJSON(url['_id'])));
        $.ajax({
            type: 'DELETE',
            url: 'http://localhost:9998/students' + '/' + self.currentStudent + '/grades/' + JSON.parse(ko.mapping.toJSON(url['_id'])),
            contentType: "application/json",
            dataType: "json",
            success: function(data) {
                location.reload();
            },
            error:function(jq, st, error){
                $.ajax({
                    type: 'GET',
                    url: 'http://localhost:9998/students/' + self.currentStudent + '/grades',
                    contentType: "application/json",
                    dataType: "json",
                    success: function(data) {
                        console.log(data);
                        gradeStudentId = data['id']
                        window.open('#grades_form', '_self');
                        self.subscribeGrade(data, self);
                    },
                    error:function(jq, st, error){
                        console.log(error);
                    }
                });
            }
        });
    }.bind(this)
    this.addStudent = function() {
        let self = this;
        $.ajax({
            type: 'POST',
            url: 'http://localhost:9998/students',
            contentType: "application/json",
            dataType: "json",
            data: ko.mapping.toJSON(this.newStudent),
            success: function(data) {
                self.students.push(ko.mapping.fromJS(data))
            },
            error:function(jq, st, error){
                console.log(error);
            }
        });

    }.bind(this)
    this.addCourse = function() {
        let self = this
        $.ajax({
            type: 'POST',
            url: 'http://localhost:9998/courses',
            contentType: "application/json",
            dataType: "json",
            data: ko.mapping.toJSON(this.newCourse),
            success: function(data) {
                self.courses.push(self.newCourse)
                location.reload();
            },
            error:function(jq, st, error){
                console.log(error);
            }
        });
    }.bind(this)
    this.addGrade = function() {
        let self = this;
        $.ajax({
            type: 'POST',
            url: 'http://localhost:9998/students/' + this.currentStudent + '/grades',// + JSON.parse(ko.mapping.toJSON(this.newStudent))['index'],
            contentType: "application/json",
            dataType: "json",
            data: ko.mapping.toJSON(this.newGrade),
            success: function(data) {
                $.ajax({
                    type: 'GET',
                    url: 'http://localhost:9998/students/' + self.currentStudent + '/grades',
                    contentType: "application/json",
                    dataType: "json",
                    success: function(data) {
                        console.log(data);
                        gradeStudentId = data['id']
                        window.open('#grades_form', '_self');
                        self.subscribeGrade(data, self);
                    },
                    error:function(jq, st, error){
                        console.log(error);
                    }
                });
            },
            error:function(jq, st, error){
            }
        });
    }.bind(this);
    this.subscribeGrade = function(data, self) {
        ko.mapping.fromJS(data, {}, self);
        ko.computed(function() {
            return ko.mapping.toJSON(self);
        }).subscribe(function(newValue) {
            let stude =  JSON.parse(newValue)['grades']
            stude.forEach(function (record) {
                let j = {"value": record["value"].toString(), "courseId": record["course"]["id"]}
                console.log(JSON.stringify(j));//JSON.stringify(JSON.parse(ko.mapping.toJSON(record))))
                self.putOnServer('students/' + self.currentStudent + '/grades/' + record['_id'], j)
            });
        });
    }
    this.putOnServer = function(url, data) {
        console.log(url, data, 'aaa')
        $.ajax({
            type: 'PUT',
            url: 'http://localhost:9998/' + url,
            contentType: "application/json",
            accept: "application/json",
            dataType: "json",
            data: JSON.stringify(data),
            success: function (data) {
            },
            error: function (jq, st, error) {
            }
        });
    }
}
let dataController;
$(function () {
    setTimeout(function() {
        dataController = new DataController(dataGlobal);
        ko.applyBindings(dataController);
    }, 500);
});