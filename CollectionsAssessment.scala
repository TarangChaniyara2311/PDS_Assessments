object CollectionsAssessment {

  // Function to return a new list with each element squared
  def squareList(numbers: List[Int]): List[Int] = {
    return numbers.map(number => number * number)
  }

  // Function to return the intersection of two sets
  def findIntersection(set1: Set[Char], set2: Set[Char]): Set[Char] = {
    return set1.intersect(set2)
  }

  def main(args: Array[String]): Unit = {

    // 1. List of the first five prime numbers
    val primes = List(2, 3, 5, 7, 11)

    println("Prime numbers: " + primes)
    println("Squared numbers: " + squareList(primes))

    // 2. Set of unique characters from "hello world"
    val characters = "hello world".toSet

    // Sort for consistent output; quotes make the space visible
    println("Unique characters: " +
      characters.toList.sorted.map(ch => "'" + ch.toString + "'").mkString(", "))

    // Another set to demonstrate intersection
    val otherCharacters = Set('h', 'o', 'w', 'z')

    val commonCharacters = findIntersection(characters, otherCharacters)

    println("Intersection: " +
      commonCharacters.toList.sorted.mkString(", "))

    // 3. Tuple containing an integer, a string, and a boolean
    val details = (21, "Tarang", true)

    // Access and print each tuple element
    println("Integer element: " + details._1)
    println("String element: " + details._2)
    println("Boolean element: " + details._3)
  }
}