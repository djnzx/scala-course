package app

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
trait PersonRepository extends JpaRepository[Person, Int]
