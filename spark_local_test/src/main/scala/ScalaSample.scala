object ScalaSample {

  // 定义一个类Person
  private class Person(val name: String, var age: Int) {
    // 类的方法：打招呼
    def greet(): String = s"Hello, my name is $name and I am $age years old."
  }

  // 定义一个方法，用于计算两个整数的和
  def sum(a: Int, b: Int): Int = a + b

  def main(args: Array[String]): Unit = {
    // 创建Person对象
    val person = new Person("Alice", 30)

    // 调用Person类的greet方法并打印结果
    println(person.greet())

    // 调用sum方法并打印结果
    val result = sum(5, 7)
    println(s"The sum of 5 and 7 is $result")

    // 使用Scala的集合框架，这里是一个简单的列表操作示例
    val numbers = List(1, 2, 3, 4, 5)
    val doubledNumbers = numbers.map(_ * 2)
    println(s"Doubled numbers: $doubledNumbers")
  }
}
