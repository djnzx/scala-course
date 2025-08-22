package threads

object Threads1 extends App {

  def tn = Thread.currentThread().getName

  pprint.log(tn)

  // Thread[Reference Handler,   10,system]
  // Thread[Signal Dispatcher,   9, system]
  // Thread[Notification Thread, 9, system]
  // Thread[Attach Listener,     9, system]
  // Thread[Finalizer,           8, system]
  // Thread[Common-Cleaner,      8, InnocuousThreadGroup]
  // Thread[Monitor Ctrl-Break,  5, main]
  // Thread[main,                5, main]
  Thread.getAllStackTraces.keySet().forEach(t => println(t))

}
