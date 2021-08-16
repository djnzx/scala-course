package catsx.reader

object BankApp extends App {

  case class Account(amount: Int) {
    def replenish(delta: Int) = Account(amount + delta)
    def withdraw(delta: Int) = (delta, Account(amount - delta))
  }

  /** 1.classic definition */
  trait BusinessLogic {
    def create(initial: Int): Account
    def replenish(acc: Account, amount: Int): Account
    def withdraw(acc: Account, amount: Int): (Int, Account)
  }

  /** 2. can be represented as */
  object BusinessLogic2 {
    def create(initial: Int)(bl: BusinessLogic): Account = bl.create(initial)
    def replenish(acc: Account, amount: Int)(bl: BusinessLogic): Account = bl.replenish(acc, amount)
    def withdraw(acc: Account, amount: Int)(bl: BusinessLogic): (Int, Account) = bl.withdraw(acc, amount)
  }

  /** 3. why do we need it? for better composition */
  object UseCase3 {
    import BusinessLogic2._

    val combination = (bl: BusinessLogic) => {
      val a1 = create(50)(bl)
      val a2 = replenish(a1, 100)(bl)
      val (w, a3) = withdraw(a2, 30)(bl)
      (w, a3)
    }

    val bl = new BusinessLogic {
      override def create(initial: Int): Account = Account(initial)
      override def replenish(acc: Account, amount: Int): Account = acc.replenish(amount)
      override def withdraw(acc: Account, amount: Int): (Int, Account) = acc.withdraw(amount)
    }

    val (x, a) = combination(bl)
    println(x)
    println(a)
  }





}
