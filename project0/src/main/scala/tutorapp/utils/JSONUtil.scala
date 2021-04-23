package tutorapp.utils


import scalaz._, Scalaz._
import argonaut._, Argonaut._
import ArgonautScalaz._


object JSONUtil {

    implicit def StudentCodecJson: CodecJson[Student] =
            casecodec14(Student.apply(), Student.unapply())("studentID", "fname", "lname", "classGrade", "classID")


    /**Uses Argonaut library to Parse and Decode the Json file that is a string**/
    def getStudentList(studentString: String) : Option[List[Student]] = {
         // Decode ignoring error messages
        val option: Option[List[Student]] = 
                    Parse.decodeOption[List[Student]](studentString)
        option
    }
    
    
}
