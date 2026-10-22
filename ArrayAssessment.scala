object ArrayAssessment {

  // Function to calculate the sum
  def sumArray(arr: Array[Int]): Int = {
    var total = 0

    for (number <- arr) {
      total = total + number
    }

    return total
  }

  // Function to concatenate strings separated by commas
  def concatenateFruits(arr: Array[String]): String = {
    return arr.mkString(", ")
  }

  def main(args: Array[String]): Unit = {

    // Create an integer array
    val numbers = Array(2, 5, 9, 14, 20)

    println("Sum of elements: " + sumArray(numbers))

    // Create an array of five fruits
    val fruits = Array("Apple", "Banana", "Mango", "Orange", "Grapes")

    println("Fruits: " + concatenateFruits(fruits))

    // Print each integer
    println("Individual integers:")
    for (number <- numbers) {
      println(number)
    }
  }
}