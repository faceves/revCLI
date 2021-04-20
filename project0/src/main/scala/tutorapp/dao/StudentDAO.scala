package tutorapp.dao

//import com.revature.bookapp.model.Book
//import com.revature.bookapp.utils.ConnectionUtil
import scala.util.Using
import scala.collection.mutable.ArrayBuffer
import tutorapp.utils.ConnectionUtil

object StudentDAO {
    /**  A student database access object with CRUD operations. All Database logic for Student is located in this class.**/
    case class Exams(){}
    case class Student(){}

     /** Retrieves all Books from the book table in the db
    *
    * @return
    */

    def getStudentListFromClass(classID: String) : ArrayBuffer[Student] = {
        try{
            //get connection
            val conn = ConnectionUtil.getConnection()
            //prepare postgresql statement
            val statement: prepareStatement = conn.prepareStatement("SELECT First Name, Last Name, StudentID FROM Student WHERE classID = ?")
            statement.setString(1,classID)
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
            case e: Exception => e.printStackTrace()
        }
        finally{
            if(conn!=null)
                conn.close()
        }
        
    }

    def updateStudentClassGrade(studentID: Int, classGrade: Float): Try[Boolean] = {
        //get connection
        val conn = ConnectionUtil.getConnection()
        Using.Manager{ use =>
            //prepare postgresql statement
            val statement: prepareStatement = use(conn.prepareStatement("UPDATE Student SET classGrade = ? WHERE studentID = ?"))
            statement.setString(2, studentID)
            statement.setString(1, classGrade)
            statement.execute()
            //check to make sure it updated ----- this last statement resultgets returned to Manager which is wrapped into a Try
            stmt.getUpdateCount() > 0 
        }
        
    }

    /**
    def getAll(): Seq[Student] = {
        val conn = ConnectionUtil.getConnection();
        Using.Manager { use =>
        val stmt = use(conn.prepareStatement("SELECT * FROM book;"))
        stmt.execute()
        val rs = use(stmt.getResultSet())
        // lets use an ArrayBuffer, we're adding one element at a time
        val allBooks: ArrayBuffer[Student] = ArrayBuffer()
        while (rs.next()) {
            allBooks.addOne(Student.fromResultSet(rs))
        }
        allBooks.toList
        }.get
        // the .get retrieves the value from inside the Try[Seq[Book]] returned by Using.Manager { ...
        // it may be better to not call .get and instead return the Try[Seq[Book]]
        // that would let the calling method unpack the Try and take action in case of failure
        
    }
    **/

}
