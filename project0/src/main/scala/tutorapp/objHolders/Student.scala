package tutorapp.objHolders

import java.sql.ResultSet
import scalaz._, Scalaz._
import argonaut._, Argonaut._
import ArgonautScalaz._


case class Student(studentID: String, fname: String, lname: String, classGrade: Float, classID: Int){

  def columnHeader(): String = {
    f"Student ID |  Name | Class Grade | Class ID"
  }
  override def toString() : String = {
    f"$studentID | $fname%-15s | $lname%-15s | $classGrade%3.2f | $classID%6d"
  }
}

object Student{
  
  def apply():Student = {objectifyResultSet()}

  /**Transforms the result set information into objects. **/
  def objectifyResultSet(rs : ResultSet): Student ={
    //use the generated case class apply that the companion object also gets to create an instance
    val studentObj =  apply(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getFloat(4),
                        rs.getInt(5)
                      )
    studentObj
  }

  def objectifyResultSet(studentID: String = "", fname: String = "", lname: String ="", 
                          classGrade: Float = 0.0f, classID: Int = 0) : Student = {
    apply(studentID, fname, lname, classGrade, classID)
  }
}
