### How to shift execution to particular thread

- [A short guide to Blocker](https://blog.softwaremill.com/a-short-guide-to-blocker-6cd29e145a12)
- [Thread shifting](https://blog.softwaremill.com/thread-shifting-in-cats-effect-and-zio-9c184708067b)

### Four different ways

- `IO.shift(BlockingEC)` + `... .guarantee(IO.shift(contextShift))` - ContextShiftApp.scala
- `contextShift.evalOn(BlockingEC)(...)` - EvalOnApp.scala
- `Blocker[IO].use { b => blocker.blockOn(...) }` - BlockerApp.scala
- `Blocker.liftExecutionContext(ec)` + `blocker.blockOn(...)` - CustomBlockerApp.scala
