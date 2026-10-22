import org.saddle._
import org.saddle.io._
import spire.math.Rational
import vegas._

import java.io.PrintWriter

object FruitStatistics {

  // Convert a decimal price into an exact fraction
  def exactPrice(text: String): Rational = {
    val decimal = BigDecimal(text.trim).bigDecimal
    val numerator = BigInt(decimal.unscaledValue())
    val scale = decimal.scale()

    if (scale >= 0) {
      Rational(numerator, BigInt(10).pow(scale))
    } else {
      Rational(numerator * BigInt(10).pow(-scale), BigInt(1))
    }
  }

  // Calculate the exact mean
  def mean(prices: Seq[Rational]): Rational = {
    require(prices.nonEmpty, "Prices cannot be empty.")

    val total = prices.foldLeft(Rational(0)) {
      (result, price) => result + price
    }

    total / Rational(prices.size)
  }

  // Calculate population variance: sum of squared differences / N
  def variance(prices: Seq[Rational]): Rational = {
    val average = mean(prices)

    val squaredDifferences = prices.map { price =>
      val difference = price - average
      difference * difference
    }

    mean(squaredDifferences)
  }

  def main(args: Array[String]): Unit = {

    // 1. Load the dataset using Saddle
    val file = CsvFile("fruits.csv")

    val data = CsvParser
      .parse(List(0, 1, 2, 3))(file)
      .withColIndex(0)
      .sortedRows(0)

    require(data.numRows > 0, "The dataset is empty.")

    // 2. Find the different fruit names
    val fruits = (0 until data.numRows)
      .map(i => data.rowAt(i).raw(1).trim)
      .distinct
      .sorted

    println("Fruit price statistics")
    println("Variance type: population variance\n")

    // 3. Filter each fruit using Saddle
    // Calculate its mean and variance using Spire
    for (fruit <- fruits) {

      val fruitData = data.rfilter { row =>
        row.raw(1).trim == fruit
      }

      val prices = (0 until fruitData.numRows).map { i =>
        exactPrice(fruitData.rowAt(i).raw(2))
      }

      val average = mean(prices)
      val priceVariance = variance(prices)

      println(fruit)
      println("  Number of records: " + prices.size)
      println("  Exact mean: " + average)
      println("  Exact variance: " + priceVariance)

      // Convert only for readable display
      println(f"  Mean price: Rs. ${average.toDouble}%.2f")
      println(f"  Population variance: ${priceVariance.toDouble}%.2f")
      println()
    }

    // 4. Prepare the price history for visualization
    val chartData = (0 until data.numRows).map { i =>
      val row = data.rowAt(i)

      Map[String, Any](
        "Date" -> row.raw(0),
        "Fruit" -> row.raw(1).trim,
        "Price" -> exactPrice(row.raw(2)).toDouble
      )
    }

    // 5. Create a separate colored line for each fruit
    val plot = Vegas("Fruit Prices Over Time")
      .withData(chartData)
      .mark(Line)
      .encodeX("Date", Temp)
      .encodeY("Price", Quant)
      .encodeColor("Fruit", Nom)

    // 6. Save an interactive browser chart
    val html = s"""
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Fruit Price Statistics</title>
  <script src="https://cdn.jsdelivr.net/npm/vega@3"></script>
  <script src="https://cdn.jsdelivr.net/npm/vega-lite@2"></script>
  <script src="https://cdn.jsdelivr.net/npm/vega-embed@3"></script>
</head>
<body style="font-family: Arial; margin: 40px;">
  <h2>Fruit Prices Over Time</h2>
  <p>Prices are in rupees per kilogram.</p>
  <p>Hover for details, scroll to zoom, and drag to pan.</p>
  <div id="chart"></div>

  <script>
    var spec = ${plot.toJson};

    spec["$$schema"] =
      "https://vega.github.io/schema/vega-lite/v2.json";

    spec.width = 700;
    spec.height = 400;
    spec.mark = {type: "line", point: true};

    spec.selection = {
      zoom: {type: "interval", bind: "scales"}
    };

    spec.encoding.tooltip = [
      {field: "Fruit", type: "nominal"},
      {field: "Date", type: "temporal"},
      {field: "Price", type: "quantitative"}
    ];

    vegaEmbed("#chart", spec);
  </script>
</body>
</html>
"""

    val writer = new PrintWriter("fruit_statistics.html")

    try {
      writer.write(html)
    } finally {
      writer.close()
    }

    println("Chart saved as fruit_statistics.html")
  }
}