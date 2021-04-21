package tutorapp.dao

import scala.util.Using
import scala.util.{Try, Success, Failure}
import java.sql.Connection
import tutorapp.utils.ConnectionUtil
import tutorapp.objHolders.Exams

object ExamsDAO {
  def insertExams(exam1: Float, exam2: Float, miderm: Float, finalExam: Float,
                 studentID: Int): Try[Boolean] = {
        Using.Manager{ use =>
            val conn: Connection = use(ConnectionUtil.getConnection())
            val statement = use(conn.prepareStatement("INSERT INTO Exams (exam1, exam2, midterm, finalexam, studentid)" +
                                                        "values (?, ? ,? ,?,?)"))                                         
            statement.setFloat(1, exam1)
            statement.setFloat(2, exam2)
            statement.setFloat(3, miderm)
            statement.setFloat(4, finalExam)
            statement.setInt(5, studentID)
            //exectues and returns the number of rows updated/changed if any
            statement.executeUpdate() > 0
            
        }   
  }

  def getStudentExams(studentID: Int): Try[Exams] = {
      Using.Manager{ use =>
            val conn: Connection = use(ConnectionUtil.getConnection())
            val statement = use(conn.prepareStatement("SELECT * FROM Exams WHERE studentid= ?"))
            statement.setInt(1, studentID)
            statement.execute()
            val rs = use(statement.getResultSet())
            //grab the one record
            if(rs.next())
                Exams.objectifyResultSet(rs)
            else
                throw new Exception(s"No Exams found for Student with ID: $studentID!") //Manager will catch and wrap it in a Failure
        }
  }
}
