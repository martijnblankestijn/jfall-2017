package nl.codestar.jfall.persistence

import java.time.LocalDateTime
import java.time.LocalDateTime.now
import java.util.UUID

import akka.actor.{ActorSystem, Props}
import akka.persistence.PersistentActor
import akka.persistence.journal.Tagged
import akka.persistence.query.Offset.noOffset
import akka.persistence.query.{Offset, PersistenceQuery}
import akka.persistence.query.journal.leveldb.scaladsl.LeveldbReadJournal
import akka.persistence.query.journal.leveldb.scaladsl.LeveldbReadJournal.Identifier
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink
import nl.codestar.jfall.persistence.AppointmentActor._

import scala.concurrent.duration._


object QueryDemo {
  implicit val system = ActorSystem()
  implicit val mat = ActorMaterializer()

  def main(args: Array[String]): Unit = {
    val entityId = UUID.randomUUID()
    val actor = system.actorOf(Props[AppointmentActor], entityId.toString)

    actor ! Create(entityId, now())
    actor ! Conclude(entityId, 5, now())

    // DEMO
    PersistenceQuery(system)
        .readJournalFor[LeveldbReadJournal](Identifier)
          .eventsByTag("appointment", Offset.noOffset)
            .map(println)
      .runWith(Sink.ignore)
    
    
    
    



    //


    // shutdown the demo after 10 seconds
    val terminate: Runnable = () => system.terminate()
    system.scheduler.scheduleOnce(10 seconds, terminate)(system.dispatcher)
  }
}


object AppointmentActor {
  def props = Props(new AppointmentActor)

  sealed trait Command

  sealed trait Event

  case class Create(appointmentId: UUID, start: LocalDateTime) extends Command

  case class Conclude(appointmentId: UUID, quality: Int, time: LocalDateTime) extends Command

  case class Cancel(appointmentId: UUID, on: LocalDateTime, reason: String) extends Command

  case class AppointmentCreated(start: LocalDateTime) extends Event

  case class AppointmentCancelled(cancelledOn: LocalDateTime, reason: String) extends Event

  case class AppointmentConcluded(quality: Int, time: LocalDateTime) extends Event

  // Internal
  case class AppointmentState(start: LocalDateTime, conclusion: Option[Conclusion] = None)

  case class Conclusion(quality: Int, concludedAt: LocalDateTime)

}


class AppointmentActor extends PersistentActor {
  /**
    * persistenceId must be unique to a given entity in the journal (database table/keyspace). 
    * When replaying messages persisted to the journal, you query messages with a persistenceId. 
    * So, if two different entities share the same persistenceId, message-replaying behavior is corrupted.
    *
    * self.path.name is the entity identifier (utf-8 URL-encoded)
    */
  override val persistenceId = self.path.name

  var state: AppointmentState = _

  override def receiveRecover = {
    case event: Event => updateState(event)
  }

  override def receiveCommand = uninitialized

  def persistTagged(event: Any)(handler: Any ⇒ Unit): Unit =
    super.persist(tag(event))(tagged => handler(tagged.payload))

  def updateState(event: Any): Unit = {
    event match {
      case AppointmentCreated(start) =>
        state = AppointmentState(start)
        context.become(initialized())
      case AppointmentConcluded(q, time) =>
        state = state.copy(conclusion = Some(Conclusion(q, time)))
    }
  }

  private def uninitialized: Receive = {
    case Create(appointmentId, start) => persistTagged(AppointmentCreated(start))(updateState)
  }

  private def initialized(): Receive = {
    case Conclude(appointmentId, q, time) => persistTagged(AppointmentConcluded(q, time))(updateState)
  }

  private def tag[A](event: A): Tagged = Tagged(event, Set("appointment"))

}

