package tutorapp.objHolders

import java.sql.ResultSet
import scalaz._, Scalaz._
import argonaut._, Argonaut._
import ArgonautScalaz._


case class Student(studentID: String, fname: String, lname: String, classGrade: Float, classID: Int){

  override def toString() : String = {
    f"$studentID%-10s $fname%-15s $lname%-15s $classGrade%11.2f $classID%8d"
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

  def columnHeader(): String = {

    "\nStud ID" + " " *4  + "First Name" + " " *6+ "Last Name"+ " "*12+ "C Grade" + " " *4+ "C ID"
  }

  def printColumnHeader(): Unit = {
    println(columnHeader())
    println("-"*67)
  }
}
