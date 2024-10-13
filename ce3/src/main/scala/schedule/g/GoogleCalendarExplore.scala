package schedule.g

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.DateTime
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.CalendarScopes
import com.google.api.services.calendar.model._
import java.util
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import scala.jdk.CollectionConverters.IterableHasAsScala
import scala.jdk.CollectionConverters.SeqHasAsJava
import schedule.g.GoogleToken.getTokenScope

// https://developers.google.com/calendar/api/guides/create-events#java
class GoogleCalendarExplore extends AnyFunSuite with Matchers with ScalaCheckPropertyChecks {

  val json = GsonFactory.getDefaultInstance
  val http = GoogleNetHttpTransport.newTrustedTransport

  val REQUESTING_SCOPES = List(
    CalendarScopes.CALENDAR,
    CalendarScopes.CALENDAR_EVENTS,
    CalendarScopes.CALENDAR_EVENTS_READONLY,
    CalendarScopes.CALENDAR_READONLY,
    CalendarScopes.CALENDAR_SETTINGS_READONLY
  )
  val cred = GoogleOAuthCredentials.get(http, json, REQUESTING_SCOPES)

  // instance
  lazy val service = new Calendar.Builder(http, json, cred).setApplicationName("Google Calendar API Java Quickstart").build

  val calendarId =
    //    "primary"
    "48af332a1d310cb62c80ab2443ccfea91e20002a0b8288f22379e4071ef18c86@group.calendar.google.com"

  test("just check the scope") {
    val t1 = cred.getAccessToken
    pprint.log(t1)
    if (t1 != null) {
      val scope = getTokenScope(t1)
      pprint.log(scope)
      //    pprint.log(invalidateToken(t1))
    }
  }

  /** TODO:
    *    400 Bad Request
    *    POST https://oauth2.googleapis.com/token
    *    {
    *      "error": "invalid_grant",
    *      "error_description": "Token has been expired or revoked."
    *    }
    *
    *    https://developers.google.com/identity/protocols/oauth2#expiration
    *    https://support.google.com/accounts/answer/14012355?visit_id=638613838969772900-734702207&rd=1#remove-access
    *    https://support.google.com/a/answer/7281227#restrictaccess
    */
  test("list of calendars") {
    val pageToken: String = null
    val calendarList = service.calendarList.list.setPageToken(pageToken).execute
    val items: util.List[CalendarListEntry] = calendarList.getItems
    items.forEach { c =>
      println(c.getSummary -> c.getAccessRole -> c.getId)
    }
    calendarList.getNextPageToken match {
      case null => println("- no more calendars")
      case next => println(s"- next page token $next")
    }
  }

  test("get 10") {
    val now = new DateTime(System.currentTimeMillis)

    val events = service.events
      .list(calendarId)
      .setMaxResults(20)
      .setTimeMin(now)
      .setOrderBy("startTime")
      .setSingleEvents(true)
      .execute

    events.getItems.asScala.toList match {
      case Nil   => println("No upcoming events found.")
      case items =>
        println("Upcoming events")
        items.foreach { e =>
          var start = e.getStart.getDateTime
          if (start == null) start = e.getStart.getDate
          printf("%s (%s)\n", e.getSummary, start)
        }
    }

  }

  test("create") {
    val event = new Event()
      .setSummary("Google I/O 2015")
      .setLocation("800 Howard St., San Francisco, CA 94103")
      .setDescription("A chance to hear more about Google's developer products.")

    val startDateTime = new DateTime("2024-09-08T09:00:00-07:00")
    val start = new EventDateTime()
      .setDateTime(startDateTime)
      .setTimeZone("America/Los_Angeles");
    event.setStart(start)

    val endDateTime = new DateTime("2024-09-08T10:00:00-07:00")
    val end = new EventDateTime()
      .setDateTime(endDateTime)
      .setTimeZone("America/Los_Angeles")
    event.setEnd(end)

    val recurrence = List("RRULE:FREQ=DAILY;COUNT=2")
    event.setRecurrence(recurrence.asJava)

    val attendees = List(
      new EventAttendee().setEmail("lpage@example.com"),
      new EventAttendee().setEmail("sbrin@example.com"),
    )
    event.setAttendees(attendees.asJava)

    val reminderOverrides = List(
      new EventReminder().setMethod("email").setMinutes(24 * 60),
      new EventReminder().setMethod("popup").setMinutes(10),
    )

    val reminders = new Event.Reminders()
      .setUseDefault(false)
      .setOverrides(reminderOverrides.asJava)
    event.setReminders(reminders)

    val event0 = service.events().insert(calendarId, event).execute()
    printf("Event created: %s\n", event0.getHtmlLink)

  }
}
