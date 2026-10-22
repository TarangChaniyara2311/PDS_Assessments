//> using scala "2.13.16"
//> using jvm "17"
//> using dep "org.scalanlp::breeze:2.1.0"
//> using dep "org.plotly-scala::plotly-render:0.8.5"

import breeze.linalg.{DenseVector, sum}
import scala.io.Source

import plotly._
import plotly.Plotly._

object BreezeAssessment {

  case class FruitRecord(
      date: String,
      fruit: String,
      price: Double,
      quantity: Double
  )

  def main(args: Array[String]): Unit = {

    // 1. Load the supplied simple CSV dataset
    val source = Source.fromFile("fruits.csv")

    val records = try {
      source.getLines()
        .drop(1)                       // Skip the header
        .filter(_.trim.nonEmpty)        // Ignore empty lines
        .map { line =>
          val columns = line.split(",").map(_.trim)

          FruitRecord(
            columns(0),
            columns(1),
            columns(2).toDouble,
            columns(3).toDouble
          )
        }
        .toList
    } finally {
      source.close()
    }

    require(records.nonEmpty, "The dataset must contain records.")

    // 2. Group records by fruit type
    val groupedFruits = records.groupBy(_.fruit)

    // 3. Calculate total revenue for each fruit using Breeze
    val revenues = groupedFruits.toSeq.map {
      case (fruit, fruitRecords) =>

        val prices = DenseVector(
          fruitRecords.map(_.price).toArray
        )

        val quantities = DenseVector(
          fruitRecords.map(_.quantity).toArray
        )

        // Multiply each price by its corresponding quantity
        val rowRevenues = prices *:* quantities

        // Add all row revenues for this fruit
        val totalRevenue = sum(rowRevenues)

        (fruit, totalRevenue)
    }.sortBy(_._1)

    // 4. Print the total revenue for each fruit
    println("Total revenue for each fruit:")

    revenues.foreach {
      case (fruit, revenue) =>
        println(f"$fruit: Rs. $revenue%.2f")
    }

    // 5. Prepare the bar chart data
    val fruitNames = revenues.map(_._1)
    val revenueValues = revenues.map(_._2)

    // 6. Create an interactive bar chart using Plotly Scala
    val chart = Bar(fruitNames, revenueValues)

    Seq(chart).plot(
      title = "Total Revenue by Fruit (INR)"
    )

    println("\nBar chart generated using Plotly.")
  }
}
