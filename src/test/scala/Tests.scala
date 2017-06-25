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


  case class Building(floorCount: Int) {}

  case class Lift(currFloor: Position) {}

  case class Scenario(lift: Lift, floors: FloorMap)


  object LiftSpecification extends Properties("lift") {

    private val genLift = Gen.posNum.map {
      Lift(_)
    }
    private val genBuildingWithSingleUser = Gen.posNum.map {
      Building(_)
    }
    private val genEmptyBuilding = Gen.posNum.map {
      Building(_)
    }
    private val genScenario = Gen.posNum.map { _ => (Lift(0), Building(0)) }


    property("when empty, and noone waiting, goes nowhere") =
      forAll(genScenario) { case (lift, building) =>
        true
      }

    property("when building otherwise empty, goes directly to next caller")
      = forAll(genBuildingWithSingleUser, genLift) {
      (building, lift) => {

        lift.next();

        true
      }
    }

  }



}