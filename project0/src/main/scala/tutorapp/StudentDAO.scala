package tutorapp

//import com.revature.bookapp.model.Book
//import com.revature.bookapp.utils.ConnectionUtil
import scala.util.Using
import scala.collection.mutable.ArrayBuffer

object StudentDAO {
    /**  A student database access object with CRUD operations. All Database logic for Student is located in this class.**/
    case class Exams(){}
    case class Student(){}

     /** Retrieves all Books from the book table in the db
    *
    * @return
    */

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
