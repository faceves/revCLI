package tutorapp

import tutorapp.utils.ConnectionUtil
import tutorapp.utils.FileUtil
import tutorapp.utils.JSONUtil
import scala.util.matching.Regex
import scala.io.StdIn
import scala.util.{Try, Success,Failure}
import java.io.FileNotFoundException
import org.postgresql.util.PSQLException
import tutorapp.dao.StudentDAO
import tutorapp.dao.ExamsDAO
import tutorapp.objHolders.Student
import scala.collection.mutable.ArrayBuffer

class Cli {
    private val commandPattern: Regex =  "(\\w+)\\s*(.*)".r
    private val jsonFileName = "Students.json"
    private val jsonFilePath = "."

    def run(){
        //check if connection is established otherwise exit
        val conn = Try(ConnectionUtil.getConnection())
        conn match{
            case Success(_) => {}
            case Failure(e) =>{
                println("Database connection not established, please retry again.")
                System.exit(1)
            }
        }
        loadJSONFile(jsonFilePath, jsonFileName)
        menu()
    }
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
                case commandPattern(cmd, arg) if cmd == "getstudentexams" => {
                        println(ExamsDAO.getStudentExamsExtensive(1))                    
                }
                case commandPattern(cmd, arg) if cmd == "update" => {
                        println(StudentDAO.updateStudentClassGrade(6, 95.25f))                  
                }
                case commandPattern(cmd, arg) if cmd == "add" => {
                        println(StudentDAO.insertStudent("frank", "ace",99.99f,2))                    
                }
                case commandPattern(cmd, arg) if cmd == "addexams" => {
                        println(ExamsDAO.insertExams(22.3f,22.3f,22.3f,22.3f,10))                    
                }
                case commandPattern(cmd, arg) if cmd == "update" => {
                        runStudentListMenu()                    
                }
                case commandPattern(cmd, arg) if cmd == "delete" => {
                        println(StudentDAO.deleteStudent(10))                  
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
        println("\nWelcome to the Tutorapp!")
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
            "student: Retrieves a student including their class grade.",
            "studentexams: Retrieves a student exams grades.",
            "update: Updates a student's midterm exam.",
            "add: Add's a student.",
            "addexams: Add's a student exams.",
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

    def loadJSONFile(filepath: String, filename: String) : Unit = {
        var fileString = FileUtil.getTextContent(filename)
        var studentData = JSONUtil.getStudentList(fileString)
        studentData match{
            case Some(studentList) =>{
                //val conn = ConnectionUtil.getConnection()
                studentList.foreach((x:Student)=>StudentDAO.insertStudent(x))
            }
            case None => println("Loading JSON error.")
        }

    }


 /**
    def runLoadJsonMenu(argFilePath: String):Unit = {
        var contLoop = true;
        var userInput =  ""
        val extractArgs = "("
        do{
            println("Enter file path and file name (e.g: ./ FileName.json) :\n")
            //does not do any user input checking for formatting for args
            StdIn.readLine() match{
                case 

            }

            val studentList: ArrayBuffer[Student] = StudentDAO.getStudentListFromClass(userInput.toInt)
            studentList.foreach(println)
            print("\nDone? y or n\n")
            userInput = StdIn.readLine()
            if(userInput == "y")
                userDone = true
        }while(!userDone)
    }
    **/

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
