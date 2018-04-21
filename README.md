# SmartALec
An implementation of a "smart interactive classroom" application for the 50.003 course in SUTD.

## Features
### System administrators
Create and edit the details of users with 2 simple to use Python scripts (found in /Admin)
### Instructors
Create new lectures with the touch of a single button, allowing students to leave anonymous feedback
Create new multiple-choice quizzes before the start of lectures and set them to active as required
Automated client side marking of quizzes and updating of scores in a list which is easily accesible for the instructors
### Students
Leave anonymous feedback and during-lecture questions for instructors to see

## Software testing
Unit tests to test particular class functions and some system tests to test the overall flow of the program have been implemented.
In addition, the system has undergone extensive stress testing with the help of Firebases' "Test Lab for Android", with
pseudo randomly generated input events on up to 10 devices at once.