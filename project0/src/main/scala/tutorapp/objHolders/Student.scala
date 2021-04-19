package tutorapp.objHolders

import java.sql.ResultSet

case class Student(fname: String, lname: String, studentID: Int, classID: Int, classGrade: Float){}

object Student{
  /**Transforms the result set information into objects. **/
  def objectifyResultSet(rs : ResultSet): Student ={
    //use the generated case class apply that the companion object also gets to create an instance
    val studentObj =  apply(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getInt(3),
                        rs.getInt(4),
                        rs.getFloat(5)
                      )
    studentObj
  }
}
