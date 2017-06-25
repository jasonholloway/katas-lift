package liftKata {
  import scala.collection.immutable.Queue

  package object tests {
    type Person = Int
    type Position = Int
    type FloorMap = Map[Position, Queue[Person]]
  }

}

package liftKata.tests {

  import org.scalacheck._
  import org.scalacheck.Prop.forAll
  import org.scalatest._

  import scala.collection.immutable.Queue


  case class Building(floorCount: Int, floors: FloorMap, lift: Lift) {}

  case class Lift(currFloor: Position, passengers: Set[Person]) {}


  case class LiftSystem(floorCount: Int, liftPos: Int) {
    def addPerson(floor: Position, person: Person): LiftSystem = ???
    def next(): LiftSystem = this
  }




  object LiftSystemSpecification extends Properties("Lift") {

    val genFreshSystem = for {
      fc <- Gen.choose[Int](1, 30)
      liftPos <- Gen.choose[Int](0, fc - 1)
    } yield LiftSystem(fc, liftPos)

    val genSystem = for {
      system <- genFreshSystem
      actions <- Gen.posNum[Int]
    } yield 13

    property("when empty, and no-one waiting, goes nowhere") =
      forAll(genFreshSystem) { initial =>
        val next = initial.next()
        next.liftPos == initial.liftPos
      }

    property("when person appears, lift goes to meet them") =
      forAll(genFreshSystem) { system =>
        val next = system.addPerson(0, 0: Person)
        val next2 = next.next()
        false
      }



//
//    property("when building otherwise empty, goes directly to next caller")
//      = forAll(genBuildingWithSingleUser, genLift) {
//      (building, lift) => {
//
//        lift.next();
//
//        true
//      }


  }



}