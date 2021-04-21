package tutorapp

import tutorapp.utils.ConnectionUtil
import scala.util.matching.Regex
import scala.io.StdIn
import java.io.FileNotFoundException
import org.postgresql.util.PSQLException
import tutorapp.dao.StudentDAO
import tutorapp.objHolders.Student
import scala.collection.mutable.ArrayBuffer

class Cli {
    private val commandPattern: Regex =  "(\\w+)\\s*(.*)".r

    def menu(){
        var contMenuLoop : Boolean = true

        printGreeting()
        do{
            printMenuOptions()

            var userInput:String = StdIn.readLine() //blocking
            
            userInput match {
                case commandPattern(cmd, arg) if cmd == "studentlist" => {
                        runStudentListMenu()                    
                }
                case commandPattern(cmd, arg) if cmd == "student" => {
                        println(StudentDAO.getStudent(0))                    
                }
                case commandPattern(cmd, arg) if cmd == "update" => {
                        println(StudentDAO.updateStudentClassGrade(6, 95.25f))                  
                }
                case commandPattern(cmd, arg) if cmd == "add" => {
                        println(StudentDAO.insertStudent("frank", "ace",99.99f,2))                    
                }
                case commandPattern(cmd, arg) if cmd == "update" => {
                        runStudentListMenu()                    
                }
                case commandPattern(cmd, arg) if cmd == "delete" => {
                        println(StudentDAO.deleteStudent(7))                  
                }
                case commandPattern(cmd, arg) if cmd == "exit" => {
                    contMenuLoop = false
                }
                case commandPattern(cmd, arg) => {
                    println(s"Parsed command $cmd with args $arg  did not correspond to an option")
                }
                case _ => {
                    println("Failed to parse command.")
                }
            }

        }while(contMenuLoop)
    }

    /** Display welcome message to console
     * Input: nothing
     * Output: nothing
     * */
    private def printGreeting():Unit = {
        println("Welcome to the Tutorapp!\n")
    }

    /** Display Menu Options to console
     * Input: nothing
     * Output: nothing
     * */
    private def printMenuOptions():Unit = {
        List(
            "\nMenu Options:",
            "-------------",
            "studentlist: Retrieves a student list given a class id.",
            "student: Retrieves a student including their grade.",
            "update: Updates a student's midterm exam.",
            "add: Add's a student.",
            "delete: Delete's a student given their student id.",
            "exit: Exits the app.\n"
            ).foreach(println)
    }

    def runStudentListMenu(): Unit = {
        var userDone = false
        var userInput =  ""

        //val commandPattern = 
        do{
            println("Enter the Class ID to retrieve the list: ")
            userInput = StdIn.readLine()
            val studentList: ArrayBuffer[Student] = StudentDAO.getStudentListFromClass(userInput.toInt)
            studentList.foreach(println)
            print("\nDone? y or n\n")
            userInput = StdIn.readLine()
            if(userInput == "y")
                userDone = true
        }while(!userDone)
    }
/**
    def runUpdateStudentMenu(): Unit = {
        var userDone = false
        var userInput =  ""

        do{
            println("Enter the Student ID as an int then the Class Grade as a float (ex. 2 99.5 ): ")
            userInput = StdIn.readLine()
            val commandPattern: Regex = "([1-9])\\s+"
            val studentList: ArrayBuffer[Student] = StudentDAO.getStudentListFromClass(userInput.toInt)
            studentList.foreach(println)
            print("\nDone? y or n\n")
            userInput = StdIn.readLine()
            if(userInput == "y")
                userDone = true
        }while(!userDone)
    } **/
}
