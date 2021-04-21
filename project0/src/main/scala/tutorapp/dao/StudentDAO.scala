package tutorapp.dao

//import com.revature.bookapp.model.Book
//import com.revature.bookapp.utils.ConnectionUtil
import scala.util.Using
import scala.collection.mutable.ArrayBuffer
import scala.util.Try
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
                                println("in catch")
                                ArrayBuffer[Student]()}
        }
        finally{
            if(conn!=null)
                conn.close()
        }
        
    }

    def updateStudentClassGrade(studentID: Int, classGrade: Float): Try[Boolean] = {
        
        Using.Manager{ use =>
            //get connection
            val conn: Connection = use(ConnectionUtil.getConnection())
            //prepare postgresql statement
            val statement = use(conn.prepareStatement("UPDATE Student SET classGrade = ? WHERE studentID = ?"))
            statement.setInt(2, studentID)
            statement.setFloat(1, classGrade)
            statement.execute()
            //check to make sure it updated ----- this last statement result gets returned to Manager which is wrapped into a Try
            statement.getUpdateCount() > 0 
        }
        
    }

    def insertStudent():Unit = {

    }

    def deleteStudent():Unit = {
        
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
