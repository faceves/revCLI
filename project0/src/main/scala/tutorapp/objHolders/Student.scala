package tutorapp.objHolders

import java.sql.ResultSet

case class Student(studentID: Int, fname: String, lname: String, classGrade: Float, classID: Int){}

object Student{
  
  def apply():Student = {objectifyResultSet()}

  /**Transforms the result set information into objects. **/
  def objectifyResultSet(rs : ResultSet): Student ={
    //use the generated case class apply that the companion object also gets to create an instance
    val studentObj =  apply(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getFloat(4),
                        rs.getInt(5)
                      )
    studentObj
  }

  def objectifyResultSet(studentID: Int = 0, fname: String = "", lname: String ="", 
                          classGrade: Float = 0.0f, classID: Int = 0) : Student = {
    apply(studentID, fname, lname, classGrade, classID)
  }
}
