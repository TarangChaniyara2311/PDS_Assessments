import org.saddle._
import org.saddle.io._
import vegas._

import java.io.PrintWriter

object FruitAssessment {

  def main(args: Array[String]): Unit = {

    // 1. Load the CSV dataset using Saddle
    // Column positions: Date=0, Fruit=1, Price=2, Quantity=3
    val file = CsvFile("fruits.csv")

    val data = CsvParser
      .parse(List(0, 1, 2, 3))(file)
      .withColIndex(0)

    println("Dataset loaded successfully.")
    println("Total records: " + data.numRows)

    // 2. Filter rows to keep only apples
    val apples = data
      .rfilter(row => row.raw(1).trim.equalsIgnoreCase("Apple"))
      .sortedRows(0)

    if (apples.numRows == 0) {
      println("No apple records found.")
      return
    }

    // 3. Convert apple prices to numbers and calculate the mean
    val prices = apples.colAt(2).mapValues(_.toDouble)
    val averagePrice = prices.mean

    println("\nApple records:")
    println("Date | Fruit | Price | Quantity")

    for (i <- 0 until apples.numRows) {
      val row = apples.rowAt(i)

      println(
        row.raw(0) + " | " +
        row.raw(1) + " | " +
        row.raw(2) + " | " +
        row.raw(3)
      )
    }

    println(f"\nAverage apple price: Rs. $averagePrice%.2f per kg")

    // 4. Prepare the filtered data for Vegas
    val chartData = (0 until apples.numRows).map { i =>
      val row = apples.rowAt(i)

      Map[String, Any](
        "Date" -> row.raw(0),
        "Price" -> row.raw(2).toDouble,
        "Quantity" -> row.raw(3).toInt
      )
    }

    // 5. Create the apple price trend using Vegas
    val plot = Vegas("Apple Price Trend")
      .withData(chartData)
      .mark(Line)
      .encodeX("Date", Temp)
      .encodeY("Price", Quant)

    // 6. Create an HTML page with tooltips, zoom and pan
    val html = s"""
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Apple Price Trend</title>
  <script src="https://cdn.jsdelivr.net/npm/vega@3"></script>
  <script src="https://cdn.jsdelivr.net/npm/vega-lite@2"></script>
  <script src="https://cdn.jsdelivr.net/npm/vega-embed@3"></script>
</head>
<body style="font-family: Arial; margin: 40px;">
  <h2>Apple Price Trend</h2>
  <p>Average price: Rs. ${"%.2f".format(averagePrice)} per kg</p>
  <p>Hover over points for details. Scroll to zoom and drag to pan.</p>

  <div id="chart"></div>

  <script>
    var spec = ${plot.toJson};

    spec["$$schema"] =
      "https://vega.github.io/schema/vega-lite/v2.json";

    spec.width = 650;
    spec.height = 350;
    spec.mark = {type: "line", point: true};

    spec.selection = {
      zoom: {type: "interval", bind: "scales"}
    };

    spec.encoding.tooltip = [
      {field: "Date", type: "temporal"},
      {field: "Price", type: "quantitative"},
      {field: "Quantity", type: "quantitative"}
    ];

    vegaEmbed("#chart", spec);
  </script>
</body>
</html>
"""

    val writer = new PrintWriter("apple_price_trend.html")

    try {
      writer.write(html)
    } finally {
      writer.close()
    }

    println("Chart saved as apple_price_trend.html")
  }
}