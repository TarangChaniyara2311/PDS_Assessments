object CaseClassAssessment {

  // Define a case class
  case class Person(name: String, age: Int, city: String)

  // 1. Remove people older than 30
  def filterPeople(people: List[Person]): List[Person] = {
    return people.filter(person => person.age <= 30)
  }

  // 2. Group people by their city
  def groupByCity(people: List[Person]): Map[String, List[Person]] = {
    return people.groupBy(person => person.city)
  }

  // 3. Transform each Person into a descriptive string
  def describePeople(people: List[Person]): List[String] = {
    return people.map(person =>
      s"${person.name} is ${person.age} years old and lives in ${person.city}"
    )
  }

  // 4. Sort people by age in ascending order
  def sortByAge(people: List[Person]): List[Person] = {
    return people.sortBy(person => person.age)
  }

  def main(args: Array[String]): Unit = {

    // Create five different individuals
    val people = List(
      Person("Tarang", 23, "Rajkot"),
      Person("Priyanka", 28, "Chennai"),
      Person("Srinivas", 35, "Vellore"),
      Person("Rahul", 30, "Rajkot"),
      Person("Anjali", 32, "Chennai")
    )

    println("Original list:")
    people.foreach(person => println(person))

    println("\nPeople aged 30 or younger:")
    filterPeople(people).foreach(person => println(person))

    println("\nPeople grouped by city:")
    for ((city, residents) <- groupByCity(people).toList.sortBy(_._1)) {
      println(city + ": " + residents.map(person => person.name).mkString(", "))
    }

    println("\nPerson descriptions:")
    describePeople(people).foreach(description => println(description))

    println("\nPeople sorted by age (ascending):")
    sortByAge(people).foreach(person => println(person))
  }
}