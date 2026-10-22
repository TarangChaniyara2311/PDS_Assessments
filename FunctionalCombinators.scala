object FunctionalCombinators {

  def main(args: Array[String]): Unit = {

    // 1. Create a list of integers from 1 to 10
    val numbers = (1 to 10).toList
    println("Original list: " + numbers)

    // 2. Use filter to select only even numbers
    val evenNumbers = numbers.filter(number => number % 2 == 0)
    println("Even numbers: " + evenNumbers)

    // 3. Use map to multiply each element by 3
    val multipliedNumbers = numbers.map(number => number * 3)
    println("Multiplied by 3: " + multipliedNumbers)

    // 4. Use flatMap to pair each integer with its square
    val squarePairs = numbers.flatMap(number => List((number, number * number)))
    println("Numbers and their squares: " + squarePairs)

    // 5. Use foldLeft to calculate the product
    val product = numbers.foldLeft(1)((result, number) => result * number)
    println("Product of all elements: " + product)
  }
}