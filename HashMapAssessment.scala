import scala.collection.mutable

object HashMapAssessment {

  // Merge two HashMaps, keeping the higher grade for duplicate keys
  def mergeMaps(
      map1: mutable.HashMap[String, Int],
      map2: mutable.HashMap[String, Int]
  ): mutable.HashMap[String, Int] = {

    val merged = mutable.HashMap[String, Int]()

    for ((name, grade) <- map1) {
      merged(name) = grade
    }

    for ((name, grade) <- map2) {
      if (merged.contains(name)) {
        merged(name) = math.max(merged(name), grade)
      } else {
        merged(name) = grade
      }
    }

    return merged
  }

  // Store multiple grades for a key using a list
  def insertWithChaining(
      map: mutable.HashMap[String, List[Int]],
      name: String,
      grade: Int
  ): Unit = {

    val existingGrades = map.getOrElse(name, List.empty[Int])
    map(name) = existingGrades :+ grade
  }

  def main(args: Array[String]): Unit = {

    // 1. Create a HashMap of student names and grades
    val students = mutable.HashMap[String, Int]()

    students("Tarang") = 85
    students("Priyanka") = 90
    students("Srinivas") = 78

    println("Tarang's original grade: " + students("Tarang"))

    // Insert a duplicate key with a different grade
    students("Tarang") = 95

    println("Tarang's updated grade: " + students("Tarang"))

    // 2. Create another HashMap
    val otherStudents = mutable.HashMap(
      "Tarang" -> 88,
      "Priyanka" -> 96,
      "Rahul" -> 82
    )

    // Merge both maps
    val mergedStudents = mergeMaps(students, otherStudents)

    println("\nMerged grades (higher grade retained):")
    for ((name, grade) <- mergedStudents.toList.sortBy(_._1)) {
      println(name + ": " + grade)
    }

    // 3. Use lists to preserve multiple grades for each key
    val chainedStudents = mutable.HashMap[String, List[Int]]()

    insertWithChaining(chainedStudents, "Tarang", 85)
    insertWithChaining(chainedStudents, "Tarang", 95)
    insertWithChaining(chainedStudents, "Priyanka", 90)
    insertWithChaining(chainedStudents, "Priyanka", 96)
    insertWithChaining(chainedStudents, "Srinivas", 78)

    println("\nGrades stored using lists:")
    for ((name, grades) <- chainedStudents.toList.sortBy(_._1)) {
      println(name + ": " + grades.mkString(", "))
    }
  }
}
