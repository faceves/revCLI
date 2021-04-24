package tutorapp.objHolders

import java.sql.ResultSet
import scalaz._, Scalaz._
import argonaut._, Argonaut._
import ArgonautScalaz._

final case class Exams(exam1: Float, exam2: Float, midterm: Float, finExam: Float, studentID: Int){}

object Exams{
    
    def apply():Exams = {objectifyResultSet()}

    def objectifyResultSet(rs : ResultSet): Exams = {
        val examsObj =  apply(
                            rs.getFloat(1),
                            rs.getFloat(2),
                            rs.getFloat(3),
                            rs.getFloat(4),
                            rs.getInt(5)
                        )
        examsObj
    }

    def objectifyResultSet(exam1: Float = 0.0f, exam2: Float = 0.0f, midterm: Float = 0.0f, 
                            finalExam: Float = 0.0f, studentID: Int = 0) : Exams = {
        apply(exam1, exam2, midterm, finalExam, studentID)
    }
}
