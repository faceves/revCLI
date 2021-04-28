package tutorapp.utils

import scala.util.{Try,Success,Failure}
import java.sql.PreparedStatement
import tutorapp.utils.ConnectionUtil
import java.sql.Connection
import scala.util.Using
import tutorapp.utils.FileUtil
import tutorapp.objHolders.Student
import tutorapp.objHolders.Exams
import tutorapp.dao.StudentDAO
import tutorapp.dao.ExamsDAO
import scala.collection.immutable.List

object DatabaseUtil {
  //used for both exams and student, should not need obj string, TO DO is to implement
  //inheritence for Student and Exams objects to be polymorphic, stil need to do!
  def checkForDuplicates(checkVal: String, table: String):Try[Boolean] = {
    Using.Manager{use=>
      val conn = use(ConnectionUtil.getConnection())
      val statement = use(conn.prepareStatement(s"SELECT EXISTS(SELECT 1 FROM $table WHERE studentid = ?)as exists"))
      statement.setString(1,checkVal)
      statement.execute()
      val rs = use(statement.getResultSet())
      //rs.next() grabs the only row and grabs the boolean to see if its true and exists
      rs.next()
      rs.getBoolean(1)
      
    }
  }

  def loadJSONFile(filepath: String, filename: String ) : Unit = {
        var fileString = FileUtil.getTextContent(filename)
        var studentData = JSONUtil.getStudentList(fileString)
        studentData match{
            case Some(studentList) =>{
                //if a duplicate exists stop the loading of the file since files are loaded in batches.
                val duplicateExistsTry = checkForDuplicates(studentList.head.studentID.toString(), "student")
                duplicateExistsTry match{
                  case Success(duplicatesExists) =>{

                    if(!duplicatesExists){
                      studentList.foreach((x:Student)=>StudentDAO.insertStudent(x))
                      println("Students.json succestful!")
                    }
                    else
                      println("Duplicate exists, cancelling batch to load JSON file for Students.")

                  }
                  case Failure(e) => println(e.getMessage())
                }
                    
            }
            case None => println("Loading JSON error.")
        }

  }

  def loadJSONFileE (filepath: String, filename: String ) : Unit = {
        var fileString = FileUtil.getTextContent(filename)
        var examsData = JSONUtil.getExamsList(fileString)
        examsData match{
            case Some(examsList) =>{
                //if a duplicate exists stop the loading of the file since files are loaded in batches.
                val duplicateExistsTry = checkForDuplicates(examsList.head.studentID.toString(), "Exams")
                duplicateExistsTry match{
                  case Success(duplicatesExists) =>{

                    if(!duplicatesExists){
                      examsList.foreach((x:Exams)=>ExamsDAO.insertExams(x))
                      println("Exams.json succesful!")
                    }
                    else
                      println("Duplicate exists, cancelling batch to load JSON file for Exams.")

                  }
                  case Failure(e) => println(e.getMessage())
                }
                    
            }
            case None => println("Loading JSON error.")
        }
  }
}
