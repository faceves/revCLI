package tutorapp.dao

import scala.util.Using
import scala.util.{Try, Success, Failure}
import java.sql.Connection
import tutorapp.utils.ConnectionUtil
import tutorapp.objHolders.Exams
import tutorapp.objHolders.Student

object ExamsDAO {
  def insertExams(exam1: Float, exam2: Float, miderm: Float, finalExam: Float,
                 studentID: String): Try[Boolean] = {
        Using.Manager{ use =>
            val conn: Connection = use(ConnectionUtil.getConnection())
            val statement = use(conn.prepareStatement("INSERT INTO Exams (exam1, exam2, midterm, finalexam, studentid)" +
                                                        "values (?, ? ,? ,?,?)"))                                         
            statement.setFloat(1, exam1)
            statement.setFloat(2, exam2)
            statement.setFloat(3, miderm)
            statement.setFloat(4, finalExam)
            statement.setString(5, studentID)
            //exectues and returns the number of rows updated/changed if any
            statement.executeUpdate() > 0
            
        }   
  }

  def getStudentExams(studentID: String): Try[Exams] = {
      Using.Manager{ use =>
            val conn: Connection = use(ConnectionUtil.getConnection())
            val statement = use(conn.prepareStatement("SELECT * FROM Exams WHERE studentid = ?"))
            statement.setString(1, studentID)
            statement.execute()
            val rs = use(statement.getResultSet())
            //grab the one record
            if(rs.next())
                Exams.objectifyResultSet(rs)
            else
                throw new Exception(s"No Exams found for Student with ID: $studentID!") //Manager will catch and wrap it in a Failure
        }
  }

  def getStudentExamsExtensive(studentID: String): Try[(Student,Exams)] = {
        Using.Manager{ use =>
                val conn: Connection = use(ConnectionUtil.getConnection())
                val statement = use(conn.prepareStatement("SELECT fname, lname, student.studentid, classgrade, exam1, exam2," +
                                                        " midterm, finalexam FROM student INNER JOIN Exams " +
                                                        "ON Student.studentid = Exams.studentid WHERE student.studentid = ?;"))
                statement.setString(1, studentID)
                statement.execute()
                val rs = use(statement.getResultSet())
                //grab the one record
                if(rs.next()){
                    //return a tuple of (Student, Exams)
                    ( Student.objectifyResultSet(rs.getString(3), rs.getString(1), rs.getString(2),rs.getFloat(4)),
                      Exams.objectifyResultSet(rs.getFloat(5), rs.getFloat(6), rs.getFloat(7), rs.getFloat(8)) )
                }
                else
                    throw new Exception(s"No Student or Exams found for Student with ID: $studentID!") //Manager will catch and wrap it in a Failure
        }
    }
}
