package tutorapp.objHolders

import java.sql.ResultSet
import scalaz._, Scalaz._
import argonaut._, Argonaut._
import ArgonautScalaz._

final case class Exams(exam1: Float, exam2: Float, midterm: Float, finExam: Float, studentID: String){
    
    def columnHeader(): String = {
        f" Exam1 | Exam 2 | Midterm | Final Exam | Student ID"
    }

    override def toString() : String = {
        f"$exam1%3.2f | $exam2%3.2f | $midterm%3.2f | $finExam%3.2f | $studentID"
    }
}

object Exams{
    
    def apply():Exams = {objectifyResultSet()}

    def objectifyResultSet(rs : ResultSet): Exams = {
        val examsObj =  apply(
                            rs.getFloat(1),
                            rs.getFloat(2),
                            rs.getFloat(3),
                            rs.getFloat(4),
                            rs.getString(5)
                        )
        examsObj
    }

    def objectifyResultSet(exam1: Float = 0.0f, exam2: Float = 0.0f, midterm: Float = 0.0f, 
                            finalExam: Float = 0.0f, studentID: String = "") : Exams = {
        apply(exam1, exam2, midterm, finalExam, studentID)
    }
}
