package tutorapp.utils


import scalaz._, Scalaz._
import argonaut._, Argonaut._
import ArgonautScalaz._

import scala.collection.immutable.List
import tutorapp.objHolders.Student
import tutorapp.objHolders.Exams


object JSONUtil {
    //http://argonaut.io/doc/codec/
    implicit def StudentCodecJson: CodecJson[Student] =
            casecodec5(Student.apply, Student.unapply)("studentID", "fname", "lname", "classGrade", "classID")
    

    /**Uses Argonaut library to Parse and Decode the Json file that is a string**/
    def getStudentList(studentString: String) : Option[List[Student]] = {
         // Decode ignoring error messages
        val option: Option[List[Student]] = 
                    Parse.decodeOption[List[Student]](studentString)
        option
    }


    implicit def ExamsCodecJson: CodecJson[Exams] =
            casecodec5(Exams.apply, Exams.unapply)("exam1","exam2","midterm","finExam","studentID")

    def getExamsList(examsString: String) : Option[List[Exams]] = {
         // Decode ignoring error messages
        val option: Option[List[Exams]] = 
                    Parse.decodeOption[List[Exams]](examsString)
        option
    }
    
    
}
