package _degoes.hkt1

class HKTApp2 extends App {

  trait UserID
  trait User
  trait UserProfile

  trait UserRepository[F[_]] {
    def getUserById(id: UserID): F[User]
    def getUserProfile(user: User): F[UserProfile]
    def updateUserProfile(user: User, profile: UserProfile): F[Unit]
  }

}
