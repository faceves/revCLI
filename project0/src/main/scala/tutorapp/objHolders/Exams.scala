package tutorapp.objHolders
import java.sql.ResultSet

final case class Exams(exam1: Float, exam2: Float, midterm: Float, finExam: Float, studentID: Int){}

object Exams{
    /**Transforms the result set information into objects. **/
    def objectifyResultSet(rs: ResultSet) : Exams = {
        apply(
            rs.getFloat(1),
            rs.getFloat(2),
            rs.getFloat(3),
            rs.getFloat(4),
            rs.getInt(5)
        )
    }
}
