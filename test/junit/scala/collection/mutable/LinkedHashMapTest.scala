package scala.collection.mutable

import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.{ Assert, Test }

import scala.collection.mutable

/* Test for scala/bug#9095 */
@RunWith(classOf[JUnit4])
class LinkedHashMapTest {
  class TestClass extends mutable.LinkedHashMap[String, Int] {
    def lastItemRef = lastEntry
  }
  
  @Test
  def testClear: Unit = {
    val lhm = new TestClass
    Seq("a" -> 8, "b" -> 9).foreach(kv => lhm.put(kv._1, kv._2))
    
    Assert.assertNotNull(lhm.lastItemRef)
    lhm.clear()
    Assert.assertNull(lhm.lastItemRef)
  }

  @Test
  def testMaintainInsertionOrder: Unit = {
    val lhm = new TestClass
    Seq("1" -> 1, "a" -> 10, "2" -> 2, "b" -> 9, "3" -> 3, "c" -> 8).foreach(kv => lhm.put(kv._1, kv._2))

    Assert.assertEquals(Set("1", "a", "2", "b", "3", "c"), lhm.keys)
    Assert.assertEquals(Seq(1, 10, 2, 9, 3, 8), lhm.values.toSeq)

    lhm.remove("a")

    Assert.assertEquals(Set("1", "2", "b", "3", "c"), lhm.keys)
    Assert.assertEquals(Seq(1, 2, 9, 3, 8), lhm.values.toSeq)

    lhm.remove("3")

    Assert.assertEquals(Set("1", "2", "b", "c"), lhm.keys)
    Assert.assertEquals(Seq(1, 2, 9, 8), lhm.values.toSeq)

    lhm.remove("1")

    Assert.assertEquals(Set("2", "b", "c"), lhm.keys)
    Assert.assertEquals(Seq(2, 9, 8), lhm.values.toSeq)

    lhm.remove("c")

    Assert.assertEquals(Set("2", "b"), lhm.keys)
    Assert.assertEquals(Seq(2, 9), lhm.values.toSeq)
  }
}
