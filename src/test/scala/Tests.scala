package liftKata {
  import scala.collection.immutable.Queue

  package object tests {
    type Person = Int
    type Position = Int
    type Floor = Int
    type FloorMap = Map[Position, Queue[Person]]
  }
}

package liftKata.tests {

  import org.scalacheck._
  import org.scalacheck.Prop.forAll
  import org.scalatest._

  import scala.collection.immutable.Queue

  case class Building(floorCount: Int, floors: FloorMap, lift: Lift) {}

  case class Lift(currFloor: Position, passengers: Set[Person]) {
    def move(velocity: Int) = copy(currFloor = currFloor + velocity)
  }


  case class Itinerary(from: Floor, to: Floor, person: Person)

  case class LiftSystem(floorCount: Int, lift: Lift) {

    def callLift(it: Itinerary): LiftSystem
      = copy(lift = lift.move(if(it.from > lift.currFloor) 1 else -1))

    def step(): LiftSystem
      = copy(lift = lift.move(0))
  }


  object LiftSystemSpecification extends Properties("Lift") {

    val genFreshSystem = for {
      floorCount <- Gen.choose[Int](2, 30)
      liftPos <- Gen.choose[Int](0, floorCount - 1)
    } yield LiftSystem(floorCount, Lift(liftPos, Set()))

    val genSystem = for {
      system <- genFreshSystem
      actions <- Gen.posNum[Int]
    } yield 13

    val genPerson: Gen[Person] = Gen.posNum[Int]


    property("when empty, and no-one waiting, goes nowhere") =
      forAll(genFreshSystem) { initial =>
        val next = initial.step()
        next.lift.currFloor == initial.lift.currFloor
      }

    val genItinerary = (floorCount: Int) => for {
      from <- Gen.choose(0, floorCount - 1)
      to <- Gen.choose(0, floorCount - 1).suchThat(_ != from)
      person <- genPerson
    } yield Itinerary(from, to, person)

    val genLiftAndSingleItinerary = for {
      system <- genFreshSystem
      itinerary <- genItinerary(system.floorCount)
    } yield (Moment(system), itinerary)


    case class Moment[T](curr: T, history: List[T] = List()) {
      def hello() = 12
    }


    implicit class MomentExtensions[S](moment : Moment[S]) {
      def map(fn: S => S) = moment match {
        case Moment(prev, history) => Moment(fn(prev), prev :: history)
      }
    }


    implicit class LiftSystemMomentExtensions(moment: Moment[LiftSystem]) {
      def step()
        = moment.map(_.step())

      def callLift(it: Itinerary)
        = moment.map(_.callLift(it))

      def run() : Stream[Moment[LiftSystem]]
        = moment #:: moment.step().run()

      def runTill(pred: LiftSystem => Boolean)
        = moment.run()
            .take(50)
            .takeWhile(s => !pred(s.curr))
            .lastOption.getOrElse(moment)
    }


    property("when person appears, lift goes to meet them") =
      forAll(genLiftAndSingleItinerary) {
        case (start, itinerary) =>
          def liftDistance(s: LiftSystem) = Math.abs(itinerary.from - s.lift.currFloor)

          val end = start.callLift(itinerary)
                      .runTill(_.lift.currFloor == itinerary.from)

//          val floors = end.history.reverse.map(s => s.lift.currFloor)

          false

          //how to test for constant movement?
          //no stops - but not necessarily constant velocity
          //no stops, constant direction please

//          floors.scanLeft((None, 0)) { (ac, b) => a }
//


          //history should be monotonic





          //test should really be of history of movement
          //we want to know whether lift moved in constant swoop - no tarrying or zig-zagging etc

          //the lift itself shouldn't be gathering history for us - it's for us to track in our driving of the system

//          liftDistance(end) == 0
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