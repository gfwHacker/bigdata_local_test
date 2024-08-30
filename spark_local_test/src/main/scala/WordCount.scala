import org.apache.spark.{SparkConf, SparkContext}

object WordCount {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    val sc = new SparkContext("local", "test", conf)

    val lines = sc.textFile("hdfs://master:9000/hive/warehouse/ods.db/personal_info/pt_d=20240707/personal_info.csv")
    val words = lines.flatMap(_.split(","))
    val pairs = words.map((_, 1))
    val wordCounts = pairs.reduceByKey(_ + _)

    wordCounts.collect().foreach(println(_))
  }
}
