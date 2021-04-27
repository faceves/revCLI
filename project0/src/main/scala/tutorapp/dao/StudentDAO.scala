package tutorapp.dao

import scala.util.Using
import scala.collection.mutable.ArrayBuffer
import scala.util.{Try, Success, Failure}
import java.sql.Connection
import tutorapp.utils.ConnectionUtil
import tutorapp.objHolders.Student

object StudentDAO {
    /**  A student database access object with CRUD operations. All Database logic for Student is located in this class.**/

     /** Retrieves all Books from the book table in the db
    *
    * @return
    */

    def getStudentListFromClass(classID: Int) : ArrayBuffer[Student] = {
        var conn: Connection = null;
        try{
            //get connection
            conn = ConnectionUtil.getConnection()
            //prepare postgresql statement
            val statement = conn.prepareStatement("SELECT * FROM Student WHERE classID = ?")
            statement.setInt(1,classID)
            statement.execute()
            //grab result set from database
            val rs = statement.getResultSet()
            //proccess data into ArrayBuffer
            val studentList:ArrayBuffer[Student] = ArrayBuffer[Student]()
            while(rs.next()){
                studentList.+=(Student.objectifyResultSet(rs))
            }
            studentList 
        }
        catch{
            case e: Exception => {println(e.getMessage())
                                ArrayBuffer[Student]()}
        }
        finally{
            if(conn!=null)
                conn.close()
        }
        
    }

    def getStudent(studentID: String): Try[Student] = {

        Using.Manager{ use =>
            //get connection
            val conn: Connection = use(ConnectionUtil.getConnection())
            //prepare postgresql statement
            val statement = use(conn.prepareStatement("SELECT * FROM Student WHERE studentid= ?"))
            statement.setString(1, studentID)
            statement.execute()
            val rs = use(statement.getResultSet())
            //grab the one record
            if(rs.next())
                Student.objectifyResultSet(rs)
            else
                throw new Exception(s"No student found with id: $studentID!") //Manager will catch and wrap it in a Failure
        }    
    }

    def updateStudentClassGrade(studentID: String, classGrade: Float): Try[Boolean] = {
        
        Using.Manager{ use =>
            //get connection
            val conn: Connection = use(ConnectionUtil.getConnection())
            //prepare postgresql statement
            val statement = use(conn.prepareStatement("UPDATE Student SET classGrade = ? WHERE studentID = ?"))
            statement.setString(2, studentID)
            statement.setFloat(1, classGrade)
            statement.execute()
            //check to make sure it updated ----- this last statement result gets returned to Manager which is wrapped into a Try
            statement.getUpdateCount() > 0 
        }
        
    }

    def insertStudent(studentID: String, fname: String, lname: String, classGrade: Float, classID: Int): Try[Boolean]= {
        
        Using.Manager{ use =>
            //get connection
            val conn: Connection = use(ConnectionUtil.getConnection())
            //prepare postgresql statement
            val statement = use(conn.prepareStatement("INSERT INTO Student (studentid, fname, lname, classgrade, classid)" +
                                                        "values (?,?, ? ,? ,?)"))
            statement.setString(1, studentID)                                         
            statement.setString(2, fname)
            statement.setString(3, lname)
            statement.setFloat(4, classGrade)
            statement.setInt(5, classID)
            //exectues and returns the number of rows updated/changed if any
            statement.executeUpdate() > 0
            
        }   
    }

    def insertStudent(student: Student): Try[Boolean]= {
        insertStudent(student.studentID, student.fname, student.lname, student.classGrade, student.classID)   
    }

    def deleteStudent(studentID: Int):Try[Boolean] = {
        Using.Manager{ use =>
            //get connection
            val conn: Connection = use(ConnectionUtil.getConnection())
            //prepare postgresql statement
            val statement = use(conn.prepareStatement("DELETE FROM Student WHERE studentid = ?"))                                         
            statement.setInt(1, studentID)
            //exectues and returns the number of rows updated/changed if any
            statement.executeUpdate() > 0
            
        } 
    }



}
