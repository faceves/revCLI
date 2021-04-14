trait DataAccessControllerInt {
   def getAllStudents(): List[String]
   def createStudent(student:String) : String
   def getStudent(student:String) : String
   def updateStudent(student:String) : String
   def deleteStudent(student: String) : String
}
