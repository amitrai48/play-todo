package models

import com.google.inject.Inject
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import slick.lifted.{Rep, Tag}
import scala.concurrent.Future


/**
  * Created by amit on 31/7/16.
  */
case class Todo(id: Option[Int], name: String, completed: Boolean)

class TodoRepo @Inject()(dbConfigProvider: DatabaseConfigProvider){
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig.driver.api._

  lazy protected val todoTableQuery = TableQuery[TodoTable]
  lazy protected val todoTableQueryInc = todoTableQuery returning todoTableQuery.map(_.id)

  def getAllTodo(): Future[List[Todo]] = dbConfig.db.run {
    todoTableQuery.to[List].result
  }

  def createTodo(todo: Todo): Future[Int] = dbConfig.db.run {
    todoTableQueryInc += todo
  }

  def editTodo(todo: Todo): Future[Int] = dbConfig.db.run {
    todoTableQuery.filter(_.id === todo.id).update(todo)
  }

  def deleteTodo(id: Int): Future[Int] = dbConfig.db.run {
    todoTableQuery.filter(_.id === id).delete
  }

  class TodoTable(tag: Tag)extends Table[Todo](tag,"TODOS"){
    val id = column[Int]("ID", O.AutoInc, O.PrimaryKey)
    val name = column[String]("NAME", O.SqlType("VARCHAR(200)"))
    val completed = column[Boolean]("COMPLETED",O.Default(false), O.SqlType("BIT"))
    // Every table needs a * projection with the same type as the table's type parameter
    def * = (id.?, name, completed) <>(Todo.tupled, Todo.unapply)
  }
}
