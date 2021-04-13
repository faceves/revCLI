import scala.util.matching.Regex
import scala.io.StdIn
import java.io.FileNotFoundException

class Cli {
    private val trimCommandPattern: Regex =  "(\\w+)\\s*(.*)".r

    def menu(){
        var contMenuLoop : Boolean = true

        do{
            printGreeting()
            printMenuOptions()

            var userInput:String = StdIn.readLine()
            


        }while(contMenuLoop)
    }

    /** Display welcome message to console
     * Input: nothing
     * Output: nothing
     * */
    private def printGreeting():Unit = {

    }

    /** Display Menu Options to console
     * Input: nothing
     * Output: nothing
     * */
    private def printMenuOptions():Unit = {

    }
}
