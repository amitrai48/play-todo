package controllers

import javax.inject.{Inject, Singleton}

import models.{Todo, TodoRepo}
import play.api.libs.json.{JsError, Json}
import play.api.mvc.{Action, Controller}
import utils.JsonFormat._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by amit on 31/7/16.
  */

@Singleton
class TodoController @Inject()(todoRepo: TodoRepo) extends Controller{

  def getAllTodos = Action.async{
    todoRepo.getAllTodo().map{ todos =>
      Ok(Json.toJson(todos))
    }
  }

  def createTodo = Action.async(parse.json) {request =>
    request.body.validate[Todo].fold(error => Future.successful(BadRequest(JsError.toJson(error))),{todo =>
      todoRepo.createTodo(todo).map { id =>
        Ok(Json.toJson(Map("id" -> id)))
      }
    })
  }

  def editTodo = Action.async(parse.json) {request =>
    request.body.validate[Todo].fold(error => Future.successful(BadRequest(JsError.toJson(error))),{todo =>
      todoRepo.editTodo(todo).map{rowsEffected =>
        Ok(Json.toJson(rowsEffected))
      }
    })
  }

  def deleteTodo(id: Int) = Action.async {request =>
      todoRepo.deleteTodo(id).map{rowsEffected =>
        Ok(Json.toJson(rowsEffected))
    }
  }
}
